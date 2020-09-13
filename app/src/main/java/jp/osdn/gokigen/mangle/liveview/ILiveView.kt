package jp.osdn.gokigen.mangle.liveview

import jp.osdn.gokigen.mangle.liveview.image.IImageProvider
import jp.osdn.gokigen.mangle.liveview.message.IMessageDrawer

interface ILiveView
{
    fun setImageProvider(provider : IImageProvider)
    fun updateImageRotation(degrees : Int)

    fun getMessageDrawer() : IMessageDrawer
}
