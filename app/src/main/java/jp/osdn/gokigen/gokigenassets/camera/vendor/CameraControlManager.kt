package jp.osdn.gokigen.gokigenassets.camera.vendor

import android.util.Log
import jp.osdn.gokigen.gokigenassets.camera.vendor.panasonic.IPanasonicCamera

class CameraControlManager : ICameraControlManager
{
    private val panasonicCameraDeviceMap = mutableMapOf<Int, IPanasonicCamera>()

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

    override fun assignPanasonicCamera(number: Int, cameraDevice: IPanasonicCamera)
    {
        try
        {
            Log.v(TAG, "assignPanasonicCamera($number, cameraDevice)")
            panasonicCameraDeviceMap[number] = cameraDevice
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    override fun isAssignedPanasonicCamera(cameraDevice: IPanasonicCamera): Boolean
    {
        try
        {
            return (panasonicCameraDeviceMap.containsValue(cameraDevice))
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
