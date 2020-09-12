package jp.osdn.gokigen.mangle.liveview.message

interface IMessageHolder
{
    fun getSize(): Int
    fun getColor(): Int
    fun getMessage(): String
}
