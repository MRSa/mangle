package jp.osdn.gokigen.gokigenassets.camera.vendor.omds.connection

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import jp.osdn.gokigen.gokigenassets.utils.communication.SimpleHttpClient
import java.lang.Exception
import java.util.HashMap

class OmdsCameraDisconnectSequence(private val context: AppCompatActivity, private val powerOff: Boolean, private val executeUrl : String = "http://192.168.0.10") : Runnable
{
    private val headerMap: MutableMap<String, String> = HashMap()
    private val http = SimpleHttpClient()

    override fun run()
    {
        // カメラをPowerOffして接続を切る
        try
        {
            if (powerOff)
            {
                val cameraPowerOffUrl = "$executeUrl/exec_pwoff.cgi"
                val response: String = http.httpGetWithHeader(cameraPowerOffUrl, headerMap, null, TIMEOUT_MS) ?: ""
                Log.v(TAG, " $cameraPowerOffUrl $response")
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    init
    {
        headerMap["User-Agent"] = "OlympusCameraKit" // "OI.Share"
        headerMap["X-Protocol"] = "OlympusCameraKit" // "OI.Share"
    }

    companion object
    {
        private val TAG = OmdsCameraDisconnectSequence::class.java.simpleName
        private const val TIMEOUT_MS = 5000
    }
}
