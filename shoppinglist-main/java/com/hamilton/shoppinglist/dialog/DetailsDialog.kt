package com.hamilton.shoppinglist.dialog

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.TextView
import com.hamilton.shoppinglist.R
import com.hamilton.shoppinglist.ScrollingActivity
import com.hamilton.shoppinglist.data.Shopping
import kotlinx.android.synthetic.main.details_dialog.view.*

class DetailsDialog : DialogFragment() {
    private lateinit var tvName: TextView
    private lateinit var tvDate: TextView
    private lateinit var tvItemDesc: TextView

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.item_details))
        val rootView = buildShopping()

        builder.setView(rootView)
        builder.setNegativeButton(getString(R.string.close)) {
                dialog, witch ->
        }
        return builder.create()
    }

    private fun buildShopping(): View? {
        val shoppingItem = arguments?.getSerializable(
            ScrollingActivity.KEY_ITEM_TO_EDIT
        ) as Shopping
        val rootView = buildRootView()

        updateTextView(shoppingItem)
        return rootView
    }

    private fun updateTextView(shoppingItem: Shopping) {
        tvName.text = shoppingItem.shoppingText
        tvDate.text = getString(R.string.created) + shoppingItem.createDate
        tvItemDesc.text = shoppingItem.shoppingDesc
    }

    private fun buildRootView(): View? {
        val rootView = requireActivity().layoutInflater.inflate(
            R.layout.details_dialog, null
        )
        tvName = rootView.tvItemName
        tvDate = rootView.tvDate
        tvItemDesc = rootView.tvItemDesc
        return rootView
    }
}
