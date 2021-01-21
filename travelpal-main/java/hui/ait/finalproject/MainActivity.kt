package hui.ait.finalproject

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        animationStart()
    }

    private fun animationStart() {
        var doraAnim = AnimationUtils.loadAnimation(
            this@MainActivity, R.anim.dora_move
        )
        doraAnimation(doraAnim)
        ivDora.startAnimation(doraAnim)
    }

    private fun doraAnimation(doraAnim: Animation) {
        doraAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {
            }
            override fun onAnimationEnd(animation: Animation?) {
                startActivity(Intent(this@MainActivity, MapsActivity::class.java))
                finish()
            }
            override fun onAnimationStart(animation: Animation?) {
            }
        })
    }
}
