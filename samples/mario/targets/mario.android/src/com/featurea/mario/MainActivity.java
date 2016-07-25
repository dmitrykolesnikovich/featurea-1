package com.featurea.mario;

import featurea.android.FeatureaActivity;
import featurea.app.MediaPlayer;
import mario.Navigation;

public class MainActivity extends FeatureaActivity {

  @Override
  public void onCreateMediaPlayer(MediaPlayer mediaPlayer) {
    super.onCreateMediaPlayer(mediaPlayer);
    Navigation.entryPoint(mediaPlayer);
  }

}
