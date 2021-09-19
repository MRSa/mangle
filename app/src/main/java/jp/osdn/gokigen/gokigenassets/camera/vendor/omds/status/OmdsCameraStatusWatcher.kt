package jp.osdn.gokigen.gokigenassets.camera.vendor.omds.status

import android.graphics.Color
import android.util.Log
import jp.osdn.gokigen.gokigenassets.camera.interfaces.ICameraStatus
import jp.osdn.gokigen.gokigenassets.camera.interfaces.ICameraStatusUpdateNotify
import jp.osdn.gokigen.gokigenassets.camera.interfaces.ICameraStatusWatcher
import jp.osdn.gokigen.gokigenassets.liveview.message.IMessageDrawer
import java.lang.Exception
import kotlin.collections.ArrayList

class OmdsCameraStatusWatcher : ICameraStatusWatcher, ICameraStatus, IOmdsCommunicationInfo
{
    private var buffer: ByteArray? = null
    private var isWatching = false
    private var statusReceived = false
    private var notifier: ICameraStatusUpdateNotify? = null
    private var focusingStatus = 0
    private var omdsCommandList : String = ""

    override fun setOmdsCommandList(commandList: String)
    {
        //Log.v(TAG, " setOmdsCommandList() : $commandList")
        omdsCommandList = commandList

        val commandListParser = OmdsCommandListParser()
        commandListParser.startParse(omdsCommandList)
    }

    fun setRtpHeader(byteBuffer: ByteArray?)
    {
        try
        {
            //buffer = byteBuffer?.copyOf()
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
            this.notifier = notifier
            val thread = Thread {
                val waitMs = SLEEP_TIME_MS
                isWatching = true
                while (isWatching)
                {
                    if (statusReceived)
                    {
                        // データを解析する
                        parseRtpHeader()
                        statusReceived = false
                    }
                    sleep(waitMs)
                }
            }
            thread.start()
        }
        catch (e: Exception)
        {
            e.printStackTrace()
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
            while (position + 4 < maxLength)
            {
                val id: Int = ((buffer?.get(position) ?: 0).toInt() and 0xff) * 256 + ((buffer?.get(position + 1) ?: 0).toInt() and 0xff)
                val length: Int = ((buffer?.get(position + 2) ?: 0).toInt() and 0xff) * 256 + ((buffer?.get(position + 3) ?: 0).toInt() and 0xff)
                when (id)
                {
                    ID_AF_FRAME_INFO -> { checkFocused(buffer, position, length) }
                    ID_ZOOM_LENS_INFO, ID_FRAME_SIZE -> { }
                    else -> { }
                }
                position += 4 + length * 4 // header : 4bytes , data : length * 4 bytes
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
    }

    override fun getStatusList(key: String): List<String>
    {
        return (ArrayList())
    }

    override fun getStatus(key: String): String
    {
        return ("")
    }

    override fun getStatusColor(key: String): Int
    {
        return (Color.WHITE)
    }

    override fun setStatus(key: String, value: String)
    {

    }

    companion object
    {
        private val TAG = OmdsCameraStatusWatcher::class.java.simpleName

        private const val SLEEP_TIME_MS = 250
        private const val ID_FRAME_SIZE = 1
        private const val ID_AF_FRAME_INFO = 2
        private const val ID_ZOOM_LENS_INFO = 18
    }

}
