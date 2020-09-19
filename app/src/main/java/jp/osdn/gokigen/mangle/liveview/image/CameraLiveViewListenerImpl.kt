package jp.osdn.gokigen.mangle.liveview.image

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.graphics.*
import android.graphics.ImageFormat.NV21
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
    //private var currentCachePics : Int = 0
    private lateinit var imageBitmap : Bitmap
    private var bitmapConverter : IPreviewImageConverter
    private lateinit var refresher : ILiveViewRefresher

    init
    {
        bitmapConverter = ImageConvertFactory().getImageConverter(0)
    }

    fun setRefresher(refresher: ILiveViewRefresher)
    {
        this.refresher = refresher
        imageBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.a01e1)
        setupLiveviewCache()
    }

    override fun onUpdateLiveView(data: ByteArray, metadata: Map<String, Any>?)
    {
        refresh()
    }

    @SuppressLint("UnsafeExperimentalUsageError")
    override fun analyze(imageProxy: ImageProxy)
    {
        try
        {
            val rotationDegrees = imageProxy.imageInfo.rotationDegrees

            if (imageProxy.image?.planes?.get(1)?.pixelStride == 1)
            {
                // from I420 format
                convertToBitmapFromI420(imageProxy, rotationDegrees)
                return
            }
            if (imageProxy.format == ImageFormat.YUV_420_888)
            {
                convertToBitmapYUV420888(imageProxy, rotationDegrees)
                return
            }
            convertToBitmapYUV420888(imageProxy, rotationDegrees)
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    private fun convertToBitmapFromI420(imageProxy: ImageProxy, rotationDegrees: Int)
    {
        Log.v(TAG, " convertToBitmap(I420) $rotationDegrees ")

        val width = imageProxy.width
        val height = imageProxy.height
        val bytes = ByteArray(imageProxy.width * imageProxy.height * 3 / 2)
        var i = 0
        for (row in 0 until height)
        {
            for (col in 0 until width)
            {
                bytes[i++] = imageProxy.planes[0].buffer.get(col + row * (width))
            }
        }
        for (row in 0 until height / 2)
        {
            for (col in 0 until width / 2)
            {
                bytes[i++] = imageProxy.planes[2].buffer.get(col + row * (width / 2))
                bytes[i++] = imageProxy.planes[1].buffer.get(col + row * (width / 2))
            }
        }
        val yuvImage = YuvImage(bytes, NV21, width, height, null)

/*
        //  ImageFormat.YUV_420_888 : 35
        val yBuffer = imageProxy.planes[0].buffer
        val vBuffer = imageProxy.planes[1].buffer
        val uBuffer = imageProxy.planes[2].buffer

        val ySize = yBuffer.remaining()
        val uSize = uBuffer.remaining()
        val vSize = vBuffer.remaining()

        val nv21 = ByteArray(ySize + uSize + vSize)

        // YUVの並びをそのままに変更してみる
        yBuffer.get(nv21, 0, ySize)
        vBuffer.get(nv21, ySize, vSize)
        uBuffer.get(nv21, ySize + vSize, uSize)

        val width = imageProxy.width
        val height = imageProxy.height
        val yuvImage = YuvImage(nv21, ImageFormat.NV21, width, height, null)
        yuvImage.compressToJpeg(Rect(0, 0, width, height), 100, out)
*/
        val out = ByteArrayOutputStream()
        yuvImage.compressToJpeg(Rect(0, 0, width, height), 100, out)
        val imageBytes = out.toByteArray()
        imageBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        rotateImageBitmap(rotationDegrees)

        imageProxy.close()
        refresh()
    }

    private fun convertToBitmapYUV420888(imageProxy: ImageProxy, rotationDegrees: Int)
    {
        Log.v(TAG, " convertToBitmap(YUV420) $rotationDegrees ")

        //  ImageFormat.YUV_420_888 : 35
        val yBuffer = imageProxy.planes[0].buffer
        val uBuffer = imageProxy.planes[1].buffer
        val vBuffer = imageProxy.planes[2].buffer

        val ySize = yBuffer.remaining()
        val uSize = uBuffer.remaining()
        val vSize = vBuffer.remaining()

        val nv21 = ByteArray(ySize + uSize + vSize)

        yBuffer.get(nv21, 0, ySize)
        vBuffer.get(nv21, ySize, vSize)
        uBuffer.get(nv21, ySize + vSize, uSize)

        val out = ByteArrayOutputStream()
        val width = imageProxy.width
        val height = imageProxy.height
        val yuvImage = YuvImage(nv21, NV21, width, height, null)
        yuvImage.compressToJpeg(Rect(0, 0, width, height), 100, out)

        val imageBytes = out.toByteArray()
        imageBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        rotateImageBitmap(rotationDegrees)
        imageProxy.close()
        refresh()
    }

    private fun rotateImageBitmap(degrees: Int)
    {
        var addDegrees = 0
        try
        {
            val config = context.resources.configuration
            if (config.orientation == Configuration.ORIENTATION_LANDSCAPE)
            {
                addDegrees = 90
            }
            System.gc()
        }
        catch (t: Throwable)
        {
            t.printStackTrace()
        }

        val rotationDegrees = degrees + addDegrees
        val rotationMatrix = Matrix()
        rotationMatrix.postRotate(rotationDegrees.toFloat())
        imageBitmap = Bitmap.createBitmap(imageBitmap, 0, 0, imageBitmap.getWidth(), imageBitmap.getHeight(), rotationMatrix, true)
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
        if ((preference == null)||(!preference.getBoolean(
                IPreferencePropertyAccessor.CACHE_LIVEVIEW_PICTURES,
                false
            )))
        {
            return
        }

        cachePics = ArrayList()
        val nofCachePics = preference.getString(
            IPreferencePropertyAccessor.NUMBER_OF_CACHE_PICTURES,
            IPreferencePropertyAccessor.NUMBER_OF_CACHE_PICTURES_DEFAULT_VALUE
        )
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
