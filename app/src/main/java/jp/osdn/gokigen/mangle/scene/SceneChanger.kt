package jp.osdn.gokigen.mangle.scene

import android.graphics.Color
import android.util.Log
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import jp.osdn.gokigen.gokigenassets.camera.interfaces.ICameraStatusReceiver
import jp.osdn.gokigen.gokigenassets.liveview.LiveImageViewFragment
import jp.osdn.gokigen.gokigenassets.preference.MainPreferenceFragment
import jp.osdn.gokigen.gokigenassets.preference.PreferenceAccessWrapper
import jp.osdn.gokigen.gokigenassets.camera.camerax.preview.PreviewFragment
import jp.osdn.gokigen.gokigenassets.camera.interfaces.ICameraControl
import jp.osdn.gokigen.gokigenassets.scene.IChangeSceneBasic
import jp.osdn.gokigen.gokigenassets.scene.IInformationReceiver
import jp.osdn.gokigen.gokigenassets.scene.IVibrator
import jp.osdn.gokigen.gokigenassets.utils.ConfirmationDialog
import jp.osdn.gokigen.gokigenassets.utils.logcat.LogCatFragment
import jp.osdn.gokigen.mangle.R
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_METHOD_1
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_METHOD_2
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_METHOD_3
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_METHOD_4
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_SEQUENCE_1
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_SEQUENCE_2
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_SEQUENCE_3
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_SEQUENCE_4
import jp.osdn.gokigen.mangle.preference.PreferenceChanger
import jp.osdn.gokigen.mangle.preference.PreferenceValueInitializer

class SceneChanger(private val activity: AppCompatActivity, private val informationNotify: IInformationReceiver, private val vibrator : IVibrator, statusReceiver : ICameraStatusReceiver) : IChangeScene, IChangeSceneBasic
{
    private val cameraProvider = CameraProvider(activity, informationNotify, vibrator, statusReceiver)

    private val cameraControl0 = cameraProvider.getCameraXControl()
    private val cameraControl1: ICameraControl
    private val cameraControl2: ICameraControl
    private val cameraControl3: ICameraControl
    private val cameraControl4: ICameraControl

    private val preferenceChanger = PreferenceChanger(activity, this, this)
    private lateinit var liveviewFragment : LiveImageViewFragment
    private lateinit var previewFragment : PreviewFragment
    private lateinit var logCatFragment : LogCatFragment
    private lateinit var mainPreferenceFragment : MainPreferenceFragment

    init
    {
        Log.v(TAG, " SceneChanger is created. ")

        cameraControl1 = cameraProvider.decideCameraControl(PREFERENCE_CAMERA_METHOD_1)
        cameraControl2 = cameraProvider.decideCameraControl(PREFERENCE_CAMERA_METHOD_2)
        cameraControl3 = cameraProvider.decideCameraControl(PREFERENCE_CAMERA_METHOD_3)
        cameraControl4 = cameraProvider.decideCameraControl(PREFERENCE_CAMERA_METHOD_4)

        cameraControl1.initialize()
        cameraControl2.initialize()
        cameraControl3.initialize()
        cameraControl4.initialize()
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
            val isEnableCamera1 = cameraControl1.getConnectionMethod() != "NONE"
            val isEnableCamera2 = cameraControl2.getConnectionMethod() != "NONE"
            val isEnableCamera3 = cameraControl3.getConnectionMethod() != "NONE"
            val isEnableCamera4 = cameraControl4.getConnectionMethod() != "NONE"

            liveviewFragment = LiveImageViewFragment.newInstance()
            liveviewFragment.setCameraControl(informationNotify, isEnableCamera1, cameraControl1, isEnableCamera2, cameraControl2, isEnableCamera3, cameraControl3, isEnableCamera4, cameraControl4)
        }
        setDefaultFragment(liveviewFragment)

        cameraControl1.startCamera(
            isPreviewView = false,
            cameraSequence = cameraProvider.getCameraSelection(PREFERENCE_CAMERA_SEQUENCE_1)
        )
        cameraControl2.startCamera(
            isPreviewView = false,
            cameraSequence = cameraProvider.getCameraSelection(PREFERENCE_CAMERA_SEQUENCE_2)
        )
        cameraControl3.startCamera(
            isPreviewView = false,
            cameraSequence = cameraProvider.getCameraSelection(PREFERENCE_CAMERA_SEQUENCE_3)
        )
        cameraControl4.startCamera(
            isPreviewView = false,
            cameraSequence = cameraProvider.getCameraSelection(PREFERENCE_CAMERA_SEQUENCE_4)
        )

        val msg = activity.getString(R.string.app_name)
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
            val isEnableCamera1 = cameraControl1.getConnectionMethod() != "NONE"
            val isEnableCamera2 = cameraControl2.getConnectionMethod() != "NONE"
            val isEnableCamera3 = cameraControl3.getConnectionMethod() != "NONE"
            val isEnableCamera4 = cameraControl4.getConnectionMethod() != "NONE"
            liveviewFragment = LiveImageViewFragment.newInstance()
            liveviewFragment.setCameraControl(
                informationNotify,
                isEnableCamera1, cameraControl1,
                isEnableCamera2, cameraControl2,
                isEnableCamera3, cameraControl3,
                isEnableCamera4, cameraControl4
            )
        }
        changeFragment(liveviewFragment)
    }


    override fun connectToCamera()
    {
        try
        {
            cameraControl1.connectToCamera()
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
        try
        {
            cameraControl2.connectToCamera()
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
        try
        {
            cameraControl3.connectToCamera()
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
        try
        {
            cameraControl4.connectToCamera()
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
        try
        {
            vibrator.vibrate(IVibrator.VibratePattern.SIMPLE_MIDDLE)
        }
        catch (e : Exception)
        {
            e.printStackTrace()
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

    override fun selectConnectionMethod()
    {
        try
        {
            Log.v(TAG, " selectConnectionMethod ")

        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
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
        cameraControl1.finishCamera()
        cameraControl2.finishCamera()
        cameraControl3.finishCamera()
        cameraControl4.finishCamera()
    }

    fun handleKeyDown(keyCode: Int, event: KeyEvent): Boolean
    {
        try
        {
            if ((::liveviewFragment.isInitialized)&&(liveviewFragment.isActive()))
            {
                Log.v(TAG, "handleKeyDown() $keyCode")
                return (liveviewFragment.handleKeyDown(keyCode, event))
            }
            else
            {
                Log.v(TAG, "handleKeyDown : liveviewFragment is not Active...")
            }
        }
        catch (e : java.lang.Exception)
        {
            e.printStackTrace()
        }
        return (false)
    }

    companion object
    {
        private val  TAG = SceneChanger::class.java.simpleName
    }
}
