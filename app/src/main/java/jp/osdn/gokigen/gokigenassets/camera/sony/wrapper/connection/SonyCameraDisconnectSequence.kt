package jp.osdn.gokigen.gokigenassets.camera.sony.wrapper.connection

import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class SonyCameraDisconnectSequence(private val context: AppCompatActivity, private val powerOff: Boolean) : Runnable
{
    override fun run()
    {
        // カメラをPowerOffして接続を切る
        Log.v(SonyCameraDisconnectSequence::class.java.simpleName, " Disconnect from SONY.")
    }
}
