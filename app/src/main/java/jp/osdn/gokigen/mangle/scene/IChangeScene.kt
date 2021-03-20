package jp.osdn.gokigen.mangle.scene

interface IChangeScene
{
    fun initializeFragment()
    fun changeToLiveView()
    fun connectToCamera()
    fun changeToConfiguration()
    fun changeToDebugInformation()
    fun exitApplication()
}
