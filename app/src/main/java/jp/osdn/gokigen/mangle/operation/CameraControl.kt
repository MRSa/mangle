package jp.osdn.gokigen.mangle.operation

import android.util.Log
import android.util.Size
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import jp.osdn.gokigen.mangle.R
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraControl(val activity : FragmentActivity)
{
    private val TAG = toString()
    private lateinit var cameraExecutor: ExecutorService
    private val fileControl : FileControl = FileControl(activity)
    private var cameraIsStarted : Boolean = false

    init
    {

    }

    fun initialize()
    {
        Log.v(TAG, " initialize()")
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    fun startCamera()
    {
        Log.v(TAG, " startCamera()")
        if (cameraIsStarted)
        {
            Log.v(TAG, " CAMERA IS ALREADY STARTED.")
            return
        }
        cameraIsStarted = true

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
                val imageAnalyzer = ImageAnalysis.Builder()
                    .setTargetResolution(Size(800, 600))
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .also {
                        it.setAnalyzer(cameraExecutor, MyImageAnalyzer { luma ->
                            Log.d(TAG, "Average luminosity: $luma")
                        })
                    }
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(activity, cameraSelector, preview, imageCapture, imageAnalyzer)
            }
            catch(e : Exception)
            {
                Log.e(TAG, "Use case binding failed", e)
                e.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(activity))

    }

    fun finish()
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
        cameraExecutor.shutdown()
        fileControl.finish()
    }
}
