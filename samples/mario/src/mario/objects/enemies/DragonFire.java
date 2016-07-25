package mario.objects.enemies;

import featurea.platformer.Animation;
import featurea.platformer.overlap.Overlap;
import featurea.platformer.physics.Body;
import mario.Sprites;
import mario.objects.hero.Hero;

public class DragonFire extends Body {

  private static final double width = Sprites.Enemies.dragonFireBallWidth;
  private static final double height = Sprites.Enemies.dragonFireBallHeight;

  public DragonFire() {
    setSize(width, height);
    setRectangle(0, 0, width, height);
    position.z = 101;
  }

  @Override
  public String onUpdateSprite() {
    return Sprites.Enemies.dragonFireBall();
  }

  @Override
  public void onIntersect(Animation animation, Overlap.X horizontal, Overlap.Y vertical) {
    super.onIntersect(animation, horizontal, vertical);
    if (animation instanceof Hero) {
      Hero hero = (Hero) animation;
      hero.damage();
    }
  }

}
