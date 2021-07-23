package jp.osdn.gokigen.gokigenassets.camera.panasonic.status

interface ICameraEventObserver
{
    fun receivedEvent(eventData: ByteArray?)
}
