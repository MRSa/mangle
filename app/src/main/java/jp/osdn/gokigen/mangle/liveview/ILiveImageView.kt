package jp.osdn.gokigen.mangle.liveview

import jp.osdn.gokigen.mangle.liveview.image.IImageProvider

interface ILiveImageView
{
    fun refresh()
    fun setImageProvider(provider : IImageProvider)
    fun updateImageRotation(degrees : Int)
}
