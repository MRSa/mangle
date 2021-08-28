package jp.osdn.gokigen.gokigenassets.camera.vendor.pixpro.wrapper

import android.util.Log
import jp.osdn.gokigen.gokigenassets.utils.communication.SimpleLogDumper

class PixproCamera : IPixproCamera, IPixproCameraInitializer
{
    private lateinit var ipAddress: String
    private var portNumber = -1
    private var liveViewPort = -1
    private var tcpNoDelay = false
    private var isAvailable = false

    companion object
    {
        private val TAG = PixproCamera::class.java.simpleName
    }

    override fun setCommunicationParameter(ip: String, port: Int, lvPort: Int, tcpDelay: Boolean)
    {
        Log.v(TAG, "setCommunicationParameter($ip, $port, $lvPort, $tcpDelay)")
        this.ipAddress = ip
        this.portNumber = port
        this.liveViewPort = lvPort
        this.tcpNoDelay = tcpDelay
        isAvailable = true
    }

    override fun parseCommunicationParameter(data: ByteArray)
    {
        //  AOFREPLY:DC163,1,PIXPRO WPZ2,172.16.0.254,255.255.255.0,(mac address),9176,9175,0,(WIFI SSID),0
        try
        {
            val dumpSize = if (data.size > 127 ) { 127 } else { data.size }
            SimpleLogDumper.dumpBytes("[QUERY:${data.size}]", data.copyOfRange(0, dumpSize))
            isAvailable = true
        }
        catch (e: Exception)
        {
            e.printStackTrace()
            isAvailable = false
        }
    }

    override fun isAvailable(): Boolean
    {
        return (isAvailable)
    }

    override fun getIpAddress(): String
    {
        if (::ipAddress.isInitialized)
        {
            return (ipAddress)
        }
        return ("172.16.0.254")
    }

    override fun getPortNumber(): Int
    {
        if (portNumber > 0)
        {
            return (portNumber)
        }
        return (9175)
    }

    override fun getLiveViewPortNumber(): Int
    {
        if (liveViewPort > 0)
        {
            return (liveViewPort)
        }
        return (9176)
    }

    override fun getTcpNoDelay(): Boolean
    {
        return (tcpNoDelay)
    }
}
