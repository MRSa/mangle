package jp.osdn.gokigen.mangle

import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import androidx.preference.PreferenceManager
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor
import java.io.File

/**
 *
 *
 */
@RequiresApi(api = Build.VERSION_CODES.R)
class StorageOperationWithPermission(private val activity: FragmentActivity)
{
    fun requestAndroidRMediaPermission()
    {
        try
        {
            val mediaLocation = PreferenceManager.getDefaultSharedPreferences(activity).getString(IPreferencePropertyAccessor.EXTERNAL_STORAGE_LOCATION, "")
            if ((mediaLocation != null)&&(mediaLocation.length > 1))
            {
                return
            }

            val path = Environment.DIRECTORY_DCIM + File.separator + activity.getString(R.string.app_location)
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.setType("*/*")
            intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, path)

            activity.startActivityForResult(
                Intent(Intent.ACTION_OPEN_DOCUMENT_TREE),
                MainActivity.REQUEST_CODE_OPEN_DOCUMENT_TREE
            )

        }
        catch (e : Exception)
        {
           e.printStackTrace()
        }

/*

        try
        {
            val path = Environment.DIRECTORY_DCIM + File.separator + activity.getString(R.string.app_location)
            val extStorageUri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            val values = ContentValues()
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            values.put(MediaStore.Images.Media.RELATIVE_PATH, path)
            values.put(MediaStore.Images.Media.DATA, path + File.separator)
            val imageUri = activity.contentResolver.insert(extStorageUri, values)
            if (imageUri != null)
            {
                val urisToModify: List<Uri> = listOf(imageUri)
                val contentResolver: ContentResolver = activity.contentResolver
                val editPendingIntent = MediaStore.createWriteRequest(contentResolver, urisToModify)
                activity.startIntentSenderForResult(editPendingIntent.intentSender, MainActivity.REQUEST_CODE_MEDIA_EDIT, null, 0, 0, 0)
            }
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
*/
    }

    fun requestAndroidRMediaPermission(requestUri : Uri)
    {
        try
        {
            val urisToModify: List<Uri> = listOf( requestUri )
            val contentResolver: ContentResolver = activity.contentResolver
            val editPendingIntent = MediaStore.createWriteRequest(contentResolver, urisToModify)
            activity.startIntentSenderForResult(editPendingIntent.intentSender, MainActivity.REQUEST_CODE_MEDIA_EDIT,null, 0, 0, 0)
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
    }

}
