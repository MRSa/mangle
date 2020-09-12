package jp.osdn.gokigen.mangle.preview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import jp.osdn.gokigen.mangle.R

class PreviewFragment(val contentLayoutId: Int = R.layout.camera_capture) : Fragment(contentLayoutId)
{
    private lateinit var previewView : View

    companion object
    {
        fun newInstance() = PreviewFragment().apply { }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        if (::previewView.isInitialized)
        {
            return (previewView)
        }
        previewView = inflater.inflate(contentLayoutId, null, false)
        return (previewView)
    }
}
