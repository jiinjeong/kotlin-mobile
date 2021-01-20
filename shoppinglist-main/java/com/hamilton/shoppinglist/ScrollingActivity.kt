package com.hamilton.shoppinglist

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.Menu
import android.view.MenuItem
import com.hamilton.shoppinglist.adapter.ShoppingAdapter
import com.hamilton.shoppinglist.data.AppDatabase
import com.hamilton.shoppinglist.data.Shopping
import com.hamilton.shoppinglist.dialog.ShoppingDialog
import com.hamilton.shoppinglist.touch.ShoppingReyclerTouchCallback
import kotlinx.android.synthetic.main.activity_scrolling.*

class ScrollingActivity : AppCompatActivity(), ShoppingDialog.ShoppingHandler {

    companion object {
        val KEY_ITEM_TO_EDIT = "KEY_ITEM_TO_EDIT"
        public var TOTAL_EXPENSES = 0
    }

    lateinit var shoppingAdapter : ShoppingAdapter
    var moneySpent = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)

        setSupportActionBar(toolbar)
        initRecyclerViewFromDB()

        fab.setOnClickListener { view ->
            Snackbar.make(view, getString(R.string.spent) + " $" + TOTAL_EXPENSES.toString(), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.action), null).show()
        }
    }

    private fun initRecyclerViewFromDB() {
        Thread {
            var shoppingList = AppDatabase.getInstance(this@ScrollingActivity).shoppingDao().getAllShoppings()

            runOnUiThread {
                shoppingAdapter = ShoppingAdapter(this, shoppingList)
                createRecycler()

                val callback = ShoppingReyclerTouchCallback(shoppingAdapter)
                val touchHelper = ItemTouchHelper(callback)
                touchHelper.attachToRecyclerView(recyclerShopping)
            }
        }.start()
    }

    private fun createRecycler() {
        recyclerShopping.layoutManager = LinearLayoutManager(this)
        recyclerShopping.adapter = shoppingAdapter

        val itemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        recyclerShopping.addItemDecoration(itemDecoration)
    }

    private fun showAddShoppingDialog() {
        ShoppingDialog().show(supportFragmentManager, getString(R.string.tag_add_dialog))
    }

    var editIndex: Int = -1

    public fun showEditShoppingDialog(shoppingToEdit: Shopping, idx: Int) {
        editIndex = idx
        val editItemDialog = ShoppingDialog()
        val bundle = Bundle()
        bundle.putSerializable(KEY_ITEM_TO_EDIT, shoppingToEdit)

        editItemDialog.arguments = bundle
        editItemDialog.show(supportFragmentManager, getString(R.string.edit_dialog))
    }

    private fun deleteList(){
        shoppingAdapter.deleteAllItems()
        TOTAL_EXPENSES = 0
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_scrolling, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_add_item) {
            showAddShoppingDialog()}
        if (item.itemId == R.id.action_delete_list){
            deleteList()}
        return true
    }

    override fun shoppingCreated(item: Shopping) {
        Thread {
            var newId = AppDatabase.getInstance(this).shoppingDao().insertShopping(item)
            item.shoppingId = newId
            runOnUiThread {
                shoppingAdapter.addShopping(item)
                TOTAL_EXPENSES += item.estimatedPrice
            }
        }.start()
    }

    override fun shoppingUpdated(item: Shopping) {
        Thread {
            AppDatabase.getInstance(this).shoppingDao().updateShopping(item)
            runOnUiThread {
                TOTAL_EXPENSES -= item.estimatedPrice
                shoppingAdapter.updateShopping(item, editIndex)
            }
        }.start()
    }
}
