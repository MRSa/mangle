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
        return (listOf(
            "1/2000",
            "1/1600",
            "1/1200",
            "1/1000",
            "1/800",
            "1/600",
            "1/500",
            "1/400",
            "1/320",
            "1/250",
            "1/200",
            "1/160",
            "1/125",
            "1/100",
            "1/80",
            "0x10",
            "0x11",
            "0x12",
            "0x13",
            "0x14",
            "0x15",
            "0x16",
            "0x17",
            "0x18",
            "0x19",
            "0x1a",
            "0x1b",
            "0x1c",
            "0x1d",
            "0x1e",
            "0x1f",
            "0x20",
            "0x21",
            "0x22",
            "0x23",
            "0x24",
            "0x25",
            "0x26",
            "0x27",
            "0x28",
            "0x29",
            "0x2a",
            "0x2b",
            "0x2c",
            "0x2d",
            "0x2e",
            "0x2f",
            "0x30",
            "0x31",
        ))
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
