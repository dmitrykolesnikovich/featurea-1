package mario.objects.bonuses;

import featurea.app.Layer;
import mario.Sprites;
import mario.objects.hero.HeroState;
import mario.objects.hero.World;

public class BecomeFieryBonus extends Bonus {

  private static final double width = Sprites.Items.fireFlowerWidth;
  private static final double height = Sprites.Items.fireFlowerHeight;

  public BecomeFieryBonus() {
    setSize(width, height);
    setRectangle(0, 0, width, height);
  }

  @Override
  public boolean work(Layer layer) {
    World world = (World) layer;
    if (world.getHero().getState() != HeroState.small) {
      return super.work(layer);
    } else {
      BecomeBigBonus bonus = (BecomeBigBonus) new BecomeBigBonus().setBlock(getBlock()).setPosition(position).build();
      return bonus.work(layer);
    }
  }

  @Override
  public String onUpdateSprite() {
    return Sprites.Items.fireFlower();
  }

}
