package jp.osdn.gokigen.gokigenassets.camera.panasonic.connection

import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class PanasonicCameraDisconnectSequence(private val context: AppCompatActivity, private val powerOff: Boolean) : Runnable
{
    override fun run()
    {
        // カメラをPowerOffして接続を切る
        Log.v(PanasonicCameraDisconnectSequence::class.java.simpleName, " Disconnect from Panasonic.")
    }
}
