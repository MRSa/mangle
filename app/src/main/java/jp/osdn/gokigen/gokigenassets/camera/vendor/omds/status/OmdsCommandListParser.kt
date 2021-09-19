package jp.osdn.gokigen.gokigenassets.camera.vendor.omds.status

import android.util.Log
import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import java.io.StringReader

class OmdsCommandListParser
{
    fun startParse(targetData: String)
    {
        try
        {
            var parsedData = ""
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
            Log.v(TAG, " <<< COMMAND LIST >>>")
            Log.v(TAG, parsedData)
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }

    }

    companion object
    {
        private val TAG = OmdsCommandListParser::class.java.simpleName
    }

}