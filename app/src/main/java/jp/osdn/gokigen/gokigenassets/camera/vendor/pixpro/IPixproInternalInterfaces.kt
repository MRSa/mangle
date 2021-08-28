package jp.osdn.gokigen.gokigenassets.camera.vendor.pixpro

import jp.osdn.gokigen.gokigenassets.camera.vendor.pixpro.wrapper.command.IPixproCommandPublisher
import jp.osdn.gokigen.gokigenassets.camera.vendor.pixpro.wrapper.command.IPixproCommunication
import jp.osdn.gokigen.gokigenassets.scene.IInformationReceiver

interface IPixproInternalInterfaces
{
    fun getIPixproCommunication() : IPixproCommunication
    fun getIPixproCommandPublisher() : IPixproCommandPublisher
    fun getInformationReceiver() : IInformationReceiver

}