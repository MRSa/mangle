package jp.osdn.gokigen.gokigenassets.camera.panasonic.status

import android.content.Context
import android.util.Log
import jp.osdn.gokigen.gokigenassets.camera.interfaces.ICardSlotSelector
import jp.osdn.gokigen.gokigenassets.camera.panasonic.ICameraChangeListener
import jp.osdn.gokigen.gokigenassets.camera.panasonic.IPanasonicCamera
import jp.osdn.gokigen.gokigenassets.utils.communication.SimpleHttpClient


class CameraEventObserver(context: Context, private val remote: IPanasonicCamera, cardSlotSelector: ICardSlotSelector): ICameraEventObserver
{
    private val statusHolder = CameraStatusHolder(context, remote, cardSlotSelector)
    private var isEventMonitoring = false
    private var isActive = false

    override fun start(): Boolean
    {
        if (!isActive)
        {
            Log.w(TAG, "start() observer is not active.")
            return false
        }
        if (isEventMonitoring) {
            Log.w(TAG, "start() already starting.")
            return false
        }
        isEventMonitoring = true
        try {
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

    override fun stop() {
        isEventMonitoring = false
    }

    override fun release() {
        isEventMonitoring = false
        isActive = false
    }

    override fun setEventListener(listener: ICameraChangeListener)
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

    override fun clearEventListener() {
        try {
            statusHolder.clearEventChangeListener()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getCameraStatusHolder(): ICameraStatusHolder {
        return statusHolder
    }

    override fun activate()
    {
        isActive = true
    }

    companion object
    {
        private val TAG = CameraEventObserver::class.java.simpleName
        private const val TIMEOUT_MS = 3000
    }

}