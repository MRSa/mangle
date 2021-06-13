package jp.osdn.gokigen.gokigenassets.camera

import android.view.KeyEvent
import android.view.View
import androidx.camera.core.CameraSelector
import jp.osdn.gokigen.gokigenassets.camera.interfaces.ICameraControl
import jp.osdn.gokigen.gokigenassets.camera.interfaces.IDisplayInjector
import jp.osdn.gokigen.gokigenassets.camera.interfaces.IKeyDown
import jp.osdn.gokigen.gokigenassets.liveview.ILiveView
import jp.osdn.gokigen.gokigenassets.liveview.ILiveViewRefresher

class DummyCameraControl() : ICameraControl, View.OnClickListener, IKeyDown
{
    override fun getConnectionMethod(): String { return ("NONE") }
    override fun initialize() { }
    override fun connectToCamera() { }
    override fun startCamera(isPreviewView: Boolean, cameraSelector: CameraSelector) { }
    override fun finishCamera() { }
    override fun changeCaptureMode(mode: String) { }
    override fun setRefresher(refresher: ILiveViewRefresher, imageView: ILiveView) { }
    override fun captureButtonReceiver(id: Int): View.OnClickListener { return (this) }
    override fun keyDownReceiver(id: Int): IKeyDown { return (this) }
    override fun getDisplayInjector(): IDisplayInjector? { return (null) }
    override fun onClick(v: View?) { }
    override fun handleKeyDown(keyCode: Int, event: KeyEvent): Boolean { return (false) }
}
