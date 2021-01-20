package com.hamilton.shoppinglist.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.*
import com.hamilton.shoppinglist.R
import com.hamilton.shoppinglist.ScrollingActivity
import com.hamilton.shoppinglist.data.Shopping
import kotlinx.android.synthetic.main.new_shopping_dialog.view.*
import java.lang.RuntimeException
import java.util.*

class ShoppingDialog : DialogFragment() {

    interface ShoppingHandler {
        fun shoppingCreated(item: Shopping)
        fun shoppingUpdated(item: Shopping)
    }

    private lateinit var shoppingHandler: ShoppingHandler

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is ShoppingHandler) {
            shoppingHandler = context
        } else {
            throw RuntimeException(getString(R.string.shopping_handler_exception))
        }
    }

    private lateinit var etShoppingText: EditText
    private lateinit var etShoppingDate: EditText
    private lateinit var cbShoppingBoughtStatus: CheckBox
    private lateinit var spinnerCategory: Spinner
    private lateinit var etShoppingPrice: EditText
    private lateinit var etShoppingItemDesc: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = build()
        val arguments = this.arguments
        editShopping(arguments, builder)

        builder.setPositiveButton(getString(R.string.ok)) {
                dialog, witch -> }
        builder.setNegativeButton(getString(R.string.close)) {
                dialog, witch -> }
        return builder.create()
    }

    private fun build(): AlertDialog.Builder {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.new_shopping))

        val rootView = requireActivity().layoutInflater.inflate(
            R.layout.new_shopping_dialog, null)
        buildRootView(rootView)
        buildSpinnerAdapter()

        builder.setView(rootView)
        return builder
    }

    private fun buildRootView(rootView: View) {
        etShoppingText = rootView.etItemName
        etShoppingDate = rootView.etDate
        cbShoppingBoughtStatus = rootView.cbBoughtStatus
        spinnerCategory = rootView.spinnerCategory
        etShoppingPrice = rootView.etPrice
        etShoppingItemDesc = rootView.etItemDesc
    }

    private fun ShoppingDialog.buildSpinnerAdapter() {
        val categoryAdapter = ArrayAdapter.createFromResource(
            activity,
            R.array.array_categories,
            android.R.layout.simple_dropdown_item_1line)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        spinnerCategory.adapter = categoryAdapter
    }

    private fun editShopping(arguments: Bundle?, builder: AlertDialog.Builder) {
        if (arguments != null && arguments.containsKey(
                ScrollingActivity.KEY_ITEM_TO_EDIT
            )
        ) {
            val shoppingItem = arguments.getSerializable(
                ScrollingActivity.KEY_ITEM_TO_EDIT
            ) as Shopping
            setShoppingText(shoppingItem)
            if (shoppingItem.category == getString(R.string.food)) {
                spinnerCategory.setSelection(0)
            }
            builder.setTitle(getString(R.string.edit_shopping))
        }
    }

    private fun setShoppingText(shoppingItem: Shopping) {
        etShoppingText.setText(shoppingItem.shoppingText)
        etShoppingDate.setText(shoppingItem.createDate)
        cbShoppingBoughtStatus.isChecked = shoppingItem.boughtStatus
        etShoppingPrice.setText(shoppingItem.estimatedPrice.toString())
        etShoppingItemDesc.setText(shoppingItem.shoppingDesc)
    }

    override fun onResume() {
        super.onResume()

        val positiveButton = (dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE)
        checkError(positiveButton)
    }

    private fun checkError(positiveButton: Button) {
        positiveButton.setOnClickListener {
            if (etShoppingText.text.isNotEmpty() && etShoppingPrice.text.isNotEmpty() && etShoppingItemDesc.text.isNotEmpty()) {
                createOrEdit()
            } else if (etShoppingText.text.isEmpty()) {
                etShoppingText.error = getString(R.string.empty_error)
            } else if (etShoppingPrice.text.isEmpty()) {
                etShoppingPrice.error = getString(R.string.empty_error)
            } else if (etShoppingItemDesc.text.isEmpty()) {
                etShoppingItemDesc.error = getString(R.string.empty_error)
            }
        }
    }

    private fun createOrEdit() {
        val arguments = this.arguments
        if (arguments != null && arguments.containsKey(ScrollingActivity.KEY_ITEM_TO_EDIT)) {
            handleShoppingEdit()
        } else {
            handleShoppingCreate()
        }
        dialog.dismiss()
    }

    private fun handleShoppingCreate() {
        shoppingHandler.shoppingCreated(
            Shopping(
                null,
                spinnerCategory.selectedItem.toString(),
                Date(System.currentTimeMillis()).toString(),
                etShoppingText.text.toString(),
                etShoppingItemDesc.text.toString(),
                etShoppingPrice.text.toString().toInt(),
                cbShoppingBoughtStatus.isChecked
            )
        )
    }

    private fun handleShoppingEdit() {
        val shoppingToEdit = arguments?.getSerializable(
            ScrollingActivity.KEY_ITEM_TO_EDIT
        ) as Shopping
        shoppingToEdit.shoppingText = etShoppingText.text.toString()
        shoppingToEdit.createDate = etShoppingDate.text.toString()
        shoppingToEdit.boughtStatus = cbShoppingBoughtStatus.isChecked
        shoppingToEdit.category = spinnerCategory.selectedItem.toString()
        shoppingToEdit.estimatedPrice = etShoppingPrice.text.toString().toInt()
        shoppingToEdit.shoppingDesc = etShoppingItemDesc.text.toString()
        shoppingHandler.shoppingUpdated(shoppingToEdit)
    }
}