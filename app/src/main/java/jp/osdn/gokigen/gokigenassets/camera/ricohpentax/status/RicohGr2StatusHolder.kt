package jp.osdn.gokigen.gokigenassets.camera.ricohpentax.status

import android.util.Log
import jp.osdn.gokigen.gokigenassets.camera.interfaces.ICameraStatusUpdateNotify
import org.json.JSONObject
import java.util.*

/**
 *
 *
 */
class RicohGr2StatusHolder(private val notifier: ICameraStatusUpdateNotify?)
{
    private var latestResultObject: JSONObject? = null
    private var focused = false
    private var focusLocked = false
    private var avStatus = ""
    private var tvStatus = ""
    private var xvStatus = ""
    private var exposureModeStatus = ""
    private var meteringModeStatus = ""
    private var wbModeStatus = ""
    private var batteryStatus = ""

    /**
     *
     *
     */
    fun getAvailableItemList(key: String): List<String> {
        val itemList: MutableList<String> = ArrayList()
        try {
            val array = latestResultObject!!.getJSONArray(key) ?: return itemList
            val nofItems = array.length()
            for (index in 0 until nofItems) {
                try {
                    itemList.add(array.getString(index))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return itemList
    }

    fun getItemStatus(key: String): String {
        try {
            return latestResultObject!!.getString(key)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    private fun getStatusString(obj: JSONObject?, name: String): String {
        try {
            if (obj == null)
            {
                return ""
            }
            return obj.getString(name)
        } catch (e: Exception) {
            //e.printStackTrace();
        }
        return ""
    }

    private fun getBooleanStatus(obj: JSONObject?, name: String): Boolean {
        try {
            if (obj == null)
            {
                return false
            }
            return obj.getBoolean(name)
        } catch (e: Exception) {
            //e.printStackTrace();
        }
        return false
    }

    /**
     *
     *
     */
    fun updateStatus(replyString: String?) {
        if (replyString == null || replyString.length < 1) {
            Log.v(TAG, "httpGet() reply is null. ")
            return
        }
        try {
            latestResultObject = JSONObject(replyString)
            val result = getStatusString(latestResultObject, "errMsg")
            val av = getStatusString(latestResultObject, "av")
            val tv = getStatusString(latestResultObject, "tv")
            val xv = getStatusString(latestResultObject, "xv")
            val exposureMode = getStatusString(latestResultObject, "exposureMode")
            val meteringMode = getStatusString(latestResultObject, "meteringMode")
            val wbMode = getStatusString(latestResultObject, "WBMode")
            val battery = getStatusString(latestResultObject, "battery")
            val focus = getBooleanStatus(latestResultObject, "focused")
            val focusLock = getBooleanStatus(latestResultObject, "focusLocked")
            if (result.contains("OK")) {
                if (avStatus != av) {
                    avStatus = av
                    notifier?.updatedAperture(avStatus)
                }
                if (tvStatus != tv) {
                    tvStatus = tv
                    notifier?.updatedShutterSpeed(tvStatus)
                }
                if (xvStatus != xv) {
                    xvStatus = xv
                    notifier?.updatedExposureCompensation(xvStatus)
                }
                if (exposureModeStatus != exposureMode) {
                    exposureModeStatus = exposureMode
                    notifier?.updatedTakeMode(exposureModeStatus)
                }
                if (meteringModeStatus != meteringMode) {
                    meteringModeStatus = meteringMode
                    notifier?.updatedMeteringMode(meteringModeStatus)
                }
                if (wbModeStatus != wbMode) {
                    wbModeStatus = wbMode
                    notifier?.updatedWBMode(wbModeStatus)
                }
                if (batteryStatus != battery) {
                    batteryStatus = battery
                    notifier?.updateRemainBattery(batteryStatus.toInt())
                }
                if (focus != focused || focusLock != focusLocked) {
                    focused = focus
                    focusLocked = focusLock
                    notifier?.updateFocusedStatus(focused, focusLocked)
                }
            }
            System.gc()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     *
     *
     */
    companion object
    {
        private val TAG = RicohGr2StatusHolder::class.java.simpleName
    }
}
