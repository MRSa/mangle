package jp.osdn.gokigen.gokigenassets.camera.vendor

import android.util.Log
import jp.osdn.gokigen.gokigenassets.scene.IInformationReceiver

class CameraControlCoordinator(private val informationReceiver: IInformationReceiver) : ICameraControlCoordinator
{
    private val panasonicCameraDeviceMap = mutableMapOf<Int, String>()
    private val cameraControlInfo = mutableMapOf<Int, String>()
    private var connectingCameraNumber = -1

    override fun startConnectToCamera(number: Int)
    {
        connectingCameraNumber = number
    }

    override fun isOtherCameraConnecting(number: Int): Boolean
    {
        return (connectingCameraNumber != number)
    }

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
            connectingCameraNumber = -1
            panasonicCameraDeviceMap.remove(number)
            cameraControlInfo.remove(number)
            updateInformation()
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
            connectingCameraNumber = -1
            panasonicCameraDeviceMap[number] = deviceId
            cameraControlInfo[number] = "$number "
            updateInformation()
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

    private fun updateInformation()
    {
        var message = ""
        for ((k, v) in cameraControlInfo)
        {
            message += v
        }
        informationReceiver.updateMessage(message)

    }

    companion object
    {
        private val TAG = CameraControlCoordinator::class.java.simpleName
    }
}
