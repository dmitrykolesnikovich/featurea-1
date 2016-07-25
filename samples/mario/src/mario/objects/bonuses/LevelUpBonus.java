package mario.objects.bonuses;

import featurea.platformer.Animation;
import featurea.platformer.overlap.Overlap;
import featurea.platformer.physics.Body;
import mario.Sprites;
import mario.config.Gameplay;

public class LevelUpBonus extends Bonus {

  private static final double width = Sprites.Items.mushroomWidth;
  private static final double height = Sprites.Items.mushroomHeight;

  public LevelUpBonus() {
    setSize(width, height);
    setRectangle(0, 0, width, height);
    isAppear = true;
  }

  @Override
  public String onUpdateSprite() {
    return Sprites.Items.levelUp();
  }

  @Override
  public void onCreate() {
    super.onCreate();
    setMass(Gameplay.mass);
  }

}
