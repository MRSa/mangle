package jp.osdn.gokigen.gokigenassets.camera.vendor.omds.status

import android.util.Log

class OmdsCommandListParser
{
    fun startParse(targetData: String)
    {
        try
        {
/*

            var parsedData = targetData
            val parser: XmlPullParser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(StringReader(targetData))
            var eventType = parser.eventType
            while (eventType != XmlPullParser.END_DOCUMENT)
            {
                when (eventType) {
                    XmlPullParser.START_DOCUMENT -> { }
                    XmlPullParser.START_TAG -> {
                        //parsedData += "   [${parser.name}(${parser.attributeCount}) "
                        parsedData += "["
                        if (parser.attributeCount > 0)
                        {
                            parsedData += "${parser.getAttributeValue(0)} "
                        }
                    }
                    XmlPullParser.END_TAG -> { parsedData += "] " }
                    else -> { }
                }
                eventType = parser.next()
            }
*/
            if (isDumpLog)
            {
                val parsedData = targetData
                Log.v(TAG, "     ------------------------------------------ ")
                Log.v(TAG, " <<< COMMAND LIST >>>")
                for (pos in 0..parsedData.length step 768)
                {
                    val lastIndex = if ((pos + 768) > parsedData.length)
                    {
                        parsedData.length
                    }
                    else
                    {
                        pos + 768
                    }
                    Log.v(TAG, " ${parsedData.substring(pos, lastIndex)}")
                }
                Log.v(TAG, "     ------------------------------------------ ")
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }

    }

    companion object
    {
        private val TAG = OmdsCommandListParser::class.java.simpleName
        private const val isDumpLog = false
    }

}