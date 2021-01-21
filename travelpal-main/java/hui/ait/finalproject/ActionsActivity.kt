package hui.ait.finalproject

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.BottomNavigationView
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import hui.ait.finalproject.MapsActivity.Companion.KEY_NAME
import hui.ait.finalproject.adapter.PostAdapter
import hui.ait.finalproject.data.AppDatabase
import hui.ait.finalproject.data.DataPreferences
import hui.ait.finalproject.data.Post
import hui.ait.finalproject.dialog.PostDialog
import kotlinx.android.synthetic.main.activity_actions.*
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt

class ActionsActivity : AppCompatActivity(), PostDialog.PostHandler, OnMapReadyCallback {

    companion object {
        val KEY_ITEM_TO_EDIT = "KEY_ITEM_TO_EDIT"
    }

    lateinit var postAdapter: PostAdapter

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_packing_list -> {
                startActivity(Intent(this@ActionsActivity, PackingListActivity::class.java))
            }
            R.id.navigation_travel_info -> {
                startActivity(Intent(this@ActionsActivity, TravelInfoActivity::class.java))
            }
            R.id.navigation_budget -> {
                var intentBudget = Intent(this, BudgetActivity::class.java)
                intentBudget.putExtra(BudgetActivity.LOCATION_NAME, KEY_NAME)
                ContextCompat.startActivity(this, intentBudget, null)
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actions)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        if (intent.extras.containsKey(KEY_NAME)){
            locationName.text = intent.getStringExtra(KEY_NAME)
        }

        fab.setOnClickListener { view ->
            showAddPostDialog()
        }

        if (!wasOpenedEarlier()) {
            MaterialTapTargetPrompt.Builder(this)
                .setTarget(R.id.fab)
                .setPrimaryText(getString(R.string.new_post))
                .setSecondaryText(getString(R.string.fab_tutorial))
                .show()
        }

        saveFirstOpenInfo()

        initRecyclerViewFromDB()

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    private lateinit var mMap: GoogleMap

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setMapStyle(MapStyleOptions(MapsActivity.json))

        val currentLocation = DataPreferences.selectedLoc!!
        mMap.addMarker(MarkerOptions().position(currentLocation).title(KEY_NAME))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation))

        val cameraPosition = CameraPosition.Builder()
            .target(currentLocation)
            .zoom(5f)
            .build()
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    fun saveFirstOpenInfo() {
        var sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        var editor = sharedPref.edit()
        editor.putBoolean("KEY_WAS_OPEN", true)
        editor.apply()
    }

    fun wasOpenedEarlier() : Boolean {
        var sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        return sharedPref.getBoolean("KEY_WAS_OPEN", false)
    }

    private fun initRecyclerViewFromDB() {
        Thread {
            var postList = AppDatabase.getInstance(this@ActionsActivity).postDao().getAllPosts()

            runOnUiThread {
                postAdapter = PostAdapter(this, postList)

                recyclerPost.layoutManager = LinearLayoutManager(this)
                recyclerPost.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL ,false)

                recyclerPost.adapter = postAdapter

                val itemDecoration = DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL)
                recyclerPost.addItemDecoration(itemDecoration)
            }

        }.start()
    }

    private fun showAddPostDialog() {
        PostDialog().show(supportFragmentManager, "TAG_TODO_DIALOG")
    }

    var editIndex: Int = -1

    public fun showEditPostDialog(postToEdit: Post, idx: Int) {
        editIndex = idx
        val editPostDialog = PostDialog()

        val bundle = Bundle()
        bundle.putSerializable(KEY_ITEM_TO_EDIT, postToEdit)
        editPostDialog.arguments = bundle

        editPostDialog.show(supportFragmentManager,
            "EDITITEMDIALOG")
    }

    override fun postCreated(post: Post) {
        Thread {
            var newId = AppDatabase.getInstance(this).postDao().insertPost(post)

            post.postID = newId
            runOnUiThread {
                postAdapter.addPost(post)
            }

        }.start()
    }

    override fun postUpdated(item: Post) {
        Thread {
            AppDatabase.getInstance(this).postDao().updatePost(item)

            runOnUiThread {
                postAdapter.updatePost(item, editIndex)
            }
        }.start()
    }

}
