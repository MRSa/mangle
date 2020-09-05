package jp.osdn.gokigen.mangle

import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import java.nio.ByteBuffer

typealias MyImageListener = (img: Double) -> Unit

class MyImageAnalyzer(private val listener: MyImageListener) : ImageAnalysis.Analyzer
{
    private val TAG = toString()

    init
    {

    }

    private fun ByteBuffer.toByteArray(): ByteArray
    {
        rewind()
        val data = ByteArray(remaining())
        get(data)
        return data
    }

    override fun analyze(image: ImageProxy)
    {
        Log.v(TAG, " image analyze...")

        val buffer = image.planes[0].buffer
        val data = buffer.toByteArray()
        val pixels = data.map { it.toInt() and 0xFF }
        val luma = pixels.average()

        listener(luma)

        image.close()
    }
}