package mario.objects.enemies;

import featurea.platformer.physics.Body;
import mario.Sprites;

public class CannonBullet extends Body {

  private static final double width = Sprites.Enemies.Cannon.bulletWidth;
  private static final double height = Sprites.Enemies.Cannon.bulletHeight;

  public CannonBullet() {
    setSize(width, height);
    setRectangle(0, 0, width, height);
  }

  @Override
  public String onUpdateSprite() {
    return Sprites.Enemies.Cannon.bullet();
  }

}
