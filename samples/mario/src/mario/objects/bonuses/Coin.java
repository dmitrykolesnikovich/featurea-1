package mario.objects.bonuses;

import featurea.platformer.Animation;
import featurea.platformer.overlap.Overlap;
import featurea.platformer.physics.Body;
import mario.Sprites;
import mario.objects.hero.Hero;
import mario.objects.hero.World;

public class Coin extends Body {

  private static final double width = Sprites.Items.Coin.blinkWidth;
  private static final double height = Sprites.Items.Coin.blinkHeight;

  public Coin() {
    setSize(width, height);
    setRectangle(2, 0, width - 2, height);
  }

  @Override
  public String onUpdateSprite() {
    return Sprites.Items.Coin.blink();
  }

  @Override
  public String toString() {
    return "Coin";
  }

  @Override
  public void onIntersect(Animation animation, Overlap.X horizontal, Overlap.Y vertical) {
    super.onIntersect(animation, horizontal, vertical);
    if (animation == getWorld().getHero()) {
      getWorld().getHero().obtainCoin(this);
    }
  }

  private World getWorld() {
    return (World) getLayer();
  }

}
