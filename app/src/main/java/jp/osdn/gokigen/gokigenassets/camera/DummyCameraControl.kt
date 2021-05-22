package jp.osdn.gokigen.gokigenassets.camera

import android.view.View
import androidx.camera.core.CameraSelector
import jp.osdn.gokigen.gokigenassets.camera.interfaces.ICameraControl
import jp.osdn.gokigen.gokigenassets.liveview.ILiveView
import jp.osdn.gokigen.gokigenassets.liveview.ILiveViewRefresher

class DummyCameraControl() : ICameraControl, View.OnClickListener
{
    override fun getConnectionMethod(): String
    {
        return ("NONE")
    }
    override fun initialize() { }
    override fun connectToCamera() { }
    override fun startCamera(isPreviewView: Boolean, cameraSelector: CameraSelector) { }
    override fun finishCamera() { }
    override fun changeCaptureMode(mode: String) { }
    override fun setRefresher(refresher: ILiveViewRefresher, imageView: ILiveView) { }
    override fun captureButtonReceiver(id: Int): View.OnClickListener
    {
        return (this)
    }
    override fun onClick(v: View?) { }
}