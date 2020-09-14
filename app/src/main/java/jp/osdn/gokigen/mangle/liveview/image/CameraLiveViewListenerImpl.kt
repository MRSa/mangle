package jp.osdn.gokigen.mangle.liveview.image

import android.content.Context
import android.graphics.*
import android.media.Image
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.preference.PreferenceManager
import jp.osdn.gokigen.mangle.R
import jp.osdn.gokigen.mangle.liveview.ILiveViewRefresher
import jp.osdn.gokigen.mangle.liveview.bitmapconvert.IPreviewImageConverter
import jp.osdn.gokigen.mangle.liveview.bitmapconvert.ImageConvertFactory
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor
import java.io.ByteArrayOutputStream
import java.util.*

class CameraLiveViewListenerImpl(val context: Context) : IImageDataReceiver, IImageProvider, ImageAnalysis.Analyzer
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


    override fun analyze(imageProxy: ImageProxy)
    {
        val v = Log.v(TAG, " ----- received image ----- ")

        val yBuffer = imageProxy.planes[0].buffer // Y
        val uBuffer = imageProxy.planes[1].buffer // U
        val vBuffer = imageProxy.planes[2].buffer // V

        val ySize = yBuffer.remaining()
        val uSize = uBuffer.remaining()
        val vSize = vBuffer.remaining()

        val nv21 = ByteArray(ySize + uSize + vSize)

        //U and V are swapped
        yBuffer.get(nv21, 0, ySize)
        vBuffer.get(nv21, ySize, vSize)
        uBuffer.get(nv21, ySize + vSize, uSize)
        val yuvImage = YuvImage(nv21, ImageFormat.NV21, imageProxy.width, imageProxy.height, null)
        val out = ByteArrayOutputStream()
        yuvImage.compressToJpeg(Rect(0, 0, yuvImage.width, yuvImage.height), 50, out)
        val imageBytes = out.toByteArray()
        imageBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

        imageProxy.close()
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
