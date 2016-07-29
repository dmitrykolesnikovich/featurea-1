package mario.objects.landscape;

import featurea.app.Camera;
import featurea.graphics.Graphics;
import featurea.platformer.physics.Body;
import mario.Sprites;
import mario.config.Gameplay;

public class Platto extends Body {

  private int count;

  public Platto() {
    double width = Sprites.Items.plattoWidth;
    double height = Sprites.Items.plattoHeight;
    setSize(width, height);
    setSolid(true);
    setCount(1);
  }

  @Override
  public String onUpdateSprite() {
    return Sprites.Items.platto();
  }

  public void setCount(int count) {
    this.count = count;
  }

  @Override
  public Platto build() {
    double x1 = 0;
    double y1 = 0;
    double x2 = x1 + Sprites.Items.plattoWidth * count;
    double y2 = y1 + Sprites.Items.plattoHeight;
    setRectangle(x1, y1, x2, y2);
    return this;
  }

  @Override
  public void onDrawSpriteIfVisible(Graphics graphics) {
    if (!graphics.containsDrawTexture()) {
      for (int i = 0; i < count; i++) {
        double x1 = position.x + i * Sprites.Items.plattoWidth;
        double x2 = x1 + Sprites.Items.plattoWidth;
        double y1 = position.y;
        double y2 = y1 + Sprites.Items.plattoHeight;
        sprite.draw(graphics, x1, y1, x2, y2, 0, 0, null, false, false);
      }
    }
  }

  @Override
  public void onTick(double elapsedTime) {
    super.onTick(elapsedTime);
    if (getLayer() != null) {
      Camera camera = getLayer().getCamera();
      if (moveVector.y > 0 && top() >= camera.bottom()) {
        setBottom(Gameplay.hudHeight);
      }
      if (moveVector.y < 0 && bottom() <= Gameplay.hudHeight) {
        setTop(camera.bottom());
      }
    }
  }
}
