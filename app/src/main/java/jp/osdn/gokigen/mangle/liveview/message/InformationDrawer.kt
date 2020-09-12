package jp.osdn.gokigen.mangle.liveview.message

import android.graphics.Canvas
import android.graphics.Color
import jp.osdn.gokigen.mangle.liveview.message.LevelHolder.Companion.LEVEL_GAUGE_THRESHOLD_MIDDLE
import jp.osdn.gokigen.mangle.liveview.message.LevelHolder.Companion.LEVEL_GAUGE_THRESHOLD_OVER

class InformationDrawer : IMessageDrawer, IInformationDrawer
{
    private val upLeftMessage : MessageHolder = MessageHolder()
    private val upCenterMessage : MessageHolder = MessageHolder()
    private val upRightMessage : MessageHolder = MessageHolder()
    private val centerLeftMessage : MessageHolder = MessageHolder()
    private val centerMessage : MessageHolder = MessageHolder()
    private val centerRightMessage : MessageHolder = MessageHolder()
    private val lowLeftMessage : MessageHolder = MessageHolder()
    private val lowCenterMessage : MessageHolder = MessageHolder()
    private val lowRightMessage : MessageHolder = MessageHolder()

    private val horizontalAngleLevel : LevelHolder = LevelHolder()
    private val verticalAngleLevel : LevelHolder = LevelHolder()

    override fun setMessageToShow(message: String, area: IMessageDrawer.MessageArea, color: Int, size: Int)
    {
        when (area)
        {
            IMessageDrawer.MessageArea.UPLEFT -> upLeftMessage.setValue(message, color, size)
            IMessageDrawer.MessageArea.UPRIGHT -> upRightMessage.setValue(message, color, size)
            IMessageDrawer.MessageArea.CENTER -> centerMessage.setValue(message, color, size)
            IMessageDrawer.MessageArea.LOWLEFT -> lowLeftMessage.setValue(message, color, size)
            IMessageDrawer.MessageArea.LOWRIGHT -> lowRightMessage.setValue(message, color, size)
            IMessageDrawer.MessageArea.UPCENTER -> upCenterMessage.setValue(message, color, size)
            IMessageDrawer.MessageArea.LOWCENTER -> lowCenterMessage.setValue(message, color, size)
            IMessageDrawer.MessageArea.CENTERLEFT -> centerLeftMessage.setValue(message, color, size)
            IMessageDrawer.MessageArea.CENTERRIGHT -> centerRightMessage.setValue(message, color, size)
        }
    }

    override fun setLevelToShow(value: Float, area: IMessageDrawer.LevelArea)
    {
        when (area)
        {
            IMessageDrawer.LevelArea.LEVEL_HORIZONTAL -> horizontalAngleLevel.setAngleLevel(value)
            IMessageDrawer.LevelArea.LEVEL_VERTICAL -> verticalAngleLevel.setAngleLevel(value)
        }
    }

    override fun getMessageToShow(area: IMessageDrawer.MessageArea) : IMessageHolder
    {
        return when (area)
        {
            IMessageDrawer.MessageArea.UPLEFT -> upLeftMessage
            IMessageDrawer.MessageArea.UPRIGHT -> upRightMessage
            IMessageDrawer.MessageArea.CENTER -> centerMessage
            IMessageDrawer.MessageArea.LOWLEFT -> lowLeftMessage
            IMessageDrawer.MessageArea.LOWRIGHT -> lowRightMessage
            IMessageDrawer.MessageArea.UPCENTER -> upCenterMessage
            IMessageDrawer.MessageArea.LOWCENTER -> lowCenterMessage
            IMessageDrawer.MessageArea.CENTERLEFT -> centerLeftMessage
            IMessageDrawer.MessageArea.CENTERRIGHT -> centerRightMessage
        }
    }

    override fun getLevelToShow(area: IMessageDrawer.LevelArea) : ILevelHolder
    {
        return when (area)
        {
            IMessageDrawer.LevelArea.LEVEL_VERTICAL -> verticalAngleLevel
            IMessageDrawer.LevelArea.LEVEL_HORIZONTAL -> horizontalAngleLevel
        }
    }

    override fun getLevelColor(value: Float): Int
    {
        val checkValue = Math.abs(value)
        if (checkValue < LEVEL_GAUGE_THRESHOLD_MIDDLE)
        {
            return (Color.GREEN)
        }
        return if (checkValue > LEVEL_GAUGE_THRESHOLD_OVER) { (Color.RED) } else (Color.YELLOW)
    }

    override fun drawInformationMessages(canvas : Canvas)
    {
        TODO("Not yet implemented")
    }

    override fun drawLevelGauge(canvas: Canvas)
    {
        TODO("Not yet implemented")
    }

}
