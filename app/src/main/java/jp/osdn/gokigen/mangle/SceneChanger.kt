package jp.osdn.gokigen.mangle

import android.graphics.Color
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import jp.osdn.gokigen.mangle.logcat.LogCatFragment
import jp.osdn.gokigen.mangle.preview.PreviewFragment

class SceneChanger(val activity: FragmentActivity, val informationNotify: IInformationReceiver) : IChangeScene
{
    private val TAG = toString()
    private val cameraControl: CameraControl = CameraControl(activity)
    private var count : Int = 0
    private lateinit var previewFragment : PreviewFragment
    private lateinit var logCatFragment : LogCatFragment

    init
    {
        Log.v(TAG, " SceneChanger is created. ")
    }

    override fun initializeFragment()
    {
        if (!::previewFragment.isInitialized)
        {
            previewFragment = PreviewFragment.newInstance()
        }
        val transaction: FragmentTransaction = activity.supportFragmentManager.beginTransaction()
        previewFragment.setCameraControl(cameraControl)
        cameraControl.startCamera()
        previewFragment.retainInstance = true
        transaction.replace(R.id.fragment1, previewFragment)
        transaction.commitAllowingStateLoss()
        count++
        informationNotify.updateMessage(" changeToPreview " + count, false, true, Color.BLUE)
    }

    override fun changeToPreview()
    {
        TODO("Not yet implemented")
    }

    override fun changeSceneToConfiguration()
    {
        TODO("Not yet implemented")
    }

    override fun changeSceneToDebugInformation()
    {
        if (!::logCatFragment.isInitialized)
        {
            logCatFragment = LogCatFragment.newInstance()
        }
        val transaction : FragmentTransaction = activity.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment1, logCatFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun finish()
    {
        cameraControl.finish()
    }
}
