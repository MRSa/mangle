package jp.osdn.gokigen.mangle.operation

import android.view.View
import jp.osdn.gokigen.mangle.liveview.ILiveView
import jp.osdn.gokigen.mangle.liveview.ILiveViewRefresher

interface ICameraControl
{
    fun initialize()
    fun startCamera(isPreviewView : Boolean = true)
    fun finishCamera()

    fun setRefresher(refresher : ILiveViewRefresher, imageView : ILiveView)
    fun captureButtonReceiver() : View.OnClickListener
}
