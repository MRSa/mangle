package jp.osdn.gokigen.gokigenassets.camera.vendor.pixpro.wrapper.status

import android.graphics.Color
import android.util.Log
import jp.osdn.gokigen.gokigenassets.camera.interfaces.ICameraStatusUpdateNotify
import jp.osdn.gokigen.gokigenassets.camera.interfaces.ICameraStatus
import jp.osdn.gokigen.gokigenassets.camera.interfaces.ICameraStatusWatcher
import jp.osdn.gokigen.gokigenassets.camera.vendor.pixpro.wrapper.command.IPixproCommandPublisher
import jp.osdn.gokigen.gokigenassets.camera.vendor.pixpro.wrapper.command.messages.IPixproCommandCallback
import jp.osdn.gokigen.gokigenassets.camera.vendor.pixpro.wrapper.command.messages.base.PixproCommandOnlyCallback
import jp.osdn.gokigen.gokigenassets.camera.vendor.pixpro.wrapper.command.messages.specific.PixproChangeExposureCompensation
import jp.osdn.gokigen.gokigenassets.camera.vendor.pixpro.wrapper.command.messages.specific.PixproStatusRequest
import jp.osdn.gokigen.gokigenassets.liveview.message.IMessageDrawer
import jp.osdn.gokigen.gokigenassets.utils.communication.SimpleLogDumper
import java.lang.Exception

class PixproStatusChecker : IPixproCommandCallback, ICameraStatusWatcher, ICameraStatus
{
    private val statusHolder = PixproStatusHolder()
    private var whileFetching = false
    private var notifier: ICameraStatusUpdateNotify? = null
    private lateinit var commandPublisher : IPixproCommandPublisher

    companion object
    {
        private val TAG = PixproStatusChecker::class.java.simpleName
        private const val EVENT_POLL_QUEUE_MS = 1000
    }

    fun setCommandPublisher(commandPublisher : IPixproCommandPublisher)
    {
        this.commandPublisher = commandPublisher
        statusHolder.setCommandPublisher(commandPublisher)
    }

    override fun getStatusList(key: String): List<String?>
    {
        try
        {
            return (statusHolder.getAvailableItemList(key))
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
            return (statusHolder.getItemStatus(key))
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        return ("")
    }

    override fun getStatusColor(key: String): Int
    {
        try
        {
            return (statusHolder.getItemStatusColor(key))
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        return (Color.WHITE)
    }

    override fun setStatus(key: String, value: String)
    {
        Log.v(TAG, "setStatus($key, $value)")
        try
        {
            return (statusHolder.setItemStatus(key, value))
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    override fun startStatusWatch(indicator: IMessageDrawer?, notifier: ICameraStatusUpdateNotify?)
    {
        if (whileFetching)
        {
            Log.v(TAG, "startStatusWatch() already starting.")
            return
        }
        try
        {
            this.notifier = notifier
            whileFetching = true

            val thread = Thread {
                while (whileFetching)
                {
                    try
                    {
                        Thread.sleep(EVENT_POLL_QUEUE_MS.toLong())

                        Log.v(TAG, "  ----- POLL EVENT -----  ")
                        if (::commandPublisher.isInitialized)
                        {
                            commandPublisher.enqueueCommand(PixproStatusRequest(this))
                        }
                    }
                    catch (e: Exception)
                    {
                        e.printStackTrace()
                    }
                }
            }
            try
            {
                thread.start()
            }
            catch (e: Exception)
            {
                e.printStackTrace()
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    override fun stopStatusWatch()
    {
        Log.v(TAG, "stopStatusWatch()")
        whileFetching = false
        notifier = null
    }

    override fun receivedMessage(id: Int, rx_body: ByteArray?)
    {
        Log.v(TAG, " RECEIVED EVENT : ${rx_body?.size} bytes.")
        SimpleLogDumper.dumpBytes("EVT[${rx_body?.size}]", rx_body)
        try
        {
            val length = rx_body?.size ?: 0
            if (length == 2744)
            {
                // TAKE MODE
                val takeMode = when (val take0 = rx_body?.get(16 * 136 + 8))
                {
                    0x01.toByte() -> "P"
                    0x04.toByte() -> "M"
                    0x06.toByte() -> "ASCN"
                    0x07.toByte() -> "Video"
                    0x0c.toByte() -> "Cont."
                    else -> "($take0)"
                }
                statusHolder.updateValue(ICameraStatus.TAKE_MODE, takeMode)

                // FLASH Mode
                val flashMode = when (val flash0 = rx_body?.get(16 * 127 + 8))
                {
                    0x01.toByte() -> "OFF"
                    0x02.toByte() -> "AUTO"
                    0x04.toByte() -> "ON"
                    else -> "($flash0)"
                }
                statusHolder.updateValue(ICameraStatus.TORCH_MODE, flashMode)

                // WHITE BALANCE
                val whiteBalance = when (val wb0 = rx_body?.get(16 * 125 + 8))
                {
                    0x01.toByte() -> "AUTO"
                    0x02.toByte() -> "Daylight"
                    0x04.toByte() -> "Cloudy"
                    0x10.toByte() -> "Fluorescent"
                    0x20.toByte() -> "Fluorescent CWF"
                    0x80.toByte() -> "Incandescent"
                    else -> "($wb0)"
                }
                statusHolder.updateValue(ICameraStatus.WHITE_BALANCE, whiteBalance)

                // ISO SENSITIVITY
                val isoSensitivity = when (val iso0 = rx_body?.get(16 * 70 + 0))
                {
                    0x00.toByte() -> "Auto"
                    0x01.toByte() -> "100"
                    0x02.toByte() -> "200"
                    0x03.toByte() -> "400"
                    0x04.toByte() -> "800"
                    0x05.toByte() -> "1600"
                    0x06.toByte() -> "3200"
                    else -> "($iso0)"
                }
                statusHolder.updateValue(ICameraStatus.ISO_SENSITIVITY, isoSensitivity)

                // EXPOSURE Compensation
                val exposureCompensation = when (val exprev0 = rx_body?.get(16 * 61 + 0))
                {
                    0x00.toByte() -> "-3.0"
                    0x01.toByte() -> "-2.7"
                    0x02.toByte() -> "-2.3"
                    0x03.toByte() -> "-2.0"
                    0x04.toByte() -> "-1.7"
                    0x05.toByte() -> "-1.3"
                    0x06.toByte() -> "-1.0"
                    0x07.toByte() -> "-0.7"
                    0x08.toByte() -> "-0.3"
                    0x09.toByte() -> "0.0"
                    0x0a.toByte() -> "+0.3"
                    0x0b.toByte() -> "+0.7"
                    0x0c.toByte() -> "+1.0"
                    0x0d.toByte() -> "+1.3"
                    0x0e.toByte() -> "+1.7"
                    0x0f.toByte() -> "+2.0"
                    0x10.toByte() -> "+2.3"
                    0x11.toByte() -> "+2.7"
                    0x12.toByte() -> "+3.0"

                    else -> "($exprev0)"
                }
                statusHolder.updateValue(ICameraStatus.EXPREV, exposureCompensation)
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }
}
