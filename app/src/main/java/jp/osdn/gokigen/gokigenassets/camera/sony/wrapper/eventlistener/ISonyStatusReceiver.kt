package jp.osdn.gokigen.gokigenassets.camera.sony.wrapper.eventlistener

import org.json.JSONObject

interface ISonyStatusReceiver
{
    fun updateStatus(jsonObject: JSONObject)
}