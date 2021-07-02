package jp.osdn.gokigen.gokigenassets.camera

interface ICameraPreferenceProvider
{
    fun getCameraMethod() : String
    fun getConnectionSequence() : String

    fun getCameraOption1() : String
    fun getCameraOption2() : String
    fun getCameraOption3() : String
    fun getCameraOption4() : String
    fun getCameraOption5() : String

    fun getUpdator() : ICameraPreferenceUpdater?
}
