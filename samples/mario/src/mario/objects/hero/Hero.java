package mario.objects.hero;

import featurea.app.Context;
import featurea.input.InputAdapter;
import featurea.input.Key;
import featurea.motion.Motion;
import featurea.platformer.Animation;
import featurea.platformer.View;
import featurea.platformer.overlap.Overlap;
import featurea.platformer.physics.HeroBody;
import featurea.platformer.util.Rectangle;
import featurea.util.Preferences;
import mario.*;
import mario.config.Gameplay;
import mario.objects.bonuses.*;
import mario.objects.enemies.Enemy;
import mario.objects.enemies.Goomba;
import mario.objects.enemies.KoopaTroopa;
import mario.objects.landscape.Platform;
import mario.objects.landscape.Tube;
import mario.util.MarioPreferences;

import static mario.config.Gameplay.heroFireballStartVelocityX;

public class Hero extends HeroBody {

  private static final Rectangle smallRectangle = new Rectangle(2, 16, 16 - 2, 32);
  private static final Rectangle bigRectangle = new Rectangle(2, 0, 16 - 2, 32);
  private static final Rectangle sitRectangle = new Rectangle(2, 10, 16 - 2, 32);

  public HeroState state;
  private boolean isInvisibility;
  private double fireDelay;
  private double fireballDelay;
  public boolean isBack;
  private int fireballCounter;

  public Hero() {
    sprite = new HeroSprite(this);
    setSize(16, 32);
    setState(HeroState.small);
    setMass(Gameplay.mass);
  }

  private HeroSprite getFierySprite() {
    return (HeroSprite) sprite;
  }

  public void setState(HeroState state) {
    this.state = state;
  }

  @Override
  public Hero build() {
    onCreateInputForDebug();
    position.z = Gameplay.heroZ;
    return this;
  }

  @Override
  public void onTick(double elapsedTime) {
    super.onTick(elapsedTime);
    onTickFireDelay(elapsedTime);
    onTickFireballDelay(elapsedTime);
  }

  private void onTickFireDelay(double elapsedTime) {
    fireDelay -= elapsedTime;
    if (fireDelay < 0) {
      fireDelay = 0;
    }
  }

  private void onTickFireballDelay(double elapsedTime) {
    fireballDelay -= elapsedTime;
  }

  private void onCreateInputForDebug() {
    Context.getInput().inputListeners.add(new InputAdapter() {
      @Override
      public void onKeyDown(Key key) {
        if (key == Key.B) {
          becomeBig();
        }
        if (key == Key.F) {
          becomeFiery();
        }
        if (key == Key.X) {
          becomeSmall();
        }
      }
    });
  }

  public void becomeBig() {
    System.out.println("Hero.becomeBig");
    if (!isDead()) {
      if (getState() == HeroState.small) {
        setState(HeroState.big);
        getFierySprite().becomeBig();
        Context.getAudio().play(Assets.Audio.Sounds.Powerup);
      }
    }
  }

  public void becomeFiery() {
    if (!isDead()) {
      if (getState() == HeroState.big) {
        getFierySprite().becomeFiery();
        setState(HeroState.fiery);
        Context.getAudio().play(Assets.Audio.Sounds.Powerup);
      } else if (getState() == HeroState.small) {
        becomeBig();
      }
    }
  }

  public void becomeSmall() {
    if (!isDead()) {
      if (getState() != HeroState.small) {
        System.out.println("becomeSmall");
        getFierySprite().isBlink = true;
        getFierySprite().becomeSmall(new Runnable() {
          @Override
          public void run() {
            Context.getTimer().delay(new Runnable() {
              @Override
              public void run() {
                getFierySprite().isBlink = false;
              }
            }, 1200);
          }
        });
        setState(HeroState.small);
        Context.getAudio().play(Assets.Audio.Sounds.Power_Down);
      }
    }
  }

  public boolean isFiery() {
    return getState() == HeroState.fiery;
  }

  @Override
  public String onUpdateSprite() {
    if (!isDead()) {
      if (!getFierySprite().isTransform()) {
        if (isJump()) {
          if (getView() == View.sit) {
            switch (getState()) {
              case small:
                return Sprites.Mario.Small.Color0.jump();
              case big:
                return Sprites.Mario.Big.Color0.sit();
              case fiery:
                return Sprites.Mario.Fiery.Color0.sit();
              default:
                return null;
            }
          } else {
            switch (getState()) {
              case small:
                return Sprites.Mario.Small.Color0.jump();
              case big:
                return Sprites.Mario.Big.Color0.jump();
              case fiery:
                return Sprites.Mario.Fiery.Color0.jump();
              default:
                return null;
            }
          }
        } else {
          switch (getView()) {
            case stay: {
              if (fireDelay != 0) {
                return Sprites.Mario.Fiery.Color0.Fire._0();
              }
              if (hasPlatform()) {
                switch (getState()) {
                  case small:
                    return Sprites.Mario.Small.Color0.stay();
                  case big:
                    return Sprites.Mario.Big.Color0.stay();
                  case fiery:
                    return Sprites.Mario.Fiery.Color0.stay();
                  default:
                    return null;
                }
              } else {
                if (Sprites.theme == Theme.underwater) {
                  switch (getState()) {
                    case small:
                      return Sprites.Mario.Small.Color0.jump();
                    case big:
                      return Sprites.Mario.Big.Color0.jump();
                    case fiery:
                      return Sprites.Mario.Fiery.Color0.jump();
                    default:
                      return null;
                  }
                } else {
                  switch (getState()) {
                    case small:
                      return Sprites.Mario.Small.Color0.Walk._0();
                    case big:
                      return Sprites.Mario.Big.Color0.Walk._0();
                    case fiery:
                      return Sprites.Mario.Fiery.Color0.Walk._0();
                    default:
                      return null;
                  }
                }
              }
            }

            case walk: {
              if (fireDelay != 0) {
                return Sprites.Mario.Fiery.Color0.fire();
              }
              switch (getState()) {
                case small:
                  return Sprites.Mario.Small.Color0.walk();
                case big:
                  return Sprites.Mario.Big.Color0.walk();
                case fiery:
                  return Sprites.Mario.Fiery.Color0.walk();
                default:
                  return null;
              }
            }

            case turn: {
              switch (getState()) {
                case small:
                  return Sprites.Mario.Small.Color0.turn();
                case big:
                  return Sprites.Mario.Big.Color0.turn();
                case fiery:
                  return Sprites.Mario.Fiery.Color0.turn();
                default:
                  return null;
              }
            }

            case climb: {
              switch (getState()) {
                case small:
                  return Sprites.Mario.Small.Color0.climb();
                case big:
                  return Sprites.Mario.Big.Color0.climb();
                case fiery:
                  return Sprites.Mario.Fiery.Color0.climb();
                default:
                  return null;
              }
            }

            case sit: {
              switch (getState()) {
                case small:
                  return Sprites.Mario.Small.Color0.stay();
                case big:
                  return Sprites.Mario.Big.Color0.sit();
                case fiery:
                  return Sprites.Mario.Fiery.Color0.sit();
                default:
                  return null;
              }
            }

            case fire: {
              switch (getState()) {
                case fiery:
                  return Sprites.Mario.Fiery.Color0.fire();
              }
            }

            default: {
              throw new IllegalStateException("view: " + getView());
            }

          }
        }
      }
    } else {
      return Sprites.Mario.Small.Color0.dead();
    }
    return null;
  }

  @Override
  public void fire() {
    if (getView() == View.sit) {
      return;
    }
    if (fireballDelay <= 0 && fireballCounter < 2) {
      fireballDelay = Gameplay.fireballDelay;
      if (isFiery()) {
        super.fire();
        this.fireDelay = Gameplay.fireDelay;
        Context.getTimer().delay(new Runnable() {
          @Override
          public void run() {
            final HeroFireball heroFireball = new HeroFireball() {
              @Override
              public void onRemove() {
                super.onRemove();
                fireballCounter--;
              }
            };
            double x = position.x + (isRight() ? getSize().width : 0);
            double y = position.y + 12;
            heroFireball.setPosition(x, y);
            heroFireball.velocity.x = heroFireballStartVelocityX * (isRight() ? 1 : -1);
            heroFireball.velocity.y = heroFireballStartVelocityX * 0.75;
            getLayer().add(heroFireball);
            fireballCounter++;
          }
        });
        Context.getAudio().play(Assets.Audio.Sounds.Fireball);
      }
    }
  }

  @Override
  public void joystickDown() {
    super.joystickDown();
    if (getPlatform() instanceof Tube) {
      Tube tube = (Tube) getPlatform();
      tube.onIntersect(this, null, Overlap.Y.top);
    }
  }

  @Override
  public void onIntersect(Animation animation, Overlap.X horizontal, Overlap.Y vertical) {
    super.onIntersect(animation, horizontal, vertical);
    if (isDead()) {
      return;
    }
    if (animation instanceof Platform && vertical == Overlap.Y.top) {
      Context.getAudio().play(Assets.Audio.Sounds.Bump);
    }
    if (animation instanceof Bonus) {
      Bonus bonus = (Bonus) animation;
      if (bonus.isCreate) {
        if (bonus instanceof BecomeBigBonus) {
          becomeBig();
          bonus.removeSelf();
        }
        if (bonus instanceof BecomeFieryBonus) {
          becomeFiery();
          bonus.removeSelf();
        }
        if (bonus instanceof LevelUpBonus) {
          plusOneLife();
          bonus.removeSelf();
        }
        if (bonus instanceof StarBonus) {
          performInvisibility();
          bonus.removeSelf();
        }
      }
    }

    if (!getFierySprite().isTransform()) {
      if (animation instanceof Enemy) {
        final Enemy enemy = (Enemy) animation;
        if (isInvisibility) {
          enemy.destroyBecauseOfFire();
        } else {
          boolean isDamage = false;
          if (enemy instanceof Goomba) {
            Goomba goomba = (Goomba) enemy;
            isDamage = goomba.tryDamage(this, Overlap.X.revert(horizontal), Overlap.Y.revert(vertical));
          }
          if (enemy instanceof KoopaTroopa) {
            KoopaTroopa koopaTroopa = (KoopaTroopa) enemy;
            isDamage = koopaTroopa.tryDamage(this, Overlap.X.revert(horizontal), Overlap.Y.revert(vertical));
          }
          if (isDamage && !getFierySprite().isBlink) {
            damage();
          }
        }
      }
    }
  }

  private void performInvisibility() {
    isInvisibility = true;
    getFierySprite().becomeInvisibility(Gameplay.starBonusTime, new Runnable() {
      @Override
      public void run() {
        isInvisibility = false;
      }
    });
  }

  private void plusOneLife() {
    Context.getAudio().play(Assets.Audio.Sounds.Gain_Life);
  }

  public void damage() {
    if (!getFierySprite().isBlink) {
      if (getState() != HeroState.small) {
        becomeSmall();
      } else {
        destroy();
      }
    }
  }

  public boolean isTimeStop() {
    return getFierySprite().isTimeStop();
  }

  @Override
  public void destroy() {
    if (!isDead()) {
      super.destroy();
      setMass(0);
      position.z = 100;
      GameplayScreen.disableJoystick();
      Context.getTimer().delay(new Runnable() {
        @Override
        public void run() {
          setMass(1);
          smallJump();
          Context.getTimer().delay(new Runnable() {
            @Override
            public void run() {
              lose();
            }
          }, 1200);
        }
      }, 120);
      Context.getAudio().play(Assets.Audio.Sounds.Player_Dies);
    }
  }

  public boolean isJoystickEnable() {
    return !getFierySprite().isTransform();
  }

  @Override
  public boolean isMotionableVertically() {
    return super.isMotionableVertically() && isJoystickEnable();
  }

  @Override
  public boolean isMotionableHorizontally() {
    return super.isMotionableHorizontally() && isJoystickEnable();
  }

  @Override
  public void jump() {
    if (hasPlatform()) {
      if (getState() == HeroState.small) {
        Context.getAudio().play(Assets.Audio.Sounds.Jump_Small);
      } else {
        Context.getAudio().play(Assets.Audio.Sounds.Jump_Super);
      }
    }
    super.jump();
  }

  public void obtainCoin(final Coin coin) {
    Navigation.preferences.score += Gameplay.coinScore;
    Context.getAudio().play(Assets.Audio.Sounds.Coin);
    Context.getTimer().delay(new Runnable() {
      @Override
      public void run() {
        coin.removeSelf();
      }
    });
  }

  public void background() {
    velocity.setValue(0, 0);
    setMass(0);
    position.z = -1;
    GameplayScreen.disableJoystick();
  }

  public void foreground() {
    setMass(1);
    position.z = Gameplay.heroZ;
    GameplayScreen.enableJoystick();
  }

  public void goInsideBonus(final Tube tube) {
    Context.getAudio().play(Assets.Audio.Sounds.Pipe);

    background();
    Motion motion = tube.getPenetrationMotion(this);
    timeline.add(motion);
    motion.onStop = new Runnable() {
      @Override
      public void run() {
        Navigation.setBonusWorld(tube.getBonus(), tube.getWorld());
        foreground();
      }
    };
  }

  public void goOutsideBonus(Tube tube) {
    background();
    Motion motion = tube.getPenetrationMotion(this);
    timeline.add(motion);
    World bonusWorld = tube.getWorld();
    bonusWorld.comeback(tube.getIn(), motion);
  }

  @Override
  public void move(double dx, double dy, double dz) {
    super.move(dx, dy, dz);
    checkForLose();
  }

  private void checkForLose() {
    if (!isDead()) {
      if (top() > getWorld().getSize().height) {
        lose();
      }
    }
  }

  private void lose() {
    setState(HeroState.small);
    Context.getTimer().delay(new Runnable() {
      @Override
      public void run() {
        Navigation.setWorld(Worlds.current());
      }

      @Override
      public String toString() {
        return "Hero.lose()";
      }
    });
  }

  public HeroState getState() {
    return state;
  }

  public void onApplyPreferences(Preferences preferences) {
    MarioPreferences marioPreferences = (MarioPreferences) preferences;
    HeroState state = marioPreferences.state;
    if (state != null) {
      setState(state);
    }
  }

  public World getWorld() {
    return (World) getLayer();
  }

  @Override
  public String toString() {
    return "Hero";
  }

  public boolean isStrongerThenEnemy() {
    return velocity.y > 0 && getView() != View.walk && !hasPlatform();
  }

  @Override
  public void setView(View view) {
    if (getView() != view) {
      super.setView(view);
      if (state == HeroState.small) {
        setRectangle(smallRectangle);
      } else {
        if (view == View.sit) {
          setRectangle(sitRectangle);
        } else {
          setRectangle(bigRectangle);
        }
      }
    }
  }


}
