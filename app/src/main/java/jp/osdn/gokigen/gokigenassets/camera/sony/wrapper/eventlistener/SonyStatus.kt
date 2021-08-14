package jp.osdn.gokigen.gokigenassets.camera.sony.wrapper.eventlistener

import android.graphics.Color
import jp.osdn.gokigen.gokigenassets.camera.interfaces.*
import jp.osdn.gokigen.gokigenassets.liveview.message.IMessageDrawer
import org.json.JSONObject

class SonyStatus(jsonObject : JSONObject) : ICameraStatusUpdateNotify, ICameraStatusWatcher, ICameraStatus, ICameraChangeListener, ISonyStatusReceiver
{
    private var jsonObject : JSONObject
    init
    {
        this.jsonObject = jsonObject
    }

    override fun updatedTakeMode(mode: String?) { }

    override fun updatedShutterSpeed(tv: String?) { }

    override fun updatedAperture(av: String?) { }

    override fun updatedExposureCompensation(xv: String?) { }

    override fun updatedMeteringMode(meteringMode: String?) { }

    override fun updatedWBMode(wbMode: String?) {}

    override fun updateRemainBattery(percentage: Int) {}

    override fun updateFocusedStatus(focused: Boolean, focusLocked: Boolean) {}

    override fun updateIsoSensitivity(sv: String?) {}

    override fun updateWarning(warning: String?) {}

    override fun updateStorageStatus(status: String?) {}

    override fun startStatusWatch(indicator: IMessageDrawer?, notifier: ICameraStatusUpdateNotify?) { }

    override fun stopStatusWatch() { }

    override fun setStatus(key: String, value: String) { }

    override fun getStatusList(key: String): List<String?>
    {
        return (ArrayList())
    }

    override fun getStatus(key: String): String
    {
        return ("")
    }

    override fun getStatusColor(key: String): Int
    {
        return (Color.WHITE)
    }

    override fun onApiListModified(apis: List<String?>?) {
        //TODO("Not yet implemented")
    }

    override fun onCameraStatusChanged(status: String?) {
        //TODO("Not yet implemented")
    }

    override fun onLiveviewStatusChanged(status: Boolean) {
        //TODO("Not yet implemented")
    }

    override fun onShootModeChanged(shootMode: String?) {
        //TODO("Not yet implemented")
    }

    override fun onZoomPositionChanged(zoomPosition: Int) {
        //TODO("Not yet implemented")
    }

    override fun onStorageIdChanged(storageId: String?) {
        //TODO("Not yet implemented")
    }

    override fun onFocusStatusChanged(focusStatus: String?) {
        //TODO("Not yet implemented")
    }

    override fun onResponseError() {
        //TODO("Not yet implemented")
    }

    override fun updateStatus(jsonObject: JSONObject)
    {
        this.jsonObject = jsonObject
    }

}
