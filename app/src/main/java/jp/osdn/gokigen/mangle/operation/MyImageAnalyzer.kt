package jp.osdn.gokigen.mangle.operation

import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import jp.osdn.gokigen.mangle.liveview.image.IImageDataReceiver
import java.nio.ByteBuffer

class MyImageAnalyzer(private val dataReceiver : IImageDataReceiver) : ImageAnalysis.Analyzer
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
        Log.v(TAG, " ----- received image ----- ")
        val buffer = image.planes[0].buffer
        val data = buffer.toByteArray()
        dataReceiver.onUpdateLiveView(data, null)

        image.close()
    }
}