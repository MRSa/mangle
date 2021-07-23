package jp.osdn.gokigen.gokigenassets.camera.camerax.operation

import android.graphics.Color
import jp.osdn.gokigen.gokigenassets.camera.interfaces.ICameraStatus

class CameraXCameraStatusHolder : ICameraStatus
{
    override fun getStatusList(key: String): List<String?>
    {
        return (ArrayList<String>())
    }
    override fun setStatus(key: String, value: String) { }

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
        return (Color.WHITE)
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

}
