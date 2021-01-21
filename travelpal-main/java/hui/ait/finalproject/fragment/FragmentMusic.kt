package hui.ait.finalproject.fragment

import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import hui.ait.finalproject.R
import kotlinx.android.synthetic.main.fragment_music.*

class FragmentMusic : Fragment(), MediaPlayer.OnPreparedListener {

    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(
            R.layout.fragment_music,
            container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvWelcomeMusic.text = getString(R.string.recommended_music) + "\nfor " + getString(R.string.budapest_ex)
        btnRingtone.setOnClickListener {
            playNotifTone()
        }

        btnStart.setOnClickListener {
            mediaPlayer = MediaPlayer.create(activity, R.raw.demo)
            mediaPlayer.setOnPreparedListener(this@FragmentMusic)
        }

        btnSeek.setOnClickListener {
            mediaPlayer.seekTo(61000)
        }

        btnStop.setOnClickListener {
            mediaPlayer.stop()
        }
    }
    private fun playNotifTone() {
        val uriNotif = RingtoneManager.getDefaultUri(
            RingtoneManager.TYPE_NOTIFICATION
        )
        val r = RingtoneManager.getRingtone(
            activity?.applicationContext, uriNotif
        )
        r.play()
    }

    override fun onPrepared(player: MediaPlayer) {
        mediaPlayer.start()
    }

    override fun onStop() {
        try {
            mediaPlayer?.stop()
        }catch (e: Exception){
            e.printStackTrace()
        }
        super.onStop()
    }

    companion object {
        val TAG = "FragmentMusic"
    }
}