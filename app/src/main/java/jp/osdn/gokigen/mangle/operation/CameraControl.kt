package jp.osdn.gokigen.mangle.operation

import android.util.Log
import android.util.Size
import android.view.Surface
import android.view.View
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.preference.PreferenceManager
import jp.osdn.gokigen.mangle.R
import jp.osdn.gokigen.mangle.liveview.ILiveView
import jp.osdn.gokigen.mangle.liveview.ILiveViewRefresher
import jp.osdn.gokigen.mangle.liveview.image.CameraLiveViewListenerImpl
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraControl(val activity : FragmentActivity) : ICameraControl
{
    private val TAG = toString()
    private lateinit var cameraExecutor: ExecutorService
    private val fileControl : FileControl = FileControl(activity)
    private lateinit var liveViewListener : CameraLiveViewListenerImpl
    private var cameraIsStarted = false

    init
    {
        //val preference = PreferenceManager.getDefaultSharedPreferences(activity)
    }

    override fun initialize()
    {
        Log.v(TAG, " initialize()")
        liveViewListener = CameraLiveViewListenerImpl(activity)
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    override fun setRefresher(refresher: ILiveViewRefresher, imageView : ILiveView)
    {
        liveViewListener.setRefresher(refresher)
        imageView.setImageProvider(liveViewListener)
    }

    override fun startCamera(isPreviewView : Boolean)
    {
        Log.v(TAG, " startCamera()")
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
            startCameraForPreviewView()
        }
        else
        {
            // Liveview View
            startCameraForLiveView()
        }
    }

    private fun startCameraForPreviewView()
    {
        Log.v(TAG, " startCameraPreviewView()")

        val cameraProviderFuture = ProcessCameraProvider.getInstance(activity)
        cameraProviderFuture.addListener( {
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(activity.findViewById<androidx.camera.view.PreviewView>(R.id.viewFinder).createSurfaceProvider())
                }
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
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

    private fun startCameraForLiveView()
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
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
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

    override fun captureButtonReceiver() : View.OnClickListener
    {
        return (fileControl)
    }
}
