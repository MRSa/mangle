package jp.osdn.gokigen.gokigenassets.camera.camerax.operation

import android.util.Log
import android.view.KeyEvent
import android.view.View
import jp.osdn.gokigen.gokigenassets.camera.interfaces.IKeyDown
import jp.osdn.gokigen.gokigenassets.constants.IApplicationConstantConvert
import jp.osdn.gokigen.gokigenassets.utils.imagefile.FileControl

class CameraClickKeyDownListener(private val cameraId : Int,  private val fileControl: FileControl) : View.OnClickListener, View.OnLongClickListener, IKeyDown
{
    override fun onClick(v: View?)
    {
        try
        {
            Log.v(TAG, " onClick : $v?.id ")
            when (v?.id)
            {
                IApplicationConstantConvert.ID_PREVIEW_VIEW_BUTTON_SHUTTER -> fileControl.takePhoto(cameraId)
                IApplicationConstantConvert.ID_BUTTON_SHUTTER -> fileControl.takePhoto(cameraId)
                else -> Log.v(TAG, " Unknown ID : " + v?.id)
            }
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
    }

    override fun handleKeyDown(keyCode: Int, event: KeyEvent): Boolean
    {
        try
        {
            if (event.action == KeyEvent.ACTION_DOWN && (keyCode == KeyEvent.KEYCODE_VOLUME_UP || keyCode == KeyEvent.KEYCODE_CAMERA))
            {
                fileControl.takePhoto(cameraId)
                return (true)
            }
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
        return (false)
    }

    override fun onLongClick(v: View?): Boolean
    {
        return (false)
    }

    companion object
    {
        private val TAG = CameraClickKeyDownListener::class.java.simpleName
    }
}
