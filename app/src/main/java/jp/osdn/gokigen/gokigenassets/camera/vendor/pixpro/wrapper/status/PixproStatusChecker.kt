package jp.osdn.gokigen.gokigenassets.camera.vendor.pixpro.wrapper.status

import android.graphics.Color
import android.util.Log
import jp.osdn.gokigen.gokigenassets.camera.interfaces.ICameraStatusUpdateNotify
import jp.osdn.gokigen.gokigenassets.camera.interfaces.ICameraStatus
import jp.osdn.gokigen.gokigenassets.camera.interfaces.ICameraStatusWatcher
import jp.osdn.gokigen.gokigenassets.camera.vendor.pixpro.wrapper.command.IPixproCommandPublisher
import jp.osdn.gokigen.gokigenassets.camera.vendor.pixpro.wrapper.command.messages.IPixproCommandCallback
import jp.osdn.gokigen.gokigenassets.liveview.message.IMessageDrawer
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
        Log.v(TAG, "stoptStatusWatch()")
        whileFetching = false
        notifier = null
    }

    override fun receivedMessage(id: Int, rx_body: ByteArray?)
    {
        Log.v(TAG, " receivedMessage($id) : ${rx_body?.size} bytes.")

    }
}
