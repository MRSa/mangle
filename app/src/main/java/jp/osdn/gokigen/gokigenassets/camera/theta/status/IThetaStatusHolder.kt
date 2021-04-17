package jp.osdn.gokigen.gokigenassets.camera.theta.status


interface IThetaStatusHolder
{
    var captureMode: String
    var captureStatus : String

    fun invalidate()
}
