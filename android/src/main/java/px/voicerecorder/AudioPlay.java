package px.voicerecorder;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import androidx.fragment.app.Fragment;

import cafe.adriel.androidaudiorecorder.AndroidAudioRecorder;
import cafe.adriel.androidaudiorecorder.AudioRecorderActivity;
import cafe.adriel.androidaudiorecorder.model.AudioChannel;
import cafe.adriel.androidaudiorecorder.model.AudioSampleRate;
import cafe.adriel.androidaudiorecorder.model.AudioSource;

public class AudioPlay extends AndroidAudioRecorder {
    public static final String EXTRA_FILE_PATH = "filePath";
    public static final String EXTRA_COLOR = "color";
    public static final String EXTRA_SOURCE = "source";
    public static final String EXTRA_CHANNEL = "channel";
    public static final String EXTRA_SAMPLE_RATE = "sampleRate";
    public static final String EXTRA_AUTO_START = "autoStart";
    public static final String EXTRA_KEEP_DISPLAY_ON = "keepDisplayOn";

    private AudioPlay(Activity activity) {
        super(activity);
    }

    private AudioPlay(Fragment fragment) {
        super(fragment);
    }

    public static AudioPlay with(Activity activity) {
        return new AudioPlay(activity);
    }

    public static AudioPlay with(Fragment fragment) {
        return new AudioPlay(fragment);
    }


    public void record() {
        Intent intent = new Intent(activity, AudioPlayActivity.class);
        intent.putExtra(EXTRA_FILE_PATH, filePath);
        intent.putExtra(EXTRA_COLOR, color);
        intent.putExtra(EXTRA_SOURCE, source);
        intent.putExtra(EXTRA_CHANNEL, channel);
        intent.putExtra(EXTRA_SAMPLE_RATE, sampleRate);
        intent.putExtra(EXTRA_AUTO_START, autoStart);
        intent.putExtra(EXTRA_KEEP_DISPLAY_ON, keepDisplayOn);
        activity.startActivityForResult(intent, requestCode);
    }
}
