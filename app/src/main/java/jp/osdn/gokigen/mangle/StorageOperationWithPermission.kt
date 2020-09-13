package jp.osdn.gokigen.mangle

import android.content.ContentResolver
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

/**
 *
 *
 */
@RequiresApi(api = Build.VERSION_CODES.R)
class StorageOperationWithPermission(private val activity: AppCompatActivity)
{
    fun requestAndroidRMediaPermission()
    {
        val urisToModify: List<Uri> = listOf(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        val contentResolver : ContentResolver = activity.contentResolver
        val editPendingIntent = MediaStore.createWriteRequest(contentResolver, urisToModify)

        activity.startIntentSenderForResult(editPendingIntent.intentSender, MainActivity.REQUEST_CODE_MEDIA_EDIT,null, 0, 0, 0)
    }
}
