package jp.osdn.gokigen.mangle

import android.graphics.Color
import android.graphics.Typeface
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainButtonHandler(val activity : AppCompatActivity) : View.OnClickListener,  IInformationReceiver
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

    override fun updateMessage(message: String, isBold: Boolean, isColor: Boolean, color: Int)
    {
        val messageArea = activity.findViewById<TextView>(R.id.message)
        activity.runOnUiThread {
            messageArea?.text = message
            if (isBold)
            {
                messageArea?.typeface = Typeface.DEFAULT_BOLD
            }
            if (isColor)
            {
                messageArea?.setTextColor(color)
            }
            else
            {
                messageArea?.setTextColor(Color.DKGRAY)
            }
            messageArea?.invalidate()
        }
    }

}