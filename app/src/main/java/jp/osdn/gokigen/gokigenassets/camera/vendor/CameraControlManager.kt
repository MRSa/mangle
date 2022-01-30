package jp.osdn.gokigen.gokigenassets.camera.vendor

import android.util.Log
import jp.osdn.gokigen.gokigenassets.camera.vendor.panasonic.IPanasonicCamera

class CameraControlManager : ICameraControlManager
{
    private val panasonicCameraDeviceMap = mutableMapOf<Int, String>()

    override fun isAlreadyAssignedCameraControl(number: Int): Boolean
    {
        try
        {
            return (panasonicCameraDeviceMap.containsKey(number))
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        return (false)
    }

    override fun releaseCameraControl(number: Int)
    {
        try
        {
            panasonicCameraDeviceMap.remove(number)
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    override fun assignCameraControl(number: Int, deviceId: String)
    {
        try
        {
            Log.v(TAG, "assignPanasonicCamera($number, $deviceId)")
            panasonicCameraDeviceMap[number] = deviceId
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    override fun isAssignedCameraControl(deviceId: String): Boolean
    {
        try
        {
            return (panasonicCameraDeviceMap.containsValue(deviceId))
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        return (false)
    }

    companion object
    {
        private val TAG = CameraControlManager::class.java.simpleName
    }
}
