package jp.osdn.gokigen.mangle.scene

import android.util.Log
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import jp.osdn.gokigen.mangle.R
import jp.osdn.gokigen.mangle.scene.SceneChanger

class MainButtonHandler(val activity : AppCompatActivity) : View.OnClickListener
{
    private val TAG = toString()
    private lateinit var sceneChanger : SceneChanger

    override fun onClick(v: View?)
    {
        when (v?.id)
        {
            R.id.button_camera -> camera()
            R.id.button_configure -> configure()
            R.id.message -> message()
            else -> Log.v(TAG, " onClick : " + v?.id)
        }
    }

    fun setSceneChanger(changer : SceneChanger)
    {
        sceneChanger = changer
    }

    fun initialize()
    {
        activity.findViewById<ImageButton>(R.id.button_camera).setOnClickListener(this)
        activity.findViewById<ImageButton>(R.id.button_configure).setOnClickListener(this)
    }

    fun camera()
    {
        Log.v(TAG, " - - - - - - - - - CAMERA - - - - - - - - -")
        sceneChanger.changeSceneToDebugInformation()
    }

    fun configure()
    {
        Log.v(TAG, " - - - - - - - - - CONFIGURE - - - - - - - - -")
        sceneChanger.changeSceneToConfiguration()
    }

    fun message()
    {

    }
}
