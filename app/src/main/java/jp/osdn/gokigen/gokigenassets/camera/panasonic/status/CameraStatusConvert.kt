package jp.osdn.gokigen.gokigenassets.camera.panasonic.status

import android.graphics.Color
import android.util.Log
import jp.osdn.gokigen.gokigenassets.camera.interfaces.ICameraStatus
import jp.osdn.gokigen.gokigenassets.utils.communication.SimpleLogDumper
import java.util.ArrayList

class CameraStatusConvert(private val statusHolder: CameraStatusHolder) : ICameraStatus, ICameraEventObserver
{
    //  現物合わせのクラス...
    private var eventData: ByteArray? = null
    private var currentBattery : Int = 0

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
    override fun getStatus(key: String): String
    {
        return (when (key) {
            ICameraStatus.TAKE_MODE -> getTakeMode()
            ICameraStatus.SHUTTER_SPEED -> getShutterSpeed()
            ICameraStatus.APERTURE -> getAperture()
            ICameraStatus.EXPREV -> getExpRev()
            ICameraStatus.CAPTURE_MODE -> getCaptureMode()
            ICameraStatus.ISO_SENSITIVITY -> getIsoSensitivity()
            ICameraStatus.WHITE_BALANCE -> getWhiteBalance()
            ICameraStatus.AE -> getMeteringMode()
            ICameraStatus.EFFECT -> getPictureEffect()
            ICameraStatus.BATTERY -> getRemainBattery()
            else -> ""
        })
    }

    override fun getStatusColor(key: String): Int
    {
        return (when (key) {
            ICameraStatus.BATTERY -> getRemainBatteryColor()
            else -> Color.WHITE
        })
    }

    private fun getTakeMode() : String
    {
        return ("")
    }

    private fun getShutterSpeed() : String
    {
        return ("")
    }

    private fun getAperture() : String
    {
        return ("")
    }

    private fun getExpRev() : String
    {
        return ("")
    }

    private fun getCaptureMode() : String
    {
        return ("")
    }

    private fun getIsoSensitivity() : String
    {
        return ("")
    }

    private fun getWhiteBalance() : String
    {
        return ("")
    }

    private fun getMeteringMode() : String
    {
        return ("")
    }

    private fun getPictureEffect() : String
    {
        return ("")
    }

    private fun getRemainBattery() : String
    {
        try
        {
            currentBattery = statusHolder.getCurrentStatus(ICameraStatus.BATTERY).toInt()
            return ("Batt.:$currentBattery%")
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        return ("")
    }

    private fun getRemainBatteryColor() : Int
    {
        var color = Color.WHITE
        try
        {
            if (currentBattery < 50)
            {
                color = Color.YELLOW
            }
            else if (currentBattery < 30)
            {
                color = Color.RED
            }
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
        return (color)
    }

    override fun receivedEvent(eventData: ByteArray?)
    {
        try
        {
            this.eventData = eventData
            val size = this.eventData?.size ?: 0
            Log.v(TAG, "  ----- RECEIVED STATUS $size bytes. ----- ")
            SimpleLogDumper.dumpBytes("LV DATA [$size]", this.eventData?.copyOfRange(0, size))
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    companion object
    {
        private val TAG = CameraStatusConvert::class.java.simpleName
    }
}
