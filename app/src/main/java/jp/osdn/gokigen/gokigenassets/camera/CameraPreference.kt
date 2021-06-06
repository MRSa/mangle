package jp.osdn.gokigen.gokigenassets.camera

class CameraPreference(private val method : String, private val sequence : String = "0", private val option1 : String = "", private val option2 : String = "", private val option3 : String = "", private val option4 : String = "", private val option5 : String = "") : ICameraPreferenceProvider
{
    override fun getCameraMethod(): String
    {
        return (method)
    }

    override fun getConnectionSequence(): String
    {
        return (sequence)
    }

    override fun getCameraOption1(): String
    {
        return (option1)
    }

    override fun getCameraOption2(): String
    {
        return (option2)
    }

    override fun getCameraOption3(): String
    {
        return (option3)
    }

    override fun getCameraOption4(): String
    {
        return (option4)
    }

    override fun getCameraOption5(): String
    {
        return (option5)
    }
}
