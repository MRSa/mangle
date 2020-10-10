package jp.osdn.gokigen.mangle.operation.imagefile

import android.content.ContentValues
import android.database.DatabaseUtils
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.google.android.material.snackbar.Snackbar
import jp.osdn.gokigen.mangle.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@RequiresApi(api = Build.VERSION_CODES.Q)
class ImageStoreExternal(private val context: FragmentActivity) : IImageStore
{
    private fun getExternalOutputDirectory(): File
    {
        val directoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).path + "/" + context.getString(
            R.string.app_location) + "/"
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

    override fun takePhoto(imageCapture : ImageCapture?) : Boolean
    {
        if ((!isExternalStorageWritable())||(imageCapture == null))
        {
            Log.v(TAG, " takePhotoExternal() : cannot write image to external.")
            return (false)
        }
        Log.v(TAG, " takePhotoExternal()")

        val outputDir = getExternalOutputDirectory()
        val resolver = context.contentResolver

        val mimeType = "image/jpeg"
        val now = System.currentTimeMillis()
        val path = Environment.DIRECTORY_DCIM + File.separator + context.getString(R.string.app_location) // Environment.DIRECTORY_PICTURES  + File.separator + "gokigen" //"DCIM/aira01a/"
        val photoFile = "P" + SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(now) + ".jpg"

        val extStorageUri : Uri
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, photoFile)
        values.put(MediaStore.Images.Media.DISPLAY_NAME, photoFile)
        values.put(MediaStore.Images.Media.MIME_TYPE, mimeType)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
        {
            values.put(MediaStore.Images.Media.RELATIVE_PATH, path)
            values.put(MediaStore.Images.Media.IS_PENDING, true)
            extStorageUri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            values.put(MediaStore.Images.Media.DATA, outputDir.absolutePath + File.separator + photoFile)
        }
        else
        {
            values.put(MediaStore.Images.Media.DATA, outputDir.absolutePath + File.separator + photoFile)
            extStorageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        val imageUri = resolver.insert(extStorageUri, values)
        if (imageUri != null)
        {
            resolver.update(imageUri, values, null, null)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            {
                Log.v(TAG, "  ===== StorageOperationWithPermission() : $imageUri =====")
                //StorageOperationWithPermission(context).requestAndroidRMediaPermission(imageUri)
            }

            /////////////////////////////
            val cursor = resolver.query(imageUri, null, null, null, null)
            DatabaseUtils.dumpCursor(cursor)
            cursor!!.close()
            /////////////////////////////

            val openStream = resolver.openOutputStream(imageUri)
            if (openStream != null)
            {
                val outputOptions = ImageCapture.OutputFileOptions.Builder(openStream).build()
                imageCapture.takePicture(
                    outputOptions,
                    ContextCompat.getMainExecutor(context),
                    object : ImageCapture.OnImageSavedCallback
                    {
                        override fun onError(e: ImageCaptureException)
                        {
                            Log.e(TAG, "Photo capture failed: ${e.message} ", e)
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                            {
                                //values.clear()
                                values.put(MediaStore.Images.Media.IS_PENDING, false)
                                resolver.update(imageUri, values, null, null)
                            }
                            e.printStackTrace()
                        }

                        override fun onImageSaved(output: ImageCapture.OutputFileResults)
                        {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                            {
                                //values.clear()
                                values.put(MediaStore.Images.Media.IS_PENDING, false)
                                resolver.update(imageUri, values, null, null)
                            }
                            val msg = context.getString(R.string.capture_success) + " $path/$photoFile"
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
            else
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                {
                    //values.clear()
                    values.put(MediaStore.Images.Media.IS_PENDING, false)
                    resolver.update(imageUri, values, null, null)
                }
            }
        }
        return (true)
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

    companion object
    {
        private val  TAG = this.toString()
        private const val FILENAME_FORMAT = "yyyyMMdd_HHmmss"
    }

}