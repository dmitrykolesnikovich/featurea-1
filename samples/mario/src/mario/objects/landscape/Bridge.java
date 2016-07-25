package mario.objects.landscape;

import featurea.graphics.Graphics;
import featurea.platformer.Animation;
import featurea.platformer.physics.Body;
import featurea.util.Colors;
import mario.Sprites;

public class Bridge extends Body {

  private int count;

  public Bridge() {
    setSolid(true);
    setCount(1);
  }

  public Bridge setCount(int count) {
    this.count = count;
    return this;
  }

  @Override
  public Animation build() {
    double x1 = 0;
    double y1 = 0;
    double x2 = x1 + Sprites.Items.bridgeWidth * count;
    double y2 = y1 + Sprites.Items.bridgeHeight;
    setRectangle(x1, y1, x2, y2);
    return this;
  }

  @Override
  public void onDrawSpriteIfVisible(Graphics graphics) {
    graphics.drawTile(Sprites.Items.bridge(), left(), bottom(), right(), top(), null, ox(), oy(), Colors.white, false, false, null);
  }

  @Override
  public Animation setRectangle(double x1, double y1, double x2, double y2) {
    return super.setRectangle(x1, y1 + (y2 - y1) / 2, x2, y2);
  }


}
