package mario.objects.landscape;

import featurea.graphics.Graphics;
import featurea.motion.Motion;
import featurea.motion.Movement;
import featurea.platformer.Animation;
import featurea.platformer.View;
import featurea.platformer.overlap.Overlap;
import featurea.platformer.physics.Body;
import featurea.util.Colors;
import mario.Sprites;
import mario.config.Gameplay;
import mario.objects.enemies.PiranhaPlant;
import mario.objects.hero.Hero;
import mario.objects.hero.World;

import static mario.Sprites.Items.Tube.*;
import static mario.config.Gameplay.tubePenetrationTolerance;

public class Tube extends Body {

  private static final double cornerTrunkShift = Sprites.Items.TubeCorner.tubeCornerPart1Width / 4;

  private String bonus;
  private String in;
  private String out;

  private boolean isCorner;
  private int trunkCount;
  private boolean isPiranhaPlant;
  private PiranhaPlant piranhaPlant = new PiranhaPlant();

  public Tube() {
    setSolid(true);
    setMotionableVertically(false);
    setMotionableHorizontally(false);
  }

  public void setTrunkCount(int trunkCount) {
    this.trunkCount = trunkCount;
  }

  public void setIn(String in) {
    this.in = in;
  }

  public String getIn() {
    return in;
  }

  public void setBonus(String bonus) {
    this.bonus = bonus;
  }

  public String getBonus() {
    return bonus;
  }

  public void setOut(String out) {
    this.out = out;
  }

  public String getOut() {
    return out;
  }

  public void setCorner(boolean isCorner) {
    this.isCorner = isCorner;
  }

  public void setPiranhaPlant(boolean isPiranhaPlant) {
    this.isPiranhaPlant = isPiranhaPlant;
  }

  @Override
  public void onDrawSpriteIfVisible(Graphics graphics) {
    if (!isCorner) {
      // head
      double x = position.x;
      double y = position.y;
      graphics.drawTexture(Sprites.Items.Tube.head(), x, y, x + headWidth, y + headHeight, null, 0, 0, Colors.white, false, false, null);

      // trunk
      y += headHeight;
      double trunkWidth = Sprites.Items.Tube.trunkWidth;
      double trunkHeight = Sprites.Items.Tube.trunkHeight;
      for (int i = 0; i < trunkCount; i++) {
        graphics.drawTexture(Sprites.Items.Tube.trunk(), x, y + i * trunkHeight, x + trunkWidth, y + (i + 1) * trunkHeight, null, 0, 0, Colors.white, false, false, null);
      }
    } else {
      double ox = position.x;
      double oy = position.y;


      {
        String tubeCornerPart1 = Sprites.Items.TubeCorner.tubeCornerPart1();
        double x1 = ox + Sprites.Items.TubeCorner.tubeCornerPart2Width - cornerTrunkShift;
        double x2 = x1 + Sprites.Items.TubeCorner.tubeCornerPart1Width;
        for (double i = 0, y1 = Gameplay.hudHeight; true; i++) {
          double y2 = oy + Sprites.Items.TubeCorner.tubeCornerPart2Height - Sprites.Items.TubeCorner.tubeCornerPart2Height * i;
          y1 = y2 - Sprites.Items.TubeCorner.tubeCornerPart1Height;
          if (y1 >= Gameplay.hudHeight) {
            graphics.drawTexture(tubeCornerPart1, x1, y1, x2, y2, null, 0, 0, Colors.white, false, false, null);
          } else {
            break;
          }
        }
      }

      {
        String tubeCornerPart2 = Sprites.Items.TubeCorner.tubeCornerPart2();
        double x1 = ox;
        double y1 = oy;
        double x2 = ox + Sprites.Items.TubeCorner.tubeCornerPart2Width;
        double y2 = oy + Sprites.Items.TubeCorner.tubeCornerPart2Height;
        graphics.drawTexture(tubeCornerPart2, x1, y1, x2, y2, null, 0, 0, Colors.white, false, false, null);
      }
    }
  }

  @Override
  public void onIntersect(Animation animation, Overlap.X horizontal, Overlap.Y vertical) {
    if (horizontal != null) {
      if (isCorner) {
        if (animation instanceof Hero) {
          Hero hero = (Hero) animation;
          if (hero.getView() == View.walk) {
            if (bonus != null) {
              hero.goInsideBonus(this);
            } else {
              if (in != null) {
                hero.goOutsideBonus(this);
              }
            }
          }
        }
      }
    }
    if (vertical == Overlap.Y.top) {
      if (!isCorner) {
        if (animation instanceof Hero) {
          if (bonus != null) {
            Hero hero = (Hero) animation;
            if (hero.getView() == View.sit) {
              if (hero.left() >= left() + tubePenetrationTolerance && hero.right() <= right() - tubePenetrationTolerance) {
                hero.goInsideBonus(this);
              }
            }
          }
        }
      }
    }
  }

  @Override
  public Tube build() {
    removeAllChildren();

    buildSize();
    if (isPiranhaPlant) {
      piranhaPlant.position.x = left() + (width() - piranhaPlant.width()) / 2;
      piranhaPlant.position.y = position.y;
      piranhaPlant.position.z--;
      add(piranhaPlant.build());
    }
    return this;
  }

  public void buildSize() {
    if (isCorner) {
      double width = Sprites.Items.TubeCorner.tubeCornerPart2Width;
      double height = Sprites.Items.TubeCorner.tubeCornerPart2Height;
      setRectangle(0, 0, width, height);
      Body body = new Body();
      body.setSolid(true);
      body.setPosition(right() - cornerTrunkShift, Gameplay.hudHeight);
      body.setRectangle(2, 0, trunkWidth - 2, bottom() - Gameplay.hudHeight);
      add(body);
    } else {
      setRectangle(2, 0, trunkWidth - 2, trunkHeight * (trunkCount + 1));
    }
  }

  public World getWorld() {
    return (World) getLayer();
  }

  @Override
  public double getLifeDistance() {
    return 0;
  }

  public Motion getPenetrationMotion(Animation animation) {
    if (!isCorner) {
      return new Movement().setGraph(0, animation.height() + tubePenetrationTolerance, 0).setVelocity(0.02);
    } else {
      return new Movement().setGraph(animation.width() + tubePenetrationTolerance, 0, 0).setVelocity(0.02);
    }
  }

  public Motion getExitMotion(Animation animation) {
    if (!isCorner) {
      double dy = animation.height() + tubePenetrationTolerance;
      animation.position.y += dy;
      return new Movement().setGraph(0, -dy, 0).setVelocity(0.02);
    } else {
      throw new IllegalArgumentException("tube.isCorner == true");
    }
  }

}

