package jp.osdn.gokigen.mangle.scene

interface IChangeScene
{
    fun initializeFragment()
    fun changeToPreview()
    fun changeSceneToConfiguration()
    fun changeSceneToDebugInformation()
    fun exitApplication()
}
