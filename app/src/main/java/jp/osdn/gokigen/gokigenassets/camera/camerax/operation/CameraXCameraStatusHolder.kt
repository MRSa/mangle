package jp.osdn.gokigen.gokigenassets.camera.camerax.operation

import jp.osdn.gokigen.gokigenassets.camera.interfaces.ICameraStatus

class CameraXCameraStatusHolder : ICameraStatus
{
    override fun getStatusList(key: String): List<String?>
    {
        return (ArrayList<String>())
    }
    override fun getStatus(key: String): String { return ("") }
    override fun setStatus(key: String, value: String) { }
}