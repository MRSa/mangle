package jp.osdn.gokigen.mangle.liveview.storeimage

import android.graphics.Bitmap

interface IStoreImage
{
    fun doStore(target: Bitmap? = null)
}
