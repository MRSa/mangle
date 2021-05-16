package jp.osdn.gokigen.gokigenassets.camera.ricohpentax

import android.util.Log
import android.view.View
import androidx.camera.core.CameraSelector
import androidx.fragment.app.FragmentActivity
import jp.osdn.gokigen.gokigenassets.camera.interfaces.*
import jp.osdn.gokigen.gokigenassets.camera.theta.status.ICaptureModeReceiver
import jp.osdn.gokigen.gokigenassets.liveview.ILiveView
import jp.osdn.gokigen.gokigenassets.liveview.ILiveViewRefresher
import jp.osdn.gokigen.gokigenassets.camera.ricohpentax.connection.IUseGR2CommandNotify
import jp.osdn.gokigen.gokigenassets.camera.ricohpentax.connection.RicohGr2Connection
import jp.osdn.gokigen.gokigenassets.camera.ricohpentax.liveview.RicohGr2LiveViewControl
import jp.osdn.gokigen.gokigenassets.camera.ricohpentax.operation.*
import jp.osdn.gokigen.gokigenassets.camera.ricohpentax.status.RicohGr2StatusChecker
import jp.osdn.gokigen.gokigenassets.camera.ricohpentax.wrapper.RicohGr2RunMode
import jp.osdn.gokigen.gokigenassets.camera.ricohpentax.wrapper.playback.RicohGr2PlaybackControl
import jp.osdn.gokigen.gokigenassets.liveview.IIndicatorControl
import jp.osdn.gokigen.gokigenassets.liveview.focusframe.IAutoFocusFrameDisplay

class RicohPentaxCameraControl(context: FragmentActivity, provider: ICameraStatusReceiver)  : ILiveViewController, ICameraControl, View.OnClickListener,
    ICaptureModeReceiver, ICameraShutter, IDisplayInjector, IUseGR2CommandNotify
{

    //private final Activity activity;
    //private final ICameraStatusReceiver provider;
    private val gr2Connection = RicohGr2Connection(context, provider, this)
    private val buttonControl = RicohGr2CameraButtonControl()
    private val statusChecker = RicohGr2StatusChecker(500)
    private val playbackControl = RicohGr2PlaybackControl(communicationTimeoutMs)
    private val hardwareStatus = RicohGr2HardwareStatus()
    private val runMode = RicohGr2RunMode()

    //private final boolean useGrCommand;
    private val pentaxCaptureAfterAf: Boolean = false
    private val liveViewControl = RicohGr2LiveViewControl(context)
    private var captureControl: RicohGr2CameraCaptureControl? = null
    private val zoomControl = RicohGr2CameraZoomLensControl()
    private var focusControl: RicohGr2CameraFocusControl? = null
    private var useGR2Command = false
    private var useGR2CommandUpdated = false
    private var useCameraScreen = false



    override fun startLiveView()
    {
        TODO("Not yet implemented")
    }

    override fun stopLiveView()
    {
        TODO("Not yet implemented")
    }

    override fun getConnectionMethod(): String
    {
        TODO("Not yet implemented")
    }

    override fun initialize()
    {
        TODO("Not yet implemented")
    }

    override fun connectToCamera()
    {
        TODO("Not yet implemented")
    }

    override fun startCamera(isPreviewView: Boolean, cameraSelector: CameraSelector)
    {
        TODO("Not yet implemented")
    }

    override fun finishCamera()
    {
        TODO("Not yet implemented")
    }

    override fun changeCaptureMode(mode: String)
    {
        TODO("Not yet implemented")
    }

    override fun setRefresher(refresher: ILiveViewRefresher, imageView: ILiveView)
    {
        TODO("Not yet implemented")
    }

    override fun captureButtonReceiver(id: Int): View.OnClickListener
    {
        TODO("Not yet implemented")
    }

    override fun onClick(v: View?)
    {
        TODO("Not yet implemented")
    }

    override fun changedCaptureMode(captureMode: String)
    {
        TODO("Not yet implemented")
    }

    override fun doShutter()
    {
        TODO("Not yet implemented")
    }

    override fun doShutterOff()
    {
        TODO("Not yet implemented")
    }

    override fun setUseGR2Command(useGR2Command: Boolean, useCameraScreen: Boolean)
    {
        this.useGR2Command = useGR2Command
        this.useCameraScreen = useCameraScreen
    }

    /**
     *
     *
     */
    companion object
    {
        private val TAG = RicohPentaxCameraControl::class.java.simpleName
        private const val communicationTimeoutMs = 5000
    }

    override fun injectDisplay(frameDisplayer: IAutoFocusFrameDisplay, indicator: IIndicatorControl, focusingModeNotify: IFocusingModeNotify)
    {
        Log.v(TAG, "injectDisplay()")
        focusControl = RicohGr2CameraFocusControl(frameDisplayer, indicator)
        captureControl = RicohGr2CameraCaptureControl(pentaxCaptureAfterAf, frameDisplayer, statusChecker)
        if (useGR2CommandUpdated)
        {
            captureControl?.setUseGR2Command(useGR2Command)
            focusControl?.setUseGR2Command(useGR2Command)
        }
    }

}