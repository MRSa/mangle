package jp.osdn.gokigen.gokigenassets.camera.panasonic.status

import jp.osdn.gokigen.gokigenassets.camera.panasonic.ICameraChangeListener


interface ICameraEventObserver
{
    fun activate()
    fun start(): Boolean
    fun stop()
    fun release()

    fun setEventListener(listener: ICameraChangeListener)
    fun clearEventListener()
    fun getCameraStatusHolder(): ICameraStatusHolder?
}
