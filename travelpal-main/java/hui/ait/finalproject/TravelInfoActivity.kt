package hui.ait.finalproject

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.ToxicBakery.viewpager.transforms.CubeOutTransformer
import hui.ait.finalproject.adapter.PagerAdapter
import kotlinx.android.synthetic.main.activity_travel_info.*

class TravelInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_travel_info)

        viewpager.adapter = PagerAdapter(supportFragmentManager);
        viewpager.setPageTransformer(true, CubeOutTransformer());
    }
}
