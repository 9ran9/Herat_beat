package com.example.herat_beat.view.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.herat_beat.R


class Mydialogone : DialogFragment(){
    private var title: String? = null
    private var items: Array<String>? = null
    private var checkedItem: Int = -1
    private var isMultiChoice: Boolean = false
    private var onPositiveButtonClicked: (() -> Unit)? = null

    fun getCheckedItem():Int{
        return checkedItem
    }
    companion object {
        fun newInstance(title: String, items: Array<String>, checkedItem: Int, isMultiChoice: Boolean): Mydialogone {
            val dialog = Mydialogone()
            dialog.title = title
            dialog.items = items
            dialog.checkedItem = checkedItem
            dialog.isMultiChoice = isMultiChoice
            return dialog
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext(), R.style.SmallDialogTheme)
        if (isMultiChoice) {
            val checkedItems = BooleanArray(items!!.size) { false }
            builder.setMultiChoiceItems(items, checkedItems) { dialog, which, isChecked ->
                checkedItems[which] = isChecked
            }
        } else {
            builder.setSingleChoiceItems(items, checkedItem) { dialog, which ->
                checkedItem = which
            }
        }

        builder.setTitle(title)
        builder.setPositiveButton("确定") { dialog, which ->
            onPositiveButtonClicked?.invoke()
        }
        builder.setNegativeButton("取消", null)

        return builder.create()
    }

    fun setOnPositiveButtonClickListener(listener: () -> Unit) {
        onPositiveButtonClicked = listener
    }
}