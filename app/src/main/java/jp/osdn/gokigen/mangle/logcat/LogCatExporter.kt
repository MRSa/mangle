package jp.osdn.gokigen.mangle.logcat

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemLongClickListener
import jp.osdn.gokigen.mangle.utils.ConfirmationDialog
import jp.osdn.gokigen.mangle.R

class LogCatExporter(private val activity: Activity) : OnItemLongClickListener
{
    private val TAG = toString()

    override fun onItemLongClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long): Boolean
    {
        Log.v(TAG, "onItemLongClick()")

        val confirm: ConfirmationDialog = ConfirmationDialog.newInstance(activity)

        confirm.show(
            R.string.dialog_confirm_title_output_log,
            R.string.dialog_confirm_message_output_log,
            object : ConfirmationDialog.Callback
            {
                override fun confirm()
                {
                    Log.v(TAG, "confirm()")
                    try
                    {
                        val buf = StringBuilder()
                        val adapter = parent?.getAdapter()
                        if (adapter != null)
                        {

                            for (index in 0..(adapter.count - 1))
                            {
                                buf.append(adapter.getItem(index))
                                buf.append("\r\n")
                            }
                        }
                        val intent = Intent()
                        intent.action = Intent.ACTION_SEND
                        intent.type = "text/plain"
                        intent.putExtra(Intent.EXTRA_TITLE, "debug log for " + activity.getString(R.string.app_name))
                        intent.putExtra(Intent.EXTRA_TEXT, String(buf))
                        activity.startActivity(intent)
                    }
                    catch (e: Exception)
                    {
                        Log.v(TAG, " Exception : " + e.localizedMessage)
                        e.printStackTrace()
                    }
                }
            })
        return true
    }
}
