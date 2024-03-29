package jp.osdn.gokigen.gokigenassets.camera.vendor.pixpro.wrapper.command.messages.specific

import jp.osdn.gokigen.gokigenassets.camera.vendor.pixpro.wrapper.command.messages.IPixproCommandCallback
import jp.osdn.gokigen.gokigenassets.camera.vendor.pixpro.wrapper.command.messages.base.IPixproMessages
import jp.osdn.gokigen.gokigenassets.camera.vendor.pixpro.wrapper.command.messages.base.PixproCommandBase

class PixproChangeExposureCompensation(private val callback: IPixproCommandCallback, value: Int) : PixproCommandBase()
{
    private val data0: Byte = value.toByte()

    override fun getId() : Int
    {
        return (IPixproMessages.SEQ_EXP_REV)
    }

    override fun commandBody(): ByteArray {
        return byteArrayOf(
            0x2e.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x90.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0xed.toByte(), 0x3.toByte(), 0x0.toByte(), 0x0.toByte(), 0x1.toByte(), 0x0.toByte(), 0x0.toByte(), 0x80.toByte(),
            0x1.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x1.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x1.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(),
            0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(),
            0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(),
            0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x90.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x1.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(),
            0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), data0, 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x13.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(),
            0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x1.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x2.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x3.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(),
            0x4.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x5.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x6.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x7.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(),
            0x8.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x9.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0a.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0b.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(),
            0x0c.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0d.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0e.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0f.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(),
            0x10.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x11.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x12.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(),
            0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(),
            0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(),
            0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(),
            0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(), 0x0.toByte(),

            0xff.toByte(), 0xff.toByte(), 0xff.toByte(), 0xff.toByte(),
            0x00.toByte(), 0x00.toByte(), 0x00.toByte(), 0x00.toByte()
        )
    }

    override fun responseCallback(): IPixproCommandCallback
    {
        return callback
    }
}
