package com.hamilton.shoppinglist

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.content.Intent
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.hamilton.shoppinglist.view.TypeWriter
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        animationStart()
    }

    private fun animationStart() {
        var minionAnim = AnimationUtils.loadAnimation(
            this@SplashActivity, R.anim.minion_move
        )
        minionAnimation(minionAnim)
        typewrite()
        ivMinion.startAnimation(minionAnim)
    }

    private fun minionAnimation(minionAnim: Animation) {
        minionAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {
            }
            override fun onAnimationEnd(animation: Animation?) {
                startActivity(Intent(this@SplashActivity, ScrollingActivity::class.java))
                finish()
            }
            override fun onAnimationStart(animation: Animation?) {
            }
        })
    }

    private fun typewrite() {
        val tw = findViewById(R.id.splashTxtWelcome) as TypeWriter
        tw.text = ""
        tw.setCharacterDelay(100)
        tw.animateText(getString(R.string.welcome))
    }
}
