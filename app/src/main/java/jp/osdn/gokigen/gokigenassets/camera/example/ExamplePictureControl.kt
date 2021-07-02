package jp.osdn.gokigen.gokigenassets.camera.example

import android.R.attr
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
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
import jp.osdn.gokigen.gokigenassets.liveview.IIndicatorControl
import jp.osdn.gokigen.gokigenassets.liveview.ILiveView
import jp.osdn.gokigen.gokigenassets.liveview.ILiveViewRefresher
import jp.osdn.gokigen.gokigenassets.liveview.focusframe.IAutoFocusFrameDisplay
import jp.osdn.gokigen.gokigenassets.liveview.image.CameraLiveViewListenerImpl
import jp.osdn.gokigen.gokigenassets.scene.IVibrator
import java.io.ByteArrayOutputStream
import java.io.File
import android.R.attr.data
import java.io.InputStream


class ExamplePictureControl(private val context: AppCompatActivity, private val vibrator : IVibrator, private val preference: ICameraPreferenceProvider) : IDisplayInjector, ILiveViewController, ICameraControl, View.OnClickListener, View.OnLongClickListener, ICaptureModeReceiver, ICameraShutter, IKeyDown
{
    private val liveViewListener = CameraLiveViewListenerImpl(context, isDisableCache = true)
    private lateinit var refresher : ILiveViewRefresher

    private var startForResult : ActivityResultLauncher<Intent> = context.registerForActivityResult(StartActivityForResult()) { result : ActivityResult ->
                if (result.resultCode == Activity.RESULT_OK)
                {
                    try
                    {
                        val uri = result.data?.data
                        if (uri != null)
                        {
                            val fis: InputStream? = context.contentResolver.openInputStream(uri)
                            if (fis != null)
                            {
                                val bos = ByteArrayOutputStream()
                                var data: Int
                                while (fis.read().also { data = it } != -1)
                                {
                                    bos.write(data)
                                }
                                liveViewListener.onUpdateLiveView(bos.toByteArray(), null)
                                if (::refresher.isInitialized)
                                {
                                    refresher.refresh()
                                }
                            }
                        }
/*
                        var filePath = ""
                        Log.v(TAG, " RECEIVED INTENT : $intent, $intent?.data")
                        val projection = arrayOf(MediaStore.MediaColumns.DATA)
                        val uri = intent?.data
                        if (uri != null)
                        {
                            val cursor = context.contentResolver.query(uri, projection, null, null, null)
                            if (cursor != null)
                            {
                                if (cursor.count > 0)
                                {
                                    cursor.moveToNext()
                                    filePath = cursor.getString(0)
                                }
                                cursor.close()
                            }
                        }
                        Log.v(TAG, "FILE PATH : $filePath")
                        if (applyPictureFile(filePath))
                        {
                            vibrator.vibrate(IVibrator.VibratePattern.SIMPLE_SHORT)
                        }
*/
                    }
                    catch (e : Exception)
                    {
                        e.printStackTrace()
                    }
                }
                else
                {
                    Log.v(TAG, " ACTIVITY RESULT NG : $result.resultCode")
                }
            }

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
            this.refresher = refresher
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
        try
        {
            vibrator.vibrate(IVibrator.VibratePattern.SIMPLE_LONG)
            selectImageFileFromGallery()
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
        return (true)
    }

    private fun selectImageFileFromGallery()
    {
        //val intent = Intent(Intent.ACTION_PICK)
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startForResult.launch(intent)
    }

    private fun applyPictureFile(filePath : String) : Boolean
    {
        Log.v(TAG, "applyPictureFile($filePath)")
        try
        {
            val file = File(filePath)
            if ((!file.exists())||(!file.isFile))
            {
                Log.v(TAG, "applyPictureFile() is not exist : $file")
                return (false)
            }
            val fis = file.inputStream()
            val bos = ByteArrayOutputStream()

            var data: Int
            while (fis.read().also { data = it } != -1) {
                bos.write(data)
            }
            liveViewListener.onUpdateLiveView(bos.toByteArray(), null)
            if (::refresher.isInitialized)
            {
                refresher.refresh()
            }
            return (true)
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
        return (false)
    }

    companion object
    {
        private val TAG = ExamplePictureControl::class.java.simpleName
    }

}
