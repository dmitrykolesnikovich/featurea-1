package mario.objects.landscape;

import featurea.app.Context;
import featurea.graphics.Graphics;
import featurea.platformer.Animation;
import featurea.platformer.overlap.Overlap;
import featurea.platformer.physics.Body;
import featurea.util.Colors;
import mario.Assets;
import mario.GameplayScreen;
import mario.Sprites;
import mario.config.Gameplay;
import mario.objects.hero.Hero;

public class Flagpole extends Body {

  private Hero hero;
  private boolean isStartFlagFall;
  private boolean isStartHeroWalk;

  private final Block stone = (Block) new Block().setState(Block.State.stone).build();

  private final Body flag = new Body() {
    {
      double flagWidth = Sprites.Items.Flagpole.flagWidth;
      double flagHeight = Sprites.Items.Flagpole.flagHeight;
      setSize(flagWidth, flagHeight);
      setRectangle(0, 0, flagWidth, flagHeight);
      setSprite(Sprites.Items.Flagpole.flag());
      position.z = 2;
    }

    @Override
    public void onIntersect(Animation animation, Overlap.X horizontal, Overlap.Y vertical) {
      if (animation == stone) {
        if (!isStartHeroWalk) {
          velocity.setValue(0, 0);
          isStartHeroWalk = true;
          Context.getAudio().play(Assets.Audio.Music.Level_Complete);
        }
      }
    }
  };

  public Flagpole() {
    setRectangle(0, 0, 2, 164);
    setLadder(true);
    add(flag);
    add(stone);
  }

  @Override
  public void onIntersect(Animation animation, Overlap.X horizontal, Overlap.Y vertical) {
    if (horizontal != null) {
      if (animation instanceof Hero) {
        Hero hero = (Hero) animation;
        if (hero.getMass() != 0) {
          onCollideHero(hero);
        }
      }
    }
  }

  private void onCollideHero(Hero hero) {
    this.hero = hero;
    if (!isStartFlagFall) {
      isStartFlagFall = true;
      GameplayScreen.disableJoystick();
      flag.velocity.y = Gameplay.flagFallDownVertical;
      Context.getAudio().play(Assets.Audio.Sounds.Flagpole);
    }
  }

  @Override
  public void onDrawSpriteIfVisible(Graphics graphics) {
    super.onDrawSpriteIfVisible(graphics);
    double ox = position.x + width() / 2;
    double oy = position.y;

    if (!graphics.containsFillRectangle()) {
      graphics.fillRectangle(ox - 1, oy, ox + 1, oy + height(), Colors.green);
    }

    // circle
    if (!graphics.containsDrawTexture()) {
      double circleWidth = Sprites.Items.Flagpole.circleWidth;
      double circleHeight = Sprites.Items.Flagpole.circleHeight;
      graphics.drawTexture(Sprites.Items.Flagpole.circle(),
          ox - circleWidth / 2, oy - circleHeight / 2, ox + circleWidth / 2, oy + circleHeight / 2,
          null, 0, 0, Colors.white, false, false);
    }

  }

  @Override
  public Flagpole build() {
    return this;
  }

  @Override
  public void onTick(double elapsedTime) {
    super.onTick(elapsedTime);
    if (hero != null && isStartHeroWalk) {
      hero.joystickRight();
    }
  }

  // support preview

  @Override
  public Flagpole setPosition(double x, double y, double z) {
    stone.setBottom(200);
    super.setPosition(x, 0, z);
    setBottom(stone.bottom());
    stone.position.x = left() - (stone.sprite.getWidth() - width()) / 2;

    double ox = position.x + width() / 2;
    double oy = position.y + Sprites.Items.Flagpole.circleHeight / 2;
    flag.setPosition(ox - flag.getSize().width, oy);
    return this;
  }

  @Override
  public void onAdd() {
    super.onAdd();
    redraw();
  }

}
