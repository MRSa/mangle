package jp.osdn.gokigen.mangle

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import jp.osdn.gokigen.mangle.preference.PreferenceValueInitializer
import jp.osdn.gokigen.mangle.scene.MainButtonHandler
import jp.osdn.gokigen.mangle.scene.SceneChanger
import jp.osdn.gokigen.mangle.scene.ShowMessage

class MainActivity : AppCompatActivity()
{
    private val TAG = toString()
    private val mainButtonHandler : MainButtonHandler = MainButtonHandler(this)
    private val showMessage : ShowMessage = ShowMessage(this)
    private val accessPermission : IScopedStorageAccessPermission? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) { StorageOperationWithPermission(this) } else { null }
    private val sceneChanger : SceneChanger = SceneChanger(this, showMessage, accessPermission)

    override fun onCreate(savedInstanceState: Bundle?)
    {
        Log.v(TAG, " ----- onCreate() -----")
        super.onCreate(savedInstanceState)
        mainButtonHandler.setSceneChanger(sceneChanger)

        setContentView(R.layout.activity_main)

        supportActionBar?.hide()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        try
        {
           PreferenceValueInitializer().initializeStorageLocationPreferences(
               PreferenceManager.getDefaultSharedPreferences(
                   this
               )
           )
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    )
    {
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

    companion object
    {
        private const val REQUEST_CODE_PERMISSIONS = 10
        const val REQUEST_CODE_MEDIA_EDIT = 12
        const val REQUEST_CODE_OPEN_DOCUMENT_TREE = 20

        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.VIBRATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }
}
