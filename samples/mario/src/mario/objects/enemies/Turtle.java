package mario.objects.enemies;

import featurea.app.Context;
import featurea.motion.Movement;
import featurea.motion.Rotation;
import mario.Sprites;
import mario.config.Gameplay;

public class Turtle extends Enemy {

  private double totalTime;

  public Turtle() {
    double width = Sprites.Enemies.Turtle.stayWidth;
    double height = Sprites.Enemies.Turtle.stayHeight;
    setSprite(Sprites.Enemies.Turtle.stay());
    setSize(width, height);
    setRectangle(2, 8, width - 2, height);
    setFps(2);
  }

  public void shot() {
    setSprite(Sprites.Enemies.Turtle.shot());
    Context.getTimer().delay(new Runnable() {
      @Override
      public void run() {
        TurtleHammer hammer = new TurtleHammer();
        hammer.setPosition(position);
        double k = velocity.x >= 0 ? 1 : -1;
        hammer.timeline.add(new Movement().setGraph(k, 0.25, 0).setVelocity(0.1).setLoop(true));
        hammer.timeline.add(new Rotation().setGraph(k * 360).setVelocity(0.4).setLoop(true));
        getLayer().add(hammer);
      }
    });
    Context.getTimer().delay(new Runnable() {
      @Override
      public void run() {
        setSprite(Sprites.Enemies.Turtle.stay());
      }
    }, 1200);
  }

  @Override
  public void onTick(double elapsedTime) {
    super.onTick(elapsedTime);
    totalTime += elapsedTime;
    if (totalTime > Gameplay.turtleShotTime) {
      totalTime %= Gameplay.turtleShotTime;
      shot();
    }
  }

}
