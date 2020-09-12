package jp.osdn.gokigen.mangle.liveview.storeimage

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import jp.osdn.gokigen.mangle.R
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class StoreImage(private val context: Context) : IStoreImage
{
    override fun doStore(target: Bitmap)
    {
        // 保存処理(プログレスダイアログ（「保存中...」）を表示して処理する)
        val saveDialog = ProgressDialog(context)
        saveDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        saveDialog.setMessage(context!!.getString(R.string.data_saving))
        saveDialog.isIndeterminate = true
        saveDialog.setCancelable(false)
        saveDialog.show()
        val thread = Thread {
            System.gc()
            saveImageImpl(target)
            System.gc()
            saveDialog.dismiss()
        }
        try
        {
            thread.start()
        }
        catch (t: Throwable)
        {
            t.printStackTrace()
            System.gc()
        }
    }

    private fun prepareLocalOutputDirectory(): File
    {
        val mediaDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        mediaDir?.mkdirs()
        return (if (mediaDir != null && mediaDir.exists()) mediaDir else context.filesDir)
    }

    /**
     * ビットマップイメージをファイルに出力する
     *
     * @param targetImage  出力するビットマップイメージ
     */
    private fun saveImageImpl(targetImage: Bitmap)
    {
        try
        {
            val fileName = "P" + SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(System.currentTimeMillis()) + ".jpg"
            val photoFile = File(prepareLocalOutputDirectory(), fileName)
            val outputStream = FileOutputStream(photoFile)
            targetImage.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
/*
            val resolver = context.contentResolver
            val values = ContentValues()
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            values.put(MediaStore.Images.Media.WIDTH, targetImage.width)
            values.put(MediaStore.Images.Media.HEIGHT, targetImage.height)
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q)
            {
                values.put(MediaStore.Images.Media.DATA, fileName)
            }
            resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

*/
        }
        catch (t: Throwable)
        {
            t.printStackTrace()
        }
    }

}