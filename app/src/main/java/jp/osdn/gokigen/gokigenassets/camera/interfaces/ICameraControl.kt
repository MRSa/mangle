package jp.osdn.gokigen.gokigenassets.camera.interfaces

import android.view.View
import jp.osdn.gokigen.gokigenassets.liveview.ILiveView
import jp.osdn.gokigen.gokigenassets.liveview.ILiveViewRefresher

interface ICameraControl
{
    fun getConnectionMethod() : String
    fun initialize()

    fun connectToCamera()
    fun startCamera(isPreviewView : Boolean = true, cameraSequence : Int = 0)
    fun finishCamera()

    fun changeCaptureMode(mode : String)
    fun needRotateImage() : Boolean

    fun setRefresher(refresher : ILiveViewRefresher, imageView : ILiveView)
    fun captureButtonReceiver(id : Int = 0) : View.OnClickListener
    fun onLongClickReceiver(id : Int = 0) : View.OnLongClickListener
    fun keyDownReceiver(id : Int = 0) : IKeyDown

    fun getFocusingControl(id : Int = 0) : IFocusingControl?
    fun getDisplayInjector() : IDisplayInjector?
}
