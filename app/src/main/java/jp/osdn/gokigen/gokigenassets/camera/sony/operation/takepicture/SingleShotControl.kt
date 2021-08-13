package jp.osdn.gokigen.gokigenassets.camera.sony.operation.takepicture

import android.util.Log
import jp.osdn.gokigen.gokigenassets.camera.sony.wrapper.ISonyCameraApi
import jp.osdn.gokigen.gokigenassets.liveview.IIndicatorControl
import jp.osdn.gokigen.gokigenassets.liveview.focusframe.IAutoFocusFrameDisplay
import java.lang.Exception


class SingleShotControl(private val frameDisplayer: IAutoFocusFrameDisplay, private val indicator: IIndicatorControl)
{
    private lateinit var cameraApi: ISonyCameraApi
    fun setCameraApi(sonyCameraApi: ISonyCameraApi)
    {
        cameraApi = sonyCameraApi
    }

    fun singleShot()
    {
        Log.v(TAG, "singleShot()")
        if (!::cameraApi.isInitialized)
        {
            Log.v(TAG, "ISonyCameraApi is not initialized...")
            return
        }
        try
        {
            val thread = Thread {
                try
                {
                    val resultsObj = cameraApi.actTakePicture()
                    if (resultsObj == null)
                    {
                        Log.v(TAG, "setTouchAFPosition() reply is null.")
                    }
                }
                catch (e: Exception)
                {
                    e.printStackTrace()
                }
                frameDisplayer.hideFocusFrame()
            }
            thread.start()
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    companion object
    {
        private val TAG = SingleShotControl::class.java.simpleName
    }
}
