package featurea.app;

import featurea.opengl.Render;

public class Application {

  private final MediaPlayer mediaPlayer;
  public Screen screen;

  public Application(MediaPlayer mediaPlayer) {
    this.mediaPlayer = mediaPlayer;
  }

  public void onCreate() {
    mediaPlayer.render.onCreate();
    mediaPlayer.audio.onCreate();
  }

  public void onStart() {
    mediaPlayer.render.onStart();
  }

  public void onResume() {
    if (screen != null) {
      screen.onResume();
    }
  }

  public void onPause() {
    if (screen != null) {
      screen.onPause();
    }
  }

  public void onStop() {
    mediaPlayer.render.onStop();
  }

  public void onDestroy() {
    mediaPlayer.loader.clear();
    mediaPlayer.render.onDestroy();
    mediaPlayer.audio.onDestroy();
  }

  public void onTick(double elapsedTime) {
    mediaPlayer.performance.onStartApplicationTick(elapsedTime);
    mediaPlayer.timer.setElapsedTime(elapsedTime);

    Loader.Asset asset = mediaPlayer.loader.nextAsset();
    if (asset != null) {
      mediaPlayer.loader.load(asset);
    } else {
      mediaPlayer.loader.clear(); // after clearing Loader is empty
    }

    if (screen != null) {
      screen.onTraverseTick();
      screen.onResize(mediaPlayer.render.size);
      if (mediaPlayer.loader.isEmpty()) {
        double currentElapsedTime = mediaPlayer.timer.getElapsedTime();
        if (currentElapsedTime != 0) {
          screen.onTick(currentElapsedTime);
          mediaPlayer.timer.onTick(currentElapsedTime);
        }
      }
    }

    mediaPlayer.input.clear(screen);
    mediaPlayer.performance.onFinishApplicationTick();
  }

  public void onInput() {
    if (screen != null) {
      mediaPlayer.input.flushAndUpdate(screen);
    }
  }

  public void onDrawBackground() {
    Context.getRender().clearBackground();
    if (screen != null) {
      screen.onDrawBackground();
    }
  }

  public void onDraw() {
    if (screen != null) {
      screen.onTraverseDraw();
      screen.onZoom();
      screen.onDraw(mediaPlayer.render);
    }
  }

  public void onResize(int width, int height) {
    Render render = mediaPlayer.render;
    if (render.size.width != width || render.size.height != height) {
      render.resize(width, height);
      if (screen != null) {
        screen.onResize(width, height);
      }
    }
  }

  public void exit() {
    mediaPlayer.render.window.close();
  }

}
