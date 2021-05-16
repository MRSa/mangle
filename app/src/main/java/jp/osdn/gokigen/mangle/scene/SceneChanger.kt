package jp.osdn.gokigen.mangle.scene

import android.graphics.Color
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import jp.osdn.gokigen.gokigenassets.camera.interfaces.ICameraStatusReceiver
import jp.osdn.gokigen.gokigenassets.camera.theta.ThetaCameraControl
import jp.osdn.gokigen.gokigenassets.liveview.LiveImageViewFragment
import jp.osdn.gokigen.gokigenassets.camera.camerax.operation.CameraControl
import jp.osdn.gokigen.gokigenassets.preference.MainPreferenceFragment
import jp.osdn.gokigen.gokigenassets.preference.PreferenceAccessWrapper
import jp.osdn.gokigen.gokigenassets.camera.camerax.preview.PreviewFragment
import jp.osdn.gokigen.gokigenassets.scene.IChangeSceneBasic
import jp.osdn.gokigen.gokigenassets.scene.IInformationReceiver
import jp.osdn.gokigen.gokigenassets.scene.IVibrator
import jp.osdn.gokigen.gokigenassets.utils.ConfirmationDialog
import jp.osdn.gokigen.gokigenassets.utils.logcat.LogCatFragment
import jp.osdn.gokigen.mangle.R
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor
import jp.osdn.gokigen.mangle.preference.PreferenceChanger
import jp.osdn.gokigen.mangle.preference.PreferenceValueInitializer


class SceneChanger(private val activity: AppCompatActivity, private val informationNotify: IInformationReceiver, vibrator : IVibrator, statusReceiver : ICameraStatusReceiver) : IChangeScene, IChangeSceneBasic
{
    private val cameraControl0: CameraControl
    private val cameraControl1: CameraControl
    private val cameraControl2 = ThetaCameraControl(activity, vibrator, statusReceiver)

    private val preferenceChanger = PreferenceChanger(activity, this)
    private lateinit var liveviewFragment : LiveImageViewFragment
    private lateinit var previewFragment : PreviewFragment
    private lateinit var logCatFragment : LogCatFragment
    private lateinit var mainPreferenceFragment : MainPreferenceFragment

    init
    {
        Log.v(TAG, " SceneChanger is created. ")
        cameraControl0 = CameraControl(activity)
        cameraControl0.initialize()

        cameraControl1 = CameraControl(activity)
        cameraControl1.initialize()

        cameraControl2.initialize()
        cameraControl2.connectToCamera()
    }

    private fun initializeFragmentForPreview()
    {
        if (!::previewFragment.isInitialized)
        {
            previewFragment = PreviewFragment.newInstance(cameraControl0)
        }
        setDefaultFragment(previewFragment)
        cameraControl0.startCamera()

        val msg = activity.getString(R.string.app_name) + " : " + " camerax"
        informationNotify.updateMessage(msg, isBold = false, isColor = true, color = Color.LTGRAY)
    }

    private fun initializeFragmentForLiveView()
    {
        if (!::liveviewFragment.isInitialized)
        {
            liveviewFragment = LiveImageViewFragment.newInstance()
            liveviewFragment.setCameraControl(true, cameraControl1,true, cameraControl1,true, cameraControl1,true, cameraControl2)
        }
        setDefaultFragment(liveviewFragment)

        cameraControl0.startCamera(
            isPreviewView = false,
            cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
        )
        cameraControl1.startCamera(
            isPreviewView = false,
            cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        )
        cameraControl2.startCamera(
            isPreviewView = false,
            cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        )

        val msg = activity.getString(R.string.app_name) + " : " + " STARTED."
        informationNotify.updateMessage(msg, isBold = false, isColor = true, color = Color.LTGRAY)
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
            liveviewFragment.setCameraControl(true, cameraControl0,true, cameraControl0,true, cameraControl1,true, cameraControl1)
        }
        changeFragment(liveviewFragment)
    }


    override fun connectToCamera()
    {
        try
        {
            cameraControl2.connectToCamera()
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
    }

    override fun changeToConfiguration()
    {
        if (!::mainPreferenceFragment.isInitialized)
        {
            mainPreferenceFragment = MainPreferenceFragment.newInstance(preferenceChanger, PreferenceValueInitializer())
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
            object : ConfirmationDialog.ConfirmationCallback {
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
        cameraControl0.finishCamera()
        cameraControl1.finishCamera()
        cameraControl2.finishCamera()
    }

    companion object
    {
        private val  TAG = SceneChanger::class.java.simpleName
    }
}
