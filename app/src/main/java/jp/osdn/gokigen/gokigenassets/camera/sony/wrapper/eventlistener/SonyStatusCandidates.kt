package jp.osdn.gokigen.gokigenassets.camera.sony.wrapper.eventlistener

import jp.osdn.gokigen.gokigenassets.camera.sony.wrapper.ISonyCameraApi
import org.json.JSONArray

class SonyStatusCandidates()
{
    private lateinit var cameraApi: ISonyCameraApi
    fun setCameraApi(sonyCameraApi: ISonyCameraApi)
    {
        cameraApi = sonyCameraApi
    }

    fun getAvailableTakeMode(): List<String?>
    {
        val takeModeList : ArrayList<String> = ArrayList()
        try
        {
            if (::cameraApi.isInitialized)
            {
                val replyJson = cameraApi.callGenericSonyApiMethod("camera", "getAvailableExposureMode", JSONArray(), "1.0")
                val resultsObj = replyJson?.getJSONArray("result")
                if (resultsObj?.isNull(1) == false)
                {
                    val takeModeArray = resultsObj.getJSONArray(1)
                    //Log.v(TAG, " getAvailableTakeMode() : $takeModeArray")
                    for (index in 0 until takeModeArray.length())
                    {
                        val itemString = takeModeArray.getString(index)
                        takeModeList.add(itemString)
                        //Log.v(TAG, "  --- $itemString ($index) ---")
                    }
                }
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        return (takeModeList)
    }

    fun getAvailableShutterSpeed(): List<String?>
    {
        val shutterSpeedList : ArrayList<String> = ArrayList()
        try
        {
            if (::cameraApi.isInitialized)
            {
                val replyJson = cameraApi.callGenericSonyApiMethod("camera", "getAvailableShutterSpeed", JSONArray(), "1.0")
                val resultsObj = replyJson?.getJSONArray("result")
                if (resultsObj?.isNull(1) == false)
                {
                    val availableItemArray = resultsObj.getJSONArray(1)
                    for (index in 0 until availableItemArray.length())
                    {
                        val itemString = availableItemArray.getString(index)
                        shutterSpeedList.add(itemString)
                    }
                }
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        return (shutterSpeedList)
    }

    fun getAvailableAperture(): List<String?>
    {
        val apertureList : ArrayList<String> = ArrayList()
        try
        {
            if (::cameraApi.isInitialized)
            {
                val replyJson = cameraApi.callGenericSonyApiMethod("camera",  "getAvailableFNumber", JSONArray(), "1.0")
                val resultsObj = replyJson?.getJSONArray("result")
                if (resultsObj?.isNull(1) == false)
                {
                    val availableItemArray = resultsObj.getJSONArray(1)
                    for (index in 0 until availableItemArray.length())
                    {
                        val itemString = availableItemArray.getString(index)
                        apertureList.add(itemString)
                    }
                }
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        return (apertureList)
    }

    fun getAvailableExpRev(): List<String?>
    {
/*
        val apertureList : ArrayList<String> = ArrayList()
        try
        {
            if (::cameraApi.isInitialized)
            {
                val replyJson = cameraApi.callGenericSonyApiMethod("camera",  "getAvailableExposureCompensation", JSONArray(), "1.0")
                val resultsObj = replyJson?.getJSONArray("result")
                if (resultsObj?.isNull(1) == false)
                {
                    val availableItemArray = resultsObj.getJSONArray(1)
                    for (index in 0 until availableItemArray.length())
                    {
                        val itemString = availableItemArray.getString(index)
                        apertureList.add(itemString)
                    }
                }
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
*/
        return (ArrayList())
    }

    fun getAvailableCaptureMode(): List<String?>
    {
        val shootModeList : ArrayList<String> = ArrayList()
        try
        {
            if (::cameraApi.isInitialized)
            {
                val replyJson = cameraApi.callGenericSonyApiMethod("camera",  "getAvailableShootMode", JSONArray(), "1.0")
                val resultsObj = replyJson?.getJSONArray("result")
                if (resultsObj?.isNull(1) == false)
                {
                    val availableItemArray = resultsObj.getJSONArray(1)
                    for (index in 0 until availableItemArray.length())
                    {
                        val itemString = availableItemArray.getString(index)
                        shootModeList.add(itemString)
                    }
                }
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        return (shootModeList)
    }

    fun getAvailableIsoSensitivity(): List<String?>
    {
        val isoList : ArrayList<String> = ArrayList()
        try
        {
            if (::cameraApi.isInitialized)
            {
                val replyJson = cameraApi.callGenericSonyApiMethod("camera",  "getAvailableIsoSpeedRate", JSONArray(), "1.0")
                val resultsObj = replyJson?.getJSONArray("result")
                if (resultsObj?.isNull(1) == false)
                {
                    val availableItemArray = resultsObj.getJSONArray(1)
                    for (index in 0 until availableItemArray.length())
                    {
                        val itemString = availableItemArray.getString(index)
                        isoList.add(itemString)
                    }
                }
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        return (isoList)
    }

    fun getAvailableWhiteBalance(): List<String?>
    {
        val whiteBalanceList : ArrayList<String> = ArrayList()
/*
        try
        {
            if (::cameraApi.isInitialized)
            {
                val replyJson = cameraApi.callGenericSonyApiMethod("camera",  "getAvailableWhiteBalance", JSONArray(), "1.0")
                val resultsObj = replyJson?.getJSONArray("result")
                if (resultsObj?.isNull(1) == false)
                {
                    val availableItemArray = resultsObj.getJSONArray(1)
                    for (index in 0 until availableItemArray.length())
                    {
                        val itemString = availableItemArray.getString(index)
                        whiteBalanceList.add(itemString)
                    }
                }
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
*/
        return (whiteBalanceList)
    }

    fun getAvailableMeteringMode(): List<String?>
    {
        return (ArrayList())
    }

    fun getAvailablePictureEffect(): List<String?>
    {
        return (ArrayList())
    }

    fun getAvailableRemainBattery(): List<String?>
    {
        return (ArrayList())
    }

    fun getAvailableTorchMode(): List<String?>
    {
        return (ArrayList())
    }

    fun setTakeMode(value: String) { }
    fun setShutterSpeed(value: String) { }
    fun setAperture(value: String) { }
    fun setExpRev(value: String) { }
    fun setCaptureMode(value: String) { }
    fun setIsoSensitivity(value: String) { }
    fun setWhiteBalance(value: String) { }
    fun setMeteringMode(value: String) { }
    fun setPictureEffect(value: String) { }
    fun setRemainBattery(value: String) { }
    fun setTorchMode(value: String) { }

    companion object
    {
        private val TAG = SonyStatusCandidates::class.java.simpleName
    }
}
