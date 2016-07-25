package mario.objects.background;

import featurea.app.Context;
import featurea.graphics.Graphics;
import featurea.motion.Movement;
import featurea.platformer.Animation;
import featurea.platformer.physics.Body;
import featurea.util.Vector;
import mario.Sprites;
import mario.config.Gameplay;
import mario.objects.bonuses.Firework;
import mario.objects.hero.Hero;
import mario.objects.hero.World;

public class CastleBackground extends Animation {

  private Vector castle2;
  private Vector castle4;
  private boolean isWin;

  public void setCastle2(Vector castle2) {
    this.castle2 = castle2;
  }

  public void setCastle4(Vector castle4) {
    this.castle4 = castle4;
  }

  @Override
  protected void onDrawSpriteIfVisible(Graphics graphics) {
    super.onDrawSpriteIfVisible(graphics);
    // castle2
    if (castle2 != null) {
      double x1 = castle2.x;
      double y2 = 200;
      double y1 = y2 - Sprites.Background.castle2Height;
      double x2 = x1 + Sprites.Background.castle2Width;
      graphics.drawTexture(Sprites.Background.castle2(), x1, y1, x2, y2, null, 0, 0, null, false, false, null);
    }

    // castle4
    if (castle4 != null) {
      double x1 = castle4.x;
      double y2 = 200;
      double y1 = y2 - Sprites.Background.castle4Height;
      double x2 = x1 + Sprites.Background.castle4Width;
      graphics.drawTexture(Sprites.Background.castle4(), x1, y1, x2, y2, null, 0, 0, null, false, false, null);
    }
  }

  @Override
  public void onTick(double elapsedTime) {
    if (!isWin) {
      if (castle2 != null && (castle4 == null || castle4.x < castle2.x)) {
        if (getWorld().getHero().position.x >= castle2.x + 32) {
          isWin = true;
          win();
        }
      }
      if (castle4 != null && (castle2 == null || castle2.x < castle4.x)) {
        if (getWorld().getHero().position.x >= castle4.x + 32) {
          isWin = true;
          win();
        }
      }
    }
  }

  public void win() {
    Body flag = new Body();
    flag.setSprite(Sprites.Background.flag());
    flag.setSize(Sprites.Background.flagWidth, Sprites.Background.flagHeight);
    flag.setPosition(castle2.x + Sprites.Background.castle2Width / 2 - Sprites.Background.flagWidth / 2,
        200 - Sprites.Background.castle2Height + 8);
    flag.add(new Movement().setGraph(0, -24, 0).setVelocity(0.02));
    flag.position.z = z() - 1;
    add(flag);
    final Hero hero = getWorld().getHero();
    hero.removeSelf();
    Context.getTimer().delay(new Runnable() {
      @Override
      public void run() {
        Firework firework = new Firework() {
          @Override
          protected void onFinishFirework() {
            remove(this);
            getWorld().onFinish();
          }
        }.setPosition(new Vector(castle2.x, 0));
        add(firework);
        firework.setCount(hero);
      }
    }, Gameplay.fireworkStartDelay);
  }

  private World getWorld() {
    return (World) getLayer();
  }

  @Override
  public Animation setPosition(double x, double y, double z) {
    return super.setPosition(0, 0, 0);
  }

}
