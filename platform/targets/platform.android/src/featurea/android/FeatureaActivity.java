package featurea.android;

import android.annotation.TargetApi;
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import featurea.app.Context;
import featurea.app.MediaPlayer;
import featurea.input.Key;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class FeatureaActivity extends Activity implements SensorEventListener, featurea.graphics.Window {

  private MediaPlayer mediaPlayer;
  private SensorManager mSensorManager;
  private Sensor mAccelerometer;
  public MySurfaceView mySurfaceView;

  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    mySurfaceView = new MySurfaceView(this);
    mySurfaceView.setRenderer(new MyRender(this));
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
      mySurfaceView.setPreserveEGLContextOnPause(true);
    }
    onCreateContentView(mySurfaceView);
  }

  protected void onCreateContentView(View contentView) {
    setContentView(contentView);
  }

  @Override
  protected void onStart() {
    super.onStart();
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
      if (mediaPlayer != null) {
        Context.mediaPlayers.put(Thread.currentThread(), mediaPlayer);
        mediaPlayer.app.onStart();
      }
    }
  }

  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  @Override
  protected void onResume() {
    super.onResume();
    mySurfaceView.onResume();
    mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
      if (mediaPlayer != null) {
        Context.mediaPlayers.put(Thread.currentThread(), mediaPlayer);
        mediaPlayer.app.onStart();
      }
    }
    if (mediaPlayer != null) {
      Context.mediaPlayers.put(Thread.currentThread(), mediaPlayer);
      mediaPlayer.app.onResume();
    }
  }

  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  @Override
  protected void onPause() {
    super.onPause();
    mySurfaceView.onPause();
    mSensorManager.unregisterListener(this);
    if (mediaPlayer != null) {
      Context.mediaPlayers.put(Thread.currentThread(), mediaPlayer);
      mediaPlayer.app.onPause();
    }
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
      if (mediaPlayer != null) {
        Context.mediaPlayers.put(Thread.currentThread(), mediaPlayer);
        mediaPlayer.app.onStop();
      }
    }
  }

  @Override
  protected void onStop() {
    super.onStop();
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
      if (mediaPlayer != null) {
        Context.mediaPlayers.put(Thread.currentThread(), mediaPlayer);
        mediaPlayer.app.onStop();
      }
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (mediaPlayer != null) {
      Context.mediaPlayers.put(Thread.currentThread(), mediaPlayer);
      mediaPlayer.app.onDestroy();
    }
  }

  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  @Override
  public void onSensorChanged(SensorEvent event) {
    double x = event.values[0];
    double y = -event.values[1];
    double z = event.values[2];
    if (mediaPlayer != null) {
      Context.mediaPlayers.put(Thread.currentThread(), mediaPlayer);
      /*featureaContext.input.accelerometer.setValue(x, y, z);*/
    }
  }

  @Override
  public void onAccuracyChanged(Sensor sensor, int accuracy) {
    // no op
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    if (mediaPlayer != null) {
      Context.mediaPlayers.put(Thread.currentThread(), mediaPlayer);
      mediaPlayer.input.keyboard.keyDown(Key.BACK);
    }
  }

  @Override
  public void close() {
    finish();
  }

  /**
   * Called when the media player is first created.
   */
  public void onCreateMediaPlayer(MediaPlayer mediaPlayer) {
    this.mediaPlayer = mediaPlayer;
    this.mediaPlayer.render.window = this;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
      mediaPlayer.render.isReleaseTexturesOnPause = false;
    }
  }

  public MediaPlayer getMediaPlayer() {
    return mediaPlayer;
  }

  /*app lifecycle*/

  protected void onTick(double elapsedTime) {
    mediaPlayer.app.onTick(elapsedTime);
  }


}
