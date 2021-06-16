package jp.osdn.gokigen.gokigenassets.camera.interfaces

import android.view.View
import androidx.camera.core.CameraSelector
import jp.osdn.gokigen.gokigenassets.liveview.ILiveView
import jp.osdn.gokigen.gokigenassets.liveview.ILiveViewRefresher

interface ICameraControl
{
    fun getConnectionMethod() : String
    fun initialize()

    fun connectToCamera()
    fun startCamera(isPreviewView : Boolean = true, cameraSelector : CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA)
    fun finishCamera()

    fun changeCaptureMode(mode : String)

    fun setRefresher(refresher : ILiveViewRefresher, imageView : ILiveView)
    fun captureButtonReceiver(id : Int = 0) : View.OnClickListener
    fun keyDownReceiver(id : Int = 0) : IKeyDown

    fun getFocusingControl(id : Int = 0) : IFocusingControl?

    fun getDisplayInjector() : IDisplayInjector?
}
