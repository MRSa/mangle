package jp.osdn.gokigen.gokigenassets.camera.vendor.pixpro.wrapper.status

import android.util.Log
import jp.osdn.gokigen.gokigenassets.camera.interfaces.ICameraStatusUpdateNotify
import jp.osdn.gokigen.gokigenassets.camera.interfaces.ICameraStatus
import jp.osdn.gokigen.gokigenassets.camera.interfaces.ICameraStatusWatcher
import jp.osdn.gokigen.gokigenassets.camera.vendor.pixpro.wrapper.command.messages.IPixproCommandCallback
import jp.osdn.gokigen.gokigenassets.liveview.message.IMessageDrawer
import java.lang.Exception


class PixproStatusChecker : IPixproCommandCallback, ICameraStatusWatcher, ICameraStatus
{
    private val statusHolder = PixproStatusHolder()
    private var whileFetching = false
    private var notifier: ICameraStatusUpdateNotify? = null

    companion object
    {
        private val TAG = PixproStatusChecker::class.java.simpleName
    }

    override fun getStatusList(key: String): List<String?> {
        try {
            return if (statusHolder == null) {
                ArrayList()
            } else statusHolder.getAvailableItemList(key)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ArrayList()
    }

    override fun getStatus(key: String): String {
        try {
            return if (statusHolder == null) {
                ""
            } else statusHolder.getItemStatus(key)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    override fun getStatusColor(key: String): Int {
        TODO("Not yet implemented")
    }

    override fun setStatus(key: String, value: String) {
        Log.v(TAG, "setStatus($key, $value)")
    }

    fun startStatusWatch(notifier: ICameraStatusUpdateNotify) {
        if (whileFetching) {
            Log.v(TAG, "startStatusWatch() already starting.")
            return
        }
        try {
            this.notifier = notifier
            whileFetching = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun startStatusWatch(
        indicator: IMessageDrawer?,
        notifier: ICameraStatusUpdateNotify?
    ) {
        TODO("Not yet implemented")
    }

    override fun stopStatusWatch() {
        Log.v(TAG, "stoptStatusWatch()")
        whileFetching = false
        notifier = null
    }

    override fun receivedMessage(id: Int, rx_body: ByteArray?) {}


}
