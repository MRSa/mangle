package jp.osdn.gokigen.gokigenassets.camera.console

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import jp.osdn.gokigen.gokigenassets.camera.ICameraPreferenceProvider
import jp.osdn.gokigen.gokigenassets.camera.interfaces.*
import jp.osdn.gokigen.gokigenassets.camera.theta.status.ICaptureModeReceiver
import jp.osdn.gokigen.gokigenassets.liveview.*
import jp.osdn.gokigen.gokigenassets.liveview.focusframe.IAutoFocusFrameDisplay
import jp.osdn.gokigen.gokigenassets.scene.IInformationReceiver
import jp.osdn.gokigen.gokigenassets.scene.IVibrator

class ConsolePanelControl (private val context: AppCompatActivity, private val vibrator : IVibrator, informationNotify: IInformationReceiver, private val preference: ICameraPreferenceProvider) : IDisplayInjector,
    ILiveViewController, ICameraControl, View.OnClickListener, View.OnLongClickListener, ICaptureModeReceiver, ICameraShutter, IKeyDown, IAnotherDrawer, View.OnTouchListener, ICameraStatus
{
    private lateinit var refresher: ILiveViewRefresher

    private var camera0: ICameraControl? = null
    private var camera1: ICameraControl? = null
    private var camera2: ICameraControl? = null
    private var camera3: ICameraControl? = null

    companion object
    {
        private val TAG = ConsolePanelControl::class.java.simpleName
    }

    override fun getConnectionMethod(): String { return ("CONSOLE") }
    override fun startLiveView() { }
    override fun stopLiveView() { }
    override fun connectToCamera() { }
    override fun startCamera(isPreviewView: Boolean, cameraSequence: Int) { }
    override fun finishCamera() { }
    override fun changeCaptureMode(mode: String) { }
    override fun needRotateImage(): Boolean { return (false) }
    override fun captureButtonReceiver(id: Int): View.OnClickListener { return (this) }
    override fun onLongClickReceiver(id: Int): View.OnLongClickListener { return (this) }
    override fun keyDownReceiver(id: Int): IKeyDown { return (this) }
    override fun getFocusingControl(id: Int): IFocusingControl? { return (null) }
    override fun getDisplayInjector(): IDisplayInjector { return (this) }
    override fun changedCaptureMode(captureMode: String) { }
    override fun doShutter() { }
    override fun doShutterOff() { }
    override fun getAnotherTouchListener(id : Int) : View.OnTouchListener { return (this) }
    override fun setNeighborCameraControl(camera0: ICameraControl?, camera1: ICameraControl?, camera2: ICameraControl?, camera3: ICameraControl?)
    {
        this.camera0 = camera0
        this.camera1 = camera1
        this.camera2 = camera2
        this.camera3 = camera3
    }

    override fun getCameraStatus(): ICameraStatus {
        TODO("Not yet implemented")
    }

    override fun injectDisplay(frameDisplayer: IAutoFocusFrameDisplay, indicator: IIndicatorControl, focusingModeNotify: IFocusingModeNotify)
    {

    }

    override fun initialize()
    {
        try
        {

        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
    }

    override fun setRefresher(id: Int, refresher: ILiveViewRefresher, imageView: ILiveView, cachePosition: ICachePositionProvider)
    {
        try
        {
            this.refresher = refresher
            imageView.setAnotherDrawer(this)
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
    }


    override fun onClick(v: View?)
    {
        try
        {

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

        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
        return (false)
    }

    override fun handleKeyDown(keyCode: Int, event: KeyEvent): Boolean
    {
        try
        {

        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
        return (false)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean
    {
        Log.v(TAG, " onTouch")
        try
        {
            // 表示を更新する
            if (::refresher.isInitialized)
            {
                refresher.refresh()
            }

        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
        return (false)
    }

    override fun onDraw(canvas: Canvas?)
    {
        if (canvas == null)
        {
            return
        }

        try
        {
            Log.v(TAG, " onDraw")
            canvas.drawARGB(255, 0, 0, 0)

            drawFramingGrid(canvas, Color.GREEN)

        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
    }

    fun drawFramingGrid(canvas: Canvas, color : Int)
    {
        val paint = Paint()
        canvas.width
        paint.color = color
        paint.strokeWidth = 3.0f
        paint.style = Paint.Style.STROKE
        paint.isAntiAlias = true
        val width = canvas.width / 3.0f
        val height = canvas.height / 4.0f
        val rect = RectF(0.0f, 0.0f, canvas.width.toFloat(), canvas.height.toFloat())

        canvas.drawLine(rect.left + width, rect.top, rect.left + width, rect.bottom, paint)
        canvas.drawLine(rect.left + 2.0f * width, rect.top, rect.left + 2.0f * width, rect.bottom, paint)
        canvas.drawLine(rect.left, rect.top + height, rect.right, rect.top + height, paint)
        canvas.drawLine(rect.left, rect.top + 2.0f * height, rect.right, rect.top + 2.0f * height, paint)
        canvas.drawRect(rect, paint)
    }

    override fun getStatusList(key: String): List<String?> { return (ArrayList<String>()) }
    override fun getStatus(key: String): String { return ("") }
    override fun setStatus(key: String, value: String) { }

}
