package mario.objects.enemies;

import featurea.app.Context;
import featurea.motion.Motion;
import featurea.motion.Movement;
import mario.Sprites;
import mario.config.Gameplay;

public class PiranhaPlant extends Enemy {

  private static final double width = Sprites.Enemies.piranhaPlantWidth;
  private static final double height = Sprites.Enemies.piranhaPlantHeight;
  private static final double bounceHeight = height - 2;
  private double top;

  public PiranhaPlant() {
    setMass(0);
    setFps(6);
    setSize(width, height);
    setRectangle(0, 0, width, height);
    setMotionableHorizontally(false);
  }

  private void bounceRecursively() {
    Motion motion = new Movement() {
      @Override
      public void onTick(double elapsedTime) {
        super.onTick(elapsedTime);
        redraw();
      }
    }.setGraph(0, -bounceHeight, 0, 0, bounceHeight, 0).setVelocity(0.02);
    motion.onStop = new Runnable() {
      @Override
      public void run() {
        setTop(top);
        Context.getTimer().delay(new Runnable() {
          @Override
          public void run() {
            bounceRecursively();
          }
        }, Gameplay.piranhaPlantWaitTime);
      }
    };
    add(motion);
  }

  @Override
  public PiranhaPlant build() {
    super.build();
    top = top();
    bounceRecursively();
    return this;
  }

  @Override
  public String onUpdateSprite() {
    return Sprites.Enemies.piranhaPlant();
  }

  @Override
  public void removeSelf() {
    super.removeSelf();
  }
}
