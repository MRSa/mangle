package jp.osdn.gokigen.gokigenassets.camera.panasonic.status

import android.content.Context
import android.util.Log
import jp.osdn.gokigen.gokigenassets.camera.interfaces.ICameraStatus
import jp.osdn.gokigen.gokigenassets.camera.interfaces.ICameraStatusUpdateNotify
import jp.osdn.gokigen.gokigenassets.camera.interfaces.ICameraStatusWatcher
import jp.osdn.gokigen.gokigenassets.camera.interfaces.ICardSlotSelector
import jp.osdn.gokigen.gokigenassets.camera.panasonic.ICameraChangeListener
import jp.osdn.gokigen.gokigenassets.camera.panasonic.IPanasonicCamera
import jp.osdn.gokigen.gokigenassets.liveview.message.IMessageDrawer
import jp.osdn.gokigen.gokigenassets.utils.communication.SimpleHttpClient
import java.util.ArrayList

class CameraEventObserver(context: Context, private val remote: IPanasonicCamera, cardSlotSelector: ICardSlotSelector) : ICameraStatusWatcher, ICameraStatus
{
    private val statusHolder = CameraStatusHolder(context, remote, cardSlotSelector)
    private var isEventMonitoring = false
    private var isActive = false


    override fun startStatusWatch(indicator : IMessageDrawer?, notifier: ICameraStatusUpdateNotify?)
    {
        try
        {
            isActive = true
            start()
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
    }

    override fun stopStatusWatch()
    {
        try
        {
            isEventMonitoring = false
            isActive = false
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
    }


    private fun start(): Boolean
    {
        if (!isActive)
        {
            Log.w(TAG, "start() observer is not active.")
            return false
        }
        if (isEventMonitoring)
        {
            Log.w(TAG, "start() already starting.")
            return false
        }
        isEventMonitoring = true
        try
        {
            val http = SimpleHttpClient()
            val thread: Thread = object : Thread() {
                override fun run() {
                    Log.d(TAG, "start() exec.")
                    while (isEventMonitoring) {
                        try {
                            // parse reply message
                            statusHolder.parse(
                                http.httpGet(
                                    remote.getCmdUrl() + "cam.cgi?mode=getstate",
                                    TIMEOUT_MS
                                )
                            )
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        try {
                            sleep(1000)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    isEventMonitoring = false
                }
            }
            thread.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return true
    }

    fun setEventListener(listener: ICameraChangeListener)
    {
        try
        {
            statusHolder.setEventChangeListener(listener)
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    private fun clearEventListener()
    {
        try
        {
            statusHolder.clearEventChangeListener()
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    private fun getCameraStatusHolder(): ICameraStatusHolder
    {
        return statusHolder
    }

    private fun activate()
    {
        isActive = true
    }

    override fun getStatusList(key: String): List<String>
    {
        try
        {
            val listKey = key + "List"
            return statusHolder.getAvailableItemList(listKey)
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        return ArrayList()
    }

    override fun getStatus(key: String): String
    {
        try
        {
            return statusHolder.getItemStatus(key)
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        return ""
    }

    override fun setStatus(key: String, value: String)
    {
        try
        {
            Log.v(TAG, " setStatus(key:$key, value:$value)")
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
    }

    companion object
    {
        private val TAG = CameraEventObserver::class.java.simpleName
        private const val TIMEOUT_MS = 3000
    }
}
