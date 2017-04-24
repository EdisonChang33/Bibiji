package com.bibizhaoji.bibiji.media;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

import com.bibizhaoji.bibiji.utils.ChannelUtils;
import com.speech.utils.ContextUtils;

/**
 * Created by EdisonChang on 2016/6/30.
 */
public class MediaHelper {

    private static MediaPlayer mediaPlayer;
    private static AudioManager audioManager;

    private static int originalVol;
    private static int maximalVol;

    public static boolean playing;

    private static void init(Context context) {
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    public static void playSound(Context context, int soundResourceId) {
        if (playing) {
            return;
        }
        playing = true;
        originalVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        maximalVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maximalVol, 0);

        mediaPlayer = MediaPlayer.create(context, soundResourceId);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    public static void stopSound() {
        playing = false;
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, originalVol, 0);
        }
    }

    static {
        init(ContextUtils.getApplicationContext());
    }
}
