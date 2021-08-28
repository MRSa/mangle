package jp.osdn.gokigen.gokigenassets.camera.vendor.sony.wrapper.connection

import jp.osdn.gokigen.gokigenassets.camera.interfaces.ICameraConnectionStatus.CameraConnectionStatus
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import jp.osdn.gokigen.gokigenassets.camera.vendor.sony.wrapper.ISonyCameraHolder
import jp.osdn.gokigen.gokigenassets.camera.interfaces.ICameraStatusReceiver
import android.content.*
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import jp.osdn.gokigen.gokigenassets.camera.interfaces.ICameraConnection
import jp.osdn.gokigen.gokigenassets.constants.ICameraConstantConvert
import java.lang.Exception
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class SonyCameraConnection(private val context: AppCompatActivity, private val statusReceiver: ICameraStatusReceiver, private val cameraHolder: ISonyCameraHolder) : ICameraConnection
{
    companion object
    {
        private val TAG = SonyCameraConnection::class.java.simpleName
    }
    private val connectionReceiver: BroadcastReceiver
    private val cameraExecutor: Executor = Executors.newFixedThreadPool(1)
    private var connectionStatus: CameraConnectionStatus = CameraConnectionStatus.UNKNOWN

    init
    {
        Log.v(TAG, " SonyCameraConnection()")
        connectionReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent)
            {
                onReceiveBroadcastOfConnection(context, intent)
            }
        }
    }

    private fun onReceiveBroadcastOfConnection(context: Context, intent: Intent)
    {
        statusReceiver.onStatusNotify(context.getString(ICameraConstantConvert.ID_STRING_CONNECT_CHECK_WIFI))
        Log.v(TAG, context.getString(ICameraConstantConvert.ID_STRING_CONNECT_CHECK_WIFI))
        try
        {
            val action = intent.action
            if (action == null)
            {
                Log.v(TAG, "intent.getAction() : null")
                return
            }

            @Suppress("DEPRECATION")
            if (action == ConnectivityManager.CONNECTIVITY_ACTION)
            {
                Log.v(TAG, "onReceiveBroadcastOfConnection() : CONNECTIVITY_ACTION")
                val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
                val info = wifiManager.connectionInfo
                if (wifiManager.isWifiEnabled && info != null)
                {
                    Log.v(TAG, "Network ID is " + info.networkId)
                    // 自動接続が指示されていた場合は、カメラとの接続処理を行う
                    connectToCamera()
                }
                else
                {
                    if (info == null)
                    {
                        Log.v(TAG, "NETWORK INFO IS NULL.")
                    }
                    else
                    {
                        Log.v(TAG, "isWifiEnabled : " + wifiManager.isWifiEnabled + " NetworkId : " + info.networkId)
                    }
                }
            }
        }
        catch (e: Exception)
        {
            Log.w(TAG, "onReceiveBroadcastOfConnection() EXCEPTION" + e.message)
            e.printStackTrace()
        }
    }

    /**
     * Wifi接続状態の監視
     * (接続の実処理は onReceiveBroadcastOfConnection() で実施)
     */
    fun startWatchWifiStatus(context: Context)
    {
        Log.v(TAG, "startWatchWifiStatus()")
        statusReceiver.onStatusNotify("prepare")
        val filter = IntentFilter()
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION)
        @Suppress("DEPRECATION")
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        context.registerReceiver(connectionReceiver, filter)
    }

    /**
     * Wifi接続状態の監視終了
     */
    fun stopWatchWifiStatus(context: Context)
    {
        Log.v(TAG, "stopWatchWifiStatus()")
        context.unregisterReceiver(connectionReceiver)
        disconnect(false)
    }

    /**
     * 　 カメラとの接続を解除する
     *
     * @param powerOff 真ならカメラの電源オフを伴う
     */
    fun disconnect(powerOff: Boolean)
    {
        Log.v(TAG, "disconnect()")
        disconnectFromCamera(powerOff)
        connectionStatus = CameraConnectionStatus.DISCONNECTED
        statusReceiver.onCameraDisconnected()
    }

    /**
     * カメラとの再接続を指示する
     */
    fun connect()
    {
        Log.v(TAG, "connect()")
        connectToCamera()
    }

    /**
     * 接続リトライのダイアログを出す
     *
     * @param message 表示用のメッセージ
     */
    override fun alertConnectingFailed(message: String?)
    {
        Log.v(TAG, "alertConnectingFailed() : $message")
        try
        {
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                .setTitle(context.getString(ICameraConstantConvert.ID_STRING_DIALOG_TITLE_CONNECT_FAILED))
                .setMessage(message)
                .setPositiveButton(context.getString(ICameraConstantConvert.ID_STRING_DIALOG_BUTTON_RETRY)) { _, _ -> connect() }
                .setNeutralButton(ICameraConstantConvert.ID_STRING_DIALOG_BUTTON_NETWORK_SETTINGS) { _, _ ->
                    try
                    {
                        // Wifi 設定画面を表示する
                        context.startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
                    }
                    catch (ex: ActivityNotFoundException)
                    {
                        // Activity が存在しなかった...設定画面が起動できなかった
                        Log.v(TAG, "android.content.ActivityNotFoundException...")

                        // この場合は、再試行と等価な動きとする
                        connect()
                    }
                    catch (e: Exception)
                    {
                        e.printStackTrace()
                    }
                }
            context.runOnUiThread { builder.show() }
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
    }

    fun getConnectionStatus(): CameraConnectionStatus
    {
        Log.v(TAG, "getConnectionStatus()")
        return connectionStatus
    }

    override fun forceUpdateConnectionStatus(status: CameraConnectionStatus)
    {
        Log.v(TAG, "forceUpdateConnectionStatus()")
        connectionStatus = status
    }

    /**
     * カメラとの切断処理
     */
    private fun disconnectFromCamera(powerOff: Boolean)
    {
        Log.v(TAG, "disconnectFromCamera()")
        try
        {
            cameraExecutor.execute(SonyCameraDisconnectSequence(context, powerOff))
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    /**
     * カメラとの接続処理
     */
    private fun connectToCamera()
    {
        Log.v(TAG, "connectToCamera()")
        connectionStatus = CameraConnectionStatus.CONNECTING
        try
        {
            cameraExecutor.execute(SonyCameraConnectSequence(context, statusReceiver, this, cameraHolder))
        }
        catch (e: Exception)
        {
            Log.v(TAG, "connectToCamera() EXCEPTION : " + e.message)
            e.printStackTrace()
        }
    }
}