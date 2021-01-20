package com.hamilton.shoppinglist.adapter

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hamilton.shoppinglist.dialog.DetailsDialog
import com.hamilton.shoppinglist.R
import com.hamilton.shoppinglist.ScrollingActivity
import com.hamilton.shoppinglist.ScrollingActivity.Companion.TOTAL_EXPENSES
import com.hamilton.shoppinglist.data.AppDatabase
import com.hamilton.shoppinglist.data.Shopping
import com.hamilton.shoppinglist.touch.ShoppingTouchHelperCallback
import kotlinx.android.synthetic.main.shopping_row.view.*
import java.util.*

class ShoppingAdapter : RecyclerView.Adapter<ShoppingAdapter.ViewHolder>, ShoppingTouchHelperCallback {

    var shoppingItems = mutableListOf<Shopping>()

    private val context: Context

    constructor(context: Context, listShoppings: List<Shopping>) : super() {
        this.context = context
        shoppingItems.addAll(listShoppings)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val shoppingRowView = LayoutInflater.from(context).inflate(
            R.layout.shopping_row, viewGroup, false
        )
        return ViewHolder(shoppingRowView)
    }

    override fun getItemCount(): Int {
        return shoppingItems.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val shopping = shoppingItems.get(position)

        setViewHolder(viewHolder, shopping)
        buttonFunctions(viewHolder, shopping)

        viewHolder.btnViewItemDetails.setOnClickListener {
            val detailsDialog = DetailsDialog()
            createBundle(shopping, detailsDialog)
        }
    }

    private fun setViewHolder(
        viewHolder: ViewHolder,
        shopping: Shopping
    ) {
        viewHolder.tvName.text = context.getString(R.string.item) + shopping.shoppingText
        viewHolder.cbBought.isChecked = shopping.boughtStatus
        viewHolder.tvPrice.text = context.getString(R.string.price) + " $" + shopping.estimatedPrice.toString()

        setCategoryImage(shopping, viewHolder)
    }

    private fun createBundle(
        shopping: Shopping,
        detailsDialog: DetailsDialog
    ) {
        val bundle = Bundle()
        bundle.putSerializable(ScrollingActivity.KEY_ITEM_TO_EDIT, shopping)
        detailsDialog.arguments = bundle
        detailsDialog.show(
            (context as ScrollingActivity).supportFragmentManager, context.getString(R.string.detail_dialog)
        )
    }

    private fun buttonFunctions(
        viewHolder: ViewHolder,
        shopping: Shopping
    ) {
        viewHolder.btnDelete.setOnClickListener {
            deleteShopping(viewHolder.adapterPosition)
        }

        viewHolder.btnEdit.setOnClickListener {
            (context as ScrollingActivity).showEditShoppingDialog(
                shopping,
                viewHolder.adapterPosition
            )
        }
    }

    private fun setCategoryImage(
        shopping: Shopping,
        viewHolder: ViewHolder
    ) {
        if (shopping.category == context.getString(R.string.food)) {
            viewHolder.imgCategory.setImageResource(R.drawable.food)
        } else if (shopping.category == context.getString(R.string.drink)) {
            viewHolder.imgCategory.setImageResource(R.drawable.drink)
        } else if (shopping.category == context.getString(R.string.clothes)) {
            viewHolder.imgCategory.setImageResource(R.drawable.clothes)
        } else if (shopping.category == context.getString(R.string.toy)) {
            viewHolder.imgCategory.setImageResource(R.drawable.toy)
        } else if (shopping.category == context.getString(R.string.tool)) {
            viewHolder.imgCategory.setImageResource(R.drawable.tool)
        } else {
            viewHolder.imgCategory.setImageResource(R.drawable.book)
        }
    }

    fun updateShopping(shopping: Shopping) {
        Thread {
            AppDatabase.getInstance(context).shoppingDao().updateShopping(shopping)
        }.start()
    }

    fun updateShopping(shopping: Shopping, editIndex: Int) {
        TOTAL_EXPENSES += shoppingItems.get(editIndex).estimatedPrice
        shoppingItems.set(editIndex, shopping)
        notifyItemChanged(editIndex)
    }


    fun addShopping(shopping: Shopping) {
        shoppingItems.add(0, shopping)
        notifyItemInserted(0)
    }

    fun deleteAllItems() {
        Thread {
            AppDatabase.getInstance(context).shoppingDao().deleteAllItems()
            (context as ScrollingActivity).runOnUiThread {
                shoppingItems.clear()
                notifyDataSetChanged()
            }
        }.start()
    }

    fun deleteShopping(deletePosition: Int) {
        Thread {
            AppDatabase.getInstance(context).shoppingDao().deleteShopping(shoppingItems.get(deletePosition))
            (context as ScrollingActivity).runOnUiThread {
                TOTAL_EXPENSES -= shoppingItems.get(deletePosition).estimatedPrice
                shoppingItems.removeAt(deletePosition)
                notifyItemRemoved(deletePosition)
            }
        }.start()
    }

    fun showWallet(){

    }

    override fun onDismissed(position: Int) {
        deleteShopping(position)
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        Collections.swap(shoppingItems, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvName = itemView.tvName
        var cbBought = itemView.cbBought
        var tvPrice = itemView.tvPrice
        var btnDelete = itemView.btnDelete
        var btnViewItemDetails = itemView.btnViewItemDetails
        var btnEdit = itemView.btnEdit
        var imgCategory = itemView.imgCategory
    }
}

