package jp.osdn.gokigen.mangle.liveview.gridframe

class GridFrameFactory
{
    fun getGridFrameDrawer(id: Int): IGridFrameDrawer
    {
        return (GridFrameDrawerDefault())
    }
}
