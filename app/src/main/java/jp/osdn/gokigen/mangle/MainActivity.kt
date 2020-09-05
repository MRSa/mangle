package jp.osdn.gokigen.mangle

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import jp.osdn.gokigen.mangle.scene.MainButtonHandler
import jp.osdn.gokigen.mangle.scene.ShowMessage
import jp.osdn.gokigen.mangle.scene.SceneChanger

class MainActivity : AppCompatActivity()
{
    private val TAG = toString()
    private val mainButtonHandler : MainButtonHandler = MainButtonHandler(this)
    private val showMessage : ShowMessage = ShowMessage(this)

    //private var cameraControl: CameraControl? = null
    private val sceneChanger : SceneChanger = SceneChanger(this, showMessage)
    //private var sceneChanger : SceneChanger? = null // = SceneChanger(this, this)

    override fun onCreate(savedInstanceState: Bundle?)
    {
        Log.v(TAG, " ----- onCreate() -----")
        super.onCreate(savedInstanceState)
        mainButtonHandler.setSceneChanger(sceneChanger)

        setContentView(R.layout.activity_main)

        supportActionBar?.hide()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        //sceneChanger = SceneChanger(this, this)
        //cameraControl = CameraControl(this)
        if (allPermissionsGranted())
        {
            //cameraControl?.startCamera()
            sceneChanger.initializeFragment()
            mainButtonHandler.initialize()
        }
        else
        {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
        //cameraControl?.initialize()
    }

    override fun onDestroy()
    {
        super.onDestroy()
        sceneChanger.finish()
        //cameraControl?.finish()

        //sceneChanger = null
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray)
    {
        if (requestCode == REQUEST_CODE_PERMISSIONS)
        {
            if (allPermissionsGranted())
            {
                //cameraControl?.startCamera()
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

    companion object
    {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}
