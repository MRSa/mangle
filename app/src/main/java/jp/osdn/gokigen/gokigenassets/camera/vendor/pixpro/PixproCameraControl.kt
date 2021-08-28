package jp.osdn.gokigen.gokigenassets.camera.vendor.pixpro

import android.graphics.Color
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import jp.osdn.gokigen.gokigenassets.camera.interfaces.*
import jp.osdn.gokigen.gokigenassets.camera.preference.ICameraPreferenceProvider
import jp.osdn.gokigen.gokigenassets.camera.vendor.pixpro.wrapper.PixproCamera
import jp.osdn.gokigen.gokigenassets.camera.vendor.pixpro.wrapper.command.IPixproCommandPublisher
import jp.osdn.gokigen.gokigenassets.camera.vendor.pixpro.wrapper.command.IPixproCommunication
import jp.osdn.gokigen.gokigenassets.camera.vendor.pixpro.wrapper.command.PixproCommandCommunicator
import jp.osdn.gokigen.gokigenassets.liveview.ICachePositionProvider
import jp.osdn.gokigen.gokigenassets.liveview.ILiveView
import jp.osdn.gokigen.gokigenassets.liveview.ILiveViewRefresher
import jp.osdn.gokigen.gokigenassets.scene.IInformationReceiver
import jp.osdn.gokigen.gokigenassets.scene.IVibrator


class PixproCameraControl(private val context: AppCompatActivity, private val vibrator: IVibrator, private val informationNotify : IInformationReceiver, private val preference: ICameraPreferenceProvider, provider: ICameraStatusReceiver) : ICameraControl, View.OnClickListener, View.OnLongClickListener, IKeyDown, ICameraStatus, IPixproInternalInterfaces, PixproCommandCommunicator.ICommunicationNotify
{
    private val pixproCameraParameter = PixproCamera()
    private val commandCommunicator = PixproCommandCommunicator(pixproCameraParameter, this)

    companion object
    {
        private val TAG = PixproCameraControl::class.java.simpleName
    }

    override fun getConnectionMethod(): String { return ("PIXPRO") }
    override fun initialize() { }
    override fun connectToCamera() { }
    override fun startCamera(isPreviewView: Boolean, cameraSequence : Int) { }
    override fun finishCamera() { }
    override fun changeCaptureMode(mode: String) { }
    override fun needRotateImage(): Boolean { return (false) }
    override fun setRefresher(id : Int, refresher: ILiveViewRefresher, imageView: ILiveView, cachePosition : ICachePositionProvider) { }
    override fun captureButtonReceiver(id: Int): View.OnClickListener { return (this) }
    override fun onLongClickReceiver(id: Int): View.OnLongClickListener { return (this) }
    override fun keyDownReceiver(id: Int): IKeyDown { return (this) }
    override fun getFocusingControl(id: Int): IFocusingControl? { return (null) }
    override fun getDisplayInjector(): IDisplayInjector? { return (null) }
    override fun setNeighborCameraControl(camera0: ICameraControl?, camera1: ICameraControl?, camera2: ICameraControl?, camera3: ICameraControl?) { }
    override fun getCameraStatus(): ICameraStatus { return (this) }

    override fun onClick(v: View?) { }
    override fun handleKeyDown(keyCode: Int, event: KeyEvent): Boolean { return (false) }
    override fun onLongClick(v: View?): Boolean { return (false) }

    override fun getStatusList(key: String): List<String?> { return (ArrayList<String>()) }
    override fun getStatus(key: String): String { return ("") }
    override fun getStatusColor(key: String): Int { return (Color.WHITE) }
    override fun setStatus(key: String, value: String) { }


    override fun getIPixproCommunication(): IPixproCommunication { return (commandCommunicator) }
    override fun getIPixproCommandPublisher(): IPixproCommandPublisher { return (commandCommunicator) }
    override fun getInformationReceiver(): IInformationReceiver { return (informationNotify) }

    override fun detectDisconnect()
    {

/*
    private fun detectDisconnect()
    {
        val connection: ICameraConnection = interfaceProvider.getCameraConnection()
        if (connection != null)
        {
            // 回線状態を「切断」にしてダイアログを表示する
            connection.forceUpdateConnectionStatus(ICameraConnection.CameraConnectionStatus.DISCONNECTED)
            connection.alertConnectingFailed(interfaceProvider.getStringFromResource(R.string.kodak_command_line_disconnected))
        }
    }
 */
        //TODO("Not yet implemented")
    }
}
