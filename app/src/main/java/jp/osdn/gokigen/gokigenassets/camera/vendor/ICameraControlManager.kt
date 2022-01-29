package jp.osdn.gokigen.gokigenassets.camera.vendor

import jp.osdn.gokigen.gokigenassets.camera.vendor.panasonic.IPanasonicCamera

interface ICameraControlManager
{
    fun isAlreadyAssignedCameraControl(number: Int) : Boolean
    fun releaseCameraControl(number: Int)

    fun assignPanasonicCamera(number: Int, cameraDevice: IPanasonicCamera)
    fun isAssignedPanasonicCamera(cameraDevice: IPanasonicCamera) : Boolean
}
