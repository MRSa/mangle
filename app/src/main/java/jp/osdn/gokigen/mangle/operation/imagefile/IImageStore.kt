package jp.osdn.gokigen.mangle.operation.imagefile

import androidx.camera.core.ImageCapture

interface IImageStore
{
    fun takePhoto(id : Int, imageCapture : ImageCapture?) : Boolean
}
