package jp.osdn.gokigen.gokigenassets.camera.panasonic.status

interface ICameraStatusHolder
{
    fun getCameraStatus(): String?
    fun getLiveviewStatus(): Boolean
    fun getShootMode(): String?
    fun getAvailableShootModes(): List<String?>?
    fun getZoomPosition(): Int
    fun getStorageId(): String?
}
