package mario.layers;

import featurea.graphics.Graphics;
import featurea.motion.Motion;
import featurea.motion.Movement;
import featurea.motion.Tweens;
import featurea.util.Color;
import mario.Sprites;
import mario.config.Gameplay;
import mario.objects.landscape.Platto;

public class PlattoLiftLayer extends BodyLayer {



  public enum Direction {
    up, down, left, right
  }

  private double[] plattos;
  private Direction direction;
  private boolean hasLine;

  public void setPlattos(double[] plattos) {
    this.plattos = plattos;
  }

  public void setDirection(Direction direction) {
    this.direction = direction;
  }

  public void setHasLine(boolean hasLine) {
    this.hasLine = hasLine;
  }

  @Override
  public PlattoLiftLayer build() {
    if (plattos != null) {
      for (int i = 0; i < plattos.length; i += 4) {
        double x1 = plattos[i];
        double y1 = plattos[i + 1];
        double x2 = plattos[i + 2];
        int length = (int) ((x2 - x1) / Sprites.Items.plattoWidth);
        addPlatto(x1, y1, length);
      }
    }
    return this;
  }

  private void addPlatto(double x, double y, int length) {
    Platto platto = new Platto() {
      @Override
      public void onTick(double elapsedTime) {
        super.onTick(elapsedTime);
        int y1 = -4;
        int y2 = (int) (getLayer().getCamera().height() + 4);
        if (top() >= y2) {
          setBottom(y1);
        } else if (bottom() < y1) {
          setTop(y2);
        }
      }

      private boolean isFirst = true;

      @Override
      public boolean shouldBeSwipedFromBuffer() {
        boolean beSwipedFromBuffer = super.shouldBeSwipedFromBuffer();
        if (isFirst) {
          if (beSwipedFromBuffer) {
            isFirst = false;
            switch (direction) {
              case up: {
                velocity.y = Gameplay.liftVelocity;
                break;
              }
              case down: {
                velocity.y = -Gameplay.liftVelocity;
                setMotionableHorizontally(false);
                break;
              }
              case left: {
                timeline.add(getHorizontalListMotion(-width()));
                break;
              }
              case right: {
                timeline.add(getHorizontalListMotion(width()));
                break;
              }
            }
          }
        }
        return beSwipedFromBuffer;
      }
    };
    platto.setPosition(x, y);
    platto.setCount(length);
    platto.build();

    // >> IMPORTANT
    platto.setLifeDistance(platto.right());
    if (direction != null) {
      platto.setMotionableVertically(direction == Direction.up || direction == Direction.down);
      platto.setMotionableHorizontally(direction == Direction.left || direction == Direction.right);
    }
    // <<

    add(platto);
  }

  private static Motion getHorizontalListMotion(double amplitude) {
    double velocity = Gameplay.liftVelocity / 1000d;  // todo fix this shit with division by 1000
    return new Movement().setGraph(amplitude, 0, 0).setBounce(true).setVelocity(velocity).setTween(Tweens.sin);
  }

  @Override
  protected void onDrawSpriteIfVisible(Graphics graphics) {
    if (hasLine) {

    }
    super.onDrawSpriteIfVisible(graphics);

  }
}
