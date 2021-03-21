package jp.osdn.gokigen.mangle.scene

import android.graphics.Color
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import jp.osdn.gokigen.mangle.IScopedStorageAccessPermission
import jp.osdn.gokigen.mangle.R
import jp.osdn.gokigen.mangle.liveview.LiveImageViewFragment
import jp.osdn.gokigen.mangle.logcat.LogCatFragment
import jp.osdn.gokigen.mangle.operation.CameraControl
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor
import jp.osdn.gokigen.mangle.preference.MainPreferenceFragment
import jp.osdn.gokigen.mangle.preference.PreferenceAccessWrapper
import jp.osdn.gokigen.mangle.preview.PreviewFragment
import jp.osdn.gokigen.mangle.utils.ConfirmationDialog
import jp.osdn.gokigen.mangle.utils.ConfirmationDialog.Callback

class SceneChanger(private val activity: FragmentActivity, private val informationNotify: IInformationReceiver, accessRequest : IScopedStorageAccessPermission?) : IChangeScene
{
    private val cameraControl: CameraControl
    private lateinit var liveviewFragment : LiveImageViewFragment
    private lateinit var previewFragment : PreviewFragment
    private lateinit var logCatFragment : LogCatFragment
    private lateinit var mainPreferenceFragment : MainPreferenceFragment

    init
    {
        Log.v(TAG, " SceneChanger is created. ")
        cameraControl = CameraControl(activity, accessRequest)
        cameraControl.initialize()
    }

    private fun initializeFragmentForPreview()
    {
        if (!::previewFragment.isInitialized)
        {
            previewFragment = PreviewFragment.newInstance()
            previewFragment.setCameraControl(cameraControl)
        }
        setDefaultFragment(previewFragment)
        cameraControl.startCamera()

        val msg = activity.getString(R.string.app_name) + " : " + " camerax"
        informationNotify.updateMessage(msg, false, true, Color.LTGRAY)
    }

    private fun initializeFragmentForLiveView()
    {
        if (!::liveviewFragment.isInitialized)
        {
            liveviewFragment = LiveImageViewFragment.newInstance()
            liveviewFragment.setCameraControl(true, cameraControl,false, cameraControl,false, cameraControl,false, cameraControl)
        }
        setDefaultFragment(liveviewFragment)
        cameraControl.startCamera(false)

        val msg = activity.getString(R.string.app_name) + " : " + " STARTED."
        informationNotify.updateMessage(msg, false, true, Color.LTGRAY)
    }

    override fun initializeFragment()
    {
        try
        {
            val isCameraXPreview  = PreferenceAccessWrapper(activity).getBoolean(IPreferencePropertyAccessor.PREFERENCE_USE_CAMERA_X_PREVIEW, IPreferencePropertyAccessor.PREFERENCE_USE_CAMERA_X_PREVIEW_DEFAULT_VALUE)
            if (isCameraXPreview)
            {
                initializeFragmentForPreview()
            }
            else
            {
                initializeFragmentForLiveView()
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    override fun changeToLiveView()
    {
        if (!::liveviewFragment.isInitialized)
        {
            liveviewFragment = LiveImageViewFragment.newInstance()
            liveviewFragment.setCameraControl(true, cameraControl,true, cameraControl,true, cameraControl,true, cameraControl)
        }
        changeFragment(liveviewFragment)
    }


    override fun connectToCamera()
    {
        if (!::previewFragment.isInitialized)
        {
            previewFragment = PreviewFragment.newInstance()
            previewFragment.setCameraControl(cameraControl)
        }
        changeFragment(previewFragment)
        cameraControl.startCamera()
    }

    override fun changeToConfiguration()
    {
        if (!::mainPreferenceFragment.isInitialized)
        {
            mainPreferenceFragment = MainPreferenceFragment.newInstance()
            mainPreferenceFragment.setSceneChanger(this)
        }
        changeFragment(mainPreferenceFragment)
    }

    override fun changeToDebugInformation()
    {
        if (!::logCatFragment.isInitialized)
        {
            logCatFragment = LogCatFragment.newInstance()
        }
        changeFragment(logCatFragment)
    }

    override fun exitApplication()
    {
        val dialog = ConfirmationDialog.newInstance(activity)
        dialog.show(
            R.string.dialog_title_exit_application,
            R.string.dialog_message_exit_application,
            object : Callback {
                override fun confirm()
                {
                    activity.finish()
                }
            }
        )
    }

    private fun changeFragment(fragment: Fragment)
    {
        val transaction : FragmentTransaction = activity.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment1, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun setDefaultFragment(fragment: Fragment)
    {
        val transaction: FragmentTransaction = activity.supportFragmentManager.beginTransaction()
        fragment.retainInstance = true
        transaction.replace(R.id.fragment1, fragment)
        transaction.commitAllowingStateLoss()
    }

    fun finish()
    {
        cameraControl.finishCamera()
    }

    companion object
    {
        private val  TAG = this.toString()
    }
}
