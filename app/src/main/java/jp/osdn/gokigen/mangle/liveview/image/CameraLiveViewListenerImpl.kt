package jp.osdn.gokigen.mangle.liveview.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.preference.PreferenceManager
import jp.osdn.gokigen.mangle.R
import jp.osdn.gokigen.mangle.liveview.bitmapconvert.IPreviewImageConverter
import jp.osdn.gokigen.mangle.liveview.bitmapconvert.ImageConvertFactory
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor
import java.util.*

class CameraLiveViewListenerImpl(val context: Context) : IImageDataReceiver, IImageProvider
{
    private var cachePics = ArrayList<ByteArray>()
    private var maxCachePics : Int = 0
    private var currentCachePics : Int = 0
    private var imageBitmap : Bitmap
    private var bitmapConverter : IPreviewImageConverter

    init
    {
        imageBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.a01e1)
        bitmapConverter = ImageConvertFactory().getImageConverter(0)
        setupLiveviewCache()
    }

    override fun onUpdateLiveView(data: ByteArray, metadata: Map<String, Any>)
    {

    }

    override fun getImage(position: Float) : Bitmap
    {
        return (imageBitmap)
    }

    private fun setupLiveviewCache()
    {
        val preference = PreferenceManager.getDefaultSharedPreferences(context)
        if (preference != null)
        {
            if (preference.getBoolean(IPreferencePropertyAccessor.CACHE_LIVEVIEW_PICTURES, false))
            {
                cachePics = ArrayList()
                val nofCachePics = preference.getString(IPreferencePropertyAccessor.NUMBER_OF_CACHE_PICTURES, IPreferencePropertyAccessor.NUMBER_OF_CACHE_PICTURES_DEFAULT_VALUE)
                try
                {
                    if (nofCachePics != null)
                    {
                        maxCachePics = nofCachePics.toInt()
                    }
                    else
                    {
                        maxCachePics = 500
                    }
                }
                catch (e: Exception)
                {
                    e.printStackTrace()
                    maxCachePics = 500
                }
            }
        }
    }
}
