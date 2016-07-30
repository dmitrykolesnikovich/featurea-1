package com.featurea.mario;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.featurea.mario2.R;
import featurea.android.FeatureaActivity;
import featurea.app.Context;
import featurea.app.MediaPlayer;
import mario.Navigation;

public class MainActivity extends FeatureaActivity {

  private ViewGroup fps_layout;
  private TextView fpsText;

  @Override
  public void onCreateMediaPlayer(MediaPlayer mediaPlayer) {
    super.onCreateMediaPlayer(mediaPlayer);
    Navigation.entryPoint(mediaPlayer);
  }

  @Override
  protected void onCreateContentView(View contentView) {
    fps_layout = (ViewGroup) getLayoutInflater().inflate(R.layout.fps_layout, null);
    fps_layout.addView(contentView);
    fpsText = (TextView) fps_layout.findViewById(R.id.fpsText);
    super.onCreateContentView(fps_layout);
  }

  long lifeTime;

  @Override
  protected void onTick(double elapsedTime) {
    super.onTick(elapsedTime);
    lifeTime += elapsedTime;
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        if (lifeTime > 2000) {
          fpsText.setText("FPS: " + Context.getPerformance().fps);
          lifeTime %= 2000;
        }
      }
    });
  }

}
