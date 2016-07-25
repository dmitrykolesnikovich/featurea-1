package mario.objects.landscape;

import featurea.graphics.Graphics;
import featurea.platformer.physics.Body;
import featurea.util.Color;
import featurea.util.Colors;
import mario.Sprites;
import mario.objects.hero.World;

public class GiantMushroom extends Body {

  private static final Color TRUNK_TOP_COLOR_FOR_FIRST_MODE = new Color(0, 168.0f / 255.0f, 0, 1);

  public enum Mode {
    first, second
  }

  private double count;
  private Mode mode;

  private String headTile;
  private String headLeftSprite;
  private String headRightSprite;
  private String trunkTile;
  private String trunkTopSprite;

  private double headWidth;
  private double headHeight;
  private double headLeftWidth;
  private double headLeftHeight;
  private double headRightWidth;
  private double headRightHeight;
  private double trunkWidth;
  private double trunkHeight;
  private double trunkTopWidth;
  private double trunkTopHeight;

  public GiantMushroom() {
    setSolid(true);
  }

  @Override
  public Body build() {
    setRectangle(0, 0, count * headWidth, headHeight);
    return this;
  }

  public double getCount() {
    return count;
  }

  public void setCount(double count) {
    this.count = count;
  }

  public Mode getMode() {
    return mode;
  }

  public void setMode(Mode mode) {
    this.mode = mode;
    if (mode == Mode.first) {
      headTile = Sprites.Items.GiantMushroom1.head();
      headLeftSprite = Sprites.Items.GiantMushroom1.headLeft();
      headRightSprite = Sprites.Items.GiantMushroom1.headRight();
      trunkTile = Sprites.Items.GiantMushroom1.trunk();
      trunkTopSprite = Sprites.Items.GiantMushroom1.trunk();

      headWidth = Sprites.Items.GiantMushroom1.headWidth;
      headHeight = Sprites.Items.GiantMushroom1.headHeight;
      headLeftWidth = Sprites.Items.GiantMushroom1.headLeftWidth;
      headLeftHeight = Sprites.Items.GiantMushroom1.headLeftHeight;
      headRightWidth = Sprites.Items.GiantMushroom1.headRightWidth;
      headRightHeight = Sprites.Items.GiantMushroom1.headRightHeight;
      trunkWidth = Sprites.Items.GiantMushroom1.trunkWidth;
      trunkHeight = Sprites.Items.GiantMushroom1.trunkHeight;
      trunkTopWidth = Sprites.Items.GiantMushroom1.trunkWidth;
      trunkTopHeight = Sprites.Items.GiantMushroom1.trunkHeight;
    } else if (mode == Mode.second) {
      headTile = Sprites.Items.GiantMushroom2.head();
      headLeftSprite = Sprites.Items.GiantMushroom2.headLeft();
      headRightSprite = Sprites.Items.GiantMushroom2.headRight();
      trunkTile = Sprites.Items.GiantMushroom2.trunk();
      trunkTopSprite = Sprites.Items.GiantMushroom2.trunkTop();

      headWidth = Sprites.Items.GiantMushroom2.headWidth;
      headHeight = Sprites.Items.GiantMushroom2.headHeight;
      headLeftWidth = Sprites.Items.GiantMushroom2.headLeftWidth;
      headLeftHeight = Sprites.Items.GiantMushroom2.headLeftHeight;
      headRightWidth = Sprites.Items.GiantMushroom2.headRightWidth;
      headRightHeight = Sprites.Items.GiantMushroom2.headRightHeight;
      trunkWidth = Sprites.Items.GiantMushroom2.trunkWidth;
      trunkHeight = Sprites.Items.GiantMushroom2.trunkHeight;
      trunkTopWidth = Sprites.Items.GiantMushroom2.trunkTopWidth;
      trunkTopHeight = Sprites.Items.GiantMushroom2.trunkTopHeight;
    } else {
      throw new IllegalArgumentException("mode = " + mode);
    }
  }

  @Override
  public void onDrawSpriteIfVisible(Graphics graphics) {
    super.onDrawSpriteIfVisible(graphics);
    drawTrunk(graphics);
    drawTrunkTopLine(graphics);
    drawHead(graphics);
  }

  /*draw*/

  private void drawTrunk(Graphics graphics) {
    double y1 = getHeadBottom() - 2;
    World layer = (World) graphics.layer;
    double y2 = layer.getSize().height;
    if (mode == Mode.first) {
      double x1 = position.x + headWidth;
      double x2 = position.x + (count - 1) * headWidth;
      graphics.drawTile(trunkTile, x1, y1, x2, y2, null, 0, 0, Colors.white, false, false, null);
    } else if (mode == Mode.second) {
      double x = position.x + (count - 1) * headWidth / 2;
      graphics.drawTile(trunkTile, x, y1, x + trunkWidth, y2, null, 0, 0, Colors.white, false, false, null);
    }
  }

  private void drawTrunkTopLine(Graphics graphics) {
    double x = position.x;
    double y = position.y;
    if (mode == Mode.first) {
      double x1 = x + headWidth;
      double x2 = x + (count - 1) * headWidth;
      double y2 = y + headHeight;
      double y1 = y2 - 4;
      graphics.fillRectangle(x1, y1, x2, y2, TRUNK_TOP_COLOR_FOR_FIRST_MODE);
    } else if (mode == Mode.second) {
      double x1 = position.x + (count - 1) * headWidth / 2;
      double x2 = x1 + headWidth;
      double y1 = getHeadBottom() - 2;
      double y2 = y1 + trunkTopHeight;
      graphics.drawTexture(trunkTopSprite, x1, y1, x2, y2, null, 0, 0, Colors.white, false, false, null);
    }
  }

  private void drawHead(Graphics graphics) {
    double headBottom = getHeadBottom();
    double x = position.x;
    double y = position.y;
    for (int index = 0; index < count; index++) {
      String sprite = getSprite(index);
      double width = getSpriteWidth(index);
      double x1 = x + index * width;
      double x2 = x + (index + 1) * width;
      graphics.drawTexture(sprite, x1, y, x2, headBottom, null, 0, 0, Colors.white, false, false, null);
    }
  }

  /*util*/

  private String getSprite(int index) {
    if (index == 0) {
      return headLeftSprite;
    } else if (index == count - 1) {
      return headRightSprite;
    } else {
      return headTile;
    }
  }

  private double getSpriteWidth(int index) {
    if (index == 0) {
      return headLeftWidth;
    } else if (index == count - 1) {
      return headRightWidth;
    } else {
      return headWidth;
    }
  }

  private double getHeadBottom() {
    return position.y + headHeight;
  }

}
