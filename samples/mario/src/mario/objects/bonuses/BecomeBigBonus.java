package mario.objects.bonuses;

import featurea.app.Layer;
import mario.Sprites;
import mario.config.Gameplay;
import mario.objects.hero.HeroState;
import mario.objects.hero.World;

public class BecomeBigBonus extends Bonus {

  private static final double width = Sprites.Items.mushroomWidth;
  private static final double height = Sprites.Items.mushroomHeight;

  public BecomeBigBonus() {
    setSize(width, height);
    setRectangle(0, 0, width, height);
    isAppear = true;
  }

  @Override
  public boolean work(Layer layer) {
    World world = (World) layer;
    if (world.getHero().getState() == HeroState.small) {
      return super.work(layer);
    } else {
      BecomeFieryBonus bonus = (BecomeFieryBonus) new BecomeFieryBonus().setBlock(getBlock()).setPosition(position).build();
      return bonus.work(layer);
    }
  }

  @Override
  public String onUpdateSprite() {
    return Sprites.Items.mushroom();
  }

  @Override
  public void onCreate() {
    super.onCreate();
    setMass(Gameplay.mass);
  }

}