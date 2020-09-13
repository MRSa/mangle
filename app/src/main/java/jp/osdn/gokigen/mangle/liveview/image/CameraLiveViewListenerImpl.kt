package jp.osdn.gokigen.mangle.liveview.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.preference.PreferenceManager
import jp.osdn.gokigen.mangle.R
import jp.osdn.gokigen.mangle.liveview.ILiveViewRefresher
import jp.osdn.gokigen.mangle.liveview.bitmapconvert.IPreviewImageConverter
import jp.osdn.gokigen.mangle.liveview.bitmapconvert.ImageConvertFactory
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor
import java.util.*

class CameraLiveViewListenerImpl(val context: Context) : IImageDataReceiver, IImageProvider
{
    private val TAG = toString()
    private var cachePics = ArrayList<ByteArray>()
    private var maxCachePics : Int = 0
    private var currentCachePics : Int = 0
    private lateinit var imageBitmap : Bitmap
    private var bitmapConverter : IPreviewImageConverter
    private lateinit var refresher : ILiveViewRefresher

    init
    {
        bitmapConverter = ImageConvertFactory().getImageConverter(0)
    }

    fun setRefresher(refresher : ILiveViewRefresher)
    {
        this.refresher = refresher
        imageBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.a01e1)
        setupLiveviewCache()
    }

    override fun onUpdateLiveView(data: ByteArray, metadata: Map<String, Any>?)
    {
        refresh()
    }

    override fun getImage(position: Float) : Bitmap
    {
        return (imageBitmap)
    }

    private fun refresh()
    {
        if (::refresher.isInitialized)
        {
            refresher.refresh()
        }
    }

    private fun setupLiveviewCache()
    {
        val preference = PreferenceManager.getDefaultSharedPreferences(context)
        if ((preference == null)||(!preference.getBoolean(IPreferencePropertyAccessor.CACHE_LIVEVIEW_PICTURES, false)))
        {
            return
        }

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
