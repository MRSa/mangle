package jp.osdn.gokigen.mangle

import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import jp.osdn.gokigen.mangle.operation.imagefile.IImageStoreGrant
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor
import jp.osdn.gokigen.mangle.preference.PreferenceAccessWrapper
import jp.osdn.gokigen.mangle.preference.PreferenceValueInitializer
import java.io.File

/**
 *
 *
 */
@RequiresApi(api = Build.VERSION_CODES.R)
class StorageOperationWithPermission(private val activity: FragmentActivity) : IScopedStorageAccessPermission
{
    private var callbackOwner : IImageStoreGrant? = null


    override fun requestStorageAccessFrameworkLocation()
    {
        try
        {
            val mediaLocation = PreferenceAccessWrapper(activity).getString(IPreferencePropertyAccessor.EXTERNAL_STORAGE_LOCATION, "")
            if (mediaLocation.length > 1)
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
    }

    override fun responseStorageAccessFrameworkLocation(resultCode: Int, data: Intent?)
    {
        if (resultCode == AppCompatActivity.RESULT_OK)
        {
            Log.v(TAG, " DOCUMENT TREE GRANTED  ${data}")
            data?.data?.also { uri ->
                val contentResolver = activity.applicationContext.contentResolver
                val takeFlags: Int =
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                contentResolver.takePersistableUriPermission(uri, takeFlags)
                PreferenceValueInitializer().storeStorageLocationPreference(activity, uri)
            }
        }
        else
        {
            Log.v(TAG, " DOCUMENT TREE DENIED  ${resultCode}")
        }
    }

    override fun requestAccessPermission(requestUri : Uri, grantResponse : IImageStoreGrant)
    {
        try
        {
            callbackOwner = grantResponse
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

    override fun responseAccessPermission(resultCode: Int, data: Intent?)
    {
        if (resultCode == AppCompatActivity.RESULT_OK)
        {
            Log.v(TAG, " WRITE PERMISSION GRANTED  $data")
        }
        else
        {
            Log.v(TAG, " WRITE PERMISSION DENIED  $resultCode")
        }
    }

    companion object
    {
        private val  TAG = this.toString()
    }

}
