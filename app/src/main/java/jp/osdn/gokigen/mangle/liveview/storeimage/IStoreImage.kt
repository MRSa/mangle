package jp.osdn.gokigen.mangle.liveview.storeimage

import android.graphics.Bitmap

interface IStoreImage
{
    fun doStore(id : Int = 0, target: Bitmap? = null)
}
