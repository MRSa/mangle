package jp.osdn.gokigen.gokigenassets.camera.example

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import jp.osdn.gokigen.gokigenassets.camera.ICameraPreferenceProvider
import jp.osdn.gokigen.gokigenassets.camera.interfaces.*
import jp.osdn.gokigen.gokigenassets.camera.theta.status.ICaptureModeReceiver
import jp.osdn.gokigen.gokigenassets.liveview.ICachePositionProvider
import jp.osdn.gokigen.gokigenassets.liveview.IIndicatorControl
import jp.osdn.gokigen.gokigenassets.liveview.ILiveView
import jp.osdn.gokigen.gokigenassets.liveview.ILiveViewRefresher
import jp.osdn.gokigen.gokigenassets.liveview.focusframe.IAutoFocusFrameDisplay
import jp.osdn.gokigen.gokigenassets.liveview.image.CameraLiveViewListenerImpl
import jp.osdn.gokigen.gokigenassets.scene.IInformationReceiver
import jp.osdn.gokigen.gokigenassets.scene.IVibrator
import java.io.InputStream


class ExamplePictureControl(private val context: AppCompatActivity, private val vibrator : IVibrator, informationNotify: IInformationReceiver, private val preference: ICameraPreferenceProvider) : IDisplayInjector, ILiveViewController, ICameraControl, View.OnClickListener, View.OnLongClickListener, ICaptureModeReceiver, ICameraShutter, IKeyDown, ICameraStatus
{
    private val liveViewListener = CameraLiveViewListenerImpl(context, informationNotify, isDisableCache = true)
    private lateinit var refresher : ILiveViewRefresher

    private var startForResult : ActivityResultLauncher<Intent> = context.registerForActivityResult(StartActivityForResult()) { result : ActivityResult ->
                if (result.resultCode == Activity.RESULT_OK)
                {
                    try
                    {
                        vibrator.vibrate(IVibrator.VibratePattern.SIMPLE_SHORT)
                        val uri = result.data?.data
                        if (uri != null)
                        {
                            applyPictureFile(uri, isStoreUri = true)
                        }
                    }
                    catch (e : Exception)
                    {
                        e.printStackTrace()
                    }
                }
                else
                {
                    Log.v(TAG, " ACTIVITY RESULT is NG. : $result.resultCode")
                }
            }

    private fun selectImageFileFromGallery()
    {
        //val intent = Intent(Intent.ACTION_PICK)
        //val intent = Intent(Intent.ACTION_GET_CONTENT)
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        startForResult.launch(intent)
    }

    override fun injectDisplay(frameDisplayer: IAutoFocusFrameDisplay, indicator: IIndicatorControl, focusingModeNotify: IFocusingModeNotify) { }
    override fun initialize() {  }
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
    override fun setNeighborCameraControl(camera0: ICameraControl?, camera1: ICameraControl?, camera2: ICameraControl?, camera3: ICameraControl?) { }
    override fun getCameraStatus(): ICameraStatus { return (this) }
    override fun getStatusList(key: String): List<String?> { return (ArrayList<String>()) }
    override fun getStatus(key: String): String { return ("") }
    override fun setStatus(key: String, value: String) { }

    override fun setRefresher(id: Int, refresher: ILiveViewRefresher, imageView: ILiveView, cachePosition : ICachePositionProvider)
    {
        try
        {
            this.refresher = refresher
            liveViewListener.setRefresher(refresher)
            imageView.setImageProvider(liveViewListener)

            // OPTION1 に入っている content:// 情報から、作例を展開（画面表示）する
            val option1 = preference.getCameraOption1()
            if ((option1.isNotEmpty())&&(option1.contains("content://")))
            {
                applyPictureFile(Uri.parse(option1))
            }
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
    }

    override fun onLongClick(v: View?): Boolean
    {
        try
        {
            vibrator.vibrate(IVibrator.VibratePattern.SIMPLE_MIDDLE)
            selectImageFileFromGallery()
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
        return (true)
    }

    private fun applyPictureFile(uri : Uri, isStoreUri : Boolean = false)
    {
        Log.v(TAG, " applyPictureFile(URI: $uri , storeUri: $isStoreUri)")
        val thread = Thread {
            try
            {
                val fis: InputStream? = context.contentResolver.openInputStream(uri)
                if (fis != null)
                {
                    if (isStoreUri)
                    {
                        preference.getUpdater()?.setCameraOption1(uri.toString())
                        vibrator.vibrate(IVibrator.VibratePattern.SIMPLE_LONG)
                    }
                    liveViewListener.onUpdateLiveView(fis.readBytes(), null)
                    if (::refresher.isInitialized)
                    {
                        refresher.refresh()
                    }
                }
            }
            catch (e: Throwable)
            {
                e.printStackTrace()
            }
        }
        thread.start()
    }

    companion object
    {
        private val TAG = ExamplePictureControl::class.java.simpleName
    }
}
