package jp.osdn.gokigen.gokigenassets.scene

import android.graphics.Color

interface IInformationReceiver
{
    fun updateMessage(message: String = "", isBold: Boolean = false, isColor: Boolean = false, color: Int = Color.BLACK)
}
