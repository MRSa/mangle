package jp.osdn.gokigen.mangle.operation

import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.google.android.material.snackbar.Snackbar
import jp.osdn.gokigen.mangle.R
import java.io.File
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class FileControl(private val context: FragmentActivity) : View.OnClickListener
{
    private val TAG = toString()
    private val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"

    private lateinit var outputDirectory: File
    private var imageCapture: ImageCapture? = null

    init
    {

    }

    fun initialize()
    {
        outputDirectory = getOutputDirectory()
        context.findViewById<Button>(R.id.camera_capture_button)?.setOnClickListener( this )
    }

    fun prepare() : ImageCapture?
    {
        try
        {
            imageCapture = ImageCapture.Builder().build()
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
        return imageCapture
    }

    fun finish()
    {

    }

    private fun getOutputDirectory(): File
    {
        val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
            File(it, context.resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return (if (mediaDir != null && mediaDir.exists()) mediaDir else context.filesDir)
    }

    private fun takePhoto()
    {
        Log.v(TAG, " takePhoto()")

        val imageCapture = imageCapture ?: return
        val photoFile = File(outputDirectory, SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis()) + ".jpg")
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions, ContextCompat.getMainExecutor(context), object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    val msg = context.getString(R.string.capture_success) + " $savedUri"
                    //Toast.makeText(context.baseContext, msg, Toast.LENGTH_SHORT).show()
                    Snackbar.make(context.findViewById<androidx.constraintlayout.widget.ConstraintLayout>(
                        R.id.main_layout
                    ), msg, Snackbar.LENGTH_SHORT).show()
                    Log.v(TAG, msg)
                }
            })
    }

    override fun onClick(v: View?)
    {
        when (v?.id)
        {
            R.id.camera_capture_button -> takePhoto()
            else -> Log.v(TAG, " Unknown ID : " + v?.id)
        }
    }
}