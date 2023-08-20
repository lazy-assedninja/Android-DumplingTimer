package me.lazy_assedninja.android_dumpling_timer.util

import android.content.Context
import android.media.SoundPool
import me.lazy_assedninja.android_dumpling_timer.R

class SoundEffectUtils(val context: Context) {

    companion object {
        private const val VOLUME = 0.8F
    }

    private val soundPool = SoundPool.Builder().setMaxStreams(1).build()

    private val soundWarning = soundPool.load(context, R.raw.warning, 0)

    fun playWarningSound() {
        play(soundWarning)
    }

    fun release() {
        soundPool.release()
    }

    private fun play(soundID: Int) {
        soundPool.play(soundID, VOLUME, VOLUME, 0, 0, 1F)
    }
}