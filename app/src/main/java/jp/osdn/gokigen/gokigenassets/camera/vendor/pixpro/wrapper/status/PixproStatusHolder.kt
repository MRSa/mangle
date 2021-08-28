package jp.osdn.gokigen.gokigenassets.camera.vendor.pixpro.wrapper.status

import android.graphics.Color
import android.util.Log
import jp.osdn.gokigen.gokigenassets.camera.interfaces.ICameraStatusUpdateNotify
import androidx.collection.SparseArrayCompat

import android.util.SparseIntArray
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList


class PixproStatusHolder
{

    companion object
    {
        private val TAG = PixproStatusHolder::class.java.simpleName
    }

    private val statusHolder: SparseIntArray = SparseIntArray()
    private val statusNameArray: SparseArrayCompat<String>  = SparseArrayCompat()
    private fun prepareStatusNameArray() {
/*
        statusNameArray.clear();
        statusNameArray.append(BATTERY_LEVEL, BATTERY_LEVEL_STR);
        statusNameArray.append(WHITE_BALANCE, WHITE_BALANCE_STR);
        statusNameArray.append(APERTURE, APERTURE_STR);
        statusNameArray.append(FOCUS_MODE, FOCUS_MODE_STR);
        statusNameArray.append(SHOOTING_MODE, SHOOTING_MODE_STR);
        statusNameArray.append(FLASH, FLASH_STR);
        statusNameArray.append(EXPOSURE_COMPENSATION, EXPOSURE_COMPENSATION_STR);
        statusNameArray.append(SELF_TIMER, SELF_TIMER_STR);
        statusNameArray.append(FILM_SIMULATION, FILM_SIMULATION_STR);
        statusNameArray.append(IMAGE_FORMAT, IMAGE_FORMAT_STR);
        statusNameArray.append(RECMODE_ENABLE, RECMODE_ENABLE_STR);
        statusNameArray.append(F_SS_CONTROL, F_SS_CONTROL_STR);
        statusNameArray.append(ISO, ISO_STR);
        statusNameArray.append(MOVIE_ISO, MOVIE_ISO_STR);
        statusNameArray.append(FOCUS_POINT, FOCUS_POINT_STR);
        statusNameArray.append(DEVICE_ERROR, DEVICE_ERROR_STR);
        statusNameArray.append(IMAGE_FILE_COUNT, IMAGE_FILE_COUNT_STR);
        statusNameArray.append(SDCARD_REMAIN_SIZE, SDCARD_REMAIN_SIZE_STR);
        statusNameArray.append(FOCUS_LOCK, FOCUS_LOCK_STR);
        statusNameArray.append(MOVIE_REMAINING_TIME, MOVIE_REMAINING_TIME_STR);
        statusNameArray.append(SHUTTER_SPEED, SHUTTER_SPEED_STR);
        statusNameArray.append(IMAGE_ASPECT,IMAGE_ASPECT_STR);
        statusNameArray.append(BATTERY_LEVEL_2, BATTERY_LEVEL_2_STR);

        statusNameArray.append(UNKNOWN_DF00, UNKNOWN_DF00_STR);
        statusNameArray.append(PICTURE_JPEG_COUNT, PICTURE_JPEG_COUNT_STR);
        statusNameArray.append(UNKNOWN_D400, UNKNOWN_D400_STR);
        statusNameArray.append(UNKNOWN_D401, UNKNOWN_D401_STR);
        statusNameArray.append(UNKNOWN_D52F, UNKNOWN_D52F_STR);
*/
    }

    fun updateValue(
        notifier: ICameraStatusUpdateNotify?,
        id: Int,
        data0: Byte,
        data1: Byte,
        data2: Byte,
        data3: Byte
    ) {
        try {
            val value =
                (data3.toInt() and 0xff shl 24) + (data2.toInt() and 0xff shl 16) + (data1.toInt() and 0xff shl 8) + (data0.toInt() and 0xff)
            val currentValue = statusHolder[id, -1]
            Log.v(TAG, "STATUS  ID: $id  value : $value ($currentValue)")
            statusHolder.put(id, value)
            if (currentValue != value) {
                //Log.v(TAG, "STATUS  ID: " + id + " value : " + currentValue + " -> " + value);
                notifier?.let { updateDetected(it, id, currentValue, value) }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun updateDetected(
        notifier: ICameraStatusUpdateNotify,
        id: Int,
        previous: Int,
        current: Int
    ) {
        try {
            val idName = statusNameArray[id, "Unknown"]
            Log.v(
                TAG,
                java.lang.String.format(
                    Locale.US,
                    "<< UPDATE STATUS >> id: 0x%04x[%s] 0x%08x(%d) -> 0x%08x(%d)",
                    id,
                    idName,
                    previous,
                    previous,
                    current,
                    current
                )
            )
            //Log.v(TAG, "updateDetected(ID: " + id + " [" + idName + "] " + previous + " -> " + current + " )");
/*
            if (id == FOCUS_LOCK)
            {
                if (current == 1)
                {
                    // focus Lock
                    notifier.updateFocusedStatus(true, true);
                }
                else
                {
                    // focus unlock
                    notifier.updateFocusedStatus(false, false);
                }
            }
*/
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 認識したカメラのステータス名称のリストを応答する
     *
     */
    private val availableStatusNameList: List<String?>
        private get() {
            val selection: ArrayList<String?> = ArrayList()
            try {
                for (index in 0 until statusHolder.size()) {
                    val key = statusHolder.keyAt(index)
                    selection.add(
                        statusNameArray[key, java.lang.String.format(
                            Locale.US,
                            "0x%04x",
                            key
                        )]
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return selection
        }

    fun getAvailableItemList(listKey: String?): List<String?> {
        if (listKey == null) {
            // アイテム名の一覧を応答する
            return availableStatusNameList
        }

        /////  選択可能なステータスの一覧を取得する : でも以下はアイテム名の一覧... /////
        val selection: ArrayList<String?> = ArrayList()
        try {
            for (index in 0 until statusHolder.size()) {
                val key = statusHolder.keyAt(index)
                selection.add(statusNameArray[key])
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return selection
    }

    fun getItemStatusColor(key: String): Int
    {
        return (Color.WHITE)
    }

    fun getItemStatus(orgKey: String): String {
        var key = orgKey
        try {
            val strIndex = key.indexOf("x")
            Log.v(TAG, "getItemStatus() : $key [$strIndex]")
            if (strIndex >= 1) {
                key = key.substring(strIndex + 1)
                try {
                    val id = key.toInt(16)
                    val value = statusHolder[id]
                    Log.v(TAG, "getItemStatus() value : $value")
                    return value.toString() + ""
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            for (index in 0 until statusNameArray.size()) {
                val id = statusNameArray.keyAt(index)
                val strKey = statusNameArray.valueAt(index)
                if (key.contentEquals(strKey)) {
                    val value = statusHolder[id]
                    return value.toString() + ""
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return "? [$key]"
    }

    init {
        statusHolder.clear()
        prepareStatusNameArray()
    }
}
