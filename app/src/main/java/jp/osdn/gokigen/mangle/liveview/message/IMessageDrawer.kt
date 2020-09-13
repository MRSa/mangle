package jp.osdn.gokigen.mangle.liveview.message

import android.graphics.Color

interface IMessageDrawer
{
    enum class MessageArea
    {
        UPLEFT, UPRIGHT, CENTER, LOWLEFT, LOWRIGHT, UPCENTER, LOWCENTER, CENTERLEFT, CENTERRIGHT
    }

    enum class LevelArea
    {
        LEVEL_HORIZONTAL, LEVEL_VERTICAL
    }

    fun setMessageToShow(message: String, area: MessageArea, color: Int = Color.WHITE, size: Int = 8)
    fun setLevelToShow(value: Float, area: LevelArea)
}
