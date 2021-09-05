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
        return (listOf("P", "M", "ASCN"))
        //return (listOf("P", "M", "ASCN", "Video", "Cont. Shot"))
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
        return (listOf(
            "-3.0",
            "-2.7",
            "-2.3",
            "-2.0",
            "-1.7",
            "-1.3",
            "-1.0",
            "-0.7",
            "-0.3",
            "0.0",
            "+0.3",
            "+0.7",
            "+1.0",
            "+1.3",
            "+1.7",
            "+2.0",
            "+2.3",
            "+2.7",
            "+3.0"
        ))
    }

    fun getAvailableCaptureMode(): List<String?>
    {
        return (ArrayList())
    }

    fun getAvailableIsoSensitivity(): List<String?>
    {
        return (listOf("AUTO", "100", "200", "400", "800",  "1600", "3200"))
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
        return (listOf("Zoom In", "Zoom Out"))
    }

    fun getAvailableTorchMode(): List<String?>
    {
        return (listOf("OFF", "ON", "AUTO"))
    }

}
