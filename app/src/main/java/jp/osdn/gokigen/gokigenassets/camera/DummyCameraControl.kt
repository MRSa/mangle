package jp.osdn.gokigen.gokigenassets.camera

import android.view.KeyEvent
import android.view.View
import androidx.camera.core.CameraSelector
import jp.osdn.gokigen.gokigenassets.camera.interfaces.ICameraControl
import jp.osdn.gokigen.gokigenassets.camera.interfaces.IDisplayInjector
import jp.osdn.gokigen.gokigenassets.camera.interfaces.IFocusingControl
import jp.osdn.gokigen.gokigenassets.camera.interfaces.IKeyDown
import jp.osdn.gokigen.gokigenassets.liveview.ILiveView
import jp.osdn.gokigen.gokigenassets.liveview.ILiveViewRefresher

class DummyCameraControl() : ICameraControl, View.OnClickListener, View.OnLongClickListener, IKeyDown
{
    override fun getConnectionMethod(): String { return ("NONE") }
    override fun initialize() { }
    override fun connectToCamera() { }
    override fun startCamera(isPreviewView: Boolean, cameraSequence : Int) { }
    override fun finishCamera() { }
    override fun changeCaptureMode(mode: String) { }
    override fun needRotateImage(): Boolean { return (false) }
    override fun setRefresher(refresher: ILiveViewRefresher, imageView: ILiveView) { }
    override fun captureButtonReceiver(id: Int): View.OnClickListener { return (this) }
    override fun onLongClickReceiver(id: Int): View.OnLongClickListener { return (this) }
    override fun keyDownReceiver(id: Int): IKeyDown { return (this) }
    override fun getFocusingControl(id: Int): IFocusingControl? { return (null) }
    override fun getDisplayInjector(): IDisplayInjector? { return (null) }
    override fun onClick(v: View?) { }
    override fun handleKeyDown(keyCode: Int, event: KeyEvent): Boolean { return (false) }
    override fun onLongClick(v: View?): Boolean { return (false) }
}
