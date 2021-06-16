package jp.osdn.gokigen.gokigenassets.liveview

import android.annotation.SuppressLint
import android.util.Log
import android.view.MotionEvent
import android.view.View
import jp.osdn.gokigen.gokigenassets.camera.interfaces.ICameraControl

class LiveViewOnTouchListener(private val cameraControl : ICameraControl, private val id : Int = 0) :  View.OnTouchListener
{
    companion object
    {
        private val TAG = LiveViewOnTouchListener::class.java.simpleName
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean
    {
        try
        {
            val focusControl = cameraControl.getFocusingControl(id)
            if (focusControl != null)
            {
                return (focusControl.driveAutoFocus(event))
            }
            Log.v(TAG, " onTouch() : focusControl is NULL...")
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
        return (false)
    }
}
