package jp.osdn.gokigen.gokigenassets.liveview

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import jp.osdn.gokigen.gokigenassets.constants.IApplicationConstantConvert.Companion.ID_BUTTON_SHUTTER
import jp.osdn.gokigen.gokigenassets.constants.IApplicationConstantConvert.Companion.ID_CACHE_SEEKBAR_0
import jp.osdn.gokigen.gokigenassets.constants.IApplicationConstantConvert.Companion.ID_CACHE_SEEKBAR_1
import jp.osdn.gokigen.gokigenassets.constants.IApplicationConstantConvert.Companion.ID_CACHE_SEEKBAR_2
import jp.osdn.gokigen.gokigenassets.constants.IApplicationConstantConvert.Companion.ID_CACHE_SEEKBAR_3
import jp.osdn.gokigen.gokigenassets.constants.IApplicationConstantConvert.Companion.ID_LIVE_VIEW_LAYOUT_DEFAULT
import jp.osdn.gokigen.gokigenassets.constants.IApplicationConstantConvert.Companion.ID_PREFERENCE_CACHE_LIVE_VIEW_PICTURES
import jp.osdn.gokigen.gokigenassets.constants.IApplicationConstantConvert.Companion.ID_VIEW_FINDER_0
import jp.osdn.gokigen.gokigenassets.constants.IApplicationConstantConvert.Companion.ID_VIEW_FINDER_1
import jp.osdn.gokigen.gokigenassets.constants.IApplicationConstantConvert.Companion.ID_VIEW_FINDER_2
import jp.osdn.gokigen.gokigenassets.constants.IApplicationConstantConvert.Companion.ID_VIEW_FINDER_3
import jp.osdn.gokigen.gokigenassets.constants.IApplicationConstantConvert.Companion.ID_VIEW_LOWER_AREA
import jp.osdn.gokigen.gokigenassets.constants.IApplicationConstantConvert.Companion.ID_VIEW_UPPER_AREA
import jp.osdn.gokigen.gokigenassets.camera.interfaces.ICameraControl

class LiveImageViewFragment(private val contentLayoutId: Int = ID_LIVE_VIEW_LAYOUT_DEFAULT) : Fragment(contentLayoutId), View.OnClickListener
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

    private var isCacheImage = false

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
        liveviewView.findViewById<ImageButton>(ID_BUTTON_SHUTTER)?.setOnClickListener(this)

        try
        {
            val preferences : SharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
            this.isCacheImage = preferences.getBoolean(ID_PREFERENCE_CACHE_LIVE_VIEW_PICTURES, false)
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }

        try
        {
            if (::cameraControl0.isInitialized)
            {
                val imageCache0 = liveviewView.findViewById<SeekBar>(ID_CACHE_SEEKBAR_0)
                imageCache0.visibility = View.GONE

                val imageView0 = liveviewView.findViewById<LiveImageView>(ID_VIEW_FINDER_0)
                if (isCameraControl0)
                {
                    cameraControl0.setRefresher(imageView0, imageView0)
                    if (isCacheImage)
                    {
                        imageCache0.visibility = View.VISIBLE
                        imageCache0.setOnSeekBarChangeListener(imageView0)
                    }
                }
                else
                {
                    imageView0.visibility = View.GONE
                }
            }
            if (::cameraControl1.isInitialized)
            {
                val imageCache1 = liveviewView.findViewById<SeekBar>(ID_CACHE_SEEKBAR_1)
                imageCache1.visibility = View.GONE

                val imageView1 = liveviewView.findViewById<LiveImageView>(ID_VIEW_FINDER_1)
                if (isCameraControl1)
                {
                    cameraControl1.setRefresher(imageView1, imageView1)
                    if (isCacheImage)
                    {
                        imageCache1.visibility = View.VISIBLE
                        imageCache1.setOnSeekBarChangeListener(imageView1)
                    }
                }
                else
                {
                    imageView1.visibility = View.GONE
                }
            }
            if (::cameraControl2.isInitialized)
            {
                val imageCache2 = liveviewView.findViewById<SeekBar>(ID_CACHE_SEEKBAR_2)
                imageCache2.visibility = View.GONE

                val imageView2 = liveviewView.findViewById<LiveImageView>(ID_VIEW_FINDER_2)
                if (isCameraControl2)
                {
                    cameraControl2.setRefresher(imageView2, imageView2)
                    if (isCacheImage)
                    {
                        imageCache2.visibility = View.VISIBLE
                        imageCache2.setOnSeekBarChangeListener(imageView2)
                    }
                }
                else
                {
                    imageView2.visibility = View.GONE
                }
            }
            if (::cameraControl3.isInitialized)
            {
                val imageCache3 = liveviewView.findViewById<SeekBar>(ID_CACHE_SEEKBAR_3)
                imageCache3.visibility = View.GONE

                val imageView3 = liveviewView.findViewById<LiveImageView>(ID_VIEW_FINDER_3)
                if (isCameraControl3)
                {
                    cameraControl3.setRefresher(imageView3, imageView3)
                    if (isCacheImage)
                    {
                        imageCache3.visibility = View.VISIBLE
                        imageCache3.setOnSeekBarChangeListener(imageView3)
                    }
                }
                else
                {
                    imageView3.visibility = View.GONE
                }
            }
            if ((!isCameraControl0)&&(!isCameraControl1))
            {
                val area = liveviewView.findViewById<LiveImageView>(ID_VIEW_UPPER_AREA)
                area.visibility = View.GONE
            }
            if ((!isCameraControl2)&&(!isCameraControl3))
            {
                val area = liveviewView.findViewById<LiveImageView>(ID_VIEW_LOWER_AREA)
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
