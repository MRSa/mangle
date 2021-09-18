package jp.osdn.gokigen.gokigenassets.camera.vendor.omds.operation

import android.graphics.PointF
import android.graphics.RectF
import android.util.Log
import jp.osdn.gokigen.gokigenassets.liveview.IIndicatorControl
import jp.osdn.gokigen.gokigenassets.liveview.focusframe.IAutoFocusFrameDisplay
import jp.osdn.gokigen.gokigenassets.utils.communication.SimpleHttpClient
import java.lang.Exception
import java.util.*
import kotlin.math.floor

class OmdsAutoFocusControl(private val frameDisplayer: IAutoFocusFrameDisplay, private val indicator: IIndicatorControl, private val executeUrl : String = "http://192.168.0.10")
{
    private val headerMap: MutableMap<String, String> = HashMap()
    private val http = SimpleHttpClient()

    fun lockAutoFocus(point: PointF)
    {
        Log.v(TAG, "lockAutoFocus() : [" + point.x + ", " + point.y + "]")
        try {
            val thread = Thread {
                val preFocusFrameRect = getPreFocusFrameRect(point)
                try {
                    showFocusFrame(
                        preFocusFrameRect,
                        IAutoFocusFrameDisplay.FocusFrameStatus.Running,
                        0.0
                    )
                    val posX = floor((point.x * scaleX).toDouble()).toInt()
                    val posY = floor((point.y * scaleY).toDouble()).toInt()
                    Log.v(
                        TAG,
                        "AF ($posX, $posY)"
                    )
                    val sendUrl = String.format(
                        Locale.US,
                        "%s%s&point=%04dx%04d",
                        executeUrl,
                        AF_FRAME_COMMAND,
                        posX,
                        posY
                    )
                    val reply: String = http.httpGetWithHeader(
                        sendUrl,
                        headerMap,
                        null,
                        TIMEOUT_MS
                    ) ?: ""
                    if (!reply.contains("ok")) {
                        Log.v(
                            TAG,
                            "setTouchAFPosition() reply is null."
                        )
                    }
                    if (findTouchAFPositionResult(
                            reply,
                            " [" + posX + "x" + posY + "]"
                        )
                    ) {
                        // AF FOCUSED
                        Log.v(
                            TAG,
                            "lockAutoFocus() : FOCUSED"
                        )
                        showFocusFrame(
                            preFocusFrameRect,
                            IAutoFocusFrameDisplay.FocusFrameStatus.Focused,
                            0.0
                        )
                    } else {
                        // AF ERROR
                        Log.v(
                            TAG,
                            "lockAutoFocus() : ERROR"
                        )
                        showFocusFrame(
                            preFocusFrameRect,
                            IAutoFocusFrameDisplay.FocusFrameStatus.Failed,
                            1.0
                        )
                    }
                    //showFocusFrame(preFocusFrameRect, IAutoFocusFrameDisplay.FocusFrameStatus.Errored, 1.0);
                } catch (e: Exception) {
                    e.printStackTrace()
                    try {
                        showFocusFrame(
                            preFocusFrameRect,
                            IAutoFocusFrameDisplay.FocusFrameStatus.Errored,
                            1.0
                        )
                    } catch (ee: Exception) {
                        ee.printStackTrace()
                    }
                }
            }
            thread.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * シャッター半押し処理
     *
     */
    fun halfPressShutter(isPressed: Boolean)
    {
        if (isPressed)
        {
            lockAutoFocus(PointF(0.5f, 0.5f))
        }
        else
        {
            unlockAutoFocus()
        }
    }

    fun unlockAutoFocus()
    {
        Log.v(TAG, "unlockAutoFocus()")
        try {
            val thread = Thread {
                try {
                    val reply: String = http.httpGetWithHeader(
                        executeUrl + AF_RELEASE_COMMAND,
                        headerMap,
                        null,
                        TIMEOUT_MS
                    ) ?: ""
                    if (!reply.contains("ok")) {
                        Log.v(
                            TAG,
                            "unlockAutoFocus() reply is null."
                        )
                    }
                    hideFocusFrame()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            thread.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showFocusFrame(rect: RectF, status: IAutoFocusFrameDisplay.FocusFrameStatus, duration: Double)
    {
        frameDisplayer.showFocusFrame(rect, status, duration.toFloat())
        indicator.onAfLockUpdate(IAutoFocusFrameDisplay.FocusFrameStatus.Focused === status)
    }

    private fun hideFocusFrame()
    {
        frameDisplayer.hideFocusFrame()
        indicator.onAfLockUpdate(false)
    }

    private fun getPreFocusFrameRect(point: PointF): RectF
    {
        val imageWidth: Float = frameDisplayer.getContentSizeWidth()
        val imageHeight: Float = frameDisplayer.getContentSizeHeight()

        // Display a provisional focus frame at the touched point.
        val focusWidth = 0.125f // 0.125 is rough estimate.
        var focusHeight = 0.125f
        focusHeight *= if (imageWidth > imageHeight) {
            imageWidth / imageHeight
        } else {
            imageHeight / imageWidth
        }
        return RectF(
            point.x - focusWidth / 2.0f, point.y - focusHeight / 2.0f,
            point.x + focusWidth / 2.0f, point.y + focusHeight / 2.0f
        )
    }

    private fun findTouchAFPositionResult(replyXml: String, position: String): Boolean {
        try {
            Log.v(
                TAG,
                " REPLY : $replyXml $position"
            )
            if (replyXml.contains("ok")) {
                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    init
    {
        headerMap["User-Agent"] = "OlympusCameraKit" // "OI.Share"
        headerMap["X-Protocol"] = "OlympusCameraKit" // "OI.Share"
    }

    companion object
    {
        private val TAG = OmdsAutoFocusControl::class.java.simpleName
        private const val TIMEOUT_MS = 3000

        private const val AF_FRAME_COMMAND = "/exec_takemotion.cgi?com=assignafframe"
        private const val AF_RELEASE_COMMAND = "/exec_takemotion.cgi?com=releaseafframe"
        private const val scaleX = 640.0f
        private const val scaleY = 480.0f
    }
}
