package jp.osdn.gokigen.gokigenassets.camera.camerax.operation

import android.annotation.SuppressLint
import android.graphics.Color
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CaptureRequest.*
import android.util.Log
import jp.osdn.gokigen.gokigenassets.camera.interfaces.ICameraStatus

class CameraXCameraStatusHolder(private val cameraXCameraControl: CameraXCameraControl) : ICameraStatus
{
    companion object
    {
        private val TAG = CameraXCameraStatusHolder::class.java.simpleName
    }


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

    @SuppressLint("UnsafeOptInUsageError")
    private fun getTakeMode() : String
    {
/*
        try
        {
            val cameraControl = cameraXCameraControl.getCamera2CameraControl()
            if (cameraControl != null)
            {
                Log.v(TAG, "  -========-")
                val captureOptions = cameraControl.captureRequestOptions
                // https://developer.android.com/reference/android/hardware/camera2/CaptureRequest
                //val controlMode = captureOptions.getCaptureRequestOption(CONTROL_MODE)
                //val controlMode = captureOptions.getCaptureRequestOption(CONTROL_AE_MODE)
                //val controlMode = captureOptions.getCaptureRequestOption(CONTROL_AF_MODE)
                //val controlMode = captureOptions.getCaptureRequestOption(LENS_APERTURE)
                //val controlMode = captureOptions.getCaptureRequestOption(SENSOR_SENSITIVITY)
                //val controlMode = captureOptions.getCaptureRequestOption(TONEMAP_MODE)
                //val controlMode = captureOptions.getCaptureRequestOption(SENSOR_EXPOSURE_TIME)
                //val controlMode = captureOptions.getCaptureRequestOption(CONTROL_EFFECT_MODE)
                val controlMode = captureOptions.getCaptureRequestOption(CONTROL_SCENE_MODE)
                Log.v(TAG, "  Control Mode : $controlMode")

                Log.v(TAG, "  -========-")
                //return ("$controlMode")
            }
            val cameraInfo = cameraXCameraControl.getCamera2CameraInfo()
            if (cameraInfo != null)
            {
                Log.v(TAG, "  ---  ---  ---")
                val effect = cameraInfo.getCameraCharacteristic(CameraCharacteristics.CONTROL_AVAILABLE_EFFECTS)
                if (effect != null)
                {
                    for (eValue in effect)
                    {
                        Log.v(TAG, "getTakeMode : $eValue")
                    }
                }
                //val hwLevel = cameraInfo.getCameraCharacteristic(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL)
                //Log.v(TAG, "Hardware Level : $hwLevel")
                //val orientation = cameraInfo.getCameraCharacteristic(CameraCharacteristics.SENSOR_ORIENTATION)
                //Log.v(TAG, "Sensor Orientation : $orientation")

                Log.v(TAG, "  ---  ---  ---")
            }
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
*/
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
        try
        {
            // ExposureCompensation : ExpRev
            val exposureState = cameraXCameraControl.getExposureState()
            if ((exposureState != null)&&(exposureState.isExposureCompensationSupported))
            {
                val index = exposureState.exposureCompensationIndex
                val step = exposureState.exposureCompensationStep
                val value = index.toDouble() * step.toDouble()
                if (value != 0.0)
                {
                    return (String.format("2.1f", value))
                }
            }
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
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


    @SuppressLint("UnsafeOptInUsageError")
    private fun getRemainBattery() : String
    {
        try
        {

            val cameraInfo = cameraXCameraControl.getCamera2CameraInfo()
            if (cameraInfo != null)
            {
                val hwLevel = cameraInfo.getCameraCharacteristic(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL)
                return ("ID: ${cameraInfo.cameraId}  (L$hwLevel)")
            }
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
        return ("")
    }

}
