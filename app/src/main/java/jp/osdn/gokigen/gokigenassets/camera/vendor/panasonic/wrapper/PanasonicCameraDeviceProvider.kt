package jp.osdn.gokigen.gokigenassets.camera.vendor.panasonic.wrapper

import android.util.Log
import jp.osdn.gokigen.gokigenassets.camera.vendor.panasonic.IPanasonicApiService
import jp.osdn.gokigen.gokigenassets.camera.vendor.panasonic.IPanasonicCamera
import jp.osdn.gokigen.gokigenassets.utils.communication.SimpleHttpClient
import jp.osdn.gokigen.gokigenassets.utils.communication.XmlElement
import java.util.*
import kotlin.collections.ArrayList


class PanasonicCameraDeviceProvider(private val ddUrl: String, private val friendlyName: String, private val modelName: String, private val udn: String, private val iconUrl: String) : IPanasonicCamera
{
    private var apiServices: List<IPanasonicApiService> = ArrayList()
    private val uniqueID: String = UUID.randomUUID().toString()

    init
    {
        Log.v(TAG, "Panasonic Device : " + this.friendlyName + "(" + this.modelName + ") " + this.ddUrl + "  " + this.udn + " [" + this.iconUrl + "]")
        Log.v(TAG, "ANDROID DEVICE : $uniqueID")
    }

    /**
     *
     *
     */
    override fun hasApiService(serviceName: String?): Boolean
    {
        try
        {
            for (apiService in apiServices)
            {
                if (serviceName == apiService.getName())
                {
                    return true
                }
            }
            Log.v(TAG, "no API Service : " + serviceName + "[" + apiServices.size + "]")
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        return false
    }

    /**
     *
     *
     */
    override fun getApiServices(): List<IPanasonicApiService>
    {
        return apiServices
    }

    /**
     *
     *
     */
    override fun getFriendlyName(): String
    {
        return friendlyName
    }

    /**
     *
     *
     */
    override fun getModelName(): String
    {
        return modelName
    }

    /**
     *
     *
     */
    override fun getddUrl(): String
    {
        return ddUrl
    }

    /**
     *
     *
     */
    override fun getCmdUrl(): String
    {
        // コマンド送信先を応答する
        return ddUrl.substring(0, ddUrl.indexOf(":", 7)) + "/"
    }

    /**
     *
     *
     */
    override fun getObjUrl(): String
    {
        // オブジェクト取得用の送信先を応答する
        return ddUrl.substring(0, ddUrl.indexOf("/", 7)) + "/"
    }

    /**
     *
     *
     */
    override fun getPictureUrl(): String
    {
        // 画像取得先を応答する
        return ddUrl.substring(0, ddUrl.indexOf(":", 7)) + ":50001/"
    }

    override fun getClientDeviceUuId(): String
    {
        return uniqueID
    }

/*
    private void addApiService(String name, String actionUrl)
    {
        Log.v(TAG, "API : " + name + "  : " + actionUrl);
        PanasonicApiService service = new PanasonicApiService(name, actionUrl);
        apiServices.add(service);
    }
*/

    /*
    private void addApiService(String name, String actionUrl)
    {
        Log.v(TAG, "API : " + name + "  : " + actionUrl);
        PanasonicApiService service = new PanasonicApiService(name, actionUrl);
        apiServices.add(service);
    }
*/
    /**
     *
     *
     */
    fun searchPanasonicCameraDevice(ddUrl: String): IPanasonicCamera?
    {
        var device: PanasonicCameraDeviceProvider? = null
        val ddXml: String
        try
        {
            val http = SimpleHttpClient()
            ddXml = http.httpGet(ddUrl, -1)
            Log.d(TAG, "fetch () httpGet done. : " + ddXml.length)
            if (ddXml.length < 2)
            {
                // 内容がないときは...終了する
                Log.v(TAG, "NO BODY")
                return null
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
            return null
        }
        try
        {
            //Log.v(TAG, "ddXml : " + ddXml);
            val rootElement = XmlElement.parse(ddXml)

            // "root"
            if ("root" == rootElement.tagName)
            {
                // "device"
                val deviceElement = rootElement.findChild("device")
                val friendlyName = deviceElement.findChild("friendlyName").value
                val modelName = deviceElement.findChild("modelName").value
                val udn = deviceElement.findChild("UDN").value

                // "iconList"
                var iconUrl = ""
                val iconListElement = deviceElement.findChild("iconList")
                val iconElements = iconListElement.findChildren("icon")
                for (iconElement in iconElements)
                {
                    // Choose png icon to show Android UI.
                    if ("image/png" == iconElement.findChild("mimetype").value)
                    {
                        val uri = iconElement.findChild("url").value
                        val hostUrl = toSchemeAndHost(ddUrl)
                        iconUrl = hostUrl + uri
                    }
                }
                device = PanasonicCameraDeviceProvider(ddUrl, friendlyName, modelName, udn, iconUrl)
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        Log.d(TAG, "fetch () parsing XML done.")
        if (device == null)
        {
            Log.v(TAG, "device is null.")
        }
        return device
    }

    private fun toSchemeAndHost(url: String): String
    {
        val i = url.indexOf("://") // http:// or https://
        if (i == -1) { return "" }
        val j = url.indexOf("/", i + 3)
        return if (j == -1) { "" } else url.substring(0, j)
    }

    private fun toHost(url: String): String
    {
        val i = url.indexOf("://") // http:// or https://
        if (i == -1) { return "" }
        val j = url.indexOf(":", i + 3)
        return if (j == -1) { "" } else url.substring(i + 3, j)
    }

    companion object
    {
        private val TAG = PanasonicCameraDeviceProvider::class.java.simpleName
    }

}