package mario.objects.landscape;

import featurea.app.Context;
import featurea.platformer.overlap.Overlap;
import featurea.platformer.physics.Body;
import featurea.platformer.Animation;
import mario.Sprites;
import mario.config.Gameplay;

public class Trampoline extends Body {

  private static final double width = Sprites.Items.trampolineWidth;
  private static final double height = Sprites.Items.trampolineHeight;

  private double resistance;
  private double force;
  private boolean hasLoad;

  public Trampoline() {
    setSize(width, height);
    setRectangle(0, 0, width, height);
    setSolid(true);
    setSprite(Sprites.Items.Trampoline._0());
  }

  public void setResistance(double resistance) {
    this.resistance = resistance;
  }

  public void setForce(double force) {
    this.force = force;
  }

  @Override
  public void onIntersect(final Animation animation, Overlap.X horizontal, Overlap.Y vertical) {
    if (animation instanceof Body) {
      final Body body = (Body) animation;
      if (vertical == Overlap.Y.top) {
        if (!hasLoad) {
          hasLoad = true;
          Context.getTimer().delay(new Runnable() {
            @Override
            public void run() {
              if (getLoads().contains(body)) {
                startBodyJump(body);
              } else {
                hasLoad = false;
              }
            }
          }, Gameplay.squeezeWaitTime);
        }
      }
    }

  }

  private void startBodyJump(final Body body) {
    addTask(new Runnable() {
      @Override
      public void run() {
        setRectangle(0, 8, width, height);
        setSprite(Sprites.Items.Trampoline._1());
        if (getLoads().contains(body)) {
          body.setBottom(top());
          addTask(new Runnable() {
            @Override
            public void run() {
              setRectangle(0, 16, width, height);
              setSprite(Sprites.Items.Trampoline._2());
              if (getLoads().contains(body)) {
                body.setBottom(top());
                addTask(new Runnable() {
                  @Override
                  public void run() {
                    stopSqueeze(body);
                  }
                });
              } else {
                stopSqueeze(body);
              }
            }
          });
        } else {
          stopSqueeze(body);
        }
      }
    });
  }

  private void stopSqueeze(Body body) {
    setRectangle(0, 0, width, height);
    setSprite(Sprites.Items.Trampoline._0());
    if (getLoads().contains(body)) {
      body.setBottom(top());
      body.longJump();
    }
    hasLoad = false;
  }

  private static void addTask(Runnable task) {
    Context.getTimer().delay(task, Gameplay.squeezeTime);
  }

}
