package jp.osdn.gokigen.gokigenassets.camera.camerax.operation

import android.util.Log
import android.view.Surface
import android.view.View
import androidx.appcompat.app.AppCompatActivity
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
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraControl(private val activity : AppCompatActivity, private val preference: ICameraPreferenceProvider) : ICameraControl
{
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var liveViewListener : CameraLiveViewListenerImpl
    private lateinit var fileControl : FileControl
    private lateinit var storeImage : StoreImage
    private var cameraIsStarted = false

    override fun getConnectionMethod(): String
    {
        return ("CAMERA_X")
    }

    override fun initialize()
    {
        Log.v(TAG, " initialize()")
        liveViewListener = CameraLiveViewListenerImpl(activity)
        cameraExecutor = Executors.newSingleThreadExecutor()
        storeImage = StoreImage(activity, liveViewListener)
        fileControl = FileControl(activity, storeImage)
    }

    override fun connectToCamera()
    {
        Log.v(TAG, " connectToCamera() : camerax ")
    }

    override fun changeCaptureMode(mode : String)
    {
        Log.v(TAG, " changeCaptureMode() : $mode ")
    }

    override fun setRefresher(refresher: ILiveViewRefresher, imageView : ILiveView)
    {
        liveViewListener.setRefresher(refresher)
        imageView.setImageProvider(liveViewListener)
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
            //val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            val imageCapture = fileControl.prepare()

            try
            {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(activity, cameraSelector, preview, imageCapture)
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
/*
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(activity.findViewById<androidx.camera.view.PreviewView>(R.id.viewFinder).createSurfaceProvider())
                }
*/
            //val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            val imageCapture = fileControl.prepare()

            try
            {
                val imageAnalyzer = ImageAnalysis.Builder()
                    //.setTargetResolution(Size(800, 600))
                    .setTargetRotation(getImageRotation())
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .also {
                        it.setAnalyzer(cameraExecutor, liveViewListener)
                    }
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(activity, cameraSelector, imageCapture, imageAnalyzer)
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
        try
        {
            fileControl.setId(id)
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
        return (fileControl)
    }

    override fun keyDownReceiver(id: Int): IKeyDown
    {
        try
        {
            fileControl.setId(id)
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
        return (fileControl)
    }

    override fun getFocusingControl(id: Int): IFocusingControl?
    {
        return (null)
    }

    override fun getDisplayInjector(): IDisplayInjector?
    {
        return (null)
    }

    companion object
    {
        private val TAG = CameraControl::class.java.simpleName
    }
}
