package jp.osdn.gokigen.gokigenassets.camera.vendor.pixpro.wrapper.command.messages.specific

import jp.osdn.gokigen.gokigenassets.camera.vendor.pixpro.wrapper.command.messages.IPixproCommandCallback
import jp.osdn.gokigen.gokigenassets.camera.vendor.pixpro.wrapper.command.messages.base.IPixproMessages
import jp.osdn.gokigen.gokigenassets.camera.vendor.pixpro.wrapper.command.messages.base.PixproCommandBase

class PixproExecuteShutterPrev(private val callback: IPixproCommandCallback) : PixproCommandBase()
{
    override fun getId() : Int
    {
        return (IPixproMessages.SEQ_SHUTTER)
    }
    override fun commandBody(): ByteArray
    {
        return byteArrayOf( //  (byte) 0xf9, (byte) 0x03
            0x2e.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
            0x30.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
            0xf9.toByte(), 0x03.toByte(), 0x00.toByte(), 0x00.toByte(),
            0x03.toByte(), 0x00.toByte(), 0x00.toByte(), 0x80.toByte(),

            0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
            0x01.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
            0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
            0x02.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),

            0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
            0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
            0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
            0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),

            0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
            0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
            0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
            0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),

            0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
            0x08.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
            0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
            0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),

            0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
            0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
            0x08.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
            0x28.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),

            0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
            0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
            0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
            0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),

            0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
            0x01.toByte(), 0x20.toByte(), 0x00.toByte(), 0x90.toByte(),
            0x01.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(),
            0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),

            0x01.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(),
            0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
            0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
            0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),

            0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
            0xbf.toByte(), 0x4d.toByte(), 0x00.toByte(), 0x00.toByte(),
            0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
            0x01.toByte(), 0x88.toByte(), 0x65.toByte(), 0xb6.toByte(),

            0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(),
            0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),  // (byte) 0xef, (byte) 0x03


            0x2e.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
            0x08.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),

            0xef.toByte(), 0x03.toByte(), 0x00.toByte(), 0x00.toByte(),
            0x01.toByte(), 0x00.toByte(), 0x00.toByte(), 0x80.toByte(),
            0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
            0x01.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),

            0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
            0x01.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
            0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
            0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),

            0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
            0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
            0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
            0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),

            0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
            0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
            0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
            0x08.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),

            0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
            0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
            0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
            0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),

            0x02.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),
            0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte(),

            0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(),
            0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte()
        )
    }

    override fun responseCallback(): IPixproCommandCallback
    {
        return callback
    }
}
