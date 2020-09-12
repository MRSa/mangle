package jp.osdn.gokigen.mangle.liveview.bitmapconvert

import android.graphics.Bitmap

interface IPreviewImageConverter
{
    fun getModifiedBitmap(src: Bitmap): Bitmap
}
