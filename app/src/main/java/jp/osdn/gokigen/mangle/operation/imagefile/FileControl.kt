package jp.osdn.gokigen.mangle.operation.imagefile

import android.os.Build
import android.util.Log
import android.view.View
import androidx.camera.core.ImageCapture
import androidx.fragment.app.FragmentActivity
import jp.osdn.gokigen.mangle.IScopedStorageAccessPermission
import jp.osdn.gokigen.mangle.R
import jp.osdn.gokigen.mangle.liveview.storeimage.IStoreImage
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor
import jp.osdn.gokigen.mangle.preference.PreferenceAccessWrapper

class FileControl(private val context: FragmentActivity, private val storeImage : IStoreImage, accessRequest : IScopedStorageAccessPermission?) : View.OnClickListener
{
    private val storeLocal = ImageStoreLocal(context)
    private val storeExternal = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) { ImageStoreExternal(context, accessRequest) } else { ImageStoreExternalLegacy(context) }
    private var imageCapture: ImageCapture? = null
    private var addId : Int = 0

    fun prepare() : ImageCapture?
    {
        try
        {
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

    fun setId(id : Int)
    {
        addId = id
    }

    private fun takePhoto()
    {
        Log.v(TAG, " takePhoto()")
        try
        {
            //  preferenceから設定を取得する
            val preference = PreferenceAccessWrapper(context)
            val isLocalLocation  = preference.getBoolean(
                IPreferencePropertyAccessor.PREFERENCE_SAVE_LOCAL_LOCATION,
                IPreferencePropertyAccessor.PREFERENCE_SAVE_LOCAL_LOCATION_DEFAULT_VALUE
            )
            val captureBothCamera  = preference.getBoolean(
                IPreferencePropertyAccessor.CAPTURE_BOTH_CAMERA_AND_LIVE_VIEW,
                IPreferencePropertyAccessor.CAPTURE_BOTH_CAMERA_AND_LIVE_VIEW_DEFAULT_VALUE
            )
            if (captureBothCamera)
            {
                // ライブビュー画像を保管する場合...
                val thread = Thread { storeImage.doStore(addId) }
                try
                {
                    thread.start()
                }
                catch (e : Exception)
                {
                    e.printStackTrace()
                }
            }

            // 保管用クラスが準備できていない場合は、何もしない。
            if (imageCapture == null)
            {
                Log.v(TAG, " ImageCapture is NULL, so do nothing.")
                return
            }

            var isStoreLocal = isLocalLocation
            if (!isLocalLocation)
            {
                // 共用フォルダに保存
                isStoreLocal = !storeExternal.takePhoto(addId, imageCapture)
            }
            if (isStoreLocal)
            {
                // アプリ専用フォルダに登録
                storeLocal.takePhoto(addId, imageCapture)
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    override fun onClick(v: View?)
    {
        Log.v(TAG, " onClick : $v?.id ")
        when (v?.id)
        {
            R.id.camera_capture_button -> takePhoto()
            R.id.button_camera -> takePhoto()
            else -> Log.v(TAG, " Unknown ID : " + v?.id)
        }
    }

    companion object
    {
        private val  TAG = this.toString()
    }
}
