package mario.objects.bonuses;

import featurea.app.Context;
import featurea.platformer.Animation;
import featurea.platformer.physics.Body;
import featurea.util.Vector;
import mario.Assets;
import mario.Navigation;
import mario.Sprites;
import mario.WorldFirework;
import mario.config.Gameplay;
import mario.objects.hero.Hero;

public class Firework extends Animation {

  private double currentTime;
  private int count;
  private int currentCount;

  public Firework setCount(int count) {
    this.count = count;
    return this;
  }

  public Firework setCount(Hero hero) {
    double[] values = WorldFirework.getValue();
    if (values == null) {
      throw new IllegalStateException();
    }
    int count;
    if (Navigation.preferences.score > values[1]) {
      count = 6;
    } else if (Navigation.preferences.score > values[0]) {
      count = 3;
    } else {
      count = 0;
    }
    return setCount(count);
  }

  @Override
  public void onTick(double elapsedTime) {
    super.onTick(elapsedTime);
    if (count != 0) {
      currentTime += elapsedTime;
      if (currentTime >= Gameplay.fireworkDelay) {
        currentTime %= Gameplay.fireworkDelay;
        currentCount++;
        makeFire(getRandomVector());
      }
    }
    if (currentCount >= count) {
      onFinishFirework();
    }
  }

  private void makeFire(Vector vector) {
    final Body fire = new Body();
    fire.sprite.setLoop(false);
    fire.setSprite(Sprites.Items.Fireball.dead());
    fire.setSize(Sprites.Items.Fireball.deadWidth, Sprites.Items.Fireball.deadHeight);
    fire.setPosition(vector);
    Context.getTimer().delay(new Runnable() {
      @Override
      public void run() {
        fire.removeSelf();
      }
    }, Gameplay.fireworkDelay / 2);
    add(fire);
    Context.getAudio().play(Assets.Audio.Sounds.Firework);
  }

  private Vector getRandomVector() {
    double minX = position.x - 20;
    double minY = position.y;
    double maxX = position.x + 80;
    double maxY = 100;
    double x = Math.random() * (maxX - minX) + minX;
    double y = Math.random() * (maxY - minY) + minY;
    return new Vector(x, y);
  }

  protected void onFinishFirework() {
    // no op
  }

  @Override
  public final Firework setPosition(Vector position) {
    return (Firework) super.setPosition(position);
  }

}
