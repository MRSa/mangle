package jp.osdn.gokigen.mangle.scene

import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import jp.osdn.gokigen.mangle.R

class MainButtonHandler(private val activity : AppCompatActivity) : View.OnClickListener
{
    private val TAG = toString()
    private lateinit var sceneChanger : SceneChanger

    override fun onClick(v: View?)
    {
        when (v?.id)
        {
            R.id.button_connect -> connect()
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
        activity.findViewById<ImageButton>(R.id.button_connect).setOnClickListener(this)
        activity.findViewById<TextView>(R.id.message).setOnClickListener(this)
    }

    private fun connect()
    {
        Log.v(TAG, " - - - - - - - - - CONNECT - - - - - - - - -")
        sceneChanger.changeToPreview()
    }

    private fun camera()
    {
        Log.v(TAG, " - - - - - - - - - CAMERA - - - - - - - - -")
        sceneChanger.changeToDebugInformation()
    }

    private fun configure()
    {
        Log.v(TAG, " - - - - - - - - - CONFIGURE - - - - - - - - -")
        sceneChanger.changeToConfiguration()
    }

    private fun message()
    {
        Log.v(TAG, " - - - - - - - - - MESSAGE - - - - - - - - -")
    }
}
