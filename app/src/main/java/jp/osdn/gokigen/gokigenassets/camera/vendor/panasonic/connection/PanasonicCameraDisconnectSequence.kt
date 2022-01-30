package jp.osdn.gokigen.gokigenassets.camera.vendor.panasonic.connection

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import jp.osdn.gokigen.gokigenassets.camera.vendor.ICameraControlManager

class PanasonicCameraDisconnectSequence(private val context: AppCompatActivity, private val powerOff: Boolean, private val cameraManager: ICameraControlManager, private val number : Int) : Runnable
{
    override fun run()
    {
        // カメラをPowerOffして接続を切る
        Log.v(PanasonicCameraDisconnectSequence::class.java.simpleName, " Disconnect from Panasonic.")
        try
        {
            // カメラとの接続を切る
            cameraManager.releaseCameraControl(number)
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }
}
