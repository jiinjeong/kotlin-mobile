package hui.ait.finalproject.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import hui.ait.finalproject.PackingListActivity
import hui.ait.finalproject.R
import hui.ait.finalproject.data.AppDatabase
import hui.ait.finalproject.data.PackingItem
import hui.ait.finalproject.touch.PackingItemTouchHelperCallback
import kotlinx.android.synthetic.main.packing_item_row.view.*
import java.util.*

class PackingItemAdapter : RecyclerView.Adapter<PackingItemAdapter.ViewHolder>, PackingItemTouchHelperCallback {

    private var packingItemList = mutableListOf<PackingItem>()
    private val context: Context

    constructor(context: Context, listPackingItem: List<PackingItem>) : super() {
        this.context = context
        packingItemList.addAll(listPackingItem)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvPackingItemName = itemView.tvPackingItemName
        var tvPackingItemQuantity = itemView.tvPackingItemQuantity
        var cbPackedStatus = itemView.cbItemPackedStatus

        var btnDelete = itemView.btnDelete
        var btnAdd = itemView.btnAdd
        var btnMinus = itemView.btnMinus
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val packingItemRowView = LayoutInflater.from(context).inflate(
            R.layout.packing_item_row, viewGroup, false
        )
        return ViewHolder(packingItemRowView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val packingItem = packingItemList.get(position)

        setViewHolder(viewHolder, packingItem)
        buttonFunctions(viewHolder)
    }

    private fun setViewHolder(
        viewHolder: ViewHolder,
        packingItem: PackingItem
    ) {
        viewHolder.tvPackingItemName.text = packingItem.itemName
        viewHolder.tvPackingItemQuantity.text = packingItem.itemQuantity
        viewHolder.cbPackedStatus.isChecked = packingItem.packedStatus
    }

    private fun buttonFunctions(
        viewHolder: ViewHolder
    ) {
        viewHolder.btnDelete.setOnClickListener {
            deletePackingItem(viewHolder.adapterPosition)
        }
        viewHolder.btnAdd.setOnClickListener {
            var q =
                packingItemList.get(viewHolder.adapterPosition).itemQuantity.toInt()
            q++
            packingItemList.get(viewHolder.adapterPosition).itemQuantity = "$q"
            notifyItemChanged(viewHolder.adapterPosition)
        }
        viewHolder.btnMinus.setOnClickListener {
            var q =
                packingItemList.get(viewHolder.adapterPosition).itemQuantity.toInt()
            q--
            if (q > -1) {
                packingItemList.get(viewHolder.adapterPosition).itemQuantity = "$q"
            }
            notifyItemChanged(viewHolder.adapterPosition)
        }
    }

    override fun getItemCount(): Int {
        return packingItemList.size
    }

    fun addPackingItem(packingItem: PackingItem) {
        packingItemList.add(0, packingItem)
        notifyItemInserted(0)
    }

    fun updatePackingItem(packingItem: PackingItem, editIndex: Int) {
        packingItemList.set(editIndex, packingItem)
        notifyItemChanged(editIndex)
    }

    private fun deletePackingItem(deletePosition: Int) {
        Thread {
            AppDatabase.getInstance(context).packingItemDao().deletePackingItem(packingItemList.get(deletePosition))
            (context as PackingListActivity).runOnUiThread {
                packingItemList.removeAt(deletePosition)
                notifyItemRemoved(deletePosition)
            }
        }.start()
    }

    fun deleteAllPackingItems() {
        Thread {
            AppDatabase.getInstance(context).packingItemDao().deleteAllPackingItems()
            (context as PackingListActivity).runOnUiThread {
                packingItemList.clear()
                notifyDataSetChanged()
            }
        }.start()
    }

    override fun onDismissed(position: Int) {
        deletePackingItem(position)
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        Collections.swap(packingItemList, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }
}
