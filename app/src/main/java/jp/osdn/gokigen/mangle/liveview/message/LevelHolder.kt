package jp.osdn.gokigen.mangle.liveview.message

class LevelHolder(private var angleLevel: Float = 0.0f) : ILevelHolder
{
    override fun getAngleLevel(): Float { return (angleLevel) }
    fun setAngleLevel(angleLevel: Float) { this.angleLevel = angleLevel }

    companion object
    {
        const val LEVEL_GAUGE_THRESHOLD_MIDDLE = 2.0f
        const val LEVEL_GAUGE_THRESHOLD_OVER = 15.0f
    }

}
