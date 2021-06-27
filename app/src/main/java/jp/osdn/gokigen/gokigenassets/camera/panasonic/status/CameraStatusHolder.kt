package jp.osdn.gokigen.gokigenassets.camera.panasonic.status

import android.content.Context
import android.util.Log
import jp.osdn.gokigen.gokigenassets.camera.interfaces.ICardSlotSelectionReceiver
import jp.osdn.gokigen.gokigenassets.camera.interfaces.ICardSlotSelector
import jp.osdn.gokigen.gokigenassets.camera.panasonic.ICameraChangeListener
import jp.osdn.gokigen.gokigenassets.camera.panasonic.IPanasonicCamera
import jp.osdn.gokigen.gokigenassets.utils.communication.SimpleHttpClient
import java.util.ArrayList


class CameraStatusHolder(private val context: Context, private val remote: IPanasonicCamera, private val cardSlotSelector: ICardSlotSelector) : ICardSlotSelectionReceiver, ICameraStatusHolder
{
    private var listener: ICameraChangeListener? = null
    private var current_sd = "sd1"
    private var isInitialized = false
    private var isDualSlot = false

    fun parse(reply: String)
    {
        try
        {
            // Log.v(TAG, " getState : " + reply);
            var isEnableDualSlot = false
            if (reply.contains("<sd_memory>set</sd_memory>") && reply.contains("<sd2_memory>set</sd2_memory>")) {
                // カードが2枚刺さっている場合...
                isEnableDualSlot = true
            }
            if (!isInitialized || isDualSlot != isEnableDualSlot) {
                // 初回だけの実行...
                if (isEnableDualSlot) {
                    // カードが2枚刺さっている場合...
                    cardSlotSelector.setupSlotSelector(true, this)
                } else {
                    // カードが１つしか刺さっていない場合...
                    cardSlotSelector.setupSlotSelector(false, null)
                }
                isInitialized = true
                isDualSlot = isEnableDualSlot
            }
            checkCurrentSlot(reply)
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    private fun checkCurrentSlot(reply: String)
    {
        try
        {
            val header = "<current_sd>"
            val indexStart = reply.indexOf(header)
            val indexEnd = reply.indexOf("</current_sd>")
            if (indexStart > 0 && indexEnd > 0 && indexStart < indexEnd)
            {
                val currentSlot = reply.substring(indexStart + header.length, indexEnd)
                if (current_sd != currentSlot)
                {
                    current_sd = currentSlot
                    cardSlotSelector.changedCardSlot(current_sd)
                }
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    fun setEventChangeListener(listener: ICameraChangeListener)
    {
        this.listener = listener
    }

    fun clearEventChangeListener()
    {
        listener = null
    }

    override fun getCameraStatus(): String? {
        return null
    }

    override fun getLiveviewStatus(): Boolean {
        return false
    }

    override fun getShootMode(): String?
    {
        return null
    }

    override fun getAvailableShootModes(): List<String?>? {
        return null
    }

    override fun getZoomPosition(): Int {
        return 0
    }

    override fun getStorageId(): String {
        return current_sd
    }

    override fun slotSelected(slotId: String)
    {
        Log.v(TAG, " slotSelected : $slotId")
        if (current_sd != slotId)
        {
            // スロットを変更したい！
            requestToChangeSlot(slotId)
        }
    }
    /**
     *
     *
     */
    fun getAvailableItemList(key: String): List<String>
    {
        val itemList: MutableList<String> = ArrayList()
        try
        {
/*
            val array = latestResultObject!!.getJSONArray(key) ?: return itemList
            val nofItems = array.length()
            for (index in 0 until nofItems) {
                try {
                    itemList.add(array.getString(index))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
*/
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        return itemList
    }

    fun getItemStatus(key: String): String
    {
/*
        try
        {
            return latestResultObject!!.getString(key)
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
*/
        return ""
    }

    private fun requestToChangeSlot(slotId: String)
    {
        try
        {
            val thread = Thread {
                try
                {
                    var loop = true
                    val http = SimpleHttpClient()
                    while (loop)
                    {
                        val reply: String = http.httpGet(remote.getCmdUrl() + "cam.cgi?mode=setsetting&type=current_sd&value=" + slotId, TIMEOUT_MS)
                        if (reply.indexOf("<result>ok</result>") > 0)
                        {
                            loop = false
                            cardSlotSelector.selectSlot(slotId)
                        }
                        else
                        {
                            Thread.sleep(1000) // 1秒待つ
                        }
                    }
                }
                catch (e: Exception)
                {
                    e.printStackTrace()
                }
            }
            thread.start()
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    companion object
    {
        private val TAG = CameraStatusHolder::class.java.simpleName
        private const val TIMEOUT_MS = 3000
    }
}
