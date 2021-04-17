package jp.osdn.gokigen.gokigenassets.liveview

import jp.osdn.gokigen.gokigenassets.liveview.image.IImageProvider
import jp.osdn.gokigen.gokigenassets.liveview.message.IMessageDrawer

interface ILiveView
{
    fun setImageProvider(provider : IImageProvider)
    fun updateImageRotation(degrees : Int)
    fun getMessageDrawer() : IMessageDrawer
    fun invalidate()
}
