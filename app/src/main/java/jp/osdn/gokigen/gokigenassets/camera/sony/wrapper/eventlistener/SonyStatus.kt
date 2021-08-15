package jp.osdn.gokigen.gokigenassets.camera.sony.wrapper.eventlistener

import android.graphics.Color
import android.util.Log
import jp.osdn.gokigen.gokigenassets.camera.interfaces.*
import jp.osdn.gokigen.gokigenassets.camera.sony.wrapper.ISonyCameraApi
import org.json.JSONObject
import java.lang.Exception

class SonyStatus(jsonObject : JSONObject) : ICameraStatus, ICameraChangeListener, ISonyStatusReceiver
{
    private val statusCandidates = SonyStatusCandidates()

    private var currentExposureMode = ""
    private var currentShootMode = ""
    private var currentExposureCompensation = ""
    private var currentFlashMode = ""
    private var currentFNumber = ""
    private var currentFocusMode = ""
    private var currentIsoSpeedRate = ""
    private var currentShutterSpeed = ""
    private var currentWhiteBalanceMode = ""
    private var currentCameraStatus = ""

    fun setCameraApi(sonyCameraApi: ISonyCameraApi)
    {
        statusCandidates.setCameraApi(sonyCameraApi)
    }

    private var jsonObject : JSONObject
    init
    {
        this.jsonObject = jsonObject
    }

    override fun setStatus(key: String, value: String)
    {
         when (key) {
            ICameraStatus.TAKE_MODE -> statusCandidates.setTakeMode(value)
            ICameraStatus.SHUTTER_SPEED -> statusCandidates.setShutterSpeed(value)
            ICameraStatus.APERTURE -> statusCandidates.setAperture(value)
            ICameraStatus.EXPREV -> statusCandidates.setExpRev(value)
            ICameraStatus.CAPTURE_MODE -> statusCandidates.setCaptureMode(value)
            ICameraStatus.ISO_SENSITIVITY -> statusCandidates.setIsoSensitivity(value)
            ICameraStatus.WHITE_BALANCE -> statusCandidates.setWhiteBalance(value)
            ICameraStatus.AE -> statusCandidates.setMeteringMode(value)
            ICameraStatus.EFFECT -> statusCandidates.setPictureEffect(value)
            ICameraStatus.BATTERY -> statusCandidates.setRemainBattery(value)
            ICameraStatus.TORCH_MODE -> statusCandidates.setTorchMode(value)
            else -> { return }
        }
    }

    override fun getStatusList(key: String): List<String?>
    {
        val statusList : List<String?> = (when (key) {
            ICameraStatus.TAKE_MODE -> statusCandidates.getAvailableTakeMode()
            ICameraStatus.SHUTTER_SPEED -> statusCandidates.getAvailableShutterSpeed()
            ICameraStatus.APERTURE -> statusCandidates.getAvailableAperture()
            ICameraStatus.EXPREV -> statusCandidates.getAvailableExpRev()
            ICameraStatus.CAPTURE_MODE -> statusCandidates.getAvailableCaptureMode()
            ICameraStatus.ISO_SENSITIVITY -> statusCandidates.getAvailableIsoSensitivity()
            ICameraStatus.WHITE_BALANCE -> statusCandidates.getAvailableWhiteBalance()
            ICameraStatus.AE -> statusCandidates.getAvailableMeteringMode()
            ICameraStatus.EFFECT -> statusCandidates.getAvailablePictureEffect()
            ICameraStatus.BATTERY -> statusCandidates.getAvailableRemainBattery()
            ICameraStatus.TORCH_MODE -> statusCandidates.getAvailableTorchMode()
            else -> { ArrayList() }
        })
        return (statusList)
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
            ICameraStatus.TORCH_MODE -> getTorchMode()
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

    private fun getRemainBatteryColor() : Int
    {
        return (Color.WHITE)
    }

    private fun getTakeMode() : String
    {
        return (currentExposureMode)
    }

    private fun getShutterSpeed() : String
    {
        return (currentShutterSpeed)
    }

    private fun getAperture() : String
    {
        return (currentFNumber)
    }

    private fun getExpRev() : String
    {
        return (currentExposureCompensation)
    }

    private fun getCaptureMode() : String
    {
        return (currentShootMode)
    }

    private fun getIsoSensitivity() : String
    {
        return (currentIsoSpeedRate)
    }
    private fun getWhiteBalance() : String
    {
        return (currentWhiteBalanceMode)
    }

    private fun getMeteringMode() : String
    {
        return ""
    }

    private fun getPictureEffect() : String
    {
        return ""
    }

    private fun getRemainBattery() : String
    {
        return ""
    }

    private fun getTorchMode() : String
    {
        return (currentCameraStatus)
    }

    override fun onApiListModified(apis: List<String?>?) {
        //TODO("Not yet implemented")
    }

    override fun onCameraStatusChanged(status: String?) {
        //TODO("Not yet implemented")
    }

    override fun onLiveviewStatusChanged(status: Boolean) {
        //TODO("Not yet implemented")
    }

    override fun onShootModeChanged(shootMode: String?) {
        //TODO("Not yet implemented")
    }

    override fun onZoomPositionChanged(zoomPosition: Int) {
        //TODO("Not yet implemented")
    }

    override fun onStorageIdChanged(storageId: String?) {
        //TODO("Not yet implemented")
    }

    override fun onFocusStatusChanged(focusStatus: String?) {
        //TODO("Not yet implemented")
    }

    override fun onResponseError() {
        //TODO("Not yet implemented")
    }

    override fun updateStatus(jsonObject: JSONObject)
    {
        this.jsonObject = jsonObject

        currentCameraStatus = parseEventStatus(currentCameraStatus, jsonObject, "cameraStatus", "cameraStatus", 1)
        currentExposureMode = parseEventStatus(currentExposureMode, jsonObject, "exposureMode", "currentExposureMode", 18)
        currentShootMode = parseEventStatus(currentShootMode, jsonObject, "shootMode", "currentShootMode",21)
        currentExposureCompensation = parseEventStatus(currentExposureCompensation, jsonObject, "exposureCompensation", "currentExposureCompensation",25)

        currentFlashMode = parseEventStatus(currentFlashMode, jsonObject, "flashMode", "currentFlashMode",26)
        currentFNumber = parseEventStatus(currentFNumber, jsonObject, "fNumber", "currentFNumber",27)
        currentFocusMode = parseEventStatus(currentFocusMode, jsonObject, "focusMode", "currentFocusMode",28)

        currentIsoSpeedRate = parseEventStatus(currentIsoSpeedRate, jsonObject, "isoSpeedRate", "currentIsoSpeedRate", 29)
        currentShutterSpeed = parseEventStatus(currentShutterSpeed, jsonObject, "shutterSpeed", "currentShutterSpeed", 32)
        currentWhiteBalanceMode = parseEventStatus(currentWhiteBalanceMode, jsonObject, "whiteBalance", "currentWhiteBalanceMode", 33)
    }

    private fun parseEventStatus(currentStatus: String, replyJson: JSONObject, item: String, key: String, indexOfCameraStatus: Int): String
    {
        var eventStatus = currentStatus
        try
        {
            val resultsObj = replyJson.getJSONArray("result")
            if (!resultsObj.isNull(indexOfCameraStatus))
            {
                val cameraStatusObj = resultsObj.getJSONObject(indexOfCameraStatus)
                val type = cameraStatusObj.getString("type")
                if (item == type)
                {
                    eventStatus = cameraStatusObj.getString(key)
                }
                else
                {
                    Log.w(TAG, "Event reply: Illegal Index ($indexOfCameraStatus: $key) $type : $cameraStatusObj")
                }
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        return (eventStatus)
    }

    companion object
    {
        private val TAG = SonyStatus::class.java.simpleName
    }
}
