package jp.osdn.gokigen.gokigenassets.camera.camerax.operation

import jp.osdn.gokigen.gokigenassets.camera.interfaces.ICameraStatus

class CameraXCameraStatusListHolder
{

    fun getStatusList(key: String): List<String>
    {
        return (when (key) {
            ICameraStatus.TAKE_MODE -> getAvailableTakeMode()
            ICameraStatus.SHUTTER_SPEED -> getAvailableShutterSpeed()
            ICameraStatus.APERTURE -> getAvailableAvailableAperture()
            ICameraStatus.EXPREV -> getAvailableExpRev()
            ICameraStatus.CAPTURE_MODE -> getAvailableCaptureMode()
            ICameraStatus.ISO_SENSITIVITY -> getAvailableIsoSensitivity()
            ICameraStatus.WHITE_BALANCE -> getAvailableWhiteBalance()
            ICameraStatus.AE -> getAvailableMeteringMode()
            ICameraStatus.EFFECT -> getAvailablePictureEffect()
            ICameraStatus.BATTERY -> ArrayList()
            ICameraStatus.TORCH_MODE -> getAvailableTorchMode()
            else -> ArrayList()
        })
    }

    private fun getAvailableTakeMode() : List<String>
    {
        return (ArrayList())
    }

    private fun getAvailableShutterSpeed() : List<String>
    {
        return (ArrayList())
    }

    private fun getAvailableAvailableAperture() : List<String>
    {
        return (ArrayList())
    }

    private fun getAvailableExpRev() : List<String>
    {
        return (ArrayList())
    }

    private fun getAvailableCaptureMode() : List<String>
    {
        return (ArrayList())
    }

    private fun getAvailableIsoSensitivity() : List<String>
    {
        return (ArrayList())
    }

    private fun getAvailableWhiteBalance() : List<String>
    {
        return (ArrayList())
    }

    private fun getAvailableMeteringMode() : List<String>
    {
        return (ArrayList())
    }

    private fun getAvailablePictureEffect() : List<String>
    {
        return (ArrayList())
    }

    private fun getAvailableTorchMode() : List<String>
    {
        return (listOf("OFF", "SINGLE", "TORCH"))
    }
}
