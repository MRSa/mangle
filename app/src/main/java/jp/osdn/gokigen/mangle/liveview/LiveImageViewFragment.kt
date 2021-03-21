package jp.osdn.gokigen.mangle.liveview

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import jp.osdn.gokigen.mangle.R
import jp.osdn.gokigen.mangle.operation.ICameraControl

class LiveImageViewFragment(private val contentLayoutId: Int = R.layout.liveimage_view) : Fragment(contentLayoutId), View.OnClickListener
{
    private lateinit var liveviewView : View

    private lateinit var cameraControl0: ICameraControl
    private lateinit var cameraControl1: ICameraControl
    private lateinit var cameraControl2: ICameraControl
    private lateinit var cameraControl3: ICameraControl

    private var isCameraControl0 = false
    private var isCameraControl1 = false
    private var isCameraControl2 = false
    private var isCameraControl3 = false

    companion object
    {
        private val TAG = LiveImageViewFragment::class.java.simpleName
        fun newInstance() = LiveImageViewFragment().apply { }
    }

    fun setCameraControl(isCameraControl0 : Boolean, cameraControl0 : ICameraControl, isCameraControl1 : Boolean, cameraControl1 : ICameraControl, isCameraControl2 : Boolean, cameraControl2 : ICameraControl, isCameraControl3 : Boolean, cameraControl3 : ICameraControl)
    {
        this.isCameraControl0 = isCameraControl0
        this.cameraControl0 = cameraControl0

        this.isCameraControl1 = isCameraControl1
        this.cameraControl1 = cameraControl1

        this.isCameraControl2 = isCameraControl2
        this.cameraControl2 = cameraControl2

        this.isCameraControl3 = isCameraControl3
        this.cameraControl3 = cameraControl3
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        if (::liveviewView.isInitialized)
        {
            return (liveviewView)
        }
        liveviewView = inflater.inflate(contentLayoutId, null, false)
        liveviewView.findViewById<ImageButton>(R.id.button_camera)?.setOnClickListener(this)

        try
        {
            if (::cameraControl0.isInitialized)
            {
                val imageView0 = liveviewView.findViewById<LiveImageView>(R.id.liveViewFinder0)
                if (isCameraControl0)
                {
                    cameraControl0.setRefresher(imageView0, imageView0)
                }
                else
                {
                    imageView0.visibility = View.GONE
                }
            }
            if (::cameraControl1.isInitialized)
            {
                val imageView1 = liveviewView.findViewById<LiveImageView>(R.id.liveViewFinder1)
                if (isCameraControl1)
                {
                    cameraControl1.setRefresher(imageView1, imageView1)
                }
                else
                {
                    imageView1.visibility = View.GONE
                }
            }
            if (::cameraControl2.isInitialized)
            {
                val imageView2 = liveviewView.findViewById<LiveImageView>(R.id.liveViewFinder2)
                if (isCameraControl2)
                {
                    cameraControl2.setRefresher(imageView2, imageView2)
                }
                else
                {
                    imageView2.visibility = View.GONE
                }
            }
            if (::cameraControl3.isInitialized)
            {
                val imageView3 = liveviewView.findViewById<LiveImageView>(R.id.liveViewFinder3)
                if (isCameraControl3)
                {
                    cameraControl3.setRefresher(imageView3, imageView3)
                }
                else
                {
                    imageView3.visibility = View.GONE
                }
            }
            if ((!isCameraControl0)&&(!isCameraControl1))
            {
                val area = liveviewView.findViewById<LiveImageView>(R.id.liveview_upper_area)
                area.visibility = View.GONE
            }
            if ((!isCameraControl2)&&(!isCameraControl3))
            {
                val area = liveviewView.findViewById<LiveImageView>(R.id.liveview_lower_area)
                area.visibility = View.GONE
            }
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
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

    override fun onClick(v: View?)
    {
        try
        {
            if ((::cameraControl0.isInitialized)&&(isCameraControl0))
            {
                cameraControl0.captureButtonReceiver(0).onClick(v)
            }
            if ((::cameraControl1.isInitialized)&&(isCameraControl1))
            {
                cameraControl1.captureButtonReceiver(1).onClick(v)
            }
            if ((::cameraControl2.isInitialized)&&(isCameraControl2))
            {
                cameraControl2.captureButtonReceiver(2).onClick(v)
            }
            if ((::cameraControl3.isInitialized)&&(isCameraControl3))
            {
                cameraControl3.captureButtonReceiver(3).onClick(v)
            }
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
    }
}
