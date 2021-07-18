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
import jp.osdn.gokigen.gokigenassets.constants.ICameraConnectionMethods.Companion.PREFERENCE_CAMERA_METHOD_CAMERAX
import jp.osdn.gokigen.gokigenassets.constants.ICameraConnectionMethods.Companion.PREFERENCE_CAMERA_METHOD_PANASONIC
import jp.osdn.gokigen.gokigenassets.constants.ICameraConnectionMethods.Companion.PREFERENCE_CAMERA_METHOD_PENTAX
import jp.osdn.gokigen.gokigenassets.constants.ICameraConnectionMethods.Companion.PREFERENCE_CAMERA_METHOD_THETA
import jp.osdn.gokigen.gokigenassets.liveview.*
import jp.osdn.gokigen.gokigenassets.liveview.focusframe.IAutoFocusFrameDisplay
import jp.osdn.gokigen.gokigenassets.scene.IInformationReceiver
import jp.osdn.gokigen.gokigenassets.scene.IVibrator

class ConsolePanelControl (private val context: AppCompatActivity, private val vibrator : IVibrator, informationNotify: IInformationReceiver, private val preference: ICameraPreferenceProvider) : IDisplayInjector,
    ILiveViewController, ICameraControl, View.OnClickListener, View.OnLongClickListener, ICaptureModeReceiver, ICameraShutter, IKeyDown, IAnotherDrawer, View.OnTouchListener, ICameraStatus
{
    private lateinit var refresher: ILiveViewRefresher

    private var currentCameraControlId : Int = -1
    private var currentCameraControl : ICameraControl? = null
    private var camera0: ICameraControl? = null
    private var camera1: ICameraControl? = null
    private var camera2: ICameraControl? = null
    private var camera3: ICameraControl? = null

    companion object
    {
        private val TAG = ConsolePanelControl::class.java.simpleName
        private const val MAX_CONTROL_CAMERAS = 4
        private const val MARGIN = 6.0f
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
        Log.v(TAG, " setNeighborCameraControl() ")
        this.camera0 = camera0
        this.camera1 = camera1
        this.camera2 = camera2
        this.camera3 = camera3
        decideCameraControl()
    }

    override fun getCameraStatus(): ICameraStatus
    {
        return (this)
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

    private fun decideCameraControl()
    {
        try
        {
            var count = 0
            do
            {
                currentCameraControlId++
                if (currentCameraControlId >= MAX_CONTROL_CAMERAS)
                {
                    currentCameraControlId = 0
                }
                if (isAvailableCameraControl(currentCameraControlId))
                {
                    setCurrentCameraControl(currentCameraControlId)
                    break   // return (true)
                }
                count++
            } while (count <= MAX_CONTROL_CAMERAS)
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
        Log.v(TAG, " currentCameraControlId == $currentCameraControlId")
    }

    override fun onLongClick(v: View?): Boolean
    {
        try
        {
            decideCameraControl()
            vibrator.vibrate(IVibrator.VibratePattern.SIMPLE_LONG)
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
        return (true)
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

            //
            drawControlPanelNumber(canvas)


            drawFramingGrid(canvas, Color.GREEN)

        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
    }

    private fun drawControlPanelNumber(canvas: Canvas)
    {
        try
        {
            val width = canvas.width / 4.0f
            val height = canvas.height / 9.0f
            val rect = RectF(width * 3, height * 8.0f, canvas.width.toFloat(), canvas.height.toFloat())

            val msg = "$currentCameraControlId : ${currentCameraControl?.getConnectionMethod()}"

            drawString(canvas, rect, msg, Color.WHITE)

        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
    }

    private fun isAvailableCameraControl(id : Int) : Boolean
    {
        var ret = false
        try
        {
            val cameraControl = when(id) {
                0 -> camera0
                1 -> camera1
                2 -> camera2
                3 -> camera3
                else -> null
            } ?: return (false)

            Log.v(TAG, " isAvailableCameraControl($id) : ${cameraControl.getConnectionMethod()}")

            ret = when (cameraControl.getConnectionMethod())
            {
                PREFERENCE_CAMERA_METHOD_CAMERAX -> true
                PREFERENCE_CAMERA_METHOD_PENTAX -> true
                PREFERENCE_CAMERA_METHOD_THETA -> true
                PREFERENCE_CAMERA_METHOD_PANASONIC -> true
                else -> false
            }
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
        return (ret)
    }

    private fun setCurrentCameraControl(id : Int)
    {
        Log.v(TAG, "  setCurrentCameraControl($id)")
        currentCameraControl = when (id) {
            0 -> camera0
            1 -> camera1
            2 -> camera2
            3 -> camera3
            else -> null
        }
    }

    private fun drawFramingGrid(canvas: Canvas, color : Int)
    {

        val paint = Paint()
        canvas.width
        paint.color = color
        paint.strokeWidth = 3.0f
        paint.style = Paint.Style.STROKE
        paint.isAntiAlias = true
        val rect = RectF(0.0f, 0.0f, canvas.width.toFloat(), canvas.height.toFloat())
        canvas.drawRect(rect, paint)
    }

    /**
     * resion内に文字を表示する
     *
     */
    fun drawString(canvas: Canvas, region: RectF, target: String?, color: Int)
    {
        if ((target == null)||(target.isEmpty()))
        {
            return
        }
        val textPaint = Paint()
        textPaint.color = color
        textPaint.isAntiAlias = true

        val maxWidth: Float = region.width() - MARGIN
        var textSize: Float = region.height() - MARGIN
        textPaint.textSize = textSize
        var textWidth = textPaint.measureText(target)

        while (maxWidth < textWidth)
        {
            // テキストサイズが横幅からあふれるまでループ
            textPaint.textSize = --textSize
            textWidth = textPaint.measureText(target)
        }

        // センタリングするための幅を取得
        val margin = (region.width() - textWidth) / 2.0f

        // 文字を表示する
        val fontMetrics = textPaint.fontMetrics
        canvas.drawText(target, region.left + margin, region.bottom - fontMetrics.descent, textPaint)
    }

    override fun getStatusList(key: String): List<String?> { return (ArrayList<String>()) }
    override fun getStatus(key: String): String { return ("") }
    override fun setStatus(key: String, value: String) { }

}
