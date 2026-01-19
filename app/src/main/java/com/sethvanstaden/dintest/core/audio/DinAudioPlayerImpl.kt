package com.sethvanstaden.dintest.core.audio

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.coroutines.resume

class DinAudioPlayerImpl(
    private val appContext: Context,
    private val digitStartDelayMs: Long = 500L,
    private val betweenDigitsDelayMs: Long = 250L,
    private val noiseEndDelay: Long = 500L
) : DinAudioPlayer {

    private val noisePlayer: ExoPlayer = ExoPlayer.Builder(appContext).build().apply {
        repeatMode = Player.REPEAT_MODE_ONE
        playWhenReady = false
    }

    private val digitPlayer: ExoPlayer = ExoPlayer.Builder(appContext).build().apply {
        repeatMode = Player.REPEAT_MODE_OFF
        playWhenReady = false
    }

    private val isPlaying = AtomicBoolean(false)

    override suspend fun playRound(noiseResId: Int, digitResIds: List<Int>) {
        require(digitResIds.size == 3) { "Expected 3 digit clips, got ${digitResIds.size}" }

        if (!isPlaying.compareAndSet(false, true)) {
            stopAll()
            isPlaying.set(true)
        }

        try {
            startNoise(noiseResId)

            delay(digitStartDelayMs)

            playDigitAndAwaitEnd(digitResIds[0])
            delay(betweenDigitsDelayMs)

            playDigitAndAwaitEnd(digitResIds[1])
            delay(betweenDigitsDelayMs)

            playDigitAndAwaitEnd(digitResIds[2])

            delay(noiseEndDelay)
        } catch (ce: CancellationException) {
            stopAll()
            throw ce
        } catch (t: Throwable) {
            stopAll()
            throw t
        } finally {
            stopAll()
            isPlaying.set(false)
        }
    }

    override fun stopAll() {
        noisePlayer.run {
            playWhenReady = false
            stop()
            clearMediaItems()
        }
        digitPlayer.run {
            playWhenReady = false
            stop()
            clearMediaItems()
        }
    }

    override fun release() {
        stopAll()
        noisePlayer.release()
        digitPlayer.release()
    }

    private fun startNoise(noiseResId: Int) {
        val item = MediaItem.fromUri(rawResToUri(noiseResId))
        noisePlayer.setMediaItem(item)
        noisePlayer.prepare()
        noisePlayer.playWhenReady = true
    }

    private suspend fun playDigitAndAwaitEnd(digitResId: Int) {
        val item = MediaItem.fromUri(rawResToUri(digitResId))
        digitPlayer.setMediaItem(item)
        digitPlayer.prepare()
        digitPlayer.playWhenReady = true
        awaitEnded(digitPlayer)
    }

    private fun rawResToUri(resId: Int): String {
        return "android.resource://${appContext.packageName}/$resId"
    }

    private suspend fun awaitEnded(player: Player) {
        if (player.playbackState == Player.STATE_ENDED) return

        suspendCancellableCoroutine<Unit> { cont ->
            val listener = object : Player.Listener {
                override fun onPlaybackStateChanged(state: Int) {
                    if (state == Player.STATE_ENDED && cont.isActive) {
                        cont.resume(Unit)
                    }
                }
            }

            player.addListener(listener)

            cont.invokeOnCancellation {
                player.removeListener(listener)
            }
        }
    }
}
