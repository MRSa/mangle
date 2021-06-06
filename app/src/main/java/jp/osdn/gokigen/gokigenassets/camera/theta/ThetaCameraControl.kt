package jp.osdn.gokigen.gokigenassets.camera.theta

import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import jp.osdn.gokigen.gokigenassets.camera.ICameraPreferenceProvider
import jp.osdn.gokigen.gokigenassets.camera.interfaces.ICameraConnectionStatus
import jp.osdn.gokigen.gokigenassets.camera.interfaces.ICameraStatusReceiver
import jp.osdn.gokigen.gokigenassets.camera.interfaces.ILiveViewController
import jp.osdn.gokigen.gokigenassets.camera.interfaces.ICameraShutter
import jp.osdn.gokigen.gokigenassets.camera.theta.connection.ThetaCameraConnection
import jp.osdn.gokigen.gokigenassets.camera.theta.liveview.ThetaLiveViewControl
import jp.osdn.gokigen.gokigenassets.camera.theta.operation.ThetaMovieRecordingControl
import jp.osdn.gokigen.gokigenassets.camera.theta.operation.ThetaOptionSetControl
import jp.osdn.gokigen.gokigenassets.camera.theta.operation.ThetaSingleShotControl
import jp.osdn.gokigen.gokigenassets.camera.theta.status.ICaptureModeReceiver
import jp.osdn.gokigen.gokigenassets.camera.theta.status.ThetaCameraStatusWatcher
import jp.osdn.gokigen.gokigenassets.camera.theta.status.ThetaSessionHolder
import jp.osdn.gokigen.gokigenassets.constants.IApplicationConstantConvert.Companion.ID_BUTTON_SHUTTER
import jp.osdn.gokigen.gokigenassets.constants.IApplicationConstantConvert.Companion.ID_PREFERENCE_CAPTURE_BOTH_CAMERA_AND_LIVE_VIEW
import jp.osdn.gokigen.gokigenassets.constants.IApplicationConstantConvert.Companion.ID_PREFERENCE_CAPTURE_BOTH_CAMERA_AND_LIVE_VIEW_DEFAULT_VALUE
import jp.osdn.gokigen.gokigenassets.constants.IApplicationConstantConvert.Companion.ID_PREFERENCE_CAPTURE_ONLY_EXTERNAL_CAMERA
import jp.osdn.gokigen.gokigenassets.constants.IApplicationConstantConvert.Companion.ID_PREFERENCE_CAPTURE_ONLY_EXTERNAL_CAMERA_DEFAULT_VALUE
import jp.osdn.gokigen.gokigenassets.liveview.ILiveView
import jp.osdn.gokigen.gokigenassets.liveview.ILiveViewRefresher
import jp.osdn.gokigen.gokigenassets.liveview.image.CameraLiveViewListenerImpl
import jp.osdn.gokigen.gokigenassets.liveview.message.IMessageDrawer
import jp.osdn.gokigen.gokigenassets.liveview.storeimage.StoreImage
import jp.osdn.gokigen.gokigenassets.camera.interfaces.ICameraControl
import jp.osdn.gokigen.gokigenassets.preference.PreferenceAccessWrapper
import jp.osdn.gokigen.gokigenassets.scene.IVibrator

class ThetaCameraControl(private val context: AppCompatActivity, private val vibrator : IVibrator, private val preference: ICameraPreferenceProvider, statusReceiver : ICameraStatusReceiver) : ILiveViewController,
    ICameraControl, View.OnClickListener, ICaptureModeReceiver, ICameraShutter
{
    private val sessionIdHolder = ThetaSessionHolder()
    private val cameraConnection = ThetaCameraConnection(context, statusReceiver, sessionIdHolder, sessionIdHolder, this)
    private var liveViewListener = CameraLiveViewListenerImpl(context)
    private val liveViewControl = ThetaLiveViewControl(liveViewListener)
    private var indicator : IMessageDrawer? = null

    private val statusWatcher = ThetaCameraStatusWatcher(sessionIdHolder, this)
    private var isStatusWatch = false
    private var isMovieRecording = false
    private val storeImage = StoreImage(context, liveViewListener)
    private var cameraPositionId = 0

    fun setIndicator(indicator : IMessageDrawer)
    {
        this.indicator = indicator
    }

    override fun getConnectionMethod(): String
    {
        return ("THETA")
    }

    override fun changeCaptureMode(mode : String)
    {
        val options = if (statusWatcher.captureMode.contains("image"))
        {
            // image -> video
            "\"captureMode\" : \"video\""
        }
        else
        {
            // video -> image
            "\"captureMode\" : \"image\""
        }
        ThetaOptionSetControl(sessionIdHolder).setOptions(options, sessionIdHolder.isApiLevelV21())
    }

    override fun changedCaptureMode(captureMode : String)
    {
        Log.v(TAG, " changedCaptureMode() : $captureMode")
/*
        try
        {
            val isImage = captureMode.contains("image")
            context.runOnUiThread {
                try
                {
                    val view : ImageButton = context.findViewById(ID_BUTTON_SHUTTER)
                    val iconId = if (isImage) { R.drawable.ic_baseline_videocam_24 } else { R.drawable.ic_baseline_camera_alt_24 }
                    view.setImageDrawable(ContextCompat.getDrawable(context, iconId))
                    view.invalidate()
                }
                catch (e : Exception)
                {
                    e.printStackTrace()
                }
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
*/
    }

    override fun initialize()
    {
        // TODO("Not yet implemented")
    }

    override fun startCamera(isPreviewView : Boolean, cameraSelector : CameraSelector)
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
                statusWatcher.stopStatusWatch()
                isStatusWatch = false
            }
            cameraConnection.disconnect(false)
            cameraConnection.stopWatchWifiStatus(context)
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
    }

    override fun connectToCamera()
    {
        Log.v(TAG, " connectToCamera() : THETA ")
        try
        {
            cameraConnection.connect()
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

    override fun onClick(v: View?)
    {
        if (v == null)
        {
            return
        }
        when (v.id)
        {
            ID_BUTTON_SHUTTER -> { doShutter() }
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
            if (statusWatcher.captureMode.contains("image"))
            {
                // image
                ThetaSingleShotControl(sessionIdHolder, vibrator, liveViewControl, statusWatcher).singleShot(sessionIdHolder.isApiLevelV21())
            }
            else
            {
                // video
                ThetaMovieRecordingControl(sessionIdHolder, vibrator, liveViewControl, statusWatcher).movieControl(sessionIdHolder.isApiLevelV21())
                isMovieRecording = true
            }
        }
        catch (e : Exception)
        {
            e.printStackTrace()
            isMovieRecording = false
        }
    }

    override fun doShutterOff()
    {
        try
        {
            if ((isMovieRecording)&&(!statusWatcher.captureMode.contains("image")))
            {
                // video
                Log.v(TAG, " doShutterOff()")
                ThetaMovieRecordingControl(sessionIdHolder, vibrator, liveViewControl, statusWatcher).movieControl(sessionIdHolder.isApiLevelV21())
            }
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
        isMovieRecording = false
    }

    private fun captureImageLiveView() : Boolean
    {
        try
        {
            //  preferenceから設定を取得する
            val captureBothCamera = PreferenceAccessWrapper(context).getBoolean(
                ID_PREFERENCE_CAPTURE_BOTH_CAMERA_AND_LIVE_VIEW,
                ID_PREFERENCE_CAPTURE_BOTH_CAMERA_AND_LIVE_VIEW_DEFAULT_VALUE
            )
            val notUseShutter = PreferenceAccessWrapper(context).getBoolean(
                ID_PREFERENCE_CAPTURE_ONLY_EXTERNAL_CAMERA,
                ID_PREFERENCE_CAPTURE_ONLY_EXTERNAL_CAMERA_DEFAULT_VALUE
            )
            if ((captureBothCamera)&&(liveViewListener.isImageReceived()))
            {
                // ライブビュー画像を保管する場合...
                val thread = Thread { storeImage.doStore(cameraPositionId, true) }
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

    companion object
    {
        private val TAG = ThetaCameraControl::class.java.simpleName
    }

    override fun startLiveView()
    {
        Log.v(TAG, " startLiveView() ")
        try
        {
            if (!isStatusWatch)
            {
                statusWatcher.startStatusWatch(indicator, null)
                isStatusWatch = true
            }
            liveViewControl.setSessionIdProvider(sessionIdHolder)
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
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
    }

    override fun captureButtonReceiver(id : Int) : View.OnClickListener
    {
        cameraPositionId = id
        return (this)
    }

}