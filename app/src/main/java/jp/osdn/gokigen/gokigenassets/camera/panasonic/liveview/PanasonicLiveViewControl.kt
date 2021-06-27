package jp.osdn.gokigen.gokigenassets.camera.panasonic.liveview

import android.util.Log
import jp.osdn.gokigen.gokigenassets.camera.panasonic.IPanasonicCamera
import jp.osdn.gokigen.gokigenassets.liveview.image.CameraLiveViewListenerImpl
import jp.osdn.gokigen.gokigenassets.utils.communication.SimpleHttpClient
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.util.*


class PanasonicLiveViewControl(private val liveViewListener : CameraLiveViewListenerImpl, private val camera: IPanasonicCamera)
{
    private var receiverSocket: DatagramSocket? = null
    private var whileStreamReceive = false
    private var errorOccur = 0

    companion object
    {
        private val TAG =  PanasonicLiveViewControl::class.java.simpleName
        private const val TIMEOUT_MAX = 3
        private const val ERROR_MAX = 30
        private const val RECEIVE_BUFFER_SIZE = 1024 * 1024 * 4
        private const val TIMEOUT_MS = 1500
        private const val LIVEVIEW_PORT = 49152
        private const val LIVEVIEW_START_REQUEST = "cam.cgi?mode=startstream&value=49152"
        private const val LIVEVIEW_STOP_REQUEST = "cam.cgi?mode=stopstream"
    }

    fun changeLiveViewSize(size: String?) {}

    fun startLiveView()
    {
        Log.v(TAG, "startLiveView()")
        try
        {
            val thread = Thread(Runnable {
                try
                {
                    startReceiveStream()
                    if (!whileStreamReceive)
                    {
                        Log.v(TAG, "CANNOT OPEN : UDP RECEIVE SOCKET")
                        return@Runnable
                    }
                    val http = SimpleHttpClient()
                    val requestUrl = camera.getCmdUrl() + LIVEVIEW_START_REQUEST
                    val reply: String = http.httpGet(requestUrl, TIMEOUT_MS)
                    if (!reply.contains("<result>ok</result>"))
                    {
                        try
                        {
                            // エラー回数のカウントアップ
                            errorOccur++

                            // 少し待つ...
                            Thread.sleep(TIMEOUT_MS.toLong())
                            if (errorOccur < ERROR_MAX)
                            {
                                Log.v(TAG, "RETRY START LIVEVIEW... : $errorOccur")
                                startLiveView()
                            }
                            else
                            {
                                Log.v(TAG, "RETRY OVER : START LIVEVIEW")
                            }
                        }
                        catch (e: Exception)
                        {
                            e.printStackTrace()
                        }
                    }
                    else
                    {
                        Log.v(TAG, "   ----- START LIVEVIEW ----- : $requestUrl")
                    }
                }
                catch (e: Exception)
                {
                    e.printStackTrace()
                }
            })
            thread.start()
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    fun stopLiveView()
    {
        Log.v(TAG, "stopLiveView()")
        try
        {
            val thread = Thread {
                try
                {
                    val http = SimpleHttpClient()
                    val reply: String = http.httpGet(camera.getCmdUrl() + LIVEVIEW_STOP_REQUEST, TIMEOUT_MS)
                    if (!reply.contains("<result>ok</result>"))
                    {
                        Log.v(TAG, "stopLiveview() reply is fail... $reply")
                    }
                    else
                    {
                        Log.v(TAG, "stopLiveview() is issued.")
                    }
                    //  ライブビューウォッチャーを止める
                    whileStreamReceive = false
                    closeReceiveSocket()
                }
                catch (e: Exception)
                {
                    e.printStackTrace()
                }
            }
            thread.start()
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    fun updateDigitalZoom() {}
    fun updateMagnifyingLiveViewScale(isChangeScale: Boolean) {}
    fun getMagnifyingLiveViewScale(): Float { return 1.0f }
    fun getDigitalZoomScale(): Float { return 1.0f }

    private fun startReceiveStream()
    {
        if (whileStreamReceive)
        {
            Log.v(TAG, "startReceiveStream() : already starting.")
            return
        }

        // ソケットをあける (UDP)
        try
        {
            receiverSocket = DatagramSocket(LIVEVIEW_PORT)
            whileStreamReceive = true
        }
        catch (e: Exception)
        {
            e.printStackTrace()
            whileStreamReceive = false
            receiverSocket = null
        }

        // 受信スレッドを動かす
        val thread = Thread { receiverThread() }
        try
        {
            thread.start()
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    private fun checkReceiveImage(packet: DatagramPacket)
    {
        val dataLength: Int = packet.length
        var searchIndex = 0
        var startPosition = 0
        val startmarker = intArrayOf(0xff, 0xd8)
        val receivedData: ByteArray = packet.data
        if (receivedData == null)
        {
            // 受信データが取れなかったので終了する
            Log.v(TAG, "RECEIVED DATA IS NULL...")
            return
        }
        //Log.v(TAG, "RECEIVED PACKET : " + dataLength);
        while (startPosition < dataLength)
        {
            // 先頭のjpegマーカーが出てくるまで読み飛ばす
            try
            {
                if (receivedData[startPosition++] == startmarker[searchIndex].toByte())
                {
                    searchIndex++
                    if (searchIndex >= startmarker.size)
                    {
                        break
                    }
                }
            }
            catch (e: Exception)
            {
                e.printStackTrace()
                return
            }
        }
        val offset = startPosition - startmarker.size
        liveViewListener.onUpdateLiveView(receivedData.copyOfRange(offset, dataLength), null)
    }

    private fun receiverThread()
    {
        var exceptionCount = 0
        val buffer = ByteArray(RECEIVE_BUFFER_SIZE)
        while (whileStreamReceive)
        {
            try
            {
                val receive_packet = DatagramPacket(buffer, buffer.size)
                if (receiverSocket != null)
                {
                    receiverSocket?.setSoTimeout(TIMEOUT_MS)
                    receiverSocket?.receive(receive_packet)
                    checkReceiveImage(receive_packet)
                    exceptionCount = 0
                }
                else
                {
                    Log.v(TAG, "receiveSocket is NULL...")
                }
            }
            catch (e: Exception)
            {
                exceptionCount++
                e.printStackTrace()
                if (exceptionCount > TIMEOUT_MAX)
                {
                    try
                    {
                        Log.v(TAG, "LV : RETRY REQUEST")
                        exceptionCount = 0
                        val http = SimpleHttpClient()
                        val reply: String = http.httpGet(camera.getCmdUrl() + LIVEVIEW_START_REQUEST, TIMEOUT_MS)
                        if (!reply.contains("ok"))
                        {
                            Log.v(TAG, "LV : RETRY COMMAND FAIL...")
                        }
                    }
                    catch (ee: Exception)
                    {
                        ee.printStackTrace()
                    }
                }
            }
        }
        closeReceiveSocket()
        Log.v(TAG, "  ----- startReceiveStream() : Finished.")
        System.gc()
    }

    private fun closeReceiveSocket()
    {
        Log.v(TAG, "closeReceiveSocket()")
        try
        {
            if (receiverSocket != null)
            {
                Log.v(TAG, "  ----- SOCKET CLOSE -----  ")
                receiverSocket?.close()
                receiverSocket = null
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }
}
