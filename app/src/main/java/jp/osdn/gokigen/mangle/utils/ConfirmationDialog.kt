package jp.osdn.gokigen.mangle.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import jp.osdn.gokigen.mangle.R


class ConfirmationDialog : DialogFragment()
{
    private lateinit var myContext : Context

    private fun prepare(context: Context)
    {
        this.myContext = context
    }

    fun show(titleResId: Int, messageResId: Int, callback: Callback)
    {
        val title = myContext.getString(titleResId)
        val message = myContext.getString(messageResId)
        show(title, message, callback)
    }

    fun show(title: String?, message: String?, callback: Callback)
    {
        val alertDialog = AlertDialog.Builder(myContext)
        alertDialog.setTitle(title)
        alertDialog.setIcon(android.R.drawable.ic_dialog_alert)
        alertDialog.setMessage(message)
        alertDialog.setCancelable(true)
        alertDialog.setPositiveButton(myContext.getString(R.string.dialog_positive_execute)
        ) { dialog, which ->
            callback.confirm()
            dialog.dismiss() }

        alertDialog.setNegativeButton(myContext.getString(R.string.dialog_negative_cancel)) { dialog, which -> dialog.cancel() }
        alertDialog.show()
    }

    fun show(iconResId: Int, title: String?, message: String?)
    {
        // 表示イアログの生成
        val alertDialog = AlertDialog.Builder(myContext)
        alertDialog.setTitle(title)
        alertDialog.setIcon(iconResId)
        alertDialog.setMessage(message)
        alertDialog.setCancelable(true)

        // ボタンを設定する（実行ボタン）
        alertDialog.setPositiveButton(myContext.getString(R.string.dialog_positive_execute)
        ) { dialog, which -> dialog.dismiss() }

        // 確認ダイアログを表示する
        alertDialog.show()
    }

    // コールバックインタフェース
    interface Callback
    {
        fun confirm()
    }

    companion object
    {
        fun newInstance(context: Context): ConfirmationDialog
        {
            val instance = ConfirmationDialog()
            instance.prepare(context)
            return (instance)
        }
    }
}
