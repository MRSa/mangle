package jp.osdn.gokigen.mangle.liveview.message

import android.graphics.Canvas
import android.graphics.RectF

interface IInformationDrawer
{
    fun drawInformationMessages(canvas : Canvas, rect : RectF)
    fun drawLevelGauge(canvas : Canvas, rotationDegrees : Int = 0)
}
