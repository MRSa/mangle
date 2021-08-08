package jp.osdn.gokigen.gokigenassets.camera.panasonic.status

import android.util.Log
import jp.osdn.gokigen.gokigenassets.camera.interfaces.ICameraStatus
import jp.osdn.gokigen.gokigenassets.camera.panasonic.IPanasonicCamera
import jp.osdn.gokigen.gokigenassets.utils.communication.SimpleHttpClient
import java.util.ArrayList

class CameraStatusListHolder(private val remote: IPanasonicCamera)
{
    val http = SimpleHttpClient()
    companion object
    {
        private val TAG = CameraStatusListHolder::class.java.simpleName
        private const val TIMEOUT_MS = 3000
        private const val MAX_RETRY_COUNT = 2
    }

    /**
     *
     *
     */
    fun getAvailableItemList(key: String): List<String>
    {
        try
        {
            return (when (key) {
                ICameraStatus.TAKE_MODE -> getAvailableTakeMode()
                ICameraStatus.SHUTTER_SPEED -> getAvailableShutterSpeed()
                ICameraStatus.APERTURE -> getAvailableAperture()
                ICameraStatus.EXPREV -> getAvailableExpRev()
                ICameraStatus.CAPTURE_MODE -> getAvailableCaptureMode()
                ICameraStatus.ISO_SENSITIVITY -> getAvailableIsoSensitivity()
                ICameraStatus.WHITE_BALANCE -> getAvailableWhiteBalance()
                ICameraStatus.AE -> getAvailableMeteringMode()
                ICameraStatus.EFFECT -> getAvailablePictureEffect()
                ICameraStatus.TORCH_MODE -> getAvailableTorchMode()
                //ICameraStatus.BATTERY -> getAvailableRemainBattery()
                else -> ArrayList()
            })
            //Log.v(TAG, " ----- getAvailableItemList($key) ")
            //sendCamGetSettingCmd("colormode")
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        return (ArrayList())
    }

    private fun getAvailableTakeMode() : List<String>
    {
        return (ArrayList())
    }

    private fun getAvailableTorchMode() : List<String>
    {
        return (listOf("OFF", "EXPOSURE-BRACKET", "FOCAL-BRACKET", "WB-BRACKET", "FOCUS-BRACKET"))
    }

    private fun getAvailableShutterSpeed() : List<String>
    {
        return (ArrayList())
    }

    private fun getAvailableAperture() : List<String>
    {
        return (ArrayList())
    }

    private fun getAvailableExpRev() : List<String>
    {
        return (listOf(
            "-5.0",
            "-4.7",
            "-4.3",
            "-4.0",
            "-3.7",
            "-3.3",
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
            "+3.0",
            "+3.3",
            "+3.7",
            "+4.0",
            "+4.3",
            "+4.7",
            "+5.0",
            ))
    }

    private fun getAvailablePictureEffect() : List<String>
    {
        return (listOf("OFF",
            "POP",
            "RETR",
            "OLD",
            "HKEY",
            "LKEY",
            "SEPI",
            "MONO",
            "D.MONO",
            "R.MONO",
            "S.MONO",
            "IART",
            "HDYN",
            "XPRO",
            "TOY",
            "TOYP",
            "BLEA",
            "DIOR",
            "SOFT",
            "FAN",
            "STAR",
            "1CLR",
            "SUN"))
    }

    private fun getAvailableIsoSensitivity() : List<String>
    {
        return (listOf("ISO-i",
            "AUTO",
            "100",
            "125",
            "160",
            "200",
            "250",
            "320",
            "400",
            "500",
            "640",
            "800",
            "1000",
            "1250",
            "1600",
            "2000",
            "2500",
            "3200",
            "4000",
            "5000",
            "6400",
            "8000",
            "10000",
            "12800",
            "16000",
            "20000",
            "25600",
            "50000",
        ))
    }

    private fun getAvailableWhiteBalance() : List<String>
    {
        return (listOf("AWB",
        "Daylight",
        "Cloudy",
        "Shade",
        "Incandescent",
        "Flash",
        "Custom1",
        "Custom2",
        "Custom3",
        "Custom4",
        "Color Temp.",
        "K1",
        "K2",
        "K3",
        "K4",
        "AWBc",
        "AWBw"))
    }

    private fun getAvailableMeteringMode() : List<String>
    {
        return (ArrayList())
        //return (listOf("MULTI", "CENTER", "SPOT", "HIGHLIGHT"))  // SPOT測光に切り替えると、LVがうまく取れないので...
    }

    private fun getAvailableCaptureMode() : List<String>
    {
        return (listOf("STD", "VIVID", "NATURAL", "MONO", "L.MONO", "SCENERY", "PORTRAIT", "CUSTOM"))
    }

    fun setStatus(key: String, value: String)
    {
        Log.v(TAG, " setStatus(key:$key, value:$value)")
        try
        {
            when (key) {
                ICameraStatus.TAKE_MODE -> setTakeMode(value)
                ICameraStatus.SHUTTER_SPEED -> setShutterSpeed(value)
                ICameraStatus.APERTURE -> setAperture(value)
                ICameraStatus.EXPREV -> setExpRev(value)
                ICameraStatus.CAPTURE_MODE -> setCaptureMode(value)
                ICameraStatus.ISO_SENSITIVITY -> setIsoSensitivity(value)
                ICameraStatus.WHITE_BALANCE -> setWhiteBalance(value)
                ICameraStatus.AE -> setMeteringMode(value)
                ICameraStatus.EFFECT -> setPictureEffect(value)
                ICameraStatus.TORCH_MODE -> setTorchMode(value)
                //ICameraStatus.BATTERY -> setRemainBattery(value)
                else -> return
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    private fun setTakeMode(value: String)
    {

    }

    private fun setTorchMode(value: String)
    {
        val setValue = when (value) {
            "OFF" -> "off"
            "EXPOSURE-BRACKET" -> "exposure"
            "FOCAL-BRACKET" -> "focal"
            "WB-BRACKET" -> "wb"
            "FOCUS-BRACKET" -> "focus"
            else -> "off"
        }
        sendCamSetSettingCmd("bracket", setValue, null)
    }

    private fun setShutterSpeed(value: String)
    {

    }

    private fun setAperture(value: String)
    {

    }

    private fun setExpRev(value: String)
    {
        val setValue = when (value) {
            "-5.0" -> "-5"
            "-4.7" -> "-14/3"
            "-4.3" -> "-13/3"
            "-4.0" -> "-4"
            "-3.7" -> "-11/3"
            "-3.3" -> "-10/3"
            "-3.0" -> "-3"
            "-2.7" -> "-8/3"
            "-2.3" -> "-7/3"
            "-2.0" -> "-2"
            "-1.7" -> "-5/3"
            "-1.3" -> "-4/3"
            "-1.0" -> "-1"
            "-0.7" -> "-2/3"
            "-0.3" -> "-1/3"
            "0.0" -> "0"
            "+0.3" -> "1/3"
            "+0.7" -> "2/3"
            "+1.0" -> "1"
            "+1.3" -> "4/3"
            "+1.7" -> "5/3"
            "+2.0" -> "2"
            "+2.3" -> "7/3"
            "+2.7" -> "8/3"
            "+3.0" -> "3"
            "+3.3" -> "10/3"
            "+3.7" -> "11/3"
            "+4.0" -> "4"
            "+4.3" -> "13/3"
            "+4.7" -> "14/3"
            "+5.0" -> "5"
            else -> "0"
        }
        sendCamSetSettingCmd("exposure", setValue, null)
    }

    private fun setCaptureMode(value: String)
    {
        //Log.v(TAG, " ===== setCaptureMode($value) =====")
        val setValue = when (value) {
            "STD" -> "standard"
            "VIVID" -> "vivid"
            "NATURAL" -> "natural"
            "MONO" -> "bw"
            "L.MONO" -> "l_bw"
            "SCENERY" -> "scenery"
            "PORTRAIT" -> "portrait"
            "CUSTOM" -> "custom"
            else -> return
        }
        sendCamSetSettingCmd("colormode", setValue, null)
    }

    private fun setIsoSensitivity(value: String)
    {
        val setValue = when (value) {
            "ISO-i" -> "i_auto"
            "AUTO" -> "auto"
            else -> value
        }
        sendCamSetSettingCmd("iso", setValue, null)
    }

    private fun setWhiteBalance(value: String)
    {
        val setValue =  when (value) {
            "AWB" -> "auto"
            "Daylight" -> "daylight"
            "Cloudy" -> "cloudy"
            "Shade" -> "shade"
            "Incandescent" -> "halogen"
            "Flash" -> "flash"
            "Custom1" -> "white_set1"
            "Custom2" -> "white_set2"
            "Custom3" -> "white_set3"
            "Custom4" -> "white_set4"
            "Color Temp." -> "color_temp"
            "K1" -> "color_temp1"
            "K2" -> "color_temp2"
            "K3" -> "color_temp3"
            "K4" -> "color_temp4"
            "AWBc" -> "auto_cool"
            "AWBw" -> "auto_warm"
            else -> "auto"
        }
        sendCamSetSettingCmd("whitebalance", setValue, null)
    }

    private fun setMeteringMode(value: String)
    {
        val setValue = when (value) {
            "MULTI" -> "multi"
            "CENTER" -> "center"
            "SPOT" -> "spot"
            "HIGHLIGHT" -> "highlight"
            else -> "multi"
        }
        sendCamSetSettingCmd("lightmetering", setValue, null)
    }

    private fun setPictureEffect(value: String)
    {
        val setValue = when (value) {
            "POP" -> "pop"
            "RETR" -> "retro"
            "OLD" -> "old_days"
            "HKEY" -> "high_key"
            "LKEY" -> "low_key"
            "SEPI" -> "sepia"
            "MONO" -> "monochro"
            "D.MONO" -> "dynamic_monochro"
            "R.MONO" -> "rough_monochro"
            "S.MONO" -> "silky_monochro"
            "IART" -> "impressive_art"
            "HDYN" -> "high_dynamic"
            "XPRO" -> "cross_proc"
            "TOY" -> "toy_photo"
            "TOYP" -> "toy_pop"
            "BLEA" -> "bleach_bypass"
            "DIOR" -> "diorama"
            "SOFT" -> "soft_focus"
            "FAN" -> "fantasy"
            "STAR" -> "cross_filter"
            "1CLR" -> "one_point_color"
            "SUN" -> "sunshine"
            else -> "noeffect"
        }
        sendCamSetSettingCmd("filter_setting", setValue, null)
    }

    private fun sendCamGetSettingCmd(type: String)
    {
        try
        {
            val urlToSend = remote.getCmdUrl() + "cam.cgi?mode=getsetting&type=$type"
            val thread = Thread {
                try
                {
                    var retryCount = 0
                    var loop = true
                    while (loop)
                    {
                        val reply: String = http.httpGet(urlToSend, TIMEOUT_MS)
                        if (reply.indexOf("<result>ok</result>") > 0)
                        {
                            loop = false
                            Log.v(TAG, " ===== $urlToSend (OK) : $reply")
                        }
                        else
                        {
                            // エラー発生時は何回か(MAX_RETRY_COUNT分)再送する
                            Log.v(TAG, " $urlToSend (NG) : $reply ")
                            Thread.sleep(1000) // 1秒待つ
                            retryCount++
                            if(retryCount >= MAX_RETRY_COUNT)
                            {
                                loop = false
                            }
                        }
                    }
                }
                catch (e: Exception)
                {
                    e.printStackTrace()
                }
            }
            thread.start()
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    private fun sendCamSetSettingCmd(msgType: String, msgValue: String, value2: String?)
    {
        try
        {
            //Log.v(TAG, "  ------- sendCamSetSettingCmd($msgType, $msgValue, $value2)")
            val sendMessage = if (value2 != null) { "cam.cgi?mode=setsetting&type=$msgType&value=$msgValue&value2=$value2" } else { "cam.cgi?mode=setsetting&type=$msgType&value=$msgValue" }
            val thread = Thread {
                try
                {
                    var retryCount = 0
                    var loop = true
                    while (loop)
                    {
                        val urlToSend = remote.getCmdUrl() + sendMessage
                        val reply: String = http.httpGet(urlToSend, TIMEOUT_MS)
                        if (reply.indexOf("<result>ok</result>") > 0)
                        {
                            loop = false
                            // Log.v(TAG, " --- SET SETTING ($msgType,$msgValue): $reply")
                        }
                        else
                        {
                            // エラー発生時は何回か(MAX_RETRY_COUNT分)再送する
                            Log.v(TAG, " $urlToSend (NG) : $reply ")
                            Thread.sleep(1000) // 1秒待つ
                            retryCount++
                            if(retryCount >= MAX_RETRY_COUNT)
                            {
                                loop = false
                            }
                        }
                    }
                }
                catch (e: Exception)
                {
                    e.printStackTrace()
                }
            }
            thread.start()
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }
}
