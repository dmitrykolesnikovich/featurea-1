package com.featurea.mario;

import android.content.Intent;
import featurea.android.FeatureaActivity;
import featurea.platformer.util.Joystick;
import mario.Navigation;

public class MainActivity extends FeatureaActivity {

  @Override
  public void onCreateFeatureaContext(featurea.app.Context featureaContext) {
    super.onCreateFeatureaContext(featureaContext);
    Navigation.entryPoint(featureaContext);
    Joystick.buttonSettingsClick = new Runnable() {
      @Override
      public void run() {
        startActivity(new Intent(MainActivity.this, LevelsActivity.class));
      }
    };
  }

}
