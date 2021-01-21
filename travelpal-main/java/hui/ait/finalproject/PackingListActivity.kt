package hui.ait.finalproject

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.Menu
import android.view.MenuItem
import hui.ait.finalproject.adapter.PackingItemAdapter
import hui.ait.finalproject.data.AppDatabase
import hui.ait.finalproject.data.PackingItem
import hui.ait.finalproject.dialog.PackingItemDialog
import hui.ait.finalproject.touch.PackingItemRecyclerTouchCallback
import kotlinx.android.synthetic.main.activity_packing_list.*

class PackingListActivity : AppCompatActivity(), PackingItemDialog.PackingItemHandler {

    companion object {
        val KEY_ITEM_TO_EDIT = "KEY_ITEM_TO_EDIT"
    }

    lateinit var packingItemAdapter: PackingItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_packing_list)

        setSupportActionBar(toolbar)

        fab.setOnClickListener {
            PackingItemDialog().show(supportFragmentManager, getString(R.string.add_dialog))
        }

        initRecyclerViewFromDB()
    }

    private fun initRecyclerViewFromDB() {
        Thread {
            var packingList = AppDatabase.getInstance(this@PackingListActivity).packingItemDao().getAllPackingItems()

            runOnUiThread {
                packingItemAdapter = PackingItemAdapter(this, packingList)
                createRecycler()

                val callback = PackingItemRecyclerTouchCallback(packingItemAdapter)
                val touchHelper = ItemTouchHelper(callback)
                touchHelper.attachToRecyclerView(recyclerPackingItem)
            }
        }.start()
    }

    private fun createRecycler() {
        recyclerPackingItem.layoutManager = LinearLayoutManager(this)
        recyclerPackingItem.adapter = packingItemAdapter

        val itemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        recyclerPackingItem.addItemDecoration(itemDecoration)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_packing_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_delete_list){
            deleteList()}
        return true
    }

    private fun deleteList(){
        packingItemAdapter.deleteAllPackingItems()
    }

    override fun packingItemCreated(item: PackingItem) {
        Thread {
            var newId = AppDatabase.getInstance(this).packingItemDao().insertPackingItem(item)
            item.itemId = newId
            runOnUiThread {
                packingItemAdapter.addPackingItem(item)
            }
        }.start()
    }

    override fun packingItemUpdated(item: PackingItem) {
        Thread {
            AppDatabase.getInstance(this).packingItemDao().updatePackingItem(item)
            runOnUiThread {
                packingItemAdapter.updatePackingItem(item, -1)
            }
        }.start()
    }
}
