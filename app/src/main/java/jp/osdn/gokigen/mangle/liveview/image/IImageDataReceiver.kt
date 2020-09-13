package jp.osdn.gokigen.mangle.liveview.image

interface IImageDataReceiver
{
    fun onUpdateLiveView(data: ByteArray, metadata: Map<String, Any>?)
}
