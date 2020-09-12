package jp.osdn.gokigen.mangle.scene

interface IChangeScene
{
    fun initializeFragment()
    fun changeToLiveView()
    fun changeToPreview()
    fun changeToConfiguration()
    fun changeToDebugInformation()
    fun exitApplication()
}
