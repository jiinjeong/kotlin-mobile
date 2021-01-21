package hui.ait.finalproject.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import hui.ait.finalproject.fragment.FragmentElectronics
import hui.ait.finalproject.fragment.FragmentMusic
import hui.ait.finalproject.fragment.FragmentWeather

class PagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> FragmentMusic()
            1 -> FragmentElectronics()
            else -> FragmentWeather()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        if (position == 0)
            return "Music"
        else if (position == 1)
            return "Electronics"
        else
            return "Weather"
    }

    override fun getCount(): Int {
        return 3
    }
}
