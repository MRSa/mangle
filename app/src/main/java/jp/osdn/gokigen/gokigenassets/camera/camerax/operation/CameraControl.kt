package jp.osdn.gokigen.gokigenassets.camera.camerax.operation

import android.util.Log
import android.util.Size
import android.view.Surface
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import jp.osdn.gokigen.gokigenassets.camera.ICameraPreferenceProvider
import jp.osdn.gokigen.gokigenassets.constants.IApplicationConstantConvert.Companion.ID_CAMERA_X_PREVIEW_LAYOUT
import jp.osdn.gokigen.gokigenassets.liveview.ILiveView
import jp.osdn.gokigen.gokigenassets.liveview.ILiveViewRefresher
import jp.osdn.gokigen.gokigenassets.liveview.image.CameraLiveViewListenerImpl
import jp.osdn.gokigen.gokigenassets.liveview.storeimage.StoreImage
import jp.osdn.gokigen.gokigenassets.utils.imagefile.FileControl
import jp.osdn.gokigen.gokigenassets.camera.interfaces.ICameraControl
import jp.osdn.gokigen.gokigenassets.camera.interfaces.IDisplayInjector
import jp.osdn.gokigen.gokigenassets.camera.interfaces.IFocusingControl
import jp.osdn.gokigen.gokigenassets.camera.interfaces.IKeyDown
import jp.osdn.gokigen.gokigenassets.liveview.ICachePositionProvider
import jp.osdn.gokigen.gokigenassets.scene.IInformationReceiver
import jp.osdn.gokigen.gokigenassets.scene.IVibrator
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraControl(private val activity : AppCompatActivity, private val preference: ICameraPreferenceProvider, private val vibrator : IVibrator, private val informationReceiver : IInformationReceiver) : ICameraControl
{
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var liveViewListener : CameraLiveViewListenerImpl
    private lateinit var fileControl : FileControl
    private lateinit var storeImage : StoreImage
    private lateinit var cameraXCamera : Camera
    private var cameraIsStarted = false
    private val cameraXCameraControl = CameraXCameraControl()
    private val clickKeyDownListeners = mutableMapOf<Int, CameraClickKeyDownListener>()
    private val cachePositionProviders = mutableMapOf<Int, ICachePositionProvider>()

    override fun getConnectionMethod(): String
    {
        return ("CAMERA_X")
    }

    override fun initialize()
    {
        Log.v(TAG, " initialize()")
        liveViewListener = CameraLiveViewListenerImpl(activity, informationReceiver)
        cameraExecutor = Executors.newSingleThreadExecutor()
        storeImage = StoreImage(activity, liveViewListener)
        clickKeyDownListeners.clear()
        fileControl = FileControl(activity, storeImage, vibrator)
    }

    override fun connectToCamera()
    {
        Log.v(TAG, " connectToCamera() : camerax ")
    }

    override fun changeCaptureMode(mode : String)
    {
        Log.v(TAG, " changeCaptureMode() : $mode ")
    }

    override fun needRotateImage(): Boolean
    {
        return (true)
    }

    override fun setRefresher(id: Int, refresher: ILiveViewRefresher, imageView : ILiveView, cachePosition : ICachePositionProvider)
    {
        liveViewListener.setRefresher(refresher)
        imageView.setImageProvider(liveViewListener)
        cachePositionProviders[id] = cachePosition
    }

    override fun startCamera(isPreviewView : Boolean, cameraSequence : Int)
    {
        Log.v(TAG, " startCamera()")
        val cameraSelector : CameraSelector = when (cameraSequence) {
                1 -> CameraSelector.DEFAULT_FRONT_CAMERA
                else -> CameraSelector.DEFAULT_BACK_CAMERA
        }

        if (cameraIsStarted)
        {
            Log.v(TAG, " ALREADY STARTED...")
            try
            {
                val cameraProvider: ProcessCameraProvider = ProcessCameraProvider.getInstance(activity).get()
                cameraProvider.unbindAll()
                cameraIsStarted = false
            }
            catch (e : Exception)
            {
                e.printStackTrace()
            }
        }
        cameraIsStarted = true
        if (isPreviewView)
        {
            // Preview View
            startCameraForPreviewView(cameraSelector)
        }
        else
        {
            // Liveview View
            startCameraForLiveView(cameraSelector)
        }
    }

    private fun startCameraForPreviewView(cameraSelector : CameraSelector)
    {
        Log.v(TAG, " startCameraPreviewView()")

        val cameraProviderFuture = ProcessCameraProvider.getInstance(activity)
        cameraProviderFuture.addListener( {
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(activity.findViewById<androidx.camera.view.PreviewView>(ID_CAMERA_X_PREVIEW_LAYOUT).surfaceProvider)
                }
            val imageCapture = fileControl.prepare()

            try
            {
                cameraProvider.unbindAll()
                cameraXCamera = cameraProvider.bindToLifecycle(activity, cameraSelector, preview, imageCapture)
                cameraXCameraControl.setCameraControl(cameraXCamera.cameraControl)
            }
            catch(e : Exception)
            {
                Log.e(TAG, "Use case binding failed", e)
                e.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(activity))
    }

    private fun getImageRotation() : Int
    {
        return (Surface.ROTATION_0)
    }

    private fun startCameraForLiveView(cameraSelector : CameraSelector)
    {
        Log.v(TAG, " startCameraForLiveView()")

        val cameraProviderFuture = ProcessCameraProvider.getInstance(activity)
        cameraProviderFuture.addListener( {
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val imageCapture = fileControl.prepare()
            val option1 = preference.getCameraOption1()  // プレビューサイズを設定する
            val previewSize = if (option1.isNotBlank()) {
                when (option1) {
                    "_8K" -> Size(4320, 7680)
                    "_6K" -> Size(3384, 6016)
                    "_4K"   -> Size(4096, 2160)
                    "_WQHD" -> Size(2560, 1440)
                    "_2K" -> Size(2048, 1080)
                    "_FHD" -> Size(1920, 1080)
                    "_SXGA" -> Size(1280, 1024) // SXGA : 1600x1200 @ Pixel3a
                    "_XGA" -> Size(1024, 768)   // XGA  : 1600x1200 @ Pixel3a
                    "_SVGA" -> Size(800, 600)   // SVGA : 1280x960  @ Pixel3a
                    "_VGA" -> Size(640, 480)   // SVGA : 1280x960  @ Pixel3a
                    "8K" -> Size(7680, 4320)
                    "6K" -> Size(6016, 3384)
                    "4K"   -> Size(2160, 4096)
                    "WQHD" -> Size(1440, 2560)
                    "2K" -> Size(1080, 2048)
                    "FHD" -> Size(1080, 1920)
                    "SXGA" -> Size(1024, 1280)
                    "XGA" -> Size(768, 1024)
                    "SVGA" -> Size(600, 800)
                    "VGA" -> Size(480, 640)
                    else -> Size(480, 640)     // VGA : 1024x768   @ Pixel3a
                }
            }
            else
            {
                Size(640, 480)
            }
            try
            {
                val imageAnalyzer = if (option1.isNotBlank()) {
                    ImageAnalysis.Builder()
                        .setTargetResolution(previewSize)
                        .setTargetRotation(getImageRotation())
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()
                        .also {
                            it.setAnalyzer(cameraExecutor, liveViewListener)
                        }
                } else {
                    ImageAnalysis.Builder()
                        .setTargetRotation(getImageRotation())
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()
                        .also {
                            it.setAnalyzer(cameraExecutor, liveViewListener)
                        }
                }
                cameraProvider.unbindAll()
                cameraXCamera = cameraProvider.bindToLifecycle(activity, cameraSelector, imageCapture, imageAnalyzer)
                cameraXCameraControl.setCameraControl(cameraXCamera.cameraControl)
            }
            catch(e : Exception)
            {
                Log.e(TAG, "Use case binding failed", e)
                e.printStackTrace()
            }

        }, ContextCompat.getMainExecutor(activity))
    }

    override fun finishCamera()
    {
        try
        {
            val cameraProvider: ProcessCameraProvider = ProcessCameraProvider.getInstance(activity).get()
            cameraProvider.unbindAll()
            cameraIsStarted = false
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }

        try
        {
            cameraExecutor.shutdown()
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
        fileControl.finish()
    }

    override fun captureButtonReceiver(id : Int) : View.OnClickListener
    {
        return (getClickKeyDownListener(id))
    }

    override fun onLongClickReceiver(id: Int): View.OnLongClickListener
    {
        return (getClickKeyDownListener(id))
    }

    override fun keyDownReceiver(id: Int): IKeyDown
    {
        return (getClickKeyDownListener(id))
    }

    override fun getFocusingControl(id: Int): IFocusingControl
    {
        return (cameraXCameraControl)
    }

    override fun getDisplayInjector(): IDisplayInjector
    {
        return (cameraXCameraControl)
    }

    private fun getClickKeyDownListener(id : Int) : CameraClickKeyDownListener
    {
        try
        {
            val listener = clickKeyDownListeners[id]
            if (listener != null)
            {
                // すでに登録されていた場合は、応答する
                return (listener)
            }
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
        val listener = CameraClickKeyDownListener(id, fileControl, cachePositionProviders[id])
        clickKeyDownListeners[id] = listener
        return (listener)
    }

    companion object
    {
        private val TAG = CameraControl::class.java.simpleName
    }
}
