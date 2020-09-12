package jp.osdn.gokigen.mangle.liveview

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import jp.osdn.gokigen.mangle.R
import jp.osdn.gokigen.mangle.operation.CameraControl

class LiveImageViewFragment(val contentLayoutId: Int = R.layout.liveimage_view) : Fragment(contentLayoutId)
{
    private val TAG = toString()
    private lateinit var liveviewView : View
    private lateinit var cameraControl: CameraControl

    companion object
    {
        fun newInstance() = LiveImageViewFragment().apply { }
    }

    fun setCameraControl(cameraControl : CameraControl)
    {
        this.cameraControl = cameraControl
        this.cameraControl.initialize()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        if (::liveviewView.isInitialized)
        {
            return (liveviewView)
        }
//        liveviewView = inflater.inflate(R.layout.liveimage_view, null, false)
        liveviewView = inflater.inflate(contentLayoutId, null, false)

        return (liveviewView)
    }

    override fun onResume()
    {
        super.onResume()
        Log.v(TAG, " onResume() : ")
    }

    override fun onPause()
    {
        super.onPause()
        Log.v(TAG, " onPause() : ")
    }
}
