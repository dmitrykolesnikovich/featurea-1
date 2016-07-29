package mario.objects.landscape;

import featurea.graphics.Graphics;
import featurea.platformer.physics.Body;
import featurea.util.Colors;
import mario.Sprites;

public class Platform extends Body {

  private int length;
  private int highness;

  public Platform() {
    setSolid(true);
    setMotionableHorizontally(false);
    setMotionableVertically(false);
    setLength(1);
    setHighness(1);
  }

  @Override
  public Platform build() {
    if (length != -1) {
      setRectangle(0, 0, length * Sprites.Items.groundWidth, highness * Sprites.Items.groundHeight);
    }
    return this;
  }

  public Platform setLength(int length) {
    this.length = length;
    return this;
  }

  public Platform setHighness(int highness) {
    this.highness = highness;
    return this;
  }

  @Override
  public void onDrawSpriteIfVisible(Graphics graphics) {
    if (!graphics.containsDrawTexture()) {
      double x1 = left();
      double y1 = top();
      double x2 = right();
      double y2 = bottom();
      graphics.drawTile(sprite.getCurrentFile(), x1, y1, x2, y2, null, 0, 0, Colors.white, false, false);
    }
  }

  @Override
  public String onUpdateSprite() {
    return Sprites.Items.ground();
  }

}
