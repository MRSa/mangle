package jp.osdn.gokigen.gokigenassets.camera.panasonic.status

import android.util.Log
import jp.osdn.gokigen.gokigenassets.camera.interfaces.ICameraStatus
import java.util.ArrayList

class CameraStatusConvert(private val statusHolder: CameraStatusHolder) : ICameraStatus
{

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
        return ("")
    }

    companion object
    {
        private val TAG = CameraStatusConvert::class.java.simpleName
    }

}