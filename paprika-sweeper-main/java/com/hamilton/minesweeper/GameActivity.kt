package com.hamilton.minesweeper

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import com.hamilton.minesweeper.model.MinesweeperModel
import kotlinx.android.synthetic.main.activity_game.*
import android.content.Intent


class GameActivity : AppCompatActivity() {

    companion object {
        var LEVEL: Int = 0
    }

    var gameOver = false

    override fun onCreate(savedInstanceState: Bundle?) {
        startGame(savedInstanceState)

        btnRestart.setOnClickListener {
            restart()
        }
    }

    override fun onBackPressed() {
    }

    private fun startGame(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        gameTimer.start()
        gameTimer.format = getString(R.string.time)

        if (intent.extras.containsKey(MainActivity.SPICINESS)) {
            showSpiciness()
        }
        MinesweeperModel.generateField()
    }

    private fun restart() {
        gameOver = false
        minesweeperView.resetGame()
        gameTimer.setBase(SystemClock.elapsedRealtime())
    }

    private fun showSpiciness() {
        val spiciness = intent.getStringExtra(MainActivity.SPICINESS)
        if (spiciness == getString(R.string.mild)) {
            LEVEL = 3
        } else if (spiciness == getString(R.string.medium)) {
            LEVEL = 5
        } else if (spiciness == getString(R.string.spicy)) {
            LEVEL = 7
        }
        welcomeText.text = getString(R.string.welcome, spiciness, LEVEL.toString())
    }

    public fun isEnd(): Boolean {
        return gameOver
    }

    public fun endGame() {
        gameOver = true
    }

    public fun isFlagMode(): Boolean {
        return btnToggle.isChecked
    }

    public fun isTryMode(): Boolean {
        return !btnToggle.isChecked
    }
}
