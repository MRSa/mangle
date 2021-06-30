package jp.osdn.gokigen.gokigenassets.camera.example

import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import jp.osdn.gokigen.gokigenassets.camera.ICameraPreferenceProvider
import jp.osdn.gokigen.gokigenassets.camera.interfaces.*
import jp.osdn.gokigen.gokigenassets.camera.theta.status.ICaptureModeReceiver
import jp.osdn.gokigen.gokigenassets.liveview.IIndicatorControl
import jp.osdn.gokigen.gokigenassets.liveview.ILiveView
import jp.osdn.gokigen.gokigenassets.liveview.ILiveViewRefresher
import jp.osdn.gokigen.gokigenassets.liveview.focusframe.IAutoFocusFrameDisplay
import jp.osdn.gokigen.gokigenassets.liveview.image.CameraLiveViewListenerImpl
import jp.osdn.gokigen.gokigenassets.scene.IVibrator

class ExamplePictureControl(private val context: AppCompatActivity, private val vibrator : IVibrator, private val preference: ICameraPreferenceProvider) : IDisplayInjector, ILiveViewController, ICameraControl, View.OnClickListener, View.OnLongClickListener, ICaptureModeReceiver, ICameraShutter, IKeyDown
{
    private val liveViewListener = CameraLiveViewListenerImpl(context, isDisableCache = true)

    override fun injectDisplay(frameDisplayer: IAutoFocusFrameDisplay, indicator: IIndicatorControl, focusingModeNotify: IFocusingModeNotify) { }
    override fun initialize() { updateExamplePicture() }
    override fun startLiveView() {  }
    override fun stopLiveView() { }
    override fun getConnectionMethod(): String { return ("EXAMPLE") }
    override fun connectToCamera() { }
    override fun startCamera(isPreviewView: Boolean, cameraSequence: Int) { }
    override fun finishCamera() { }
    override fun changeCaptureMode(mode: String) { }
    override fun needRotateImage(): Boolean { return (false) }
    override fun captureButtonReceiver(id: Int): View.OnClickListener { return (this) }
    override fun onLongClickReceiver(id: Int): View.OnLongClickListener { return (this) }

    override fun keyDownReceiver(id: Int): IKeyDown { return (this) }
    override fun getFocusingControl(id: Int): IFocusingControl? { return (null) }
    override fun getDisplayInjector(): IDisplayInjector? { return (null) }
    override fun onClick(v: View?) { }
    override fun changedCaptureMode(captureMode: String) { }
    override fun doShutter() { }
    override fun doShutterOff() { }
    override fun handleKeyDown(keyCode: Int, event: KeyEvent): Boolean { return (false) }

    override fun setRefresher(refresher: ILiveViewRefresher, imageView: ILiveView)
    {
        try
        {
            liveViewListener.setRefresher(refresher)
            imageView.setImageProvider(liveViewListener)
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
    }

    private fun updateExamplePicture()
    {
        try
        {
            //liveViewListener.onUpdateLiveView(receivedData.copyOfRange(offset, dataLength), null)
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }


    }

    override fun onLongClick(v: View?): Boolean
    {
        vibrator.vibrate(IVibrator.VibratePattern.SIMPLE_SHORT)
        return (true)
    }


}
