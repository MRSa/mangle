package jp.osdn.gokigen.mangle.liveview.bitmapconvert

class ImageConvertFactory
{
    fun getImageConverter(id: Int): IPreviewImageConverter
    {
        return (ConvertNothing())
    }
}
