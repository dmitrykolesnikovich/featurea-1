package mario.objects.background;

import com.sun.istack.internal.Nullable;
import featurea.app.Context;
import featurea.platformer.Animation;
import featurea.platformer.overlap.Overlap;
import featurea.util.Colors;
import featurea.util.Selection;
import featurea.util.Vector;
import mario.Assets;
import mario.GameplayScreen;
import mario.Sprites;
import mario.config.Gameplay;
import mario.objects.hero.Hero;

import static mario.Sprites.Background.princessHeight;
import static mario.Sprites.Background.princessWidth;
import static mario.Sprites.Items.dragonBridgeKeyHeight;
import static mario.Sprites.Items.dragonBridgeKeyWidth;

public class PrincessBackground extends Animation {

  private Vector princessVector;
  private boolean isHeroWalk;
  private Hero hero;
  private Animation key;
  private Animation princess;

  @Override
  public PrincessBackground build() {
    removeAllChildren();

    // key
    key = new Animation() {
      @Override
      public void onIntersect(Animation animation, Overlap.X horizontal, Overlap.Y vertical) {
        super.onIntersect(animation, horizontal, vertical);
        if (animation instanceof Hero) {
          onKeyIntersectsHero(this, (Hero) animation);
        }
      }
    };
    add(key.setSprite(Sprites.Items.dragonBridgeKey()).setSize(dragonBridgeKeyWidth, dragonBridgeKeyHeight).
        setRectangle(0, 0, dragonBridgeKeyWidth, dragonBridgeKeyHeight).setPosition(position));

    // princess
    princess = new Animation() {
      @Override
      public void onIntersect(Animation animation, Overlap.X horizontal, Overlap.Y vertical) {
        super.onIntersect(animation, horizontal, vertical);
        if (animation instanceof Hero) {
          onPrincessIntersectsHero(this, (Hero) animation);
        }
      }
    };
    add(princess.setSprite(Sprites.Background.princess()).setSize(princessWidth, princessHeight)
        .setRectangle(-2, 0, princessWidth + 2, princessHeight).setPosition(this.princessVector));
    return this;
  }

  private void onKeyIntersectsHero(Animation key, Hero hero) {
    if (!isHeroWalk) {
      isHeroWalk = true;
      GameplayScreen.disableJoystick();
      key.removeSelf();
      this.hero = hero;
      hero.velocity.setValue(0, 0);
      Context.getAudio().play(Assets.Audio.Music.Level_Complete);
    }
  }

  private void onPrincessIntersectsHero(Animation princess, final Hero hero) {
    isHeroWalk = false;
    hero.joystickNothing();
    hero.velocity.setValue(0, 0);
    hero.setRight(princess.left() - 2);
    Context.getTimer().delay(new Runnable() {
      @Override
      public void run() {
        hero.getWorld().onFinish();
      }
    }, Gameplay.castleWinDelay);
  }

  public void setPrincess(Vector princessVector) {
    this.princessVector = princessVector;
  }

  @Override
  public void onTick(double elapsedTime) {
    super.onTick(elapsedTime);
    if (hero != null && isHeroWalk) {
      hero.joystickRight();
    }
  }

  @Override
  public void getSelection(Selection result, @Nullable Vector position) {
    result.setRectangle(key.left(), key.top(), princess.right(), princess.bottom());
    result.color = Colors.black;
    if (position != null) {
      result.isSelected = result.contains(position.x, position.y);
    }
  }

}
