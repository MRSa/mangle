package jp.osdn.gokigen.mangle.preview

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import jp.osdn.gokigen.mangle.operation.CameraControl
import jp.osdn.gokigen.mangle.R

class PreviewFragment(contentLayoutId: Int = R.layout.camera_capture) : Fragment(contentLayoutId)
{
    private val TAG = toString()
    private var cameraControl: CameraControl? = null
    private var previewView : View? = null

    companion object
    {
        fun newInstance() = PreviewFragment().apply { }
    }

    fun setCameraControl(otherCameraControl : CameraControl)
    {
        cameraControl = otherCameraControl
        cameraControl?.initialize()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        if (previewView != null)
        {
            return (previewView)
        }
        previewView = inflater.inflate(R.layout.camera_capture, null, false)
        //cameraControl?.initialize()
        return (previewView)
    }

    override fun onResume()
    {
        super.onResume()
        Log.v(TAG, " onResume() : ")
        //cameraControl?.startCamera()
    }

    override fun onPause()
    {
        super.onPause()
        Log.v(TAG, " onPause() : ")
        //cameraControl?.finish()
    }
}
