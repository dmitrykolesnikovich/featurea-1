package com.featurea.mario;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.featurea.mario2.R;
import featurea.platformer.config.Engine;
import featurea.util.Config;
import featurea.util.ConfigUtil;
import mario.config.Gameplay;

public class ConfigActivity extends Activity {

  private static final String CONFIG_KEY = "config";
  private LinearLayout contentView;
  private Config config;

  public static void startActivity(Activity activity, String config) {
    Intent intent = new Intent(activity, ConfigActivity.class);
    intent.putExtra(CONFIG_KEY, config);
    activity.startActivity(intent);
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_config);
    contentView = (LinearLayout) findViewById(R.id.contentView);
    config = new Config(getIntent().getStringExtra(CONFIG_KEY));
    inflateScrollViewWithConfig();
  }

  private void inflateScrollViewWithConfig() {
    for (final String key : config.getKeys()) {
      String value = config.getValue(key);
      LinearLayout row = new LinearLayout(this);
      TextView keyView = new TextView(this);
      keyView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.5f));
      keyView.setText(key);
      row.addView(keyView);
      EditText valueView = new EditText(this);
      valueView.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.5f));
      valueView.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
          // no op
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
          // no op
        }

        @Override
        public void afterTextChanged(Editable s) {
          config.map.put(key, s.toString());
        }
      });
      valueView.setText(value);
      row.addView(valueView);
      contentView.addView(row);
    }
  }

  @Override
  protected void onPause() {
    super.onPause();
    ConfigUtil.save(config, config.file);
    ConfigUtil.load(config, Gameplay.class);
    ConfigUtil.load(config, Engine.class);
  }

}