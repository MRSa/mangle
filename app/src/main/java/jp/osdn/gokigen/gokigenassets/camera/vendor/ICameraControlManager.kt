package jp.osdn.gokigen.gokigenassets.camera.vendor

interface ICameraControlManager
{
    fun isAlreadyAssignedCameraControl(number: Int) : Boolean
    fun releaseCameraControl(number: Int)

    fun assignCameraControl(number: Int, deviceId: String)
    fun isAssignedCameraControl(deviceId: String) : Boolean
}
