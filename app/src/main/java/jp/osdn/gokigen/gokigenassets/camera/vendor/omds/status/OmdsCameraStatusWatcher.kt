package jp.osdn.gokigen.gokigenassets.camera.vendor.omds.status

import android.graphics.Color
import android.util.Log
import jp.osdn.gokigen.gokigenassets.camera.interfaces.ICameraStatus
import jp.osdn.gokigen.gokigenassets.camera.interfaces.ICameraStatusUpdateNotify
import jp.osdn.gokigen.gokigenassets.camera.interfaces.ICameraStatusWatcher
import jp.osdn.gokigen.gokigenassets.camera.vendor.omds.IOmdsProtocolNotify
import jp.osdn.gokigen.gokigenassets.liveview.message.IMessageDrawer
import jp.osdn.gokigen.gokigenassets.utils.communication.SimpleHttpClient
import jp.osdn.gokigen.gokigenassets.utils.communication.SimpleLogDumper
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class OmdsCameraStatusWatcher(userAgent: String = "OlympusCameraKit", private val executeUrl : String = "http://192.168.0.10") : ICameraStatusWatcher, ICameraStatus, IOmdsCommunicationInfo, IOmdsProtocolNotify
{
    private val headerMap: MutableMap<String, String> = HashMap()
    private val http = SimpleHttpClient()
    private var useOpcProtocol = false

    private var buffer: ByteArray? = null
    private var isWatching = false
    private var isWatchingEvent = false
    private var statusReceived = false
    private var notifier: ICameraStatusUpdateNotify? = null
    private var focusingStatus = 0
    private var omdsCommandList : String = ""
    private var latestEventResponse : String = ""

    private var currentTakeMode = ""
    private var currentShutterSpeed = ""
    private var currentAperture = ""
    private var currentExpRev = ""
    private var currentCaptureMode = ""
    private var currentIsoSensitivity = ""
    private var currentWhiteBalance = ""
    private var currentMeteringMode = ""
    private var currentPictureEffect = ""
    private var currentTorchMode = ""
    private var currentRemainBattery = ""
    private var currentFocusStatus = ""
    private var currentFocalLength = ""
    private var currentRemainShots = ""

    override fun setOmdsCommandList(commandList: String)
    {
        omdsCommandList = commandList

        val commandListParser = OmdsCommandListParser()
        commandListParser.startParse(omdsCommandList)

        startStatusWatch(null, null)
    }

    fun setRtpHeader(byteBuffer: ByteArray?)
    {
        try
        {
            if (byteBuffer != null)
            {
                buffer = byteBuffer
                statusReceived = true
            }
            else
            {
                statusReceived = false
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
            statusReceived = false
        }
    }

    override fun startStatusWatch(indicator: IMessageDrawer?, notifier: ICameraStatusUpdateNotify?)
    {
        try
        {
            startRtpStatusWatch(notifier)
            startEventStatusWatch()

        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    private fun startRtpStatusWatch(notifier: ICameraStatusUpdateNotify?)
    {
        try
        {
            Log.v(TAG, " startStatusWatch()")
            this.notifier = notifier
            val thread = Thread {
                isWatching = true
                while (isWatching)
                {
                    if (statusReceived)
                    {
                        // データを解析する
                        parseRtpHeader()
                        statusReceived = false
                    }
                    sleep(SLEEP_TIME_MS)
                }
            }
            thread.start()
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    private fun startEventStatusWatch()
    {
        try
        {
            Log.v(TAG, " startEventStatusWatch()")
            val thread = Thread {
                isWatchingEvent = true
                while (isWatchingEvent)
                {
                    // ----- EVENT POLLING
                    if (useOpcProtocol)
                    {
                        watchOpcStatus()
                    }
                    else
                    {
                        watchOmdsStatus()
                    }
                    sleep(SLEEP_EVENT_TIME_MS)
                }
            }
            thread.start()
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    private fun watchOmdsStatus()
    {
        try
        {
            // OMDS機のイベント受信
            val omdsEventUrl = "$executeUrl/get_camprop.cgi?com=desc&propname=desclist"
            latestEventResponse = http.httpGetWithHeader(omdsEventUrl, headerMap, null, TIMEOUT_MS) ?: ""
            if (latestEventResponse.isNotEmpty())
            {
                dumpLog(omdsEventUrl, latestEventResponse)
                parseOmdsProperties(latestEventResponse)
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    private fun watchOpcStatus()
    {
        try
        {
            // OPC機のイベント受信
            val opcEventUrl = "$executeUrl/get_camprop.cgi?com=getlist"
            val postData = "<?xml version=\"1.0\"?><get><prop name=\"AE\"/><prop name=\"APERTURE\"/><prop name=\"BATTERY_LEVEL\"/><prop name=\"COLORTONE\"/><prop name=\"EXPREV\"/><prop name=\"ISO\"/><prop name=\"RECENTLY_ART_FILTER\"/><prop name=\"SHUTTER\"/><prop name=\"TAKEMODE\"/><prop name=\"TAKE_DRIVE\"/><prop name=\"WB\"/><prop name=\"AE_LOCK_STATE\"/></get"
            latestEventResponse = http.httpPostWithHeader(opcEventUrl, postData, headerMap, null, TIMEOUT_MS) ?: ""
            dumpLog(opcEventUrl, latestEventResponse)
            parseOpcProperties(latestEventResponse)
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    private fun parseOmdsProperties(eventResponse: String)
    {
        try
        {
            currentTakeMode = getPropertyValue(eventResponse, "<propname>takemode</propname>")
            currentShutterSpeed = getPropertyValue(eventResponse, "<propname>shutspeedvalue</propname>")
            currentAperture = "F" + getPropertyValue(eventResponse, "<propname>focalvalue</propname>")

            currentIsoSensitivity = "ISO " + getPropertyValue(eventResponse, "<propname>isospeedvalue</propname>")
            currentExpRev = getPropertyValue(eventResponse, "<propname>expcomp</propname>")

            currentWhiteBalance = "WB: " + decideWhiteBalance(getPropertyValue(eventResponse, "<propname>wbvalue</propname>"))
            currentPictureEffect = getPropertyValue(eventResponse, "<propname>colortone</propname>")
            currentCaptureMode = " DRIVE: " + getPropertyValue(eventResponse, "<propname>drivemode</propname>")

        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    private fun decideWhiteBalance(wbValue: String) : String
    {
        try
        {
            return (when (wbValue)
            {
                "0" -> "AUTO"
                "18" -> "Daylight"
                "16" -> "Shade"
                "17" -> "Cloudy"
                "20" -> "Incandescent"
                "35" -> "Fluorescent"
                "64" -> "Underwater"
                "23" -> "Flash"
                "256" -> "WB1"
                "257" -> "WB2"
                "258" -> "WB3"
                "259" -> "WB4"
                "512" -> "CWB"
                else -> "($wbValue)"
            })
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        return ("($wbValue)")
    }

    private fun decideWhiteBalanceValue(wbName: String) : String
    {
        try
        {
            return (when (wbName)
            {
                "AUTO" -> "0"
                "Daylight" -> "18"
                "Shade" -> "16"
                "Cloudy" -> "17"
                "Incandescent" -> "20"
                "Fluorescent" -> "35"
                "Underwater" -> "64"
                "Flash" -> "23"
                "WB1" -> "256"
                "WB2" -> "257"
                "WB3" -> "258"
                "WB4" -> "259"
                "CWB" -> "512"
                else -> "0"
            })
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        return ("0")
    }

    private fun getPropertySelectionList(responseString: String, propertyString: String) : List<String>
    {
        try
        {
            if (responseString.isNotEmpty())
            {
                val propertyIndex = responseString.indexOf(propertyString)
                if (propertyIndex > 0)
                {
                    val propertyValueIndex =
                        responseString.indexOf("<enum>", propertyIndex) + "<enum>".length
                    val propertyValueLastIndex = responseString.indexOf("</enum>", propertyIndex)
                    val propertyListString =
                        responseString.substring(propertyValueIndex, propertyValueLastIndex)
                    if (propertyListString.isNotEmpty())
                    {
                        val propertyList = propertyListString.split(" ")
                        val selectionList: ArrayList<String> = ArrayList()
                        selectionList.addAll(propertyList)
                        return (selectionList)
                    }
                }
            }
            Log.v(TAG, "getPropertySelectionList($propertyString) $responseString ..." )
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        return (ArrayList())
    }


    private fun getPropertyValue(responseString: String, propertyString: String) : String
    {
        try
        {
            val propertyIndex = responseString.indexOf(propertyString)
            if (propertyIndex > 0)
            {
                val propertyValueIndex = responseString.indexOf("<value>", propertyIndex) + "<value>".length
                val propertyValueLastIndex = responseString.indexOf("</value>", propertyIndex)
                return (responseString.substring(propertyValueIndex, propertyValueLastIndex))
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        return ("")
    }

    private fun parseOpcProperties(eventResponse: String)
    {
        try
        {

        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    private fun dumpLog(header: String, data: String)
    {
        if (isDumpLog)
        {
            val dataStep = 1536
            Log.v(TAG, "     ------------------------------------------ ")
            for (pos in 0..data.length step dataStep) {
                val lastIndex = if ((pos + dataStep) > data.length)
                {
                    data.length
                }
                else
                {
                    pos + dataStep
                }
                Log.v(TAG, " $header ($pos/${data.length}) ${data.substring(pos, lastIndex)}")
            }
            Log.v(TAG, "     ------------------------------------------ ")
        }
    }


    private fun sleep(waitMs: Int)
    {
        try
        {
            Thread.sleep(waitMs.toLong())
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    private fun parseRtpHeader()
    {
        try
        {
            if (buffer == null)
            {
                Log.v(TAG, " parseRtpHeader() : null")
                return
            }
            var position = 16
            val maxLength = buffer?.size ?: 0
            if (maxLength <= 0)
            {
                // データがないので何もしない
                return
            }

            if (isDumpLog)
            {
                // 受信データのバッファをダンプする
                Log.v(TAG," parseRtpHeader size: $maxLength")
                SimpleLogDumper.dumpBytes("EVT[$maxLength]", buffer)
            }

            while (position + 4 < maxLength)
            {
                val commandId: Int = ((buffer?.get(position) ?: 0).toInt() and 0xff) * 256 + ((buffer?.get(position + 1) ?: 0).toInt() and 0xff)
                val length: Int = ((buffer?.get(position + 2) ?: 0).toInt() and 0xff) * 256 + ((buffer?.get(position + 3) ?: 0).toInt() and 0xff)
                when (commandId)
                {
                    ID_AF_FRAME_INFO -> { checkFocused(buffer, position, length) }
                    ID_FRAME_SIZE -> { }
                    ID_MEDIA_INFO -> { }
                    ID_ROTATION_INFO -> { }
                    ID_AVAILABLE_SHOTS -> { }
                    ID_OMDS_UNKNOWN_01 -> { }
                    ID_OMDS_UNKNOWN_02 -> { }
                    ID_SHUTTER_SPEED -> { }
                    ID_APERTURE -> { }
                    ID_EXPOSURE_COMPENSATION -> { }
                    ID_OMDS_UNKNOWN_03 -> { }
                    ID_ISO_SENSITIVITY -> { }
                    ID_OMDS_UNKNOWN_04 -> { }
                    ID_OMDS_UNKNOWN_05 -> { }
                    ID_OMDS_UNKNOWN_06 -> { }
                    ID_OPTICAL_WARNING -> { }
                    ID_FOCUS_TYPE -> { }
                    ID_ZOOM_LENS_INFO -> { }
                    ID_REMAIN_VIDEO_TIME -> { }
                    ID_POSITION_LEVEL_INFO -> { }
                    ID_FACE_DETECT_1 -> { }
                    ID_FACE_DETECT_2 -> { }
                    ID_FACE_DETECT_3 -> { }
                    ID_FACE_DETECT_4 -> { }
                    ID_FACE_DETECT_5 -> { }
                    ID_FACE_DETECT_6 -> { }
                    ID_FACE_DETECT_7 -> { }
                    else -> { }
                }
                position += 4 + length * 4  // header : 4bytes , data : length * 4 bytes
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    private fun checkFocused(buffer: ByteArray?, position: Int, length: Int)
    {
        if ((length != 5)||(buffer == null))
        {
            // データがそろっていないので何もしない
            return
        }
        val status: Int = buffer[position + 7].toInt() and 0xff
        if (status != focusingStatus)
        {
            // ドライブ停止時には、マーカの色は消さない
            if (status > 0)
            {
                val focus = status == 1
                val isError = status == 2
                notifier?.updateFocusedStatus(focus, isError)
            }
            focusingStatus = status
        }
    }

    override fun stopStatusWatch()
    {
        isWatching = false
        isWatchingEvent = false

    }

    override fun getStatusList(key: String): List<String>
    {
        if (useOpcProtocol)
        {
            return (getStatusListOpc(key, latestEventResponse))
        }
        else
        {
            return (getStatusListOmds(key, latestEventResponse))
        }
    }

    private fun getStatusListOpc(key: String, eventString: String): List<String>
    {
        try
        {
            Log.v(TAG, " getStatusListOpc($key)")
            return (when (key) {
/*
                ICameraStatus.TAKE_MODE -> getPropertySelectionList(latestEventResponse, "<propname>takemode</propname>")
                ICameraStatus.SHUTTER_SPEED -> getPropertySelectionList(latestEventResponse, "<propname>shutspeedvalue</propname>")
                ICameraStatus.APERTURE -> getPropertySelectionList(latestEventResponse, "<propname>focalvalue</propname>")

                ICameraStatus.ISO_SENSITIVITY -> getPropertySelectionList(latestEventResponse, "<propname>isospeedvalue</propname>")
                ICameraStatus.EXPREV -> getPropertySelectionList(latestEventResponse, "<propname>expcomp</propname>")

                ICameraStatus.WHITE_BALANCE -> getAvailableWhiteBalance(latestEventResponse)
                ICameraStatus.EFFECT -> getPropertySelectionList(latestEventResponse, "<propname>colortone</propname>")
                ICameraStatus.CAPTURE_MODE -> getPropertySelectionList(latestEventResponse, "<propname>drivemode</propname>")

                ICameraStatus.AE -> getAvailableMeteringMode()
                ICameraStatus.TORCH_MODE -> getAvailableTorchMode()
                ICameraStatus.BATTERY -> getAvailableRemainBattery()
                ICameraStatus.FOCUS_STATUS -> getAvailableFocusStatus()
                ICameraStatus.FOCAL_LENGTH  -> getAvailableFocalLength()
                ICameraStatus.REMAIN_SHOTS  -> getAvailableRemainShots()
*/
                else -> ArrayList()
            })
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        return (ArrayList())
    }


    private fun getStatusListOmds(key: String, eventString: String): List<String>
    {
        try
        {
            Log.v(TAG, " getStatusListOmds($key)")
            return (when (key) {
                ICameraStatus.TAKE_MODE -> getPropertySelectionList(eventString, "<propname>takemode</propname>")
                ICameraStatus.SHUTTER_SPEED -> getPropertySelectionList(eventString, "<propname>shutspeedvalue</propname>")
                ICameraStatus.APERTURE -> getPropertySelectionList(eventString, "<propname>focalvalue</propname>")

                ICameraStatus.ISO_SENSITIVITY -> getPropertySelectionList(eventString, "<propname>isospeedvalue</propname>")
                ICameraStatus.EXPREV -> getPropertySelectionList(eventString, "<propname>expcomp</propname>")

                ICameraStatus.WHITE_BALANCE -> getAvailableWhiteBalance(eventString)
                ICameraStatus.EFFECT -> getPropertySelectionList(eventString, "<propname>colortone</propname>")
                ICameraStatus.CAPTURE_MODE -> getPropertySelectionList(eventString, "<propname>drivemode</propname>")

/*
                ICameraStatus.AE -> getAvailableMeteringMode()
                ICameraStatus.TORCH_MODE -> getAvailableTorchMode()
                ICameraStatus.BATTERY -> getAvailableRemainBattery()
                ICameraStatus.FOCUS_STATUS -> getAvailableFocusStatus()
                ICameraStatus.FOCAL_LENGTH  -> getAvailableFocalLength()
                ICameraStatus.REMAIN_SHOTS  -> getAvailableRemainShots()
*/
                else -> ArrayList()
            })
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        return (ArrayList())
    }

    private fun getAvailableWhiteBalance(eventResponse: String) : List<String>
    {
        try
        {
            val wbValueList = getPropertySelectionList(eventResponse, "<propname>wbvalue</propname>")
            val wbItemList : ArrayList<String> = ArrayList()
            for (wbValue in wbValueList)
            {
                wbItemList.add(decideWhiteBalance(wbValue))
            }
            return (wbItemList)
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        return (ArrayList())
    }


    override fun getStatus(key: String): String
    {
        try
        {
            return (when (key) {
                ICameraStatus.TAKE_MODE -> currentTakeMode
                ICameraStatus.SHUTTER_SPEED -> currentShutterSpeed
                ICameraStatus.APERTURE -> currentAperture
                ICameraStatus.EXPREV -> currentExpRev
                ICameraStatus.CAPTURE_MODE -> currentCaptureMode
                ICameraStatus.ISO_SENSITIVITY -> currentIsoSensitivity
                ICameraStatus.WHITE_BALANCE -> currentWhiteBalance
                ICameraStatus.AE -> currentMeteringMode
                ICameraStatus.EFFECT -> currentPictureEffect
                ICameraStatus.TORCH_MODE -> currentTorchMode
                ICameraStatus.BATTERY -> currentRemainBattery
                ICameraStatus.FOCUS_STATUS -> currentFocusStatus
                ICameraStatus.FOCAL_LENGTH  -> currentFocalLength
                ICameraStatus.REMAIN_SHOTS  -> currentRemainShots
                else -> ""
            })
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        return ("")
    }

    override fun getStatusColor(key: String): Int
    {
        return (Color.WHITE)
    }

    override fun setStatus(key: String, value: String)
    {
        if (useOpcProtocol)
        {
            setStatusOpc(key, value)
        }
        else
        {
            setStatusOmds(key, value)
        }
    }

    private fun setStatusOmds(key: String, value: String)
    {
        try
        {
            when (key)
            {
                ICameraStatus.TAKE_MODE ->  sendStatusRequestOmds("takemode", value)
                ICameraStatus.SHUTTER_SPEED ->  sendStatusRequestOmds("shutspeedvalue", value)
                ICameraStatus.APERTURE ->  sendStatusRequestOmds("focalvalue", value)
                ICameraStatus.EXPREV ->  sendStatusRequestOmds("expcomp", value)
                ICameraStatus.ISO_SENSITIVITY ->  sendStatusRequestOmds("isospeedvalue", value)
                ICameraStatus.CAPTURE_MODE ->  sendStatusRequestOmds("drivemode", value)
                ICameraStatus.WHITE_BALANCE ->  sendStatusRequestOmds("wbvalue", decideWhiteBalanceValue(value))
                ICameraStatus.EFFECT ->  sendStatusRequestOmds("colortone", value)
                ICameraStatus.AE ->  { }
                ICameraStatus.TORCH_MODE ->  { }
                ICameraStatus.BATTERY ->  { }
                ICameraStatus.FOCUS_STATUS ->  { }
                ICameraStatus.FOCAL_LENGTH  ->  { }
                ICameraStatus.REMAIN_SHOTS  -> { }
                else -> { }
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    private fun sendStatusRequestOmds(property: String, value: String)
    {
        val requestUrl = "$executeUrl/set_camprop.cgi?com=set&propname=$property"
        val postData = "<?xml version=\"1.0\"?><set><value>$value</value></set>"
        val response: String = http.httpPostWithHeader(requestUrl, postData, headerMap, null, TIMEOUT_MS) ?: ""
        dumpLog(requestUrl, response)
    }

    private fun setStatusOpc(key: String, value: String)
    {
        try
        {
            when (key)
            {
                ICameraStatus.TAKE_MODE ->  { }
                ICameraStatus.SHUTTER_SPEED ->  { }
                ICameraStatus.APERTURE ->  { }
                ICameraStatus.EXPREV ->  { }
                ICameraStatus.CAPTURE_MODE ->  { }
                ICameraStatus.ISO_SENSITIVITY ->  { }
                ICameraStatus.WHITE_BALANCE ->  { }
                ICameraStatus.AE ->  { }
                ICameraStatus.EFFECT ->  { }
                ICameraStatus.TORCH_MODE ->  { }
                ICameraStatus.BATTERY ->  { }
                ICameraStatus.FOCUS_STATUS ->  { }
                ICameraStatus.FOCAL_LENGTH  ->  { }
                ICameraStatus.REMAIN_SHOTS  -> { }
                else -> { }
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    override fun detectedOpcProtocol(opcProtocol: Boolean)
    {
        Log.v(TAG, " --- detectedOpcProtocol($opcProtocol)")
        useOpcProtocol = opcProtocol
    }

    init
    {
        headerMap["User-Agent"] = userAgent // "OlympusCameraKit" // "OI.Share"
        headerMap["X-Protocol"] = userAgent // "OlympusCameraKit" // "OI.Share"
    }

    companion object
    {
        private val TAG = OmdsCameraStatusWatcher::class.java.simpleName

        private const val SLEEP_TIME_MS = 250
        private const val SLEEP_EVENT_TIME_MS = 350
        private const val TIMEOUT_MS = 4000

        // RTP HEADER IDs
        private const val ID_FRAME_SIZE = 0x01
        private const val ID_AF_FRAME_INFO = 0x02
        private const val ID_MEDIA_INFO = 0x03
        private const val ID_ROTATION_INFO = 0x04
        private const val ID_AVAILABLE_SHOTS = 0x05
        private const val ID_OMDS_UNKNOWN_01 = 0x06
        private const val ID_OMDS_UNKNOWN_02 = 0x07
        private const val ID_SHUTTER_SPEED = 0x08
        private const val ID_APERTURE = 0x09
        private const val ID_EXPOSURE_COMPENSATION = 0x0a
        private const val ID_OMDS_UNKNOWN_03 = 0x0b
        private const val ID_ISO_SENSITIVITY = 0x0c
        private const val ID_OMDS_UNKNOWN_04 = 0x0d
        private const val ID_OMDS_UNKNOWN_05 = 0x0e
        private const val ID_OMDS_UNKNOWN_06 = 0x0f
        private const val ID_OPTICAL_WARNING = 0x10
        private const val ID_FOCUS_TYPE = 0x11
        private const val ID_ZOOM_LENS_INFO = 0x12
        private const val ID_REMAIN_VIDEO_TIME = 0x6a
        private const val ID_POSITION_LEVEL_INFO = 0x6b
        private const val ID_FACE_DETECT_1 = 0x6c
        private const val ID_FACE_DETECT_2 = 0x6d
        private const val ID_FACE_DETECT_3 = 0x6e
        private const val ID_FACE_DETECT_4 = 0x6f
        private const val ID_FACE_DETECT_5 = 0x70
        private const val ID_FACE_DETECT_6 = 0x71
        private const val ID_FACE_DETECT_7 = 0x72

        private const val isDumpLog = false
    }

}
