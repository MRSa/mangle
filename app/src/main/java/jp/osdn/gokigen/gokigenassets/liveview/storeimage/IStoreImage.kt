package jp.osdn.gokigen.gokigenassets.liveview.storeimage

import android.graphics.Bitmap

interface IStoreImage
{
    fun doStore(id : Int = 0, isEquirectangular : Boolean = false, target: Bitmap? = null)
}
