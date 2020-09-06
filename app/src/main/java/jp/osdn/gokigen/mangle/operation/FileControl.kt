package jp.osdn.gokigen.mangle.operation

import android.content.ContentValues
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.preference.PreferenceManager
import com.google.android.material.snackbar.Snackbar
import jp.osdn.gokigen.mangle.R
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class FileControl(private val context: FragmentActivity) : View.OnClickListener
{
    private val TAG = toString()
    private val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
    private var imageCapture: ImageCapture? = null

    //private lateinit var outputDirectory: File
    //private var isLocalLocation : Boolean = false

    init
    {

    }

    fun prepare() : ImageCapture?
    {
        try
        {
            context.findViewById<Button>(R.id.camera_capture_button)?.setOnClickListener(this)
            imageCapture = ImageCapture.Builder().build()
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        return imageCapture
    }

    fun finish()
    {

    }

    /**
     *   保存用ディレクトリを準備する（ダメな場合はアプリ領域のディレクトリを確保する）
     *
     */
    private fun prepareLocalOutputDirectory(): File
    {
        val mediaDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        mediaDir?.mkdirs()
        return (if (mediaDir != null && mediaDir.exists()) mediaDir else context.filesDir)
    }

    private fun takePhotoLocal()
    {
        val imageCapture = imageCapture ?: return

        Log.v(TAG, " takePhotoLocal()")
        val photoFile = File(prepareLocalOutputDirectory(), SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis()) + ".jpg")
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback
            {
                override fun onError(exc: ImageCaptureException)
                {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults)
                {
                    val savedUri = Uri.fromFile(photoFile)
                    val msg = context.getString(R.string.capture_success) + " $savedUri"
                    //Toast.makeText(context.baseContext, msg, Toast.LENGTH_SHORT).show()
                    Snackbar.make(
                        context.findViewById<androidx.constraintlayout.widget.ConstraintLayout>(
                            R.id.main_layout
                        ), msg, Snackbar.LENGTH_SHORT
                    ).show()
                    Log.v(TAG, msg)
                }
            }
        )
    }

    private fun getExternalOutputDirectory(): File
    {
        val directoryPath = (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).path + "/" + context.getString(R.string.app_location) + "/").toLowerCase(Locale.US)
        val target = File(directoryPath)
        try
        {
            target.mkdirs()
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        Log.v(TAG, "  ----- RECORD Directory PATH : $directoryPath -----")
        return (target)
    }

    private fun takePhotoExternal()
    {
        val imageCapture = imageCapture ?: return
        if (!isExternalStorageWritable())
        {
            takePhotoLocal()
            return
        }
        Log.v(TAG, " takePhotoExternal()")

        val outputDir = getExternalOutputDirectory()
        val resolver = context.contentResolver

        val mimeType = "image/jpeg"
        val now = System.currentTimeMillis()
        val path = "DCIM/aira01a/"
        val photoFile = "P" + SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(now) + ".jpg"

        val extStorageUri : Uri
        val values = ContentValues()
        values.put(MediaStore.Images.Media.DISPLAY_NAME,photoFile)
        values.put(MediaStore.Images.Media.MIME_TYPE, mimeType)
        values.put(MediaStore.Images.Media.DATE_ADDED, now)
        values.put(MediaStore.Images.Media.DATE_MODIFIED, now)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
        {
            values.put(MediaStore.Images.Media.DATE_TAKEN, now)
            values.put(MediaStore.Images.Media.RELATIVE_PATH, path)
            values.put(MediaStore.Images.Media.IS_PENDING, true)
            extStorageUri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY + "/" + photoFile)
        }
        else
        {
            extStorageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        val imageUri = resolver.insert(extStorageUri, values)
        if (imageUri != null)
        {
            val openStream = resolver.openOutputStream(imageUri)
            if (openStream != null)
            {
                val outputOptions = ImageCapture.OutputFileOptions.Builder(openStream).build()
                imageCapture.takePicture(
                    outputOptions,
                    ContextCompat.getMainExecutor(context),
                    object : ImageCapture.OnImageSavedCallback
                    {
                        override fun onError(exc: ImageCaptureException)
                        {
                            Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                            {
                                values.clear()
                                values.put(MediaStore.Images.Media.IS_PENDING, false)
                                resolver.update(imageUri, values, null, null)
                            }
                        }

                        override fun onImageSaved(output: ImageCapture.OutputFileResults)
                        {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                            {
                                //values.clear()
                                values.put(MediaStore.Images.Media.IS_PENDING, false)
                                resolver.update(imageUri, values, null, null)
                            }
                            val msg = context.getString(R.string.capture_success) + " $extStorageUri  $path $photoFile"
                            //Toast.makeText(context.baseContext, msg, Toast.LENGTH_SHORT).show()
                            Snackbar.make(
                                context.findViewById<androidx.constraintlayout.widget.ConstraintLayout>(
                                    R.id.main_layout
                                ), msg, Snackbar.LENGTH_SHORT
                            ).show()
                            Log.v(TAG, msg)
                        }
                    })
            }
        }
    }

    private fun takePhoto()
    {
        Log.v(TAG, " takePhoto()")
        try
        {
            val isLocalLocation  = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(
                IPreferencePropertyAccessor.PREFERENCE_SAVE_LOCAL_LOCATION,
                IPreferencePropertyAccessor.PREFERENCE_SAVE_LOCAL_LOCATION_DEFAULT_VALUE
            )
            if (isLocalLocation)
            {
                takePhotoLocal()
            }
            else
            {
                takePhotoExternal()
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    private fun isExternalStorageWritable(): Boolean
    {
        return (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED)
    }

/*
    private fun isExternalStorageReadable(): Boolean
    {
        return (Environment.getExternalStorageState() in setOf(
            Environment.MEDIA_MOUNTED,
            Environment.MEDIA_MOUNTED_READ_ONLY
        ))
    }
*/

    override fun onClick(v: View?)
    {
        Log.v(TAG, " onClick : $v?.id ")
        when (v?.id)
        {
            R.id.camera_capture_button -> takePhoto()
            else -> Log.v(TAG, " Unknown ID : " + v?.id)
        }
    }
}
