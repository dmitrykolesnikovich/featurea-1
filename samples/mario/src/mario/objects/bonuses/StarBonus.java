package mario.objects.bonuses;

import mario.Sprites;
import mario.config.Gameplay;

public class StarBonus extends Bonus {

  private static final double width = Sprites.Items.starWidth;
  private static final double height = Sprites.Items.starHeight;

  public StarBonus() {
    setSize(width, height);
    setRectangle(0, 0, width, height);
    setFps(16);
    isAppear = true;
  }

  @Override
  public String onUpdateSprite() {
    return Sprites.Items.star();
  }

  @Override
  public void onTick(double elapsedTime) {
    super.onTick(elapsedTime);
    if (hasPlatform()) {
      jump();
    }
  }

  @Override
  public void onCreate() {
    super.onCreate();
    setMass(Gameplay.mass);
  }

}
