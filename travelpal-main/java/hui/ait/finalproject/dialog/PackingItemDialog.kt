package hui.ait.finalproject.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import hui.ait.finalproject.R
import hui.ait.finalproject.data.PackingItem
import kotlinx.android.synthetic.main.new_packing_item_dialog.view.*
import java.lang.RuntimeException

class PackingItemDialog : DialogFragment() {

    private lateinit var etPackingItemName: EditText
    private lateinit var etPackingItemQuantity: EditText
    private lateinit var cbPackedStatus: CheckBox

    private lateinit var packingItemHandler: PackingItemHandler

    interface PackingItemHandler {
        fun packingItemCreated(item: PackingItem)
        fun packingItemUpdated(item: PackingItem)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is PackingItemHandler) {
            packingItemHandler = context
        } else {
            throw RuntimeException(
                getString(R.string.packinghandler_exception)) as Throwable
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = build()

        builder.setPositiveButton(getString(R.string.ok)) { dialog, witch -> }
        builder.setNegativeButton(getString(R.string.close)) { dialog, witch -> }
        return builder.create()
    }

    private fun build(): AlertDialog.Builder {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.packing_item))

        val rootView = requireActivity().layoutInflater.inflate(
            R.layout.new_packing_item_dialog, null
        )
        builder.setView(rootView)
        etPackingItemName = rootView.etPackingItemName
        etPackingItemQuantity = rootView.etPackingItemQuantity
        cbPackedStatus = rootView.cbPackedStatus

        return builder
    }

    override fun onResume() {
        super.onResume()

        val positiveButton = (dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE)
        checkError(positiveButton)
    }

    private fun checkError(positiveButton: Button) {
        positiveButton.setOnClickListener {
            if (etPackingItemName.text.isNotEmpty() && etPackingItemQuantity.text.isNotEmpty()) {
                createPackingItem()
            } else if (etPackingItemName.text.isEmpty()) {
                etPackingItemName.error = getString(R.string.blank_error)
            } else if (etPackingItemQuantity.text.isEmpty()) {
                etPackingItemQuantity.error = getString(R.string.blank_error)
            }
        }
    }

    private fun createPackingItem() {
        packingItemHandler.packingItemCreated(
            PackingItem(null,
                etPackingItemName.text.toString(),
                etPackingItemQuantity.text.toString(),
                cbPackedStatus.isChecked)
        )
        dialog.dismiss()
    }
}
