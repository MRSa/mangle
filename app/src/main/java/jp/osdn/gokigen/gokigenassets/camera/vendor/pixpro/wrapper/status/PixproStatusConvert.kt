package jp.osdn.gokigen.gokigenassets.camera.vendor.pixpro.wrapper.status

import jp.osdn.gokigen.gokigenassets.camera.vendor.pixpro.wrapper.command.IPixproCommandPublisher

class PixproStatusConvert(private val statusHolder: PixproStatusHolder)
{

    companion object
    {
        private val TAG = PixproStatusConvert::class.java.simpleName
    }
    private lateinit var commandPublisher : IPixproCommandPublisher

    fun setCommandPublisher(commandPublisher : IPixproCommandPublisher)
    {
        this.commandPublisher = commandPublisher
    }

    fun getAvailableTakeMode(): List<String?>
    {
        return (ArrayList())
    }

    fun getAvailableShutterSpeed(): List<String?>
    {
        return (ArrayList())
    }

    fun getAvailableAperture(): List<String?>
    {
        return (ArrayList())
    }

    fun getAvailableExpRev(): List<String?>
    {
        return (ArrayList())
    }

    fun getAvailableCaptureMode(): List<String?>
    {
        return (ArrayList())
    }

    fun getAvailableIsoSensitivity(): List<String?>
    {
        return (ArrayList())
    }

    fun getAvailableWhiteBalance(): List<String?>
    {
        return (listOf("AUTO", "Daylight", "Cloudy", "Fluorescent", "Fluorescent CWF", "Incandescent"))
    }

    fun getAvailableMeteringMode(): List<String?>
    {
        return (ArrayList())
    }

    fun getAvailablePictureEffect(): List<String?>
    {
        return (ArrayList())
    }

    fun getAvailableTorchMode(): List<String?>
    {
        return (listOf("OFF", "ON", "AUTO"))
    }

}
