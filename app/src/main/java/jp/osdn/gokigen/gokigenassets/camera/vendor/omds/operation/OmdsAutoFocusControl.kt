package jp.osdn.gokigen.gokigenassets.camera.vendor.omds.operation

import android.graphics.PointF
import android.graphics.RectF
import android.util.Log
import jp.osdn.gokigen.gokigenassets.camera.vendor.omds.IOmdsProtocolNotify
import jp.osdn.gokigen.gokigenassets.liveview.IIndicatorControl
import jp.osdn.gokigen.gokigenassets.liveview.focusframe.IAutoFocusFrameDisplay
import jp.osdn.gokigen.gokigenassets.utils.communication.SimpleHttpClient
import java.lang.Exception
import java.util.*
import kotlin.math.floor

class OmdsAutoFocusControl(private val frameDisplayer: IAutoFocusFrameDisplay, private val indicator: IIndicatorControl, userAgent: String = "OlympusCameraKit", private val executeUrl : String = "http://192.168.0.10") : IOmdsProtocolNotify, IOpcFocusLockResult
{
    private val headerMap: MutableMap<String, String> = HashMap()
    private val http = SimpleHttpClient()
    private lateinit var preFocusFrameRect : RectF
    private var useOpcProtocol = false

    fun lockAutoFocus(point: PointF)
    {
        Log.v(TAG, "lockAutoFocus() : [" + point.x + ", " + point.y + "]")
        try
        {
            val thread = Thread {
                try
                {
                    if (useOpcProtocol)
                    {
                        // OPC時の AF LOCK 処理
                        lockCommandOpc(point)
                    }
                    else
                    {
                        // OMDS時の AF LOCK 処理
                        lockCommandOmds(point)
                    }
                }
                catch (e: Exception)
                {
                    e.printStackTrace()
                    try
                    {
                        preFocusFrameRect = getPreFocusFrameRect(point)
                        showFocusFrame(preFocusFrameRect, IAutoFocusFrameDisplay.FocusFrameStatus.Errored, 1.0)
                    }
                    catch (ee: Exception)
                    {
                        ee.printStackTrace()
                    }
                }
            }
            thread.start()
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    private fun lockCommandOmds(point: PointF)
    {
        preFocusFrameRect = getPreFocusFrameRect(point)
        try
        {
            // AF駆動前にAF UNLOCK
            val releaseUrl = executeUrl + AF_RELEASE_COMMAND
            val preReply: String = http.httpGetWithHeader(releaseUrl, headerMap, null, TIMEOUT_MS) ?: ""
            Log.v(TAG, "setTouchAFPosition() pre-release. : $releaseUrl ($preReply)")

            showFocusFrame(preFocusFrameRect, IAutoFocusFrameDisplay.FocusFrameStatus.Running, 0.0)
            val posX = floor((point.x * scaleX).toDouble()).toInt()
            val posY = floor((point.y * scaleY).toDouble()).toInt()
            Log.v(TAG, "AF ($posX, $posY)")
            val sendUrl = String.format(Locale.US, "%s%s&point=%04dx%04d", executeUrl, AF_FRAME_COMMAND, posX, posY)
            val reply: String = http.httpGetWithHeader(sendUrl, headerMap, null, TIMEOUT_MS) ?: ""
            if (!reply.contains("ok"))
            {
                Log.v(TAG, "OMDS: setTouchAFPosition() reply is illegal. : $reply ($sendUrl)")
            }
            if (findTouchAFPositionResult(reply, " [" + posX + "x" + posY + "]"))
            {
                // AF FOCUSED
                Log.v(TAG, "lockAutoFocus() : FOCUSED")
                showFocusFrame(preFocusFrameRect, IAutoFocusFrameDisplay.FocusFrameStatus.Focused, 0.0)
            }
            else
            {
                // AF FOCUS FAILURE
                Log.v(TAG, "lockAutoFocus() : ERROR")
                showFocusFrame(preFocusFrameRect, IAutoFocusFrameDisplay.FocusFrameStatus.Failed, 1.0)
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    private fun lockCommandOpc(point: PointF)
    {
        preFocusFrameRect = getPreFocusFrameRect(point)
        try
        {
            val releaseUrl = executeUrl + AF_UNLOCK_COMMAND_OPC
            val preReply: String = http.httpGetWithHeader(releaseUrl, headerMap, null, TIMEOUT_MS) ?: ""
            Log.v(TAG, "unlock AF : $releaseUrl ($preReply)")

            showFocusFrame(preFocusFrameRect, IAutoFocusFrameDisplay.FocusFrameStatus.Running, 0.0)
            val posX = floor((point.x * scaleX).toDouble()).toInt()
            val posY = floor((point.y * scaleY).toDouble()).toInt()
            Log.v(TAG, "AF ($posX, $posY)")
            val sendUrl = String.format(Locale.US, "%s%s&point=%04dx%04d", executeUrl, AF_FRAME_COMMAND_OPC, posX, posY)
            val reply: String = http.httpGetWithHeader(sendUrl, headerMap, null, TIMEOUT_MS) ?: ""

            val lockUrl = executeUrl + AF_LOCK_COMMAND_OPC
            val lockReply: String = http.httpGetWithHeader(lockUrl, headerMap, null, TIMEOUT_MS) ?: ""
            if (!lockReply.contains("ok"))
            {
                Log.v(TAG, "OPC: setTouchAFPosition() (size:${lockReply.length}) : $lockReply $reply ($lockUrl)")
            }
        }
        catch (e: Exception)
        {
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
                if (useOpcProtocol)
                {
                    unlockAFOpc()
                }
                else
                {
                    unlockAFOmds()
                }
            }
            thread.start()
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    private fun unlockAFOmds()
    {
        try
        {
            val sendUrl = executeUrl + AF_RELEASE_COMMAND
            val reply: String = http.httpGetWithHeader(sendUrl, headerMap, null, TIMEOUT_MS) ?: ""
            if (!reply.contains("ok"))
            {
                Log.v(TAG, "unlockAutoFocus() reply is null.")
            }
            hideFocusFrame()
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    private fun unlockAFOpc()
    {
        try
        {
            val releaseUrl = executeUrl + AF_UNLOCK_COMMAND_OPC
            val preReply: String = http.httpGetWithHeader(releaseUrl, headerMap, null, TIMEOUT_MS) ?: ""
            Log.v(TAG, "unlock AF : $releaseUrl ($preReply)")

            val sendUrl = executeUrl + AF_RELEASE_COMMAND_OPC
            val reply: String = http.httpGetWithHeader(sendUrl, headerMap, null, TIMEOUT_MS) ?: ""
            if (!reply.contains("ok"))
            {
                Log.v(TAG, "unlockAutoFocus() reply is null.")
            }
            hideFocusFrame()
        }
        catch (e: Exception)
        {
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
        focusHeight *= if (imageWidth > imageHeight) { imageWidth / imageHeight } else { imageHeight / imageWidth }
        return RectF(point.x - focusWidth / 2.0f, point.y - focusHeight / 2.0f, point.x + focusWidth / 2.0f, point.y + focusHeight / 2.0f)
    }

    private fun findTouchAFPositionResult(replyXml: String, position: String): Boolean
    {
        try
        {
            Log.v(TAG, " REPLY : $replyXml $position")
            if (replyXml.contains("ok"))
            {
                return true
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        return false
    }

    override fun detectedOpcProtocol(opcProtocol: Boolean)
    {
        Log.v(TAG, " --- detectedOpcProtocol($opcProtocol)")
        useOpcProtocol = opcProtocol
    }

    override fun focusResult(isFocusLocked: Boolean)
    {
        try
        {
            if (isFocusLocked)
            {
                // AF FOCUSED
                Log.v(TAG, "focusResult() : FOCUSED")
                showFocusFrame(preFocusFrameRect, IAutoFocusFrameDisplay.FocusFrameStatus.Focused, 0.0)
            }
            else
            {
                // AF FOCUS FAILURE
                Log.v(TAG, "focusResult() : ERROR")
                showFocusFrame(preFocusFrameRect, IAutoFocusFrameDisplay.FocusFrameStatus.Failed, 1.0)
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    init
    {
        headerMap["User-Agent"] = userAgent // "OlympusCameraKit" // "OI.Share"
        headerMap["X-Protocol"] = userAgent // "OlympusCameraKit" // "OI.Share"
    }

    companion object
    {
        private val TAG = OmdsAutoFocusControl::class.java.simpleName
        private const val TIMEOUT_MS = 3000

        private const val AF_FRAME_COMMAND = "/exec_takemotion.cgi?com=assignafframe"
        private const val AF_RELEASE_COMMAND = "/exec_takemotion.cgi?com=releaseafframe"

        private const val AF_FRAME_COMMAND_OPC = "/exec_takemotion.cgi?com=newassignafframe"
        private const val AF_RELEASE_COMMAND_OPC = "/exec_takemotion.cgi?com=newreleaseafframe"

        private const val AF_LOCK_COMMAND_OPC = "/exec_takemotion.cgi?com=newexecaflock"
        private const val AF_UNLOCK_COMMAND_OPC = "/exec_takemotion.cgi?com=newreleaseaflock"

        private const val scaleX = 640.0f
        private const val scaleY = 480.0f
    }

}
