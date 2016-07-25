package mario.objects.enemies;

import featurea.platformer.physics.Body;
import mario.Sprites;

public class TurtleHammer extends Body {

  public TurtleHammer() {
    double width = Sprites.Enemies.hammerWidth;
    double height = Sprites.Enemies.hammerHeight;
    setSprite(Sprites.Enemies.hammer());
    setSize(width, height);
    setRectangle(0, 0, width, height);
  }

}
