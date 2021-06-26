package jp.osdn.gokigen.gokigenassets.camera.panasonic


interface IPanasonicCameraHolder
{
    fun detectedCamera(camera: IPanasonicCamera)
    fun prepare()
    fun startRecMode()
    fun startEventWatch(listener: ICameraChangeListener?)
}