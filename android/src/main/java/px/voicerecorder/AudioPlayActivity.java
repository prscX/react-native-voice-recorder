//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package px.voicerecorder;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cleveroad.audiovisualization.DbmHandler;
import com.cleveroad.audiovisualization.GLAudioVisualizationView;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import cafe.adriel.androidaudiorecorder.Util;
import cafe.adriel.androidaudiorecorder.VisualizerHandler;
import cafe.adriel.androidaudiorecorder.model.AudioChannel;
import cafe.adriel.androidaudiorecorder.model.AudioSampleRate;
import cafe.adriel.androidaudiorecorder.model.AudioSource;
import omrecorder.AudioChunk;
import omrecorder.OmRecorder;
import omrecorder.PullTransport;
import omrecorder.Recorder;

public class AudioPlayActivity extends AppCompatActivity
        implements PullTransport.OnAudioChunkPulledListener, MediaPlayer.OnCompletionListener {

    private String filePath;
    private AudioSource source;
    private AudioChannel channel;
    private AudioSampleRate sampleRate;
    private int color;
    private boolean autoStart;
    private boolean keepDisplayOn;

    private MediaPlayer player;
    private Recorder recorder;
    private VisualizerHandler visualizerHandler;

    private Timer timer;
    private MenuItem saveMenuItem;
    private int recorderSecondsElapsed;
    private int playerSecondsElapsed;

    private RelativeLayout contentLayout;
    private GLAudioVisualizationView visualizerView;
    private TextView statusView;
    private TextView timerView;
    protected ImageButton restartView;
    protected ImageButton recordView;
    protected ImageButton playView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(cafe.adriel.androidaudiorecorder.R.layout.aar_activity_audio_recorder);

        if(savedInstanceState != null) {
            filePath = savedInstanceState.getString(AudioPlay.EXTRA_FILE_PATH);
            source = (AudioSource) savedInstanceState.getSerializable(AudioPlay.EXTRA_SOURCE);
            channel = (AudioChannel) savedInstanceState.getSerializable(AudioPlay.EXTRA_CHANNEL);
            sampleRate = (AudioSampleRate) savedInstanceState.getSerializable(AudioPlay.EXTRA_SAMPLE_RATE);
            color = savedInstanceState.getInt(AudioPlay.EXTRA_COLOR);
            autoStart = savedInstanceState.getBoolean(AudioPlay.EXTRA_AUTO_START);
            keepDisplayOn = savedInstanceState.getBoolean(AudioPlay.EXTRA_KEEP_DISPLAY_ON);
        } else {
            filePath = getIntent().getStringExtra(AudioPlay.EXTRA_FILE_PATH);
            source = (AudioSource) getIntent().getSerializableExtra(AudioPlay.EXTRA_SOURCE);
            channel = (AudioChannel) getIntent().getSerializableExtra(AudioPlay.EXTRA_CHANNEL);
            sampleRate = (AudioSampleRate) getIntent().getSerializableExtra(AudioPlay.EXTRA_SAMPLE_RATE);
            color = getIntent().getIntExtra(AudioPlay.EXTRA_COLOR, Color.BLACK);
            autoStart = getIntent().getBooleanExtra(AudioPlay.EXTRA_AUTO_START, false);
            keepDisplayOn = getIntent().getBooleanExtra(AudioPlay.EXTRA_KEEP_DISPLAY_ON, false);
        }

        if(keepDisplayOn){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setBackgroundDrawable(
                    new ColorDrawable(Util.getDarkerColor(color)));
            getSupportActionBar().setHomeAsUpIndicator(
                    ContextCompat.getDrawable(this, cafe.adriel.androidaudiorecorder.R.drawable.aar_ic_clear));
        }

        visualizerView = new GLAudioVisualizationView.Builder(this)
                .setLayersCount(1)
                .setWavesCount(6)
                .setWavesHeight(cafe.adriel.androidaudiorecorder.R.dimen.aar_wave_height)
                .setWavesFooterHeight(cafe.adriel.androidaudiorecorder.R.dimen.aar_footer_height)
                .setBubblesPerLayer(20)
                .setBubblesSize(cafe.adriel.androidaudiorecorder.R.dimen.aar_bubble_size)
                .setBubblesRandomizeSize(true)
                .setBackgroundColor(Util.getDarkerColor(color))
                .setLayerColors(new int[]{color})
                .build();

        contentLayout = (RelativeLayout) findViewById(cafe.adriel.androidaudiorecorder.R.id.content);
        statusView = (TextView) findViewById(cafe.adriel.androidaudiorecorder.R.id.status);
        timerView = (TextView) findViewById(cafe.adriel.androidaudiorecorder.R.id.timer);
        restartView = (ImageButton) findViewById(cafe.adriel.androidaudiorecorder.R.id.restart);
        recordView = (ImageButton) findViewById(R.id.record);
        playView = (ImageButton) findViewById(cafe.adriel.androidaudiorecorder.R.id.play);

        contentLayout.setBackgroundColor(Util.getDarkerColor(color));
        contentLayout.addView(visualizerView, 0);
        restartView.setVisibility(View.INVISIBLE);
        recordView.setVisibility(View.INVISIBLE);
        playView.setVisibility(View.VISIBLE);

        ((ViewGroup) restartView.getParent()).removeView(restartView);
        ((ViewGroup) recordView.getParent()).removeView(recordView);

        RelativeLayout.LayoutParams layoutParams =
                (RelativeLayout.LayoutParams) playView.getLayoutParams();
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);

        if(Util.isBrightColor(color)) {
            ContextCompat.getDrawable(this, cafe.adriel.androidaudiorecorder.R.drawable.aar_ic_clear)
                    .setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
            ContextCompat.getDrawable(this, cafe.adriel.androidaudiorecorder.R.drawable.aar_ic_check)
                    .setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
            statusView.setTextColor(Color.BLACK);
            timerView.setTextColor(Color.BLACK);
            restartView.setColorFilter(Color.BLACK);
            recordView.setColorFilter(Color.BLACK);
            playView.setColorFilter(Color.BLACK);
        }
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if(autoStart && !isPlaying()){
            togglePlaying(null);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            visualizerView.onResume();
        } catch (Exception e){ }
    }

    @Override
    protected void onPause() {
        try {
            visualizerView.onPause();
        } catch (Exception e){ }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        setResult(RESULT_CANCELED);
        try {
            visualizerView.release();
        } catch (Exception e){ }
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(AudioPlay.EXTRA_FILE_PATH, filePath);
        outState.putInt(AudioPlay.EXTRA_COLOR, color);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(cafe.adriel.androidaudiorecorder.R.menu.aar_audio_recorder, menu);
        saveMenuItem = menu.findItem(cafe.adriel.androidaudiorecorder.R.id.action_save);
        saveMenuItem.setIcon(ContextCompat.getDrawable(this, cafe.adriel.androidaudiorecorder.R.drawable.aar_ic_check));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == android.R.id.home) {
            finish();
        } else if (i == cafe.adriel.androidaudiorecorder.R.id.action_save) {
            selectAudio();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAudioChunkPulled(AudioChunk audioChunk) {
        float amplitude = 0f;
        visualizerHandler.onDataReceived(amplitude);
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        stopPlaying();
    }

    private void selectAudio() {
        setResult(RESULT_OK);
        finish();
    }

    public void togglePlaying(View v){
        Util.wait(100, new Runnable() {
            @Override
            public void run() {
                if(isPlaying()){
                    stopPlaying();
                } else {
                    startPlaying();
                }
            }
        });
    }

    protected void startPlaying(){
        try {
            player = new MediaPlayer();
            player.setDataSource(filePath);
            player.prepare();
            player.start();

            visualizerView.linkTo(DbmHandler.Factory.newVisualizerHandler(this, player));
            visualizerView.post(new Runnable() {
                @Override
                public void run() {
                    player.setOnCompletionListener(AudioPlayActivity.this);
                }
            });

            timerView.setText("00:00:00");
            statusView.setText(cafe.adriel.androidaudiorecorder.R.string.aar_playing);
            statusView.setVisibility(View.VISIBLE);
            playView.setImageResource(cafe.adriel.androidaudiorecorder.R.drawable.aar_ic_stop);

            playerSecondsElapsed = 0;
            startTimer();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    protected void stopPlaying(){
        statusView.setText("");
        statusView.setVisibility(View.INVISIBLE);
        playView.setImageResource(cafe.adriel.androidaudiorecorder.R.drawable.aar_ic_play);

        visualizerView.release();
        if(visualizerHandler != null) {
            visualizerHandler.stop();
        }

        if(player != null){
            try {
                player.stop();
                player.reset();
            } catch (Exception e){ }
        }

        stopTimer();
    }

    private boolean isPlaying(){
        try {
            return player != null && player.isPlaying();
        } catch (Exception e){
            return false;
        }
    }

    private void startTimer(){
        stopTimer();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateTimer();
            }
        }, 0, 1000);
    }

    private void stopTimer(){
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }

    private void updateTimer() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                    playerSecondsElapsed++;
                    timerView.setText(Util.formatSeconds(playerSecondsElapsed));
            }
        });
    }
}