package mario.objects.enemies;

import featurea.platformer.Animation;
import featurea.platformer.overlap.Overlap;
import featurea.platformer.physics.Body;
import mario.Sprites;
import mario.objects.hero.Hero;
import mario.objects.hero.World;

public class Spiny extends Body {

  private boolean isWalk;
  private static final double width = Sprites.Enemies.Spiny.bornWidth;
  private static final double height = Sprites.Enemies.Spiny.bornHeight;

  public Spiny() {
    setMass(0.2);
    setSize(width, height);
    setRectangle(0, 0, width, height);
    setFps(2);
  }

  @Override
  public void onIntersect(Animation animation, Overlap.X horizontal, Overlap.Y vertical) {
    if (vertical == Overlap.Y.bottom) {
      if (!isWalk) {
        isWalk = true;
        velocity.x = 0;
      }
    }
  }

  @Override
  public String onUpdateSprite() {
    if (isWalk) {
      return Sprites.Enemies.Spiny.walk();
    } else {
      return Sprites.Enemies.Spiny.born();
    }
  }

  @Override
  public void onTick(double elapsedTime) {
    super.onTick(elapsedTime);
    if (isWalk) {
      Hero hero = getWorld().getHero();
      if (Math.abs(hero.position.x - position.x) > width * 2) {
        if (hero.position.x < position.x) {
          velocity.x = -Math.abs(velocity.x);
        } else {
          velocity.x = Math.abs(velocity.x);
        }
      }
    }
  }

  @Override
  public double getHorizontalVelocity() {
    return 25;
  }

  private World getWorld() {
    return (World) super.getLayer();
  }

}
