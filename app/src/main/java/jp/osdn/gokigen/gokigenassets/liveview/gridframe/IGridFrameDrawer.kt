package jp.osdn.gokigen.gokigenassets.liveview.gridframe


import android.graphics.Canvas
import android.graphics.RectF

interface IGridFrameDrawer
{
    fun drawFramingGrid(canvas: Canvas, rect: RectF)
}
