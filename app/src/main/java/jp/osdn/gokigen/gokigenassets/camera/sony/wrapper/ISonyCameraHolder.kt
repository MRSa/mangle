package jp.osdn.gokigen.gokigenassets.camera.sony.wrapper

import jp.osdn.gokigen.gokigenassets.camera.interfaces.ICameraChangeListener

interface ISonyCameraHolder
{
    fun detectedCamera(camera: ISonyCamera)
    fun prepare()
    fun startRecMode()
    fun startPlaybackMode()
    fun startEventWatch(listener: ICameraChangeListener?)
}
