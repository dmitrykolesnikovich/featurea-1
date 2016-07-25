package mario.objects.enemies;

import featurea.platformer.physics.Body;
import mario.Sprites;
import mario.config.Gameplay;

public class FishFly extends Body {

  private static final double width = Sprites.Enemies.fishWidth;
  private static final double height = Sprites.Enemies.fishHeight;

  private boolean isActive;
  private boolean isTop;

  private double currentJumpTime;

  public FishFly() {
    setFps(2);
    setSize(width, height);
    setRectangle(0, 0, width, height);
    position.z = 101;
  }

  @Override
  public String onUpdateSprite() {
    return Sprites.Enemies.fish();
  }

  @Override
  public void move(double dx, double dy, double dz) {
    if (position.y >= getLayer().getCamera().bottom() + height && dy > 0) {
      isActive = false;
      velocity.x = 0;
      setMass(0);
    } else {
      super.move(dx, dy, dz);
    }
  }

  @Override
  public void onTick(double elapsedTime) {
    super.onTick(elapsedTime);
    if (!isTop) {
      if (position.y < 24) {
        isTop = true;
        if (velocity.y < 0) {
          velocity.y = Math.max(-10, velocity.y);
        }
      }
    }

    if (!isActive) {
      currentJumpTime += elapsedTime;
      if (currentJumpTime > Gameplay.fishFlyJumpPeriod) {
        isActive = true;
        currentJumpTime = 0;
        setMass(0.75);
        fly();
        isTop = false;
        velocity.x = 110;
      }
    }
  }

}
