package jp.osdn.gokigen.gokigenassets.camera.vendor.pixpro.operation

import android.util.Log
import jp.osdn.gokigen.gokigenassets.camera.interfaces.ICaptureControl
import jp.osdn.gokigen.gokigenassets.camera.vendor.pixpro.wrapper.command.IPixproCommandPublisher
import jp.osdn.gokigen.gokigenassets.camera.vendor.pixpro.wrapper.command.messages.IPixproCommandCallback
import jp.osdn.gokigen.gokigenassets.camera.vendor.pixpro.wrapper.command.messages.specific.PixproExecuteVideo
import jp.osdn.gokigen.gokigenassets.liveview.focusframe.IAutoFocusFrameDisplay

class MovieShotControl(private val commandPublisher: IPixproCommandPublisher, frameDisplayer: IAutoFocusFrameDisplay?) : ICaptureControl, IPixproCommandCallback
{
    private var isMovieRecording = false

    override fun doCapture(kind: Int)
    {
        try
        {
            // シャッター
            Log.v(TAG, " doCapture($kind) : $isMovieRecording ")

            isMovieRecording = if ((kind <= -1)||(isMovieRecording))
            {
                // ビデオ録画終了
                Log.v(TAG, " FINISH VIDEO")
                commandPublisher.enqueueCommand(PixproExecuteVideo(this, true))
                false
            }
            else
            {
                // ビデオ録画開始
                Log.v(TAG, " START VIDEO")
                commandPublisher.enqueueCommand(PixproExecuteVideo(this, false))
                true
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    override fun receivedMessage(id: Int, rx_body: ByteArray?)
    {
        Log.v(TAG, " MovieShotControl::receivedMessage() : ${rx_body?.size} bytes")
/*
        try
        {
            if ((rx_body != null)&&(rx_body.size > 10))
            {
                val highByte = rx_body[9]
                val lowByte = rx_body[8]
                if ((highByte == 0x20.toByte())&&(lowByte == 0x01.toByte()))
                {
                    Log.v(TAG, String.format(" OK REPLY (ID : %d) ", id))
                }
                else
                {
                    Log.v(TAG, String.format(" RECEIVED NG REPLY ID : %d, RESPONSE CODE : 0x%02x%02x ", id, highByte, lowByte))
                }
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
*/
    }

    companion object
    {
        private val TAG = MovieShotControl::class.java.simpleName
    }
}
