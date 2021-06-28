package jp.osdn.gokigen.mangle.scene

import androidx.appcompat.app.AppCompatActivity
import jp.osdn.gokigen.gokigenassets.camera.CameraPreference
import jp.osdn.gokigen.gokigenassets.camera.DummyCameraControl
import jp.osdn.gokigen.gokigenassets.camera.ICameraPreferenceProvider
import jp.osdn.gokigen.gokigenassets.camera.camerax.operation.CameraControl
import jp.osdn.gokigen.gokigenassets.camera.interfaces.*
import jp.osdn.gokigen.gokigenassets.camera.panasonic.wrapper.PanasonicCameraControl
import jp.osdn.gokigen.gokigenassets.camera.ricohpentax.RicohPentaxCameraControl
import jp.osdn.gokigen.gokigenassets.camera.theta.ThetaCameraControl
import jp.osdn.gokigen.gokigenassets.constants.ICameraConnectionMethods.Companion.PREFERENCE_CAMERA_METHOD_CAMERAX
import jp.osdn.gokigen.gokigenassets.constants.ICameraConnectionMethods.Companion.PREFERENCE_CAMERA_METHOD_NONE
import jp.osdn.gokigen.gokigenassets.constants.ICameraConnectionMethods.Companion.PREFERENCE_CAMERA_METHOD_PANASONIC
import jp.osdn.gokigen.gokigenassets.constants.ICameraConnectionMethods.Companion.PREFERENCE_CAMERA_METHOD_PENTAX
import jp.osdn.gokigen.gokigenassets.constants.ICameraConnectionMethods.Companion.PREFERENCE_CAMERA_METHOD_THETA
import jp.osdn.gokigen.gokigenassets.preference.PreferenceAccessWrapper
import jp.osdn.gokigen.gokigenassets.scene.IInformationReceiver
import jp.osdn.gokigen.gokigenassets.scene.IVibrator
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_METHOD_1
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_METHOD_1_DEFAULT_VALUE
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_METHOD_2
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_METHOD_2_DEFAULT_VALUE
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_METHOD_3
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_METHOD_3_DEFAULT_VALUE
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_METHOD_4
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_METHOD_4_DEFAULT_VALUE
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_OPTION1_1
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_OPTION1_1_DEFAULT_VALUE
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_OPTION1_2
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_OPTION1_2_DEFAULT_VALUE
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_OPTION1_3
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_OPTION1_3_DEFAULT_VALUE
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_OPTION1_4
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_OPTION1_4_DEFAULT_VALUE
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_OPTION2_1
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_OPTION2_1_DEFAULT_VALUE
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_OPTION2_2
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_OPTION2_2_DEFAULT_VALUE
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_OPTION2_3
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_OPTION2_3_DEFAULT_VALUE
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_OPTION2_4
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_OPTION2_4_DEFAULT_VALUE
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_OPTION3_1
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_OPTION3_1_DEFAULT_VALUE
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_OPTION3_2
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_OPTION3_2_DEFAULT_VALUE
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_OPTION3_3
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_OPTION3_3_DEFAULT_VALUE
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_OPTION3_4
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_OPTION3_4_DEFAULT_VALUE
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_OPTION4_1
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_OPTION4_1_DEFAULT_VALUE
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_OPTION4_2
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_OPTION4_2_DEFAULT_VALUE
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_OPTION4_3
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_OPTION4_3_DEFAULT_VALUE
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_OPTION4_4
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_OPTION4_4_DEFAULT_VALUE
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_OPTION5_1
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_OPTION5_1_DEFAULT_VALUE
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_OPTION5_2
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_OPTION5_2_DEFAULT_VALUE
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_OPTION5_3
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_OPTION5_3_DEFAULT_VALUE
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_OPTION5_4
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_OPTION5_4_DEFAULT_VALUE
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_SEQUENCE_1
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_SEQUENCE_1_DEFAULT_VALUE
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_SEQUENCE_2
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_SEQUENCE_2_DEFAULT_VALUE
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_SEQUENCE_3
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_SEQUENCE_3_DEFAULT_VALUE
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_SEQUENCE_4
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor.Companion.PREFERENCE_CAMERA_SEQUENCE_4_DEFAULT_VALUE

class CameraProvider(private val activity: AppCompatActivity, private val informationNotify: IInformationReceiver, private val vibrator : IVibrator, private val statusReceiver : ICameraStatusReceiver)
{
    private var cameraXisCreated = false
    private lateinit var cameraXControl0: ICameraControl
    private lateinit var cameraXControl1: ICameraControl

    fun decideCameraControl(preferenceKey : String) : ICameraControl
    {
        try
        {
            val wrapper = PreferenceAccessWrapper(activity)
            val cameraPreference = when (preferenceKey) {
                PREFERENCE_CAMERA_METHOD_1 -> setupCameraPreference1(wrapper)
                PREFERENCE_CAMERA_METHOD_2 -> setupCameraPreference2(wrapper)
                PREFERENCE_CAMERA_METHOD_3 -> setupCameraPreference3(wrapper)
                PREFERENCE_CAMERA_METHOD_4 -> setupCameraPreference4(wrapper)
                else -> setupCameraPreference0()
            }
            return (when (cameraPreference.getCameraMethod()) {
                PREFERENCE_CAMERA_METHOD_NONE -> DummyCameraControl()
                PREFERENCE_CAMERA_METHOD_CAMERAX -> prepareCameraXControl(cameraPreference)
                PREFERENCE_CAMERA_METHOD_THETA -> prepareThetaCameraControl(cameraPreference)
                PREFERENCE_CAMERA_METHOD_PENTAX -> preparePentaxCameraControl(cameraPreference)
                PREFERENCE_CAMERA_METHOD_PANASONIC -> preparePanasonicCameraControl(cameraPreference)
                else -> DummyCameraControl()
            })
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
        return (DummyCameraControl())
    }

    fun getCameraXControl() : ICameraControl
    {
        try
        {
            return (prepareCameraXControl(setupCameraPreference0()))
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
        return (DummyCameraControl())
    }

    fun getCameraSelection(preferenceKey : String) : Int
    {
        var cameraSequence = 0
        try
        {
            val wrapper = PreferenceAccessWrapper(activity)
            cameraSequence = wrapper.getString(preferenceKey, "0").toInt()
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
        return (cameraSequence)
    }

    private fun setupCameraPreference0() : ICameraPreferenceProvider
    {
        return (CameraPreference(PREFERENCE_CAMERA_METHOD_NONE))
    }

    private fun setupCameraPreference1(wrapper : PreferenceAccessWrapper) : ICameraPreferenceProvider
    {
        val method  = wrapper.getString(PREFERENCE_CAMERA_METHOD_1, PREFERENCE_CAMERA_METHOD_1_DEFAULT_VALUE)
        val sequence  = wrapper.getString(PREFERENCE_CAMERA_SEQUENCE_1, PREFERENCE_CAMERA_SEQUENCE_1_DEFAULT_VALUE)
        val option1  = wrapper.getString(PREFERENCE_CAMERA_OPTION1_1, PREFERENCE_CAMERA_OPTION1_1_DEFAULT_VALUE)
        val option2  = wrapper.getString(PREFERENCE_CAMERA_OPTION2_1, PREFERENCE_CAMERA_OPTION2_1_DEFAULT_VALUE)
        val option3  = wrapper.getString(PREFERENCE_CAMERA_OPTION3_1, PREFERENCE_CAMERA_OPTION3_1_DEFAULT_VALUE)
        val option4  = wrapper.getString(PREFERENCE_CAMERA_OPTION4_1, PREFERENCE_CAMERA_OPTION4_1_DEFAULT_VALUE)
        val option5  = wrapper.getString(PREFERENCE_CAMERA_OPTION5_1, PREFERENCE_CAMERA_OPTION5_1_DEFAULT_VALUE)

        return (CameraPreference(method, sequence, option1, option2, option3, option4, option5))
    }

    private fun setupCameraPreference2(wrapper : PreferenceAccessWrapper) : ICameraPreferenceProvider
    {
        val method  = wrapper.getString(PREFERENCE_CAMERA_METHOD_2, PREFERENCE_CAMERA_METHOD_2_DEFAULT_VALUE)
        val sequence  = wrapper.getString(PREFERENCE_CAMERA_SEQUENCE_2, PREFERENCE_CAMERA_SEQUENCE_2_DEFAULT_VALUE)
        val option1  = wrapper.getString(PREFERENCE_CAMERA_OPTION1_2, PREFERENCE_CAMERA_OPTION1_2_DEFAULT_VALUE)
        val option2  = wrapper.getString(PREFERENCE_CAMERA_OPTION2_2, PREFERENCE_CAMERA_OPTION2_2_DEFAULT_VALUE)
        val option3  = wrapper.getString(PREFERENCE_CAMERA_OPTION3_2, PREFERENCE_CAMERA_OPTION3_2_DEFAULT_VALUE)
        val option4  = wrapper.getString(PREFERENCE_CAMERA_OPTION4_2, PREFERENCE_CAMERA_OPTION4_2_DEFAULT_VALUE)
        val option5  = wrapper.getString(PREFERENCE_CAMERA_OPTION5_2, PREFERENCE_CAMERA_OPTION5_2_DEFAULT_VALUE)

        return (CameraPreference(method, sequence, option1, option2, option3, option4, option5))
    }

    private fun setupCameraPreference3(wrapper : PreferenceAccessWrapper) : ICameraPreferenceProvider
    {
        val method  = wrapper.getString(PREFERENCE_CAMERA_METHOD_3, PREFERENCE_CAMERA_METHOD_3_DEFAULT_VALUE)
        val sequence  = wrapper.getString(PREFERENCE_CAMERA_SEQUENCE_3, PREFERENCE_CAMERA_SEQUENCE_3_DEFAULT_VALUE)
        val option1  = wrapper.getString(PREFERENCE_CAMERA_OPTION1_3, PREFERENCE_CAMERA_OPTION1_3_DEFAULT_VALUE)
        val option2  = wrapper.getString(PREFERENCE_CAMERA_OPTION2_3, PREFERENCE_CAMERA_OPTION2_3_DEFAULT_VALUE)
        val option3  = wrapper.getString(PREFERENCE_CAMERA_OPTION3_3, PREFERENCE_CAMERA_OPTION3_3_DEFAULT_VALUE)
        val option4  = wrapper.getString(PREFERENCE_CAMERA_OPTION4_3, PREFERENCE_CAMERA_OPTION4_3_DEFAULT_VALUE)
        val option5  = wrapper.getString(PREFERENCE_CAMERA_OPTION5_3, PREFERENCE_CAMERA_OPTION5_3_DEFAULT_VALUE)

        return (CameraPreference(method, sequence, option1, option2, option3, option4, option5))
    }

    private fun setupCameraPreference4(wrapper : PreferenceAccessWrapper) : ICameraPreferenceProvider
    {
        val method  = wrapper.getString(PREFERENCE_CAMERA_METHOD_4, PREFERENCE_CAMERA_METHOD_4_DEFAULT_VALUE)
        val sequence  = wrapper.getString(PREFERENCE_CAMERA_SEQUENCE_4, PREFERENCE_CAMERA_SEQUENCE_4_DEFAULT_VALUE)
        val option1  = wrapper.getString(PREFERENCE_CAMERA_OPTION1_4, PREFERENCE_CAMERA_OPTION1_4_DEFAULT_VALUE)
        val option2  = wrapper.getString(PREFERENCE_CAMERA_OPTION2_4, PREFERENCE_CAMERA_OPTION2_4_DEFAULT_VALUE)
        val option3  = wrapper.getString(PREFERENCE_CAMERA_OPTION3_4, PREFERENCE_CAMERA_OPTION3_4_DEFAULT_VALUE)
        val option4  = wrapper.getString(PREFERENCE_CAMERA_OPTION4_4, PREFERENCE_CAMERA_OPTION4_4_DEFAULT_VALUE)
        val option5  = wrapper.getString(PREFERENCE_CAMERA_OPTION5_4, PREFERENCE_CAMERA_OPTION5_4_DEFAULT_VALUE)

        return (CameraPreference(method, sequence, option1, option2, option3, option4, option5))
    }

    private fun prepareThetaCameraControl(cameraPreference : ICameraPreferenceProvider) : ICameraControl
    {
        return (ThetaCameraControl(activity, vibrator, cameraPreference, statusReceiver))
    }

    private fun preparePentaxCameraControl(cameraPreference : ICameraPreferenceProvider) : ICameraControl
    {
        return (RicohPentaxCameraControl(activity, vibrator, cameraPreference, statusReceiver))
    }

    private fun preparePanasonicCameraControl(cameraPreference : ICameraPreferenceProvider) : ICameraControl
    {
        return (PanasonicCameraControl(activity, vibrator, cameraPreference, statusReceiver))
    }

    private fun prepareCameraXControl(cameraPreference : ICameraPreferenceProvider): ICameraControl
    {
        if ((cameraXisCreated)&&(::cameraXControl0.isInitialized))
        {
            cameraXControl1 = CameraControl(activity, cameraPreference, vibrator)
            cameraXisCreated = true
            return (cameraXControl1)
        }
        cameraXControl0 = CameraControl(activity, cameraPreference, vibrator)
        cameraXisCreated = true
        return (cameraXControl0)
    }
}