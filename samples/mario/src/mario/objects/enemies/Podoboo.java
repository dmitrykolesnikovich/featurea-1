package mario.objects.enemies;

import featurea.motion.Movement;
import featurea.platformer.Animation;
import featurea.platformer.overlap.Overlap;
import featurea.platformer.physics.Body;
import mario.Sprites;
import mario.objects.hero.Hero;

public class Podoboo extends Body {

  private static final double width = Sprites.Enemies.podobooWidth;
  private static final double height = Sprites.Enemies.podobooHeight;
  private Movement bounce;

  private int distance;

  public Podoboo() {
    setRectangle(0, 0, width, height);
    setSize(width, height);
  }

  @Override
  public Podoboo build() {
    super.build();
    bounce = (Movement) new Movement().setGraph(0, distance, 0).setVelocity(0.1).setBounce(true);
    timeline.add(bounce);
    return this;
  }

  @Override
  public String onUpdateSprite() {
    return Sprites.Enemies.podoboo();
  }

  public int getDistance() {
    return distance;
  }

  public void setDistance(int distance) {
    this.distance = distance;
  }

  @Override
  public boolean isFlipY() {
    return bounce.isReverse();
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
