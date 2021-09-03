package jp.osdn.gokigen.gokigenassets.camera.vendor.pixpro.wrapper.status

import android.graphics.Color
import android.util.Log
import jp.osdn.gokigen.gokigenassets.camera.interfaces.ICameraStatusUpdateNotify
import androidx.collection.SparseArrayCompat

import android.util.SparseIntArray
import jp.osdn.gokigen.gokigenassets.camera.interfaces.ICameraStatus
import jp.osdn.gokigen.gokigenassets.camera.vendor.pixpro.wrapper.command.IPixproCommandPublisher
import jp.osdn.gokigen.gokigenassets.camera.vendor.pixpro.wrapper.command.messages.base.PixproCommandOnlyCallback
import jp.osdn.gokigen.gokigenassets.camera.vendor.pixpro.wrapper.command.messages.specific.*
import java.lang.Exception
import java.util.*

class PixproStatusHolder
{
    companion object
    {
        private val TAG = PixproStatusHolder::class.java.simpleName
    }

    private val statusHolder: SparseIntArray = SparseIntArray()
    private val statusNameArray: SparseArrayCompat<String>  = SparseArrayCompat()
    private var statusConvert = PixproStatusConvert(this)
    private lateinit var commandPublisher : IPixproCommandPublisher

    private var currentFlashMode = ""
    private var currentWhiteBalance = ""


    fun setCommandPublisher(commandPublisher : IPixproCommandPublisher)
    {
        this.commandPublisher = commandPublisher
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

    fun updateValue(key: String, value: String)
    {
        when (key)
        {
            ICameraStatus.TAKE_MODE -> { }
            ICameraStatus.SHUTTER_SPEED -> { }
            ICameraStatus.EXPREV -> { }
            ICameraStatus.ISO_SENSITIVITY -> { }
            ICameraStatus.WHITE_BALANCE -> { currentWhiteBalance = value }
            ICameraStatus.TORCH_MODE -> { currentFlashMode = value }
            ICameraStatus.BATTERY -> { }
            //ICameraStatus.APERTURE -> { }
            //ICameraStatus.CAPTURE_MODE -> { }
            //ICameraStatus.AE -> { }
            //ICameraStatus.EFFECT -> { }
            //ICameraStatus.FOCUS_STATUS -> { }
            else -> { }
        }
    }


    fun getAvailableItemList(key: String?): List<String?>
    {
        try
        {
            return (when (key) {
                ICameraStatus.TAKE_MODE -> statusConvert.getAvailableTakeMode()
                ICameraStatus.SHUTTER_SPEED -> statusConvert.getAvailableShutterSpeed()
                ICameraStatus.APERTURE -> statusConvert.getAvailableAperture()
                ICameraStatus.EXPREV -> statusConvert.getAvailableExpRev()
                ICameraStatus.CAPTURE_MODE -> statusConvert.getAvailableCaptureMode()
                ICameraStatus.ISO_SENSITIVITY -> statusConvert.getAvailableIsoSensitivity()
                ICameraStatus.WHITE_BALANCE -> statusConvert.getAvailableWhiteBalance()
                ICameraStatus.AE -> statusConvert.getAvailableMeteringMode()
                ICameraStatus.EFFECT -> statusConvert.getAvailablePictureEffect()
                ICameraStatus.TORCH_MODE -> statusConvert.getAvailableTorchMode()
                //ICameraStatus.BATTERY -> statusConvert.getAvailableRemainBattery()
                //ICameraStatus.FOCUS_STATUS -> statusConvert.getAvailableFocusStatus()
                else -> ArrayList()
            })
            //Log.v(TAG, " ----- getAvailableItemList($key) ")
            //sendCamGetSettingCmd("colormode")
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        return (ArrayList())
    }

    fun getItemStatusColor(key: String): Int
    {
        return (when (key) {
            ICameraStatus.BATTERY -> getRemainBatteryColor()
            else -> Color.WHITE
        })
    }

    fun getItemStatus(orgKey: String): String
    {
        return (when (orgKey) {
            ICameraStatus.TAKE_MODE -> getTakeMode()
            ICameraStatus.SHUTTER_SPEED -> getShutterSpeed()
            //ICameraStatus.APERTURE -> getAperture()
            ICameraStatus.EXPREV -> getExpRev()
            //ICameraStatus.CAPTURE_MODE -> getCaptureMode()
            ICameraStatus.ISO_SENSITIVITY -> getIsoSensitivity()
            ICameraStatus.WHITE_BALANCE -> getWhiteBalance()
            //ICameraStatus.AE -> getMeteringMode()
            ICameraStatus.EFFECT -> getPictureEffect()
            ICameraStatus.BATTERY -> getRemainBattery()
            ICameraStatus.TORCH_MODE -> getTorchMode()
            //ICameraStatus.FOCUS_STATUS -> getfocusStatus()
            else -> ""
        })
    }

    private fun getRemainBatteryColor() : Int
    {
        return (Color.WHITE)
    }

    private fun getTakeMode() : String
    {
        var status = ""
        try
        {

        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        return (status)
    }

    private fun getShutterSpeed() : String
    {
        var status = ""
        try
        {

        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        return (status)
    }

    private fun getExpRev() : String
    {
        var status = ""
        try
        {

        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        return (status)
    }

    private fun getIsoSensitivity() : String
    {
        var status = ""
        try
        {

        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        return (status)
    }

    private fun getRemainBattery() : String
    {
        var status = ""
        try
        {

        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        return (status)
    }

    private fun getWhiteBalance() : String
    {
        return ("WB: $currentWhiteBalance")
    }

    private fun getPictureEffect() : String
    {
        return ("Zoom")
    }

    private fun getTorchMode() : String
    {
        return ("Flash: $currentFlashMode")
    }

    fun setItemStatus(key: String, value: String)
    {
        Log.v(TAG, " setItemStatus(key:$key, value:$value)")
        try
        {
            when (key) {
                ICameraStatus.TAKE_MODE -> setTakeMode(value)
                ICameraStatus.SHUTTER_SPEED -> setShutterSpeed(value)
                //ICameraStatus.APERTURE -> setAperture(value)
                ICameraStatus.EXPREV -> setExpRev(value)
                //ICameraStatus.CAPTURE_MODE -> setCaptureMode(value)
                ICameraStatus.ISO_SENSITIVITY -> setIsoSensitivity(value)
                ICameraStatus.WHITE_BALANCE -> setWhiteBalance(value)
                //ICameraStatus.AE -> setMeteringMode(value)
                ICameraStatus.EFFECT -> setPictureEffect(value)
                ICameraStatus.TORCH_MODE -> setTorchMode(value)
                //ICameraStatus.BATTERY -> setRemainBattery(value)
                //ICameraStatus.FOCUS_STATUS -> setfocusStatus(value)
                else -> return
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    private fun setTakeMode(value: String)
    {
        try
        {
            if (!::commandPublisher.isInitialized)
            {
                // 未初期化の場合はコマンドを送らない
                return
            }
            Log.v(TAG, " setTakeMode($value)")
            when (value)
            {
                "P" -> commandPublisher.enqueueCommand(PixproChangeMode(PixproCommandOnlyCallback(), 0x01))
                "M" -> commandPublisher.enqueueCommand(PixproChangeMode(PixproCommandOnlyCallback(), 0x08))
                "ASCN" -> commandPublisher.enqueueCommand(PixproChangeMode(PixproCommandOnlyCallback(), 0x20))
                "Video" -> commandPublisher.enqueueCommand(PixproChangeVideoMode(PixproCommandOnlyCallback()))
                "Cont. Shot" -> commandPublisher.enqueueCommand(PixproChangeMode(PixproCommandOnlyCallback(), 0x00, 0x08))
                else -> { }
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    private fun setShutterSpeed(value: String)
    {
        try
        {
            Log.v(TAG, " setShutterSpeed($value)")

        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    private fun setExpRev(value: String)
    {
        try
        {
            Log.v(TAG, " setExpRev($value)")

        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    private fun setIsoSensitivity(value: String)
    {
        try
        {
            Log.v(TAG, " setIsoSensitivity($value)")

        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    private fun setWhiteBalance(value: String)
    {
        try
        {
            if (!::commandPublisher.isInitialized)
            {
                // 未初期化の場合はコマンドを送らない
                return
            }
            Log.v(TAG, " setWhiteBalance($value)")
            when (value)
            {
                "AUTO" -> commandPublisher.enqueueCommand(PixproWhiteBalance(PixproCommandOnlyCallback(), 0x01))
                "Daylight" -> commandPublisher.enqueueCommand(PixproWhiteBalance(PixproCommandOnlyCallback(), 0x02))
                "Cloudy" -> commandPublisher.enqueueCommand(PixproWhiteBalance(PixproCommandOnlyCallback(), 0x04))
                "Fluorescent" -> commandPublisher.enqueueCommand(PixproWhiteBalance(PixproCommandOnlyCallback(), 0x10))
                "Fluorescent CWF" -> commandPublisher.enqueueCommand(PixproWhiteBalance(PixproCommandOnlyCallback(), 0x20)) //
                "Incandescent" -> commandPublisher.enqueueCommand(PixproWhiteBalance(PixproCommandOnlyCallback(), 0x80))    //
                "Unknown" -> commandPublisher.enqueueCommand(PixproWhiteBalance(PixproCommandOnlyCallback(), 0x40))         //
                "Other" -> commandPublisher.enqueueCommand(PixproWhiteBalance(PixproCommandOnlyCallback(), 0x08))           // 不明...
                else -> { }
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    private fun setPictureEffect(value: String)
    {
        try
        {
            if (!::commandPublisher.isInitialized)
            {
                // 未初期化の場合はコマンドを送らない
                return
            }
            Log.v(TAG, " setPictureEffect($value)")
            when (value)
            {
                "Zoom In" -> commandPublisher.enqueueCommand(PixproExecuteZoom(PixproCommandOnlyCallback(), 1))
                "Zoom Out" -> commandPublisher.enqueueCommand(PixproExecuteZoom(PixproCommandOnlyCallback(), -1))
                else -> { }
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }


    private fun setTorchMode(value: String)
    {
        try
        {
            if (!::commandPublisher.isInitialized)
            {
                // 未初期化の場合はコマンドを送らない
                return
            }
            Log.v(TAG, " setTorchMode($value)")
            when (value)
            {
                "OFF" -> commandPublisher.enqueueCommand(PixproFlashOff(PixproCommandOnlyCallback()))
                "ON" -> commandPublisher.enqueueCommand(PixproFlashOn(PixproCommandOnlyCallback()))
                "AUTO" -> commandPublisher.enqueueCommand(PixproFlashAuto(PixproCommandOnlyCallback()))
                else -> { }
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    init
    {
        statusHolder.clear()
    }
}
