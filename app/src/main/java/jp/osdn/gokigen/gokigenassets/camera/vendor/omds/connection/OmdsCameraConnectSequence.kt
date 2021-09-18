package jp.osdn.gokigen.gokigenassets.camera.vendor.omds.connection

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import jp.osdn.gokigen.gokigenassets.camera.interfaces.ICameraConnection
import jp.osdn.gokigen.gokigenassets.camera.interfaces.ICameraConnectionStatus
import jp.osdn.gokigen.gokigenassets.camera.interfaces.ICameraStatusReceiver
import jp.osdn.gokigen.gokigenassets.camera.vendor.omds.status.IOmdsCommunicationInfo
import jp.osdn.gokigen.gokigenassets.constants.ICameraConstantConvert
import jp.osdn.gokigen.gokigenassets.utils.communication.SimpleHttpClient
import java.lang.Exception
import java.util.HashMap

class OmdsCameraConnectSequence(private val context: AppCompatActivity, private val cameraStatusReceiver: ICameraStatusReceiver, private val cameraConnection : ICameraConnection, private val communicationInfo: IOmdsCommunicationInfo, private val liveViewQuality : String, userAgent : String, private val executeUrl : String) : Runnable
{
    private val headerMap: MutableMap<String, String> = HashMap()
    private val http = SimpleHttpClient()

    override fun run()
    {
        try
        {
            val camInfoUrl = "$executeUrl/get_caminfo.cgi"
            val getCommandListUrl = "$executeUrl/get_commandlist.cgi"
            val getConnectModeUrl = "$executeUrl/get_connectmode.cgi"
            val switchCameraModeUrl = "$executeUrl/switch_cammode.cgi"
            //val getCameraStatusUrl = "$executeUrl/get_activate.cgi"

            val response: String = http.httpGetWithHeader(getConnectModeUrl, headerMap, null, TIMEOUT_MS) ?: ""
            Log.v(TAG, " $getConnectModeUrl $response")
            if (response.isNotEmpty())
            {
                val response2: String = http.httpGetWithHeader(getCommandListUrl, headerMap, null, TIMEOUT_MS) ?: ""
                Log.v(TAG, " $getCommandListUrl (${response2.length})")
                communicationInfo.setOmdsCommandList(response2)

                val response3: String = http.httpGetWithHeader(camInfoUrl, headerMap, null, TIMEOUT_MS) ?: ""
                Log.v(TAG, " $camInfoUrl $response3")

                // 撮影モードに切り替え。
                val lvUrl = "$switchCameraModeUrl?mode=rec&lvqty=$liveViewQuality"
                val response4: String = http.httpGetWithHeader(lvUrl, headerMap, null, TIMEOUT_MS) ?: ""
                Log.v(TAG, " $lvUrl $response4")

                //// カメラのステータス取得
                //String response5 = SimpleHttpClient.httpGetWithHeader(getCameraStatusUrl, headerMap, null, TIMEOUT_MS);
                //Log.v(TAG, " " + getCameraStatusUrl + " " + response5);
                onConnectNotify()
            }
            else
            {
                cameraConnection.alertConnectingFailed(context.getString(ICameraConstantConvert.ID_STRING_CAMERA_NOT_FOUND))
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
            cameraConnection.alertConnectingFailed(e.localizedMessage)
        }
    }

    private fun onConnectNotify()
    {
        try
        {
            val thread = Thread { // カメラとの接続確立を通知する
                cameraStatusReceiver.onStatusNotify(context.getString(ICameraConstantConvert.ID_STRING_CONNECT_CONNECTED))
                cameraStatusReceiver.onCameraConnected()
                Log.v(TAG, "onConnectNotify()")
                cameraConnection.forceUpdateConnectionStatus(ICameraConnectionStatus.CameraConnectionStatus.CONNECTED)
            }
            thread.start()
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    init
    {
        headerMap["User-Agent"] = userAgent // "OlympusCameraKit" // "OI.Share"
        headerMap["X-Protocol"] = userAgent // "OlympusCameraKit" // "OI.Share"
    }

    companion object
    {
        private val TAG = OmdsCameraConnectSequence::class.java.simpleName
        private const val TIMEOUT_MS = 5000
    }
}
