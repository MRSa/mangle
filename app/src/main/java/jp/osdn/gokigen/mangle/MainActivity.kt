package jp.osdn.gokigen.mangle

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.KeyEvent
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import jp.osdn.gokigen.gokigenassets.camera.interfaces.ICameraConnectionStatus
import jp.osdn.gokigen.gokigenassets.camera.interfaces.ICameraStatusReceiver
import jp.osdn.gokigen.gokigenassets.scene.IVibrator
import jp.osdn.gokigen.gokigenassets.scene.ShowMessage
import jp.osdn.gokigen.gokigenassets.utils.IScopedStorageAccessPermission
import jp.osdn.gokigen.mangle.preference.PreferenceValueInitializer
import jp.osdn.gokigen.mangle.scene.MainButtonHandler
import jp.osdn.gokigen.mangle.scene.SceneChanger


class MainActivity : AppCompatActivity(), IVibrator, ICameraStatusReceiver
{
    private lateinit var mainButtonHandler : MainButtonHandler// = MainButtonHandler(this)
    private lateinit var sceneChanger : SceneChanger// = SceneChanger(this, showMessage)
    private var connectionStatus : ICameraConnectionStatus.CameraConnectionStatus = ICameraConnectionStatus.CameraConnectionStatus.UNKNOWN
    private val showMessage : ShowMessage = ShowMessage(this)
    private val accessPermission : IScopedStorageAccessPermission? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) { StorageOperationWithPermission(this) } else { null }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        Log.v(TAG, " ----- onCreate() -----")
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        supportActionBar?.hide()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        try
        {
            mainButtonHandler = MainButtonHandler(this)
            sceneChanger  = SceneChanger(this, showMessage, this, this)
            mainButtonHandler.setSceneChanger(sceneChanger)
            PreferenceValueInitializer().initializePreferences(this)
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }

        if (allPermissionsGranted())
        {
            checkMediaWritePermission()
            sceneChanger.initializeFragment()
            mainButtonHandler.initialize()
        }
        else
        {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
    }

    override fun onDestroy()
    {
        super.onDestroy()
        sceneChanger.finish()
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkMediaWritePermission()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
        {
            StorageOperationWithPermission(this).requestStorageAccessFrameworkLocation()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS)
        {
            if (allPermissionsGranted())
            {
                checkMediaWritePermission()
                sceneChanger.initializeFragment()
                mainButtonHandler.initialize()
            }
            else
            {
                Toast.makeText(this, getString(R.string.permission_not_granted), Toast.LENGTH_SHORT).show()
                //Snackbar.make(main_layout,"Permissions not granted by the user.", Snackbar.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_MEDIA_EDIT)
        {
            accessPermission?.responseAccessPermission(resultCode, data)
        }
        if (requestCode == REQUEST_CODE_OPEN_DOCUMENT_TREE)
        {
            accessPermission?.responseStorageAccessFrameworkLocation(resultCode, data)
        }
    }

    override fun vibrate(vibratePattern: IVibrator.VibratePattern)
    {
        try
        {
            // バイブレータをつかまえる
            val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
            if (!vibrator.hasVibrator())
            {
                Log.v(TAG, " not have Vibrator...")
                return
            }
            @Suppress("DEPRECATION") val thread = Thread {
                try
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    {
                        vibrator.vibrate(VibrationEffect.createOneShot(30, VibrationEffect.DEFAULT_AMPLITUDE))
                    }
                    else
                    {
                        when (vibratePattern)
                        {
                            IVibrator.VibratePattern.SIMPLE_SHORT_SHORT -> vibrator.vibrate(30)
                            IVibrator.VibratePattern.SIMPLE_SHORT ->  vibrator.vibrate(50)
                            IVibrator.VibratePattern.SIMPLE_MIDDLE -> vibrator.vibrate(100)
                            IVibrator.VibratePattern.SIMPLE_LONG ->  vibrator.vibrate(150)
                            else -> { }
                        }
                    }
                }
                catch (e : Exception)
                {
                    e.printStackTrace()
                }
            }
            thread.start()
        }
        catch (e: java.lang.Exception)
        {
            e.printStackTrace()
        }
    }

    override fun onStatusNotify(message: String?)
    {
        Log.v(TAG, " onStatusNotify() $message ")
        updateConnectionIcon(ICameraConnectionStatus.CameraConnectionStatus.CONNECTING)

        try
        {
            runOnUiThread {
                //if (liveView != null) {
                //    liveView.invalidate()
                //}
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    override fun onCameraConnected()
    {
        Log.v(TAG, " onCameraConnected() ")
        updateConnectionIcon(ICameraConnectionStatus.CameraConnectionStatus.CONNECTED)

        try
        {
            runOnUiThread {
                //if (liveView != null) {
                //    liveView.invalidate()
                //}
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    override fun onCameraDisconnected()
    {
        Log.v(TAG, " onCameraDisconnected() ")
        try
        {
            updateConnectionIcon(ICameraConnectionStatus.CameraConnectionStatus.DISCONNECTED)
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    override fun onCameraConnectError(msg: String?)
    {
        Log.v(TAG, " onCameraConnectError() $msg ")
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean
    {
        try
        {
            if (event.action == KeyEvent.ACTION_DOWN && (keyCode == KeyEvent.KEYCODE_VOLUME_UP || keyCode == KeyEvent.KEYCODE_CAMERA))
            {
                Log.v(TAG, "onKeyDown() $keyCode")
                if (::sceneChanger.isInitialized)
                {
                    return (sceneChanger.handleKeyDown(keyCode, event))
                }
            }
        }
        catch (e: java.lang.Exception)
        {
            e.printStackTrace()
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun updateConnectionIcon(connectionStatus : ICameraConnectionStatus.CameraConnectionStatus)
    {
        Log.v(TAG, " updateConnectionIcon() $connectionStatus")
        this.connectionStatus = connectionStatus
        try
        {
            runOnUiThread {
                try
                {
                    val view : ImageButton = this.findViewById(R.id.button_connect)
                    val iconId = when (connectionStatus)
                    {
                        ICameraConnectionStatus.CameraConnectionStatus.DISCONNECTED -> { R.drawable.ic_baseline_cloud_off_24 }
                        ICameraConnectionStatus.CameraConnectionStatus.UNKNOWN -> { R.drawable.ic_baseline_cloud_off_24 }
                        ICameraConnectionStatus.CameraConnectionStatus.CONNECTING -> { R.drawable.ic_baseline_cloud_queue_24 }
                        ICameraConnectionStatus.CameraConnectionStatus.CONNECTED -> { R.drawable.ic_baseline_cloud_done_24 }
                    }
                    view.setImageDrawable(ContextCompat.getDrawable(this, iconId))
                    view.invalidate()
                }
                catch (e : Exception)
                {
                    e.printStackTrace()
                }
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    companion object
    {
        private val TAG = MainActivity::class.java.simpleName

        private const val REQUEST_CODE_PERMISSIONS = 10
        const val REQUEST_CODE_MEDIA_EDIT = 12
        const val REQUEST_CODE_OPEN_DOCUMENT_TREE = 20

        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.VIBRATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
        )
    }
}
