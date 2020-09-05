package jp.osdn.gokigen.mangle

import android.graphics.Color
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import jp.osdn.gokigen.mangle.logcat.LogCatFragment
import jp.osdn.gokigen.mangle.preference.MainPreferenceFragment
import jp.osdn.gokigen.mangle.preview.PreviewFragment

class SceneChanger(val activity: FragmentActivity, val informationNotify: IInformationReceiver) : IChangeScene
{
    private val TAG = toString()
    private val cameraControl: CameraControl = CameraControl(activity)
    private var count : Int = 0
    private lateinit var previewFragment : PreviewFragment
    private lateinit var logCatFragment : LogCatFragment
    private lateinit var mainPreferenceFragment : MainPreferenceFragment

    init
    {
        Log.v(TAG, " SceneChanger is created. ")
    }

    override fun initializeFragment()
    {
        if (!::previewFragment.isInitialized)
        {
            previewFragment = PreviewFragment.newInstance()
            previewFragment.setCameraControl(cameraControl)
        }
        cameraControl.startCamera()
        setDefaultFragment(previewFragment)

        count++
        informationNotify.updateMessage(" changeToPreview " + count, false, true, Color.BLUE)
    }

    override fun changeToPreview()
    {
        if (!::previewFragment.isInitialized)
        {
            previewFragment = PreviewFragment.newInstance()
            previewFragment.setCameraControl(cameraControl)
        }
        changeFragment(previewFragment)
    }

    override fun changeSceneToConfiguration()
    {
        if (!::mainPreferenceFragment.isInitialized)
        {
            mainPreferenceFragment = MainPreferenceFragment.newInstance()
            mainPreferenceFragment.setSceneChanger(this)
        }
        changeFragment(mainPreferenceFragment)
    }

    override fun changeSceneToDebugInformation()
    {
        if (!::logCatFragment.isInitialized)
        {
            logCatFragment = LogCatFragment.newInstance()
        }
        changeFragment(logCatFragment)
    }

    private fun changeFragment(fragment : Fragment)
    {
        val transaction : FragmentTransaction = activity.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment1, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun setDefaultFragment(fragment : Fragment)
    {
        val transaction: FragmentTransaction = activity.supportFragmentManager.beginTransaction()
        fragment.retainInstance = true
        transaction.replace(R.id.fragment1, fragment)
        transaction.commitAllowingStateLoss()
    }

    fun finish()
    {
        cameraControl.finish()
    }
}
