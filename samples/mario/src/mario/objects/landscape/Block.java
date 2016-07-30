package mario.objects.landscape;

import featurea.app.Context;
import featurea.motion.Motion;
import featurea.motion.Movement;
import featurea.platformer.Animation;
import featurea.platformer.overlap.Overlap;
import featurea.platformer.physics.Body;
import mario.Assets;
import mario.Sprites;
import mario.config.Gameplay;
import mario.objects.bonuses.*;
import mario.objects.enemies.Enemy;
import mario.objects.hero.Hero;
import mario.objects.hero.HeroState;
import mario.objects.hero.World;
import mario.util.BlockBreakSprite;

import java.util.Set;

public class Block extends Body {

  public enum State {
    question, brick, done, stone;

    public boolean isSolid() {
      return this == done || this == stone;
    }
  }

  private Bonus bonus;
  private State state;
  private static final double width = Sprites.Items.Brick.showWidth;
  private static final double height = Sprites.Items.Brick.showHeight;
  private static final double bounceHeight = height / 2;
  private boolean isBounce;

  public Block() {
    setSize(width, height);
    setRectangle(2, 0, width - 2, height);
    setFps(6);
    setMotionableHorizontally(false);
  }

  public Bonus getBonus() {
    return bonus;
  }

  public void setBonus(Bonus bonus) {
    this.bonus = bonus;
    if (this.bonus != null) {
      this.bonus.setBlock(this);
    }
    resetState();
  }

  public State getState() {
    if (state == null) {
      if (bonus instanceof CoinRotateBonus) {
        CoinRotateBonus coinRotateBonus = (CoinRotateBonus) bonus;
        if (coinRotateBonus.getCount() == 1) {
          state = State.question;
        }
      }
      if (bonus instanceof BecomeFieryBonus || bonus instanceof BecomeBigBonus || bonus instanceof LianaBonus) {
        state = State.question;
      }
    }
    if (state == null) {
      state = State.brick;
    }
    return state;
  }

  public Block setState(State state) {
    this.state = state;
    return this;
  }

  @Override
  public void onIntersect(Animation animation, Overlap.X horizontal, Overlap.Y vertical) {
    super.onIntersect(animation, horizontal, vertical);
    if (!isDead()) {
      if (vertical == Overlap.Y.bottom) {
        if (animation instanceof Hero) {
          Hero hero = (Hero) animation;
          if (hero.velocity.y <= 0) {
            setVisible(true);
            if (!getState().isSolid()) {
              if (bonus != null) {
                addBonus();
                bounce();
              } else {
                if (hero.getState() != HeroState.small) {
                  destroy();
                } else {
                  bounce();
                }
              }
              destroyEnemyLoads();
            } else {
              Context.getAudio().play(Assets.Audio.Sounds.Bump);
            }
          }
        }
      }
    }
  }

  private void destroyEnemyLoads() {
    Set<Animation> animations = getLayer().bodyIndex.getAnimations(ox(), oy() - height);
    if (animations != null) {
      for (Animation animation : animations) {
        if (animation instanceof Enemy) {
          Enemy enemy = (Enemy) animation;
          enemy.destroyBecauseOfFire();
        }
      }
    }
  }

  @Override
  public String onUpdateSprite() {
    if (!isDead()) {
      switch (getState()) {
        case brick: {
          return Sprites.Items.Brick.show();
        }
        case question: {
          return Sprites.Items.QuestionBlock.show();
        }
        case done: {
          return Sprites.Items.QuestionBlock.afterHit();
        }
        case stone: {
          return Sprites.Items.stone();
        }
      }
    }
    return null;
  }

  private World getWorld() {
    return (World) super.getLayer();
  }

  @Override
  public boolean isSolid() {
    return isVisible() && !isDead();
  }

  private void bounce() {
    isBounce = true;
    position.z = Gameplay.bounceBlockZ;
    final double top = top();
    Motion motion = new Movement().setGraph(0, -bounceHeight, 0, 0, bounceHeight, 0).setVelocity(0.08);
    motion.onStop = new Runnable() {
      @Override
      public void run() {
        isBounce = false;
        setTop(top);
      }
    };
    add(motion);
    Context.getAudio().play(Assets.Audio.Sounds.Bump); // this is not correct todo improve this
  }

  private void addBonus() {
    bonus.setPosition(position);
    bonus.position.z--;
    bonus.position.y -= width / 2;
    Context.getTimer().delay(new Runnable() {
      @Override
      public void run() {
        if (bonus.work(getWorld())) {
          state = State.done;
          Context.getAudio().play(Assets.Audio.Sounds.Bump);
        }
      }
    });
  }

  @Override
  public void destroy() {
    super.destroy();
    redraw();
    sprite = new BlockBreakSprite().setBlock(this);
    setMass(3.5);
    longJumpNow();
    Context.getAudio().play(Assets.Audio.Sounds.Break_Block);

    // coinRotate feature
    Set<Animation> animations = getLayer().bodyIndex.getAnimations(position.x + width / 2, position.y - height / 2);
    if (animations != null) {
      for (Animation animation : animations) {
        if (animation instanceof Coin) {
          final Coin coin = (Coin) animation;
          Context.getTimer().delay(new Runnable() {
            @Override
            public void run() {
              CoinRotateBonus coinRotateBonus = (CoinRotateBonus) new CoinRotateBonus().setCount(1).
                  setBlock(Block.this).setPosition(coin.position).build();
              coinRotateBonus.work(getLayer());
              coin.removeSelf();
            }
          });
          break;
        }
      }
    }
  }

  @Override
  public double getLifeDistance() {
    if (oy() > 184 && oy() < 200) {
      return 0;
    } else {
      return super.getLifeDistance();
    }
  }

  public boolean isBounce() {
    return isBounce;
  }

  private void resetState() {
    state = null;
    getState();
  }

  @Override
  public String toString() {
    return "Block";
  }

}
