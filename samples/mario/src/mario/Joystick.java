package mario;

import featurea.platformer.physics.HeroBody;

public class Joystick extends featurea.platformer.util.Joystick {

  @Override
  public HeroBody getHero() {
    return GameplayScreen.getWorld().getHero();
  }

}
