package jp.osdn.gokigen.mangle.liveview.image

interface ILiveViewListener
{
    fun getImageByteArray() : ByteArray
    fun setCameraLiveImageView(target: IImageDataReceiver)
}
