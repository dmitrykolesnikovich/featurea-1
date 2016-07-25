package com.featurea.mario;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.featurea.mario2.R;
import featurea.app.Context;
import featurea.app.Screen;
import mario.Tests;

public class LevelsActivity extends Activity {

  private ListView listView;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_levels);
    listView = (ListView) findViewById(R.id.levels);
    listView.setAdapter(new ArrayAdapter<Screen>(this, android.R.layout.simple_list_item_1) {
      @Override
      public View getView(final int position, View convertView, ViewGroup parent) {
        TextView textView = (TextView) LayoutInflater.from(getContext()).
            inflate(android.R.layout.simple_list_item_1, parent, false).findViewById(android.R.id.text1);
        textView.setText(position + "");
        textView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            setLevel(position);
          }
        });
        return textView;
      }

      @Override
      public int getCount() {
        return Tests.size();
      }

      @Override
      public Screen getItem(int position) {
        return Tests.getLevel(position);
      }
    });
  }

  private void setLevel(int position) {
    Screen screen = Tests.getLevel(position);
    if (screen == null) {
      System.out.println("breakpoint");
    }
    Context.getApplication().screen = screen;
    finish();
  }

}
