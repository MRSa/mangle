package jp.osdn.gokigen.gokigenassets.camera.sony.wrapper

interface ISonyCamera
{
    fun hasApiService(serviceName: String): Boolean
    fun getApiServices(): List<ISonyApiService>
    fun getFriendlyName(): String
    fun getModelName(): String
    fun getDdUrl(): String
}
