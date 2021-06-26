package jp.osdn.gokigen.gokigenassets.camera.panasonic.wrapper

import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import jp.osdn.gokigen.gokigenassets.camera.interfaces.*
import jp.osdn.gokigen.gokigenassets.camera.panasonic.ICameraChangeListener
import jp.osdn.gokigen.gokigenassets.camera.panasonic.IPanasonicCamera
import jp.osdn.gokigen.gokigenassets.camera.panasonic.IPanasonicCameraHolder
import jp.osdn.gokigen.gokigenassets.camera.panasonic.connection.PanasonicCameraConnection
import jp.osdn.gokigen.gokigenassets.camera.panasonic.liveview.PanasonicLiveViewControl
import jp.osdn.gokigen.gokigenassets.camera.panasonic.operation.PanasonicCameraCaptureControl
import jp.osdn.gokigen.gokigenassets.camera.panasonic.operation.PanasonicCameraFocusControl
import jp.osdn.gokigen.gokigenassets.camera.panasonic.operation.PanasonicCameraZoomLensControl
import jp.osdn.gokigen.gokigenassets.camera.panasonic.status.CameraEventObserver
import jp.osdn.gokigen.gokigenassets.camera.panasonic.status.ICameraEventObserver
import jp.osdn.gokigen.gokigenassets.camera.theta.status.ICaptureModeReceiver
import jp.osdn.gokigen.gokigenassets.liveview.IIndicatorControl
import jp.osdn.gokigen.gokigenassets.liveview.ILiveView
import jp.osdn.gokigen.gokigenassets.liveview.ILiveViewRefresher
import jp.osdn.gokigen.gokigenassets.liveview.focusframe.IAutoFocusFrameDisplay
import jp.osdn.gokigen.gokigenassets.utils.communication.SimpleHttpClient

class PanasonicCameraWrapper(private val context: AppCompatActivity, private val provider: ICameraStatusReceiver, private val listener: ICameraChangeListener, private val cardSlotSelector: ICardSlotSelector) : IPanasonicCameraHolder, IDisplayInjector,ILiveViewController, ICameraControl, View.OnClickListener, ICaptureModeReceiver, ICameraShutter, IKeyDown
{
    private lateinit var panasonicCamera: IPanasonicCamera

    private var eventObserver: ICameraEventObserver? = null
    private var liveViewControl: PanasonicLiveViewControl? = null
    private var focusControl: PanasonicCameraFocusControl? = null
    private var captureControl: PanasonicCameraCaptureControl? = null
    private var zoomControl: PanasonicCameraZoomLensControl? = null
    private var cameraConnection: PanasonicCameraConnection? = null

    companion object
    {
        private val TAG = PanasonicCameraWrapper::class.java.simpleName
        private const val TIMEOUT_MS = 3000
    }

    override fun prepare()
    {
        Log.v(TAG, " prepare : " + panasonicCamera.getFriendlyName() + " " + panasonicCamera.getModelName())
        try {
            //this.panasonicCameraApi = PanasonicCameraApi.newInstance(panasonicCamera);
            if (eventObserver == null)
            {
                eventObserver = CameraEventObserver(context, panasonicCamera, cardSlotSelector)
            }
            if (liveViewControl == null)
            {
                liveViewControl = PanasonicLiveViewControl(context, panasonicCamera)
            }
            focusControl?.setCamera(panasonicCamera)
            captureControl?.setCamera(panasonicCamera)
            zoomControl?.setCamera(panasonicCamera)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun startRecMode()
    {
        try
        {
            val http = SimpleHttpClient()

            // 撮影モード(RecMode)に切り替え
            var reply: String = http.httpGet(panasonicCamera.getCmdUrl() + "cam.cgi?mode=camcmd&value=recmode", TIMEOUT_MS)
            if (!reply.contains("ok"))
            {
                Log.v(TAG, "CAMERA REPLIED ERROR : CHANGE RECMODE.")
            }

            //  フォーカスに関しては、１点に切り替える（仮）
            reply = http.httpGet(panasonicCamera.getCmdUrl() + "cam.cgi?mode=setsetting&type=afmode&value=1area", TIMEOUT_MS)
            if (!reply.contains("ok"))
            {
                Log.v(TAG, "CAMERA REPLIED ERROR : CHANGE AF MODE 1area.")
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    override fun startEventWatch(listener: ICameraChangeListener?)
    {
        try
        {
            if (eventObserver != null)
            {
                if (listener != null)
                {
                    eventObserver?.setEventListener(listener)
                }
                eventObserver?.activate()
                eventObserver?.start()
                val holder = eventObserver?.getCameraStatusHolder()
                holder?.getLiveviewStatus()
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    override fun detectedCamera(camera: IPanasonicCamera)
    {
        Log.v(TAG, "detectedCamera()")
        panasonicCamera = camera
    }

    fun getPanasonicCameraConnection(): ICameraConnection? {
        // PanasonicCameraConnectionは複数生成しない。
        if (cameraConnection == null) {
            cameraConnection = PanasonicCameraConnection(context, provider, this, this, listener)
        }
        return cameraConnection
    }
/*
    fun getPanasonicCamera(): IPanasonicCamera
    {
        return panasonicCamera
    }

    fun getPanasonicLiveViewControl(): ILiveViewControl? {
        return liveViewControl
    }


    fun getLiveViewListener(): ILiveViewListener? {
        return liveViewControl?.getLiveViewListener()
    }
*/

    fun getFocusingControl(): IFocusingControl? {
        return focusControl
    }

    fun getCameraInformation(): ICameraInformation? {
        return null
    }

    fun getZoomLensControl(): IZoomLensControl? {
        return zoomControl
    }

    fun getCaptureControl(): ICaptureControl? {
        return captureControl
    }

    override fun getConnectionMethod(): String {
        TODO("Not yet implemented")
    }

    override fun initialize() {
        TODO("Not yet implemented")
    }

    override fun connectToCamera() {
        TODO("Not yet implemented")
    }

    override fun startCamera(isPreviewView: Boolean, cameraSequence: Int) {
        TODO("Not yet implemented")
    }

    override fun finishCamera() {
        TODO("Not yet implemented")
    }

    override fun changeCaptureMode(mode: String) {
        TODO("Not yet implemented")
    }

    override fun needRotateImage(): Boolean {
        TODO("Not yet implemented")
    }

    override fun setRefresher(refresher: ILiveViewRefresher, imageView: ILiveView) {
        TODO("Not yet implemented")
    }

    override fun captureButtonReceiver(id: Int): View.OnClickListener {
        TODO("Not yet implemented")
    }

    override fun keyDownReceiver(id: Int): IKeyDown {
        TODO("Not yet implemented")
    }

    override fun getFocusingControl(id: Int): IFocusingControl? {
        TODO("Not yet implemented")
    }

    override fun getDisplayInjector(): IDisplayInjector
    {
        return this
    }

    override fun injectDisplay(frameDisplayer: IAutoFocusFrameDisplay, indicator: IIndicatorControl, focusingModeNotify: IFocusingModeNotify)
    {
        Log.v(TAG, "injectDisplay()")
        focusControl = PanasonicCameraFocusControl(frameDisplayer, indicator)
        captureControl = PanasonicCameraCaptureControl(frameDisplayer, indicator)
        zoomControl = PanasonicCameraZoomLensControl()
    }

    override fun startLiveView() {
        TODO("Not yet implemented")
    }

    override fun stopLiveView() {
        TODO("Not yet implemented")
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }

    override fun changedCaptureMode(captureMode: String) {
        TODO("Not yet implemented")
    }

    override fun doShutter() {
        TODO("Not yet implemented")
    }

    override fun doShutterOff() {
        TODO("Not yet implemented")
    }

    override fun handleKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        TODO("Not yet implemented")
    }
}
