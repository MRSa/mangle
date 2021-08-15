package jp.osdn.gokigen.gokigenassets.camera.sony.wrapper.eventlistener

import android.util.Log
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
                        val itemString = availableItemArray.getJSONObject(index).getString("whiteBalanceMode")
                        whiteBalanceList.add(itemString)
                    }
                }
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
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

    fun setTakeMode(value: String)
    {
        try
        {
            val replyJson = cameraApi.callGenericSonyApiMethod("camera", "setExposureMode", JSONArray().put(value), "1.0")
            if (cameraApi.isErrorReply(replyJson))
            {
                Log.v(TAG, " REPLY ERROR(exposureMode) : $replyJson")
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    fun setShutterSpeed(value: String)
    {
        try
        {
            val replyJson = cameraApi.callGenericSonyApiMethod("camera", "setShutterSpeed", JSONArray().put(value), "1.0")
            if (cameraApi.isErrorReply(replyJson))
            {
                Log.v(TAG, " REPLY ERROR(shutterSpeed) : $replyJson")
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    fun setAperture(value: String)
    {
        try
        {
            val replyJson = cameraApi.callGenericSonyApiMethod("camera", "setFNumber", JSONArray().put(value), "1.0")
            if (cameraApi.isErrorReply(replyJson))
            {
                Log.v(TAG, " REPLY ERROR(aperture) : $replyJson")
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    fun setExpRev(value: String)
    {
        try
        {
/*
            val replyJson = cameraApi.callGenericSonyApiMethod("camera", "setxxx", JSONArray().put(value), "1.0")
            if (cameraApi.isErrorReply(replyJson))
            {
                Log.v(TAG, " REPLY ERROR(aperture) : $replyJson")
            }
*/
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    fun setCaptureMode(value: String)
    {
        try
        {
            val replyJson = cameraApi.callGenericSonyApiMethod("camera", "setShootMode", JSONArray().put(value), "1.0")
            if (cameraApi.isErrorReply(replyJson))
            {
                Log.v(TAG, " REPLY ERROR(shootMode) : $replyJson")
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    fun setIsoSensitivity(value: String)
    {
        try
        {
            val replyJson = cameraApi.callGenericSonyApiMethod("camera", "setIsoSpeedRate", JSONArray().put(value), "1.0")
            if (cameraApi.isErrorReply(replyJson))
            {
                Log.v(TAG, " REPLY ERROR(ISO Sensitivity) : $replyJson")
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    fun setWhiteBalance(value: String)
    {
        try
        {
            val replyJson = cameraApi.callGenericSonyApiMethod("camera", "setWhiteBalance", JSONArray().put(value).put(false).put(2500), "1.0")
            if (cameraApi.isErrorReply(replyJson))
            {
                Log.v(TAG, " REPLY ERROR(White Balance) : $replyJson")
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    fun setMeteringMode(value: String)
    {
        try
        {
            Log.v(TAG, "setMeteringMode($value)")
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    fun setPictureEffect(value: String)
    {
        try
        {
            Log.v(TAG, "setPictureEffect($value)")
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    fun setRemainBattery(value: String)
    {
        try
        {
            Log.v(TAG, "setTorchMode($value)")
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    fun setTorchMode(value: String)
    {
        try
        {
            Log.v(TAG, "setTorchMode($value)")
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    companion object
    {
        private val TAG = SonyStatusCandidates::class.java.simpleName
    }
}
