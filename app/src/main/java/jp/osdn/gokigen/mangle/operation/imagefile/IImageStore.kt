package jp.osdn.gokigen.mangle.operation.imagefile

import androidx.camera.core.ImageCapture

interface IImageStore
{
    fun takePhoto(imageCapture : ImageCapture?) : Boolean
}
