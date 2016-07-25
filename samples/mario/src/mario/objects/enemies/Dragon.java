package mario.objects.enemies;

import featurea.app.Context;
import featurea.platformer.Animation;
import featurea.util.Size;
import mario.Sprites;
import mario.Worlds;
import mario.config.Gameplay;

public class Dragon extends Enemy {

  private static final double width = Sprites.Enemies.Dragon.mouthCloseWidth;
  private static final double height = Sprites.Enemies.Dragon.mouthCloseHeight;
  private int lifes;
  private double currentFireTime;
  private double currentJumpTime;
  private double currentWalkTime;

  public Dragon() {
    setRectangle(0, 0, width, height);
    setSize(width, height);
    setFps(4);
    setSprite(Sprites.Enemies.Dragon.mouthClose());
    setLifes(10);
  }

  @Override
  public Animation build() {
    super.build();
    if (!Context.isFeaturea()) {
      Context.getTimer().delay(new Runnable() {
        @Override
        public void run() {
          if (!isDead()) {
            if (Math.abs(getWorld().getHero().ox() - ox()) < 200) {
              dragonFire();
            }
            Context.getTimer().delay(this, Gameplay.firePeriod);
          }
        }
      }, Gameplay.firePeriod);

      Context.getTimer().delay(new Runnable() {
        @Override
        public void run() {
          if (!isDead()) {
            jump();
            Context.getTimer().delay(this, Gameplay.jumpPeriod);
          }
        }
      }, Gameplay.jumpPeriod);

      Context.getTimer().delay(new Runnable() {
        @Override
        public void run() {
          if (!isDead()) {
            velocity.x *= -1;
            Context.getTimer().delay(this, Gameplay.walkPeriod);
          }
        }
      }, Gameplay.walkPeriod);
    }
    return this;
  }

  public void setLifes(int lifes) {
    this.lifes = lifes;
  }

  @Override
  public void destroyBecauseOfFire() {
    if (lifes <= 0) {
      super.destroyBecauseOfFire();
      setSprite(getDeadSprite());
      setSize(getDeadSize());
      setAngle(180);
      smallJump();
    } else {
      lifes--;
    }
  }

  private void dragonFire() {
    Context.getTimer().delay(new Runnable() {
      @Override
      public void run() {
        setSprite(Sprites.Enemies.Dragon.mouthOpen());
        DragonFire fire = new DragonFire();
        Dragon dragon = Dragon.this;
        fire.position.y += dragon.position.y + 8;
        fire.position.z -= 0.1;
        fire.setRight(dragon.left());
        fire.velocity.x = -120;
        getLayer().add(fire);
        Context.getTimer().delay(new Runnable() {
          @Override
          public void run() {
            setSprite(Sprites.Enemies.Dragon.mouthClose());
          }
        }, Gameplay.jumpPeriod * 0.4);
      }
    });
  }

  @Override
  public boolean isFlipX() {
    return true;
  }

  private static String getDeadSprite() {
    int index = Worlds.getIndex();
    System.out.println("Dragon.getDeadSprite: " + index);
    switch (index) {
      case 3: {
        return Sprites.Enemies.Goomba.dead();
      }
      case 7: {
        return Sprites.Enemies.KoopaTroopa.dead();
      }
      case 11: {
        return Sprites.Enemies.Beetle.dead();
      }
      case 15: {
        return Sprites.Enemies.Spiny.walk();
      }
      case 19: {
        return Sprites.Enemies.Lakitu._0();
      }
      case 23: {
        return Sprites.Enemies.Octopus._1();
      }
      case 27: {
        return Sprites.Enemies.Turtle.stay();
      }
      case 31: {
        return Sprites.Enemies.Dragon.mouthClose();
      }
      default: {
        if (!Context.isProduction()) {
          return Sprites.Enemies.goomba();
        } else {
          throw new IllegalArgumentException("index: " + index);
        }
      }
    }
  }

  private static Size getDeadSize() {
    switch (Worlds.getIndex()) {
      case 3: {
        return new Size(Sprites.Enemies.Goomba.deadWidth, Sprites.Enemies.Goomba.deadHeight);
      }
      case 7: {
        return new Size(Sprites.Enemies.KoopaTroopa.deadWidth, Sprites.Enemies.KoopaTroopa.deadHeight);
      }
      case 11: {
        return new Size(Sprites.Enemies.Beetle.deadWidth, Sprites.Enemies.Beetle.deadHeight);
      }
      case 15: {
        return new Size(Sprites.Enemies.Spiny.walkWidth, Sprites.Enemies.Spiny.walkHeight);
      }
      case 19: {
        return new Size(Sprites.Enemies.Lakitu._0Width, Sprites.Enemies.Lakitu._0Height);
      }
      case 23: {
        return new Size(Sprites.Enemies.Octopus._0Width, Sprites.Enemies.Octopus._0Height);
      }
      case 27: {
        return new Size(Sprites.Enemies.Turtle.stayWidth, Sprites.Enemies.Turtle.stayHeight);
      }
      case 31: {
        return new Size(Sprites.Enemies.Dragon.mouthCloseWidth, Sprites.Enemies.Dragon.mouthCloseHeight);
      }
      default: {
        if (!Context.isProduction()) {
          return new Size(Sprites.Enemies.Goomba.deadWidth, Sprites.Enemies.Goomba.deadHeight);
        } else {
          throw new IllegalArgumentException("index: " + Worlds.getIndex());
        }
      }
    }
  }

}
