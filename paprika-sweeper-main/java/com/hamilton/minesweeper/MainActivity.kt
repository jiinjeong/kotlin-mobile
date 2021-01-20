package com.hamilton.minesweeper

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object {
        public val SPICINESS = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnMild.setOnClickListener {
            startActivity(Intent(this@MainActivity, GameActivity::class.java).putExtra(SPICINESS, getString(R.string.mild)))
        }

        btnMedium.setOnClickListener {
            startActivity(Intent(this@MainActivity, GameActivity::class.java).putExtra(SPICINESS, getString(R.string.medium)))
        }

        btnSpicy.setOnClickListener {
            startActivity(Intent(this@MainActivity, GameActivity::class.java).putExtra(SPICINESS, getString(R.string.spicy)))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_about) {
            Toast.makeText(this, getString(R.string.author), Toast.LENGTH_LONG).show()
        }
        return super.onOptionsItemSelected(item)
    }
}
