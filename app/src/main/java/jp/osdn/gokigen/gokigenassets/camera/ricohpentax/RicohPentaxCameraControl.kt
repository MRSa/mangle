package jp.osdn.gokigen.gokigenassets.camera.ricohpentax

import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import jp.osdn.gokigen.gokigenassets.camera.ICameraPreferenceProvider
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
import jp.osdn.gokigen.gokigenassets.constants.IApplicationConstantConvert
import jp.osdn.gokigen.gokigenassets.liveview.IIndicatorControl
import jp.osdn.gokigen.gokigenassets.liveview.focusframe.IAutoFocusFrameDisplay
import jp.osdn.gokigen.gokigenassets.liveview.image.CameraLiveViewListenerImpl
import jp.osdn.gokigen.gokigenassets.liveview.storeimage.StoreImage
import jp.osdn.gokigen.gokigenassets.preference.PreferenceAccessWrapper
import jp.osdn.gokigen.gokigenassets.scene.IInformationReceiver
import jp.osdn.gokigen.gokigenassets.scene.IVibrator

class RicohPentaxCameraControl(private val context: AppCompatActivity, private val vibrator : IVibrator, informationNotify: IInformationReceiver, private val preference: ICameraPreferenceProvider, statusReceiver : ICameraStatusReceiver)  : ILiveViewController, ICameraControl, View.OnClickListener, View.OnLongClickListener, ICaptureModeReceiver, ICameraShutter, IDisplayInjector, IUseGR2CommandNotify, IKeyDown
{

    //private final Activity activity;
    //private final ICameraStatusReceiver provider;
    private var liveViewListener = CameraLiveViewListenerImpl(context, informationNotify)
    private val gr2Connection = RicohGr2Connection(context, statusReceiver, this, this)
    private val buttonControl = RicohGr2CameraButtonControl()
    private val statusChecker = RicohGr2StatusChecker(500)
    private val playbackControl = RicohGr2PlaybackControl(communicationTimeoutMs)
    private val hardwareStatus = RicohGr2HardwareStatus()
    private val runMode = RicohGr2RunMode()

    //private final boolean useGrCommand;
    private val pentaxCaptureAfterAf: Boolean = false
    private val liveViewControl = RicohGr2LiveViewControl(liveViewListener, context)
    private var captureControl: RicohGr2CameraCaptureControl? = null
    private var focusControl: RicohGr2CameraFocusControl? = null
    private var useGR2Command = false
    private var useGR2CommandUpdated = false
    private var useCameraScreen = false
    private var isStatusWatch = false
    private val storeImage = StoreImage(context, liveViewListener)
    private var cameraPositionId = 0

    private val zoomControl = RicohGr2CameraZoomLensControl()

    override fun startLiveView()
    {
        Log.v(TAG, " startLiveView() ")
        try
        {
            if (!isStatusWatch)
            {
                statusChecker.startStatusWatch(null, null)
                isStatusWatch = true
            }
            liveViewControl.startLiveView()
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
    }

    override fun stopLiveView()
    {
        Log.v(TAG, " stopLiveView() ")
        try
        {
            liveViewControl.stopLiveView()
            if (isStatusWatch)
            {
                statusChecker.stopStatusWatch()
                isStatusWatch = false
            }
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
    }

    override fun getConnectionMethod(): String
    {
        return ("RICOH")
    }

    override fun initialize()
    {
        // TODO("Not yet implemented")
    }

    override fun connectToCamera()
    {
        Log.v(TAG, " connectToCamera() : PENTAX ")
        try
        {
            gr2Connection.connect()
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
    }

    override fun startCamera(isPreviewView: Boolean, cameraSequence: Int)
    {
        try
        {
            if (gr2Connection.getConnectionStatus() != ICameraConnectionStatus.CameraConnectionStatus.CONNECTED)
            {
                gr2Connection.startWatchWifiStatus(context)
            }
            else
            {
                gr2Connection.connect()
            }
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
    }

    override fun finishCamera()
    {
        try
        {
            if (isStatusWatch)
            {
                statusChecker.stopStatusWatch()
                isStatusWatch = false
            }
            gr2Connection.disconnect(false)
            gr2Connection.stopWatchWifiStatus(context)
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
    }

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

    override fun captureButtonReceiver(id: Int): View.OnClickListener
    {
        cameraPositionId = id
        return (this)
    }

    override fun onLongClickReceiver(id: Int): View.OnLongClickListener
    {
        cameraPositionId = id
        return (this)
    }

    override fun keyDownReceiver(id: Int): IKeyDown
    {
        cameraPositionId = id
        return (this)
    }

    override fun getFocusingControl(id: Int): IFocusingControl?
    {
        cameraPositionId = id
        return (focusControl)
    }

    override fun getDisplayInjector(): IDisplayInjector
    {
        return (this)
    }

    override fun onClick(v: View?)
    {
        if (v == null)
        {
            return
        }
        when (v.id)
        {
            IApplicationConstantConvert.ID_BUTTON_SHUTTER -> { doShutter() }
            else -> { }
        }
    }

    override fun changeCaptureMode(mode: String)
    {
        //TODO("Not yet implemented")
    }

    override fun changedCaptureMode(captureMode: String)
    {
        //TODO("Not yet implemented")
    }

    override fun doShutter()
    {
        try
        {
            Log.v(TAG, " doShutter()")
            val isNotDriveShutter = captureImageLiveView()
            if (isNotDriveShutter)
            {
                //  シャッターを駆動させない(けど、バイブレーションで通知する)
                vibrator.vibrate(IVibrator.VibratePattern.SIMPLE_SHORT)
                return
            }
            if (captureControl == null)
            {
                Log.v(TAG, " captureControl is NULL.")
            }
            captureControl?.doCapture(0)
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
    }

    override fun doShutterOff()
    {
        try
        {
            Log.v(TAG, " doShutterOff()")
            captureControl?.doCapture(0)
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
    }

    private fun captureImageLiveView() : Boolean
    {
        try
        {
            //  preferenceから設定を取得する
            val captureBothCamera = PreferenceAccessWrapper(context).getBoolean(
                IApplicationConstantConvert.ID_PREFERENCE_CAPTURE_BOTH_CAMERA_AND_LIVE_VIEW,
                IApplicationConstantConvert.ID_PREFERENCE_CAPTURE_BOTH_CAMERA_AND_LIVE_VIEW_DEFAULT_VALUE
            )
            val notUseShutter = PreferenceAccessWrapper(context).getBoolean(
                IApplicationConstantConvert.ID_PREFERENCE_CAPTURE_ONLY_LIVEVIEW_IMAGE,
                IApplicationConstantConvert.ID_PREFERENCE_CAPTURE_ONLY_LIVEVIEW_IMAGE_DEFAULT_VALUE
            )
            if ((captureBothCamera)&&(liveViewListener.isImageReceived()))
            {
                // ライブビュー画像を保管する場合...
                val thread = Thread { storeImage.doStore(cameraPositionId, false) }
                try
                {
                    thread.start()
                }
                catch (e: Exception)
                {
                    e.printStackTrace()
                }
            }
            return (notUseShutter)
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
        return (false)
    }

    override fun needRotateImage(): Boolean { return (false) }

    override fun setUseGR2Command(useGR2Command: Boolean, useCameraScreen: Boolean)
    {
        try
        {
            Log.v(TAG, " setUseGR2Command : $useGR2Command , $useCameraScreen")
            this.useGR2Command = useGR2Command
            this.useCameraScreen = useCameraScreen
            captureControl?.setUseGR2Command(useGR2Command)
            focusControl?.setUseGR2Command(useGR2Command)
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
    }

    override fun injectDisplay(frameDisplayer: IAutoFocusFrameDisplay, indicator: IIndicatorControl, focusingModeNotify: IFocusingModeNotify)
    {
        Log.v(TAG, "injectDisplay()")
        focusControl = RicohGr2CameraFocusControl(frameDisplayer, indicator)
        captureControl = RicohGr2CameraCaptureControl(pentaxCaptureAfterAf, frameDisplayer, statusChecker)
        captureControl?.setUseGR2Command(useGR2Command)
        focusControl?.setUseGR2Command(useGR2Command)
    }

    override fun handleKeyDown(keyCode: Int, event: KeyEvent): Boolean
    {
        if ((event.action == KeyEvent.ACTION_DOWN)&&((keyCode == KeyEvent.KEYCODE_VOLUME_UP)||(keyCode == KeyEvent.KEYCODE_CAMERA)))
        {
            doShutter()
            return (true)
        }
        return (false)
    }

    override fun onLongClick(v: View?): Boolean
    {
        return (false)
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

}