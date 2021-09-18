package jp.osdn.gokigen.gokigenassets.camera.vendor.omds.wrapper

import android.util.Log
import jp.osdn.gokigen.gokigenassets.camera.interfaces.ICameraRunMode
import jp.osdn.gokigen.gokigenassets.camera.interfaces.ICameraRunModeCallback
import jp.osdn.gokigen.gokigenassets.utils.communication.SimpleHttpClient
import java.lang.Exception
import java.util.HashMap


class OmdsRunMode(private val executeUrl : String = "http://192.168.0.10") : ICameraRunMode
{
    private val headerMap: MutableMap<String, String> = HashMap()
    private val http = SimpleHttpClient()
    private var runMode = false

    override fun changeRunMode(isRecording: Boolean, callback: ICameraRunModeCallback)
    {
        try
        {
            Log.v(TAG, " changeRunMode : $isRecording")
            val thread = Thread { // カメラとの接続確立を通知する
                var playModeUrl = "$executeUrl/switch_cammode.cgi"
                playModeUrl = if (isRecording)
                {
                    "$playModeUrl?mode=rec"
                }
                else
                {
                    "$playModeUrl?mode=play"
                }
                val response: String = http.httpGetWithHeader(playModeUrl, headerMap, null, TIMEOUT_MS) ?: ""
                Log.v(TAG, " $playModeUrl $response")
                try
                {
                    if (response.contains("ok"))
                    {
                        runMode = isRecording
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

    override fun isRecordingMode(): Boolean
    {
        return (runMode)
    }

    init
    {
        headerMap["User-Agent"] = "OlympusCameraKit" // "OI.Share"
        headerMap["X-Protocol"] = "OlympusCameraKit" // "OI.Share"
    }

    companion object
    {
        private val TAG = OmdsRunMode::class.java.simpleName
        private const val TIMEOUT_MS = 5000
    }
}
