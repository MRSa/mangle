package jp.osdn.gokigen.mangle.liveview.image

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.ImageFormat.NV21
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import jp.osdn.gokigen.mangle.R
import jp.osdn.gokigen.mangle.liveview.ILiveViewRefresher
import jp.osdn.gokigen.mangle.liveview.bitmapconvert.IPreviewImageConverter
import jp.osdn.gokigen.mangle.liveview.bitmapconvert.ImageConvertFactory
import jp.osdn.gokigen.mangle.preference.IPreferencePropertyAccessor
import jp.osdn.gokigen.mangle.preference.PreferenceAccessWrapper
import java.io.ByteArrayOutputStream
import java.util.*


class CameraLiveViewListenerImpl(private val context: Context) : IImageDataReceiver, IImageProvider, ImageAnalysis.Analyzer
{
    private var cachePics = ArrayList<ByteArray>()
    private var maxCachePics : Int = 0
    //private var currentCachePics : Int = 0
    private lateinit var imageBitmap : Bitmap
    private var bitmapConverter : IPreviewImageConverter = ImageConvertFactory().getImageConverter(0)
    private val refresher = ArrayList<ILiveViewRefresher>()

    init
    {
        refresher.clear()
    }

    fun setRefresher(refresher: ILiveViewRefresher)
    {
        this.refresher.add(refresher)
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
                convertToBitmapI420(imageProxy, rotationDegrees)
                return
            }
            if (imageProxy.format == ImageFormat.YUV_420_888)
            {
                convertToBitmapYUV420888(imageProxy, rotationDegrees)
                return
            }
            convertToBitmapYUV420888(imageProxy, rotationDegrees)
        }
        catch (e: Throwable)
        {
            e.printStackTrace()
        }
    }

    private fun convertToBitmapI420(imageProxy: ImageProxy, rotationDegrees: Int)
    {
        //Log.v(TAG, " convertToBitmap(I420) $rotationDegrees ")

        //  ImageFormat.YUV_420_888 : 35
        val yBuffer = imageProxy.planes[0].buffer
        val uBuffer = imageProxy.planes[1].buffer
        val vBuffer = imageProxy.planes[2].buffer

        val ySize = yBuffer.remaining()
        val uSize = uBuffer.remaining()
        val vSize = vBuffer.remaining()

        val nv21 = ByteArray(ySize + uSize + vSize)
        yBuffer.get(nv21, 0, ySize)
        try
        {
            var orgIndex = 0
            var index = ySize
            while (index < (ySize + uSize + vSize))
            {
                nv21[index++] = imageProxy.planes[2].buffer.get(orgIndex)
                nv21[index++] = imageProxy.planes[1].buffer.get(orgIndex)
                orgIndex++
            }
        }
        catch (t : Throwable)
        {
            t.printStackTrace()
        }

        val width = imageProxy.width
        val height = imageProxy.height
        val yuvImage = YuvImage(nv21, NV21, width, height, null)

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
        //Log.v(TAG, " convertToBitmap(YUV420) $rotationDegrees ")

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

    private fun rotateImageBitmap(rotationDegrees: Int)
    {
        try
        {
/*
            var addDegrees = 0
            val config = context.resources.configuration
            if (config.orientation == Configuration.ORIENTATION_LANDSCAPE)
            {
                addDegrees = 90
            }
            val rotationDegrees = degrees + addDegrees
*/
            val rotationMatrix = Matrix()
            rotationMatrix.postRotate(rotationDegrees.toFloat())
            imageBitmap = Bitmap.createBitmap(imageBitmap, 0, 0, imageBitmap.getWidth(), imageBitmap.getHeight(), rotationMatrix, true)
            System.gc()
        }
        catch (t: Throwable)
        {
            t.printStackTrace()
        }
    }

    override fun getImage(position: Float) : Bitmap
    {
        return (imageBitmap)
    }

    private fun refresh()
    {
        try
        {
            for (p in refresher)
            {
                p.refresh()
            }
        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }
    }

    private fun setupLiveviewCache()
    {
        val preference = PreferenceAccessWrapper(context)
        if (!preference.getBoolean(IPreferencePropertyAccessor.CACHE_LIVE_VIEW_PICTURES, false))
        {
            return
        }

        cachePics = ArrayList()
        val nofCachePics = preference.getString(IPreferencePropertyAccessor.NUMBER_OF_CACHE_PICTURES, IPreferencePropertyAccessor.NUMBER_OF_CACHE_PICTURES_DEFAULT_VALUE)
        try
        {
            maxCachePics = nofCachePics.toInt()
        }
        catch (e: Exception)
        {
            e.printStackTrace()
            maxCachePics = 500
        }
    }
}
