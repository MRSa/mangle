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
import jp.osdn.gokigen.gokigenassets.camera.vendor.camerax.preview.PreviewFragment
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
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_METHOD_5
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_METHOD_6
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_METHOD_7
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_METHOD_8
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_SEQUENCE_1
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_SEQUENCE_2
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_SEQUENCE_3
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_SEQUENCE_4
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_SEQUENCE_5
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_SEQUENCE_6
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_SEQUENCE_7
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_SEQUENCE_8
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
    private val cameraControl5: ICameraControl
    private val cameraControl6: ICameraControl
    private val cameraControl7: ICameraControl
    private val cameraControl8: ICameraControl

    private val preferenceChanger = PreferenceChanger(activity, this, this)
    private lateinit var liveviewFragment : LiveImageViewFragment
    private lateinit var previewFragment : PreviewFragment
    private lateinit var logCatFragment : LogCatFragment
    private lateinit var mainPreferenceFragment : MainPreferenceFragment

    init
    {
        Log.v(TAG, " SceneChanger is created. ")

        cameraControl1 = cameraProvider.decideCameraControl(PREFERENCE_CAMERA_METHOD_1, 1)
        cameraControl2 = cameraProvider.decideCameraControl(PREFERENCE_CAMERA_METHOD_2, 2)
        cameraControl3 = cameraProvider.decideCameraControl(PREFERENCE_CAMERA_METHOD_3, 3)
        cameraControl4 = cameraProvider.decideCameraControl(PREFERENCE_CAMERA_METHOD_4, 4)
        cameraControl5 = cameraProvider.decideCameraControl(PREFERENCE_CAMERA_METHOD_5, 5)
        cameraControl6 = cameraProvider.decideCameraControl(PREFERENCE_CAMERA_METHOD_6, 6)
        cameraControl7 = cameraProvider.decideCameraControl(PREFERENCE_CAMERA_METHOD_7, 7)
        cameraControl8 = cameraProvider.decideCameraControl(PREFERENCE_CAMERA_METHOD_8, 8)

        cameraControl1.initialize()
        cameraControl2.initialize()
        cameraControl3.initialize()
        cameraControl4.initialize()
        cameraControl5.initialize()
        cameraControl6.initialize()
        cameraControl7.initialize()
        cameraControl8.initialize()

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
            val isEnableCamera5 = cameraControl5.getConnectionMethod() != "NONE"
            val isEnableCamera6 = cameraControl6.getConnectionMethod() != "NONE"
            val isEnableCamera7 = cameraControl7.getConnectionMethod() != "NONE"
            val isEnableCamera8 = cameraControl8.getConnectionMethod() != "NONE"

            liveviewFragment = LiveImageViewFragment.newInstance()
            liveviewFragment.setCameraControl(0, isEnableCamera1, cameraControl1, isEnableCamera2, cameraControl2, isEnableCamera3, cameraControl3, isEnableCamera4, cameraControl4)
            liveviewFragment.setCameraControl(1, isEnableCamera5, cameraControl5, isEnableCamera6, cameraControl6, isEnableCamera7, cameraControl7, isEnableCamera8, cameraControl8)
            liveviewFragment.setCameraControlFinished(informationNotify)
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
        cameraControl5.startCamera(
            isPreviewView = false,
            cameraSequence = cameraProvider.getCameraSelection(PREFERENCE_CAMERA_SEQUENCE_5)
        )
        cameraControl6.startCamera(
            isPreviewView = false,
            cameraSequence = cameraProvider.getCameraSelection(PREFERENCE_CAMERA_SEQUENCE_6)
        )
        cameraControl7.startCamera(
            isPreviewView = false,
            cameraSequence = cameraProvider.getCameraSelection(PREFERENCE_CAMERA_SEQUENCE_7)
        )
        cameraControl8.startCamera(
            isPreviewView = false,
            cameraSequence = cameraProvider.getCameraSelection(PREFERENCE_CAMERA_SEQUENCE_8)
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
            val isEnableCamera5 = cameraControl5.getConnectionMethod() != "NONE"
            val isEnableCamera6 = cameraControl6.getConnectionMethod() != "NONE"
            val isEnableCamera7 = cameraControl7.getConnectionMethod() != "NONE"
            val isEnableCamera8 = cameraControl8.getConnectionMethod() != "NONE"
            liveviewFragment = LiveImageViewFragment.newInstance()
            liveviewFragment.setCameraControl(
                0,
                isEnableCamera1, cameraControl1,
                isEnableCamera2, cameraControl2,
                isEnableCamera3, cameraControl3,
                isEnableCamera4, cameraControl4
            )
            liveviewFragment.setCameraControl(
                1,
                isEnableCamera5, cameraControl5,
                isEnableCamera6, cameraControl6,
                isEnableCamera7, cameraControl7,
                isEnableCamera8, cameraControl8
            )
            liveviewFragment.setCameraControlFinished(informationNotify)
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
        try
        {
            cameraControl5.connectToCamera()
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
        try
        {
            cameraControl6.connectToCamera()
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
        try
        {
            cameraControl7.connectToCamera()
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
        try
        {
            cameraControl8.connectToCamera()
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
                    // finish()
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
        Log.v(TAG, " finishCamera() ")
        cameraControl1.finishCamera()
        cameraControl2.finishCamera()
        cameraControl3.finishCamera()
        cameraControl4.finishCamera()
        cameraControl5.finishCamera()
        cameraControl6.finishCamera()
        cameraControl7.finishCamera()
        cameraControl8.finishCamera()
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
