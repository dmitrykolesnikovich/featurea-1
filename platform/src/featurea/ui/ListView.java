package featurea.ui;

import featurea.app.Camera;
import featurea.app.Layer;
import featurea.input.InputAdapter;
import featurea.motion.Movement;
import featurea.motion.Timeline;
import featurea.motion.Tweens;
import featurea.util.TransformMove;

public class ListView extends Layer implements TransformMove {

  private final Timeline timeline = new Timeline(this);

  public ListView() {
    inputListeners.add(new InputAdapter() {

      private double currentY = -1;

      @Override
      public boolean onTouchDown(double x, double y, int id) {
        currentY = y;
        return false;
      }

      @Override
      public boolean onTouchDrag(double x, double y, int id) {
        System.out.println("onTouchDrag: " + y);
        double dy = y - currentY;
        currentY = y;
        timeline.add(new Movement().setGraph(0, dy * 1.1, 0).setVelocity(0.02).setTween(Tweens.square));
        return false;
      }
    });
  }

  @Override
  public void move(double dx, double dy, double dz) {
    Camera camera = getCamera();
    camera.move(0, -dy, 0);
  }

  @Override
  public void onTick(double elapsedTime) {
    super.onTick(elapsedTime);
    timeline.onTick(elapsedTime);
  }

}
