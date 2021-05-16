package jp.osdn.gokigen.gokigenassets.camera.interfaces

interface ICameraHardwareStatus
{
    fun isAvailableHardwareStatus(): Boolean
    fun getLensMountStatus(): String?
    fun getMediaMountStatus(): String?

    fun getMinimumFocalLength(): Float
    fun getMaximumFocalLength(): Float
    fun getActualFocalLength(): Float

    fun inquireHardwareInformation(): Map<String, Any?>?
}
