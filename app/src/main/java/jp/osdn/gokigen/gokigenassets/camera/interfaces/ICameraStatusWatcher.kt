package jp.osdn.gokigen.gokigenassets.camera.interfaces

import jp.osdn.gokigen.gokigenassets.liveview.message.IMessageDrawer

interface ICameraStatusWatcher
{
    fun startStatusWatch(indicator : IMessageDrawer?)
    fun startStatusWatch(notifier: ICameraStatusUpdateNotify)
    fun stopStatusWatch()
}
