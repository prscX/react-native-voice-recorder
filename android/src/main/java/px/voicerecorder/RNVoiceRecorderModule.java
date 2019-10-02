
package px.voicerecorder;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Environment;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReadableMap;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.List;

import cafe.adriel.androidaudiorecorder.AndroidAudioRecorder;
import cafe.adriel.androidaudiorecorder.model.AudioChannel;
import cafe.adriel.androidaudiorecorder.model.AudioSampleRate;
import cafe.adriel.androidaudiorecorder.model.AudioSource;

public class RNVoiceRecorderModule extends ReactContextBaseJavaModule {

  private Callback _onDone;
  private Callback _onCancel;

  public RNVoiceRecorderModule(ReactApplicationContext reactContext) {
    super(reactContext);
    getReactApplicationContext().addActivityEventListener(new ActivityEventListener());
  }

  @Override
  public String getName() {
    return "RNVoiceRecorder";
  }


  @ReactMethod
  public void Record(final ReadableMap props, final Callback onDone, final Callback onCancel) {

    _onDone = onDone;
    _onCancel = onCancel;

    Dexter.withActivity(getCurrentActivity())
            .withPermissions(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.RECORD_AUDIO
            ).withListener(new MultiplePermissionsListener() {
      @Override public void onPermissionsChecked(MultiplePermissionsReport report) {/* ... */

        String filePath = Environment.getExternalStorageDirectory() + "/recorded_audio.wav";

        int color = Color.parseColor("#165297");

        int requestCode = 0;
        AndroidAudioRecorder.with(getCurrentActivity())
                // Required
                .setFilePath(filePath)
                .setColor(color)
                .setRequestCode(requestCode)

                // Optional
                .setSource(AudioSource.CAMCORDER.MIC)
                .setChannel(AudioChannel.STEREO)
                .setSampleRate(AudioSampleRate.HZ_48000)
                .setAutoStart(true)
                .setKeepDisplayOn(true)

                // Start recording
                .record();
      }
      @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
    }).check();
  }


  @ReactMethod
  public void Play(final ReadableMap props, final Callback onDone, final Callback onCancel) {

    _onDone = onDone;
    _onCancel = onCancel;

    Dexter.withActivity(getCurrentActivity())
            .withPermissions(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.RECORD_AUDIO
            ).withListener(new MultiplePermissionsListener() {
      @Override public void onPermissionsChecked(MultiplePermissionsReport report) {/* ... */

        String filePath = props.getString("path");

        int color = Color.parseColor("#165297");

        int requestCode = 0;
        AudioPlay.with(getCurrentActivity())
                // Required
                .setFilePath(filePath)
                .setColor(color)
                .setRequestCode(requestCode)

                // Optional
                .setSource(AudioSource.CAMCORDER.MIC)
                .setChannel(AudioChannel.STEREO)
                .setSampleRate(AudioSampleRate.HZ_48000)
                .setAutoStart(true)
                .setKeepDisplayOn(true)

                // Start recording
                .record();
      }
      @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
    }).check();
  }



  private class ActivityEventListener implements com.facebook.react.bridge.ActivityEventListener {

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {

      if (resultCode == AppCompatActivity.RESULT_OK) {
        String filePath = Environment.getExternalStorageDirectory() + "/recorded_audio.wav";
        
        if (_onDone != null) {
          _onDone.invoke(filePath);
        }

        _onDone = null;
      } else if (resultCode == AppCompatActivity.RESULT_CANCELED) {
        if (_onCancel != null) {
          _onCancel.invoke();
        }

        _onCancel = null;
      }
    }

    @Override
    public void onNewIntent(Intent intent) {

    }
  }

}