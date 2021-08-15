package jp.osdn.gokigen.gokigenassets.camera.sony

import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import jp.osdn.gokigen.gokigenassets.camera.ICameraPreferenceProvider
import jp.osdn.gokigen.gokigenassets.camera.interfaces.*
import jp.osdn.gokigen.gokigenassets.constants.IApplicationConstantConvert
import jp.osdn.gokigen.gokigenassets.liveview.ICachePositionProvider
import jp.osdn.gokigen.gokigenassets.liveview.IIndicatorControl
import jp.osdn.gokigen.gokigenassets.liveview.ILiveView
import jp.osdn.gokigen.gokigenassets.liveview.ILiveViewRefresher
import jp.osdn.gokigen.gokigenassets.liveview.focusframe.IAutoFocusFrameDisplay
import jp.osdn.gokigen.gokigenassets.liveview.image.CameraLiveViewListenerImpl
import jp.osdn.gokigen.gokigenassets.liveview.storeimage.StoreImage
import jp.osdn.gokigen.gokigenassets.preference.PreferenceAccessWrapper
import jp.osdn.gokigen.gokigenassets.scene.IInformationReceiver
import jp.osdn.gokigen.gokigenassets.scene.IVibrator
import jp.osdn.gokigen.gokigenassets.camera.sony.liveview.SonyLiveViewControl
import jp.osdn.gokigen.gokigenassets.camera.sony.operation.SonyCameraCaptureControl
import jp.osdn.gokigen.gokigenassets.camera.sony.operation.SonyCameraFocusControl
import jp.osdn.gokigen.gokigenassets.camera.sony.operation.SonyCameraZoomLensControl
import jp.osdn.gokigen.gokigenassets.camera.sony.wrapper.ISonyCameraApi
import jp.osdn.gokigen.gokigenassets.camera.sony.wrapper.ISonyCameraHolder
import jp.osdn.gokigen.gokigenassets.camera.sony.wrapper.connection.SonyCameraConnection
import jp.osdn.gokigen.gokigenassets.camera.sony.wrapper.eventlistener.SonyCameraEventObserver
import jp.osdn.gokigen.gokigenassets.camera.sony.wrapper.ISonyCamera
import jp.osdn.gokigen.gokigenassets.camera.sony.wrapper.SonyCameraApi
import jp.osdn.gokigen.gokigenassets.camera.sony.wrapper.eventlistener.ISonyCameraEventObserver
import jp.osdn.gokigen.gokigenassets.camera.sony.wrapper.eventlistener.SonyStatus
import org.json.JSONObject
import kotlin.collections.ArrayList


class SonyCameraControl(private val context: AppCompatActivity, private val vibrator : IVibrator, private val informationNotify : IInformationReceiver, private val preference: ICameraPreferenceProvider, provider: ICameraStatusReceiver) : ISonyCameraHolder,
    IDisplayInjector, ICameraControl, View.OnClickListener, View.OnLongClickListener, ICameraShutter, IKeyDown
{
    private val sonyCameraStatus = SonyStatus(JSONObject())
    private val liveViewListener = CameraLiveViewListenerImpl(context, informationNotify)
    private val cameraConnection = SonyCameraConnection(context, provider, this)
    private val storeImage = StoreImage(context, liveViewListener)

    private lateinit var cachePositionProvider : ICachePositionProvider
    private lateinit var sonyCamera: ISonyCamera
    private lateinit var sonyCameraApi: ISonyCameraApi
    private lateinit var eventObserver: ISonyCameraEventObserver
    private lateinit var liveViewControl: SonyLiveViewControl
    private lateinit var focusControl: SonyCameraFocusControl
    private lateinit var captureControl: SonyCameraCaptureControl

    private val zoomControl = SonyCameraZoomLensControl()
    private var isStatusWatch = false
    private var cameraPositionId = 0

    companion object
    {
        private val TAG = SonyCameraControl::class.java.simpleName
    }

    override fun prepare()
    {
        if (::sonyCamera.isInitialized)
        {
            Log.v(TAG, " prepare : ${sonyCamera.getFriendlyName()} ${sonyCamera.getModelName()}")
            try
            {
                sonyCameraApi = SonyCameraApi(sonyCamera)
                eventObserver = SonyCameraEventObserver.newInstance(context, sonyCameraApi, sonyCameraStatus)
                liveViewControl = SonyLiveViewControl(context, informationNotify, liveViewListener, sonyCameraApi)
                zoomControl.setCameraApi(sonyCameraApi)
                sonyCameraStatus.setCameraApi(sonyCameraApi)
                if (::focusControl.isInitialized)
                {
                    focusControl.setCameraApi(sonyCameraApi)
                }
                if (::captureControl.isInitialized)
                {
                    captureControl.setCameraApi(sonyCameraApi)
                }
            }
            catch (e: Exception)
            {
                e.printStackTrace()
            }
        }
        else
        {
            Log.w(TAG, " ISonyCamera is not initialized...")
        }
    }

    override fun startRecMode()
    {
        try
        {
            val apiCommands: List<String> = getApiCommands()
            val index = apiCommands.indexOf("startRecMode")
            if (index > 0)
            {
                // startRecMode発行
                Log.v(TAG, "----- THIS CAMERA NEEDS COMMAND 'startRecMode'.")
                sonyCameraApi.startRecMode()
            }
        }
        catch (e: java.lang.Exception)
        {
            e.printStackTrace()
        }
    }

    override fun startEventWatch()
    {
        try
        {
            var retryCount = 2
            if (::eventObserver.isInitialized)
            {
                eventObserver.setEventListener(sonyCameraStatus)
                eventObserver.activate()
                eventObserver.start()
            }
            while (true)
            {
                val holder = eventObserver.getCameraStatusHolder()
                if (holder?.getLiveviewStatus() == true)
                {
                    break
                }
                try
                {
                    Log.v(TAG, " --- WAIT FOR LIVEVIEW ENABLE ---")
                    Thread.sleep(1500)
                    retryCount--
                    if (retryCount < 0)
                    {
                        break
                    }
                }
                catch (e: Exception)
                {
                    e.printStackTrace()
                }
            }
            if (::liveViewControl.isInitialized)
            {
                liveViewControl.startLiveView(false)
            }

        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    override fun detectedCamera(camera: ISonyCamera)
    {
        Log.v(TAG, "detectedCamera()")
        sonyCamera = camera
    }

    override fun getConnectionMethod(): String
    {
        return ("SONY")
    }

    override fun initialize()
    {
        Log.v(TAG, " --- initialize()")
        // TODO("Not yet implemented")
    }

    override fun connectToCamera()
    {
        Log.v(TAG, " connectToCamera() : SONY ")
        try
        {
            cameraConnection.connect()
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
            if (cameraConnection.getConnectionStatus() != ICameraConnectionStatus.CameraConnectionStatus.CONNECTED)
            {
                cameraConnection.startWatchWifiStatus(context)
            }
            else
            {
                cameraConnection.connect()
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
                if (::eventObserver.isInitialized)
                {
                    eventObserver.stop()
                }
                isStatusWatch = false
                if (::liveViewControl.isInitialized)
                {
                    liveViewControl.stopLiveView()
                }
            }
            cameraConnection.disconnect(false)
            cameraConnection.stopWatchWifiStatus(context)
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
    }

    override fun changeCaptureMode(mode: String)
    {
        Log.v(TAG, " --- changeCaptureMode(mode: $mode)")
        // TODO("Not yet implemented")
    }

    override fun needRotateImage(): Boolean
    {
        return (false)
    }

    override fun setRefresher(id: Int, refresher: ILiveViewRefresher, imageView: ILiveView, cachePosition : ICachePositionProvider)
    {
        try
        {
            liveViewListener.setRefresher(refresher)
            imageView.setImageProvider(liveViewListener)
            cachePositionProvider = cachePosition
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

    override fun getFocusingControl(id: Int): IFocusingControl
    {
        cameraPositionId = id
        return (focusControl)
    }

    override fun getDisplayInjector(): IDisplayInjector
    {
        return (this)
    }

    override fun injectDisplay(frameDisplayer: IAutoFocusFrameDisplay, indicator: IIndicatorControl, focusingModeNotify: IFocusingModeNotify)
    {
        Log.v(TAG, "injectDisplay()")
        focusControl = SonyCameraFocusControl(frameDisplayer, indicator)
        captureControl = SonyCameraCaptureControl(frameDisplayer, indicator)
    }
/*
    override fun startLiveView(isCameraScreen: Boolean)
    {
        Log.v(TAG, " startLiveView($isCameraScreen) ")
        try
        {
            if (!isStatusWatch)
            {
                if (::eventObserver.isInitialized)
                {
                    eventObserver.activate()
                    eventObserver.start()
                    val holder = eventObserver.getCameraStatusHolder()
                    holder?.getLiveviewStatus()
                    isStatusWatch = true
                }
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
                if (::eventObserver.isInitialized)
                {
                    eventObserver.stop()
                    isStatusWatch = false
                }
            }
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
    }
*/

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
            if (::captureControl.isInitialized)
            {
                captureControl.doCapture(0)
            }
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
            if (::captureControl.isInitialized)
            {
                captureControl.doCapture(0)
            }
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
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

    private fun captureImageLiveView() : Boolean
    {
        try
        {
            //  preferenceから設定を取得する
            val captureBothCamera = PreferenceAccessWrapper(context).getBoolean(
                IApplicationConstantConvert.ID_PREFERENCE_CAPTURE_BOTH_CAMERA_AND_LIVE_VIEW, IApplicationConstantConvert.ID_PREFERENCE_CAPTURE_BOTH_CAMERA_AND_LIVE_VIEW_DEFAULT_VALUE)
            val notUseShutter = PreferenceAccessWrapper(context).getBoolean(
                IApplicationConstantConvert.ID_PREFERENCE_CAPTURE_ONLY_LIVEVIEW_IMAGE, IApplicationConstantConvert.ID_PREFERENCE_CAPTURE_ONLY_LIVEVIEW_IMAGE_DEFAULT_VALUE)
            if ((captureBothCamera)&&(liveViewListener.isImageReceived()))
            {
                // ライブビュー画像を保管する場合...
                val thread = Thread { storeImage.doStore(cameraPositionId, false, cachePositionProvider.getCachePosition()) }
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

    private fun getApiCommands(): List<String>
    {
        Log.v(TAG, " --- getApiCommands() ")
        try
        {
            var apiList = sonyCameraApi.getAvailableApiList()?.getString("result")
            apiList = apiList?.replace("[", "")?.replace("]", "")?.replace("\"", "")
            val apiListSplit = apiList?.split(",".toRegex())?.toTypedArray()
            return (apiListSplit?.toList() ?: ArrayList())
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        return (ArrayList())
    }

    override fun onLongClick(v: View?): Boolean
    {
        return (false)
    }

    override fun setNeighborCameraControl(camera0: ICameraControl?, camera1: ICameraControl?, camera2: ICameraControl?, camera3: ICameraControl?) { }
    override fun getCameraStatus(): ICameraStatus
    {
        return (sonyCameraStatus)
    }
}
