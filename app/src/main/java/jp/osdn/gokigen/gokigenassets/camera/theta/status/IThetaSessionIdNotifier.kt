package jp.osdn.gokigen.gokigenassets.camera.theta.status


interface IThetaSessionIdNotifier
{
    fun receivedSessionId(sessionId: String?)
}
