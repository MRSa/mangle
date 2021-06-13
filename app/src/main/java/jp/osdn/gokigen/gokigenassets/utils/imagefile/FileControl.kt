package jp.osdn.gokigen.gokigenassets.utils.imagefile

import android.os.Build
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.camera.core.ImageCapture
import androidx.fragment.app.FragmentActivity
import jp.osdn.gokigen.gokigenassets.camera.interfaces.IKeyDown
import jp.osdn.gokigen.gokigenassets.constants.IApplicationConstantConvert.Companion.ID_BUTTON_SHUTTER
import jp.osdn.gokigen.gokigenassets.constants.IApplicationConstantConvert.Companion.ID_PREFERENCE_CAPTURE_BOTH_CAMERA_AND_LIVE_VIEW
import jp.osdn.gokigen.gokigenassets.constants.IApplicationConstantConvert.Companion.ID_PREFERENCE_CAPTURE_BOTH_CAMERA_AND_LIVE_VIEW_DEFAULT_VALUE
import jp.osdn.gokigen.gokigenassets.constants.IApplicationConstantConvert.Companion.ID_PREFERENCE_SAVE_LOCAL_LOCATION
import jp.osdn.gokigen.gokigenassets.constants.IApplicationConstantConvert.Companion.ID_PREFERENCE_SAVE_LOCAL_LOCATION_DEFAULT_VALUE
import jp.osdn.gokigen.gokigenassets.constants.IApplicationConstantConvert.Companion.ID_PREVIEW_VIEW_BUTTON_SHUTTER
import jp.osdn.gokigen.gokigenassets.liveview.storeimage.IStoreImage
import jp.osdn.gokigen.gokigenassets.preference.PreferenceAccessWrapper


class FileControl(private val context: FragmentActivity, private val storeImage : IStoreImage) : View.OnClickListener, IKeyDown
{
    private val storeLocal = ImageStoreLocal(context)
    private val storeExternal = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) { ImageStoreExternal(context) } else { ImageStoreExternalLegacy(context) }
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
            val isLocalLocation  = preference.getBoolean(ID_PREFERENCE_SAVE_LOCAL_LOCATION, ID_PREFERENCE_SAVE_LOCAL_LOCATION_DEFAULT_VALUE)
            val captureBothCamera  = preference.getBoolean(ID_PREFERENCE_CAPTURE_BOTH_CAMERA_AND_LIVE_VIEW, ID_PREFERENCE_CAPTURE_BOTH_CAMERA_AND_LIVE_VIEW_DEFAULT_VALUE)
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
            ID_PREVIEW_VIEW_BUTTON_SHUTTER -> takePhoto()
            ID_BUTTON_SHUTTER -> takePhoto()
            else -> Log.v(TAG, " Unknown ID : " + v?.id)
        }
    }

    override fun handleKeyDown(keyCode: Int, event: KeyEvent): Boolean
    {
        try
        {
            if (event.action == KeyEvent.ACTION_DOWN && (keyCode == KeyEvent.KEYCODE_VOLUME_UP || keyCode == KeyEvent.KEYCODE_CAMERA))
            {
                takePhoto()
                return (true)
            }
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
        return (false)
    }

    companion object
    {
        private val TAG = FileControl::class.java.simpleName
    }
}
