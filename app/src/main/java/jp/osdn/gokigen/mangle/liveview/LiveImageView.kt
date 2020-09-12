package jp.osdn.gokigen.mangle.liveview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.RectF
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.View
import jp.osdn.gokigen.mangle.liveview.focusframe.FocusFrameDrawer
import jp.osdn.gokigen.mangle.liveview.gridframe.GridFrameFactory
import jp.osdn.gokigen.mangle.liveview.gridframe.IGridFrameDrawer
import jp.osdn.gokigen.mangle.liveview.image.IImageProvider
import jp.osdn.gokigen.mangle.liveview.message.InformationDrawer
import kotlin.math.min

class LiveImageView : View, ILiveImageView
{
    private val TAG = this.toString()

    private var rotationDegrees : Int = 0
    private lateinit var imageProvider : IImageProvider
    private lateinit var gridFrameDrawer : IGridFrameDrawer
    private lateinit var focusFrameDrawer : FocusFrameDrawer
    private lateinit var informationDrawer : InformationDrawer


    constructor(context: Context) : super(context)
    {
        initComponent(context)
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    {
        initComponent(context)
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )
    {
        initComponent(context)
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    )
    {
        initComponent(context)
    }

    private fun initComponent(context: Context)
    {
        gridFrameDrawer = GridFrameFactory().getGridFrameDrawer(0)
        focusFrameDrawer = FocusFrameDrawer(context)
        informationDrawer = InformationDrawer()
    }

    override fun refresh()
    {
        refreshCanvas()
    }

    override fun setImageProvider(provider: IImageProvider)
    {
        this.imageProvider = provider
    }

    override fun updateImageRotation(degrees: Int)
    {
        this.rotationDegrees = degrees
        refreshCanvas()
    }

    override fun onDraw(canvas: Canvas?)
    {
        super.onDraw(canvas)
        if ((canvas == null)||(!(::imageProvider.isInitialized)))
        {
            Log.v(TAG, " onDraw : canvas is not ready.")
            return
        }
        canvas.drawARGB(255, 0, 0, 0)
        val imageRectF = drawImage(canvas)
        gridFrameDrawer.drawFramingGrid(canvas, imageRectF)
        focusFrameDrawer.drawFocusFrame(canvas)
        informationDrawer.drawInformationMessages(canvas)
        informationDrawer.drawLevelGauge(canvas)
    }

    private fun drawImage(canvas: Canvas) : RectF
    {
        val centerX = canvas.width / 2
        val centerY = canvas.height / 2

        val imageBitmap = imageProvider.getImage()
        val viewRect = decideViewRect(canvas, imageBitmap, rotationDegrees)

        canvas.rotate(rotationDegrees.toFloat(), centerX.toFloat(), centerY.toFloat())
        val imageRect = Rect(0, 0, imageBitmap.getWidth(), imageBitmap.getHeight())
        canvas.drawBitmap(imageBitmap, imageRect, viewRect, null)
        canvas.rotate(-rotationDegrees.toFloat(), centerX.toFloat(), centerY.toFloat())

        return (viewRect)
    }

    private fun refreshCanvas()
    {
        if (Looper.getMainLooper().thread === Thread.currentThread())
        {
            invalidate()
        }
        else
        {
            postInvalidate()
        }
    }

    private fun decideViewRect(canvas: Canvas, bitmapToShow: Bitmap, rotationDegrees: Int): RectF
    {
        val srcWidth: Int
        val srcHeight: Int
        if ((rotationDegrees == 0)||(rotationDegrees == 180))
        {
            srcWidth = bitmapToShow.width
            srcHeight = bitmapToShow.height
        }
        else
        {
            srcWidth = bitmapToShow.height
            srcHeight = bitmapToShow.width
        }
        val maxWidth = canvas.width
        val maxHeight = canvas.height
        val centerX = canvas.width / 2
        val centerY = canvas.height / 2
        val widthRatio = maxWidth / srcWidth.toFloat()
        val heightRatio = maxHeight / srcHeight.toFloat()
        val smallRatio = min(widthRatio, heightRatio)
        val dstWidth: Float
        val dstHeight: Float
        if (widthRatio < heightRatio)
        {
            dstWidth = maxWidth.toFloat()
            dstHeight = (smallRatio * srcHeight)
        }
        else
        {
            dstHeight = maxHeight.toFloat()
            dstWidth = (smallRatio * srcWidth)
        }
        val halfWidth = dstWidth * 0.5f
        val halfHeight = dstHeight * 0.5f
        return (if (rotationDegrees == 0 || rotationDegrees == 180)
        {
            RectF(
                (centerX - halfWidth),
                (centerY - halfHeight),
                ((centerX - halfWidth) + dstWidth),
                ((centerY - halfHeight) + dstHeight)
            )
        }
        else
        {
            RectF(
                (centerX - halfHeight),
                (centerY - halfWidth),
                ((centerX - halfHeight) + dstHeight),
                ((centerY - halfWidth) + dstWidth)
            )
        })
    }
}
