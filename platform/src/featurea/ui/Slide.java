package featurea.ui;

import featurea.app.Layer;
import featurea.app.Context;
import featurea.geometry.Shape;

public abstract class Slide extends Layer {

  boolean isClose;

  public abstract Shape[] getShapes();

  public abstract void next();

  public abstract void prev();

  public Slide() {
  }

  public long timeToClose;
  private long currentTime;

  @Override
  public void onTick(double elapsedTime) {
    super.onTick(elapsedTime);
    if (isClose) {
      currentTime += elapsedTime;
      if (currentTime >= timeToClose) {
        Context.getTimer().delay(new Runnable() {
          @Override
          public void run() {
            removeSelf();
          }
        });
      }
    }
  }

  void start() {
    for (Shape child : getShapes()) {
    }
  }
}
