package mario.objects.enemies;

import featurea.motion.Movement;
import featurea.platformer.Animation;
import featurea.platformer.overlap.Overlap;
import featurea.platformer.physics.Body;
import mario.Sprites;
import mario.config.Gameplay;
import mario.objects.hero.Hero;

import static mario.config.Gameplay.fishBounceDistance;

public class Fish extends Body {

  private static final double width = Sprites.Enemies.fishWidth;
  private static final double height = Sprites.Enemies.fishHeight;

  public Fish() {
    setFps(2);
    setSize(width, height);
    setRectangle(0, 0, width, height);
    timeline.add(new Movement().
        setGraph(-fishBounceDistance, fishBounceDistance, 0, -fishBounceDistance, -fishBounceDistance, 0).
        setLoop(true).setVelocity(Gameplay.fishVelocity));
  }

  @Override
  public String onUpdateSprite() {
    return Sprites.Enemies.fish();
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
