package jp.osdn.gokigen.mangle.liveview.message

import android.graphics.Canvas

interface IInformationDrawer
{
    fun drawInformationMessages(canvas : Canvas)
    fun drawLevelGauge(canvas : Canvas)
}
