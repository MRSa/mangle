package jp.osdn.gokigen.gokigenassets.camera.panasonic.status

import android.graphics.Color
import android.util.Log
import jp.osdn.gokigen.gokigenassets.camera.interfaces.ICameraStatus
import java.util.ArrayList
import kotlin.math.pow

class CameraStatusConvert(private val statusHolder: CameraStatusHolder) : ICameraStatus, ICameraEventObserver
{
    //  現物合わせのクラス...
    private var eventData: ByteArray? = null
    private var currentBattery : Int = 0

    override fun getStatusList(key: String): List<String>
    {
        try
        {
            val listKey = key + "List"
            return statusHolder.getAvailableItemList(listKey)
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        return ArrayList()
    }

    override fun setStatus(key: String, value: String)
    {
        try
        {
            Log.v(TAG, " setStatus(key:$key, value:$value)")
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
    }
    override fun getStatus(key: String): String
    {
        return (when (key) {
            ICameraStatus.TAKE_MODE -> getTakeMode()
            ICameraStatus.SHUTTER_SPEED -> getShutterSpeed()
            ICameraStatus.APERTURE -> getAperture()
            ICameraStatus.EXPREV -> getExpRev()
            ICameraStatus.CAPTURE_MODE -> getCaptureMode()
            ICameraStatus.ISO_SENSITIVITY -> getIsoSensitivity()
            ICameraStatus.WHITE_BALANCE -> getWhiteBalance()
            ICameraStatus.AE -> getMeteringMode()
            ICameraStatus.EFFECT -> getPictureEffect()
            ICameraStatus.BATTERY -> getRemainBattery()
            else -> ""
        })
    }

    override fun getStatusColor(key: String): Int
    {
        return (when (key) {
            ICameraStatus.BATTERY -> getRemainBatteryColor()
            else -> Color.WHITE
        })
    }

    private fun getTakeMode() : String
    {
        // 撮影モード
        var takeMode = ""
        try
        {
            val index = 16 * 6 + 12
            if ((eventData != null)&&((eventData?.size ?: 0) > (index + 3)))
            {
                val value = (eventData?.get(index) ?: 0).toInt()
                val value2 = (eventData?.get(index + 3) ?: 0).toInt()
                takeMode = when (value)
                {
                    1 -> "P"
                    2 -> "A"
                    3 -> "S"
                    4 -> "M"
                    9 -> "iA"
                    86 -> "Panorama"
                    29 -> "SOFT"
                    27 -> "DIOR"
                    103 -> "BLEA"
                    104 -> "TOYP"
                    28 -> "TOY"
                    32 -> "XPRO"
                    26 -> "HDYN"
                    31 -> "IART"
                    110 -> "S.MONO"
                    111 -> "R.MONO"
                    112 -> "D.MONO"
                    30 -> "D.MONO"
                    114 -> "MONO"
                    25 -> "SEPI"
                    24 -> "LKEY"
                    23 -> "HKEY"
                    101 -> "OLD"
                    22 -> "RETR"
                    21 -> "POP"
                    102 -> "SUN"
                    33 -> "1CLR"
                    34 -> "STAR"
                    105 -> "FAN"
                    60 -> "Movie"
                    0 -> "($value2)"
                    else -> "$value($value2)"
                }
            }
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
        return (takeMode)
    }


    private fun getShutterSpeed() : String
    {
        try
        {
            val index = 16 * 5 + 4
            if ((eventData != null)&&((eventData?.size ?: 0) > (index + 1)))
            {
                val value = (eventData?.get(index) ?: 0).toInt()
                val value2 = (eventData?.get(index + 1) ?: 0).toInt()
                val value3 = value * 10 + if (value2 > 0) { 3 } else if (value2 == 0) { 0 } else { 6 }

                // シャッタースピード : ベタで応答する...
                return (when (value3) {
                    0 -> "1s"
                    3 -> "1.3"
                    6 -> "1.6"
                    10 -> "2"
                    13 -> "2.5"
                    16 -> "3.2"
                    20 -> "4"
                    23 -> "5"
                    26 -> "6"
                    30 -> "8"
                    33 -> "10"
                    36 -> "13"
                    40 -> "15"
                    43 -> "20"
                    46 -> "25"
                    50 -> "30"
                    53 -> "40"
                    56 -> "50"
                    60 -> "60"
                    63 -> "80"
                    66 -> "100"
                    70 -> "125"
                    73 -> "160"
                    76 -> "200"
                    80 -> "250"
                    83 -> "320"
                    86 -> "400"
                    90 -> "500"
                    93 -> "640"
                    96 -> "800"
                    100 -> "1000"
                    103 -> "1300"
                    106 -> "1600"
                    110 -> "2000"
                    113 -> "2500"
                    116 -> "3200"
                    120 -> "4000"
                    123 -> "5000"
                    126 -> "6400"
                    130 -> "8000"
                    133 -> "10000"
                    136 -> "13000"
                    140 -> "16000"
                    640 -> "T"
                    -4 -> "1.3s"
                    -7 -> "1.6s"
                    -10 -> "2s"
                    -14 -> "2.5s"
                    -17 -> "3.2s"
                    -20 -> "4s"
                    -24 -> "5s"
                    -27 -> "6s"
                    -30 -> "8s"
                    -34 -> "10s"
                    -37 -> "13s"
                    -40 -> "15s"
                    -44 -> "20s"
                    -47 -> "25s"
                    -50 -> "30s"
                    -54 -> "40s"
                    -57 -> "50s"
                    -60 -> "60s"
                    else -> getShutterSpeedAlternate(value, value2) // 別のロジックで決定する
                })
            }
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
        return ("")
    }


    private fun getShutterSpeedAlternate(value : Int, value2 : Int) : String
    {
        // シャッタースピード (別の計算方法...)
        try
        {
            if (value == 0)
            {
                if (value2 == 0)
                {
                    return ("1s")
                }
                return if (value2 > 0) { ("1.3") } else { ("1.6") }
            }
            if ((value == 0x40)&&(value2 == 0))
            {
                return ("T")
            }
            if (value > 0)
            {
                if (value2 == 0)
                {
                    return (String.format("%2.0f", 2.0.pow(value)))
                }
                val value3 =  if (value2 > 0)
                {
                    2.0.pow(value) + ((2.0.pow(value + 1) - 2.0.pow(value)) / 3.0f)
                } else {
                    2.0.pow(value) + (((2.0.pow(value + 1) - 2.0.pow(value)) * 2.0f) / 3.0f)
                }
                return if (value3 < 5.0f) { (String.format("%2.1f", value3)) } else { (String.format("%2.0f", value3)) }
            }
            else
            {
                val value3 = value * (-1)
                if (value2 == 0)
                {
                    return (String.format("%2.0fs", 2.0.pow(value3)))
                }
                val value4 = if (value2 < 0) {
                    2.0.pow(value3) - (2.0.pow(value3) - 2.0.pow(value3 - 1)) / 3.0f * 2.0f
                } else {
                    2.0.pow(value3) - (2.0.pow(value3) - 2.0.pow(value3 - 1)) / 3.0f
                }
                return if (value4 < 5.0f) { (String.format("%2.1fs", value4)) } else { (String.format("%2.0fs", value4)) }
            }
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
        return (" $value:$value2 ")
    }

    private fun getAperture() : String
    {
        // 絞り値
        var aperture = ""
        try
        {
            val index = 16 * 4 + 8
            if ((eventData != null)&&((eventData?.size ?: 0) > (index + 1)))
            {
                val value = (eventData?.get(index) ?: 0).toInt()
                val value2 = (eventData?.get(index + 1) ?: 0).toInt()
                val value3 = value * 10 + if (value2 > 0) { 3 } else if (value2 == 0) { 0 } else if (value2 < -100) { 5 }  else { 6 }

                //return (getApertureAlternate(value, value2))
                aperture = when (value3) {
                    0 -> "F0.9"
                    3 -> "F1.0"
                    6 -> "F1.1"
                    10 -> "F1.2"
                    13 -> "F1.4"
                    15 -> "F1.7"
                    16 -> "F1.8"
                    20 -> "F2.0"
                    23 -> "F2.2"
                    26 -> "F2.5"
                    30 -> "F2.8"
                    33 -> "F3.2"
                    36 -> "F3.5"
                    40 -> "F4.0"
                    43 -> "F4.5"
                    46 -> "F5.0"
                    50 -> "F5.6"
                    53 -> "F6.3"
                    56 -> "F7.1"
                    60 -> "F8.0"
                    63 -> "F9.0"
                    66 -> "F10"
                    70 -> "F11"
                    73 -> "F13"
                    76 -> "F14"
                    80 -> "F16"
                    83 -> "F18"
                    86 -> "F20"
                    90 -> "F22"
                    93 -> "F25"
                    96 -> "F29"
                    100 -> "F32"
                    103 -> "F36"
                    106 -> "F40"
                    110 -> "F45"
                    113 -> "F51"
                    116 -> "F57"
                    120 -> "F64"
                    123 -> "F72"
                    126 -> "F81"
                    130 -> "F91"
                    else -> getApertureAlternate(value, value2)
                }
            }
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
        return (aperture)
    }

    private fun getApertureAlternate(value : Int, value2 : Int) : String
    {
        try
        {
            val value3 = if (value2 < 0) { value2 * (-1) } else { value2 }

        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
        return ("F($value:$value2)")
    }

    private fun getExpRev() : String
    {
        // 露出補正値
        var expRev = ""
        try
        {
            val index = 16 * 6 + 4
            if ((eventData != null)&&((eventData?.size ?: 0) > (index)))
            {
                val value = (eventData?.get(index) ?: 0).toInt()
                if (value != 0)
                {
                    val rev = if (value < 128) {
                        (value / 3).toFloat() + ((value % 3).toFloat() * 0.33f)
                    } else {
                        val invValue = 256 - value
                        ((invValue / 3) + ((invValue % 3).toFloat() * 0.33f)) * (-1.0f)
                    }
                    expRev = String.format("%1.1f", rev)
                }
            }
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
        return (expRev)
    }

    private fun getCaptureMode() : String
    {
        return ("")
    }

    private fun getIsoSensitivity() : String
    {
        // ISO感度
        var iso = ""
        try
        {
            val index = 16 * 8 + 15
            if ((eventData != null)&&((eventData?.size ?: 0) > (index)))
            {
                val value = (eventData?.get(index) ?: 0).toInt()
                iso = when (value) {
                    0 -> "ISO:auto"
                    1 -> "iso:100"
                    35 -> "ISO:100"
                    2 -> "ISO:125"
                    3 -> "ISO:160"
                    4 -> "ISO:200"
                    5 -> "ISO:250"
                    6 -> "ISO:320"
                    7 -> "ISO:400"
                    8 -> "ISO:500"
                    9 -> "ISO:640"
                    10 -> "ISO:800"
                    11 -> "ISO:1000"
                    12 -> "ISO:1250"
                    13 -> "ISO:1600"
                    14 -> "ISO:2000"
                    15 -> "ISO:2500"
                    16 -> "ISO:3200"
                    17 -> "ISO:4000"
                    18 -> "ISO:5000"
                    19 -> "ISO:6400"
                    20 -> "ISO:8000"
                    22 -> "ISO:10000"
                    24 -> "ISO:12800"
                    32 -> "ISO:16000"
                    33 -> "ISO:20000"
                    34 -> "ISO:25600"
                    29 -> "ISO-i"
                    else -> "ISO:($value)"
                }
            }
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
        return (iso)
    }

    private fun getWhiteBalance() : String
    {
        // ホワイトバランス
        var wb = ""
        try
        {
            val index = 16 * 9 + 0
            if ((eventData != null)&&((eventData?.size ?: 0) > (index)))
            {
                val value = (eventData?.get(index) ?: 0).toInt()
                wb = when (value) {
                    0 -> "AWB"
                    2 -> "Daylight"
                    3 -> "Cloudy"
                    4 -> "Shade"
                    5 -> "Incandescent"
                    6 -> "Flash"
                    7 -> "Custom1"
                    8 -> "Custom2"
                    9 -> "Custom3"
                    10 -> "Custom4"
                    11 -> "Color Temp."
                    14 -> "K1"
                    15 -> "K2"
                    16 -> "K3"
                    17 -> "K4"
                    18 -> "AWBc"
                    20 -> "AWBw"
                    else -> "($value)"
                }
            }
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
        return (wb)
    }

    private fun getMeteringMode() : String
    {
        return ("")
    }

    private fun getPictureEffect() : String
    {
        return ("")
    }

    private fun getRemainBattery() : String
    {
        try
        {
            currentBattery = statusHolder.getCurrentStatus(ICameraStatus.BATTERY).toInt()
            return ("Batt.:$currentBattery%")
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        return ("")
    }

    private fun getRemainBatteryColor() : Int
    {
        var color = Color.WHITE
        try
        {
            if (currentBattery < 30)
            {
                color = Color.RED
            }
            else if (currentBattery < 50)
            {
                color = Color.YELLOW
            }
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
        return (color)
    }

    override fun receivedEvent(eventData: ByteArray?)
    {
        try
        {
            this.eventData = eventData
            //val size = this.eventData?.size ?: 0
            //Log.v(TAG, "  ----- RECEIVED STATUS $size bytes. ----- ")
            //SimpleLogDumper.dumpBytes("LV DATA [$size]", this.eventData?.copyOfRange(0, size))
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    companion object
    {
        private val TAG = CameraStatusConvert::class.java.simpleName
    }
}
