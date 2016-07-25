package mario.objects.enemies;

import mario.Sprites;

public class Beetle extends Enemy {

  private static final double width = Sprites.Enemies.Beetle.moveWidth;
  private static final double height = Sprites.Enemies.Beetle.moveHeight;

  public Beetle() {
    setRectangle(0, 0, width, height);
    setSize(width, height);
    setFps(4);
  }

  @Override
  public String onUpdateSprite() {
    if (isDead()) {
      return Sprites.Enemies.Beetle.dead();
    } else {
      return Sprites.Enemies.Beetle.move();
    }
  }

  @Override
  public void destroyBecauseOfFire() {
    /*super.fireDamage();*/
  }

}
