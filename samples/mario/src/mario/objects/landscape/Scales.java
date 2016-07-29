package mario.objects.landscape;

import featurea.graphics.Graphics;
import featurea.platformer.Animation;
import featurea.util.Color;
import featurea.util.Colors;
import mario.Sprites;
import mario.config.Gameplay;

public class Scales extends Animation {

  private static final double bearingWidth = Sprites.Items.bearingWidth;
  private static final double bearingHeight = Sprites.Items.bearingHeight;
  private static final Color ropeColor = new Color().setValue(Gameplay.ropeColor);

  private int x1;
  private int x2;
  private int height;
  private int length;
  private double resistance;

  // local
  private double ropeLeftX;
  private double ropeRightX;
  private double ropeTopY;
  private double oneSideLength;
  private Platto platto1;
  private Platto platto2;
  private double dy1;
  private double dy2;

  public int getX1() {
    return x1;
  }

  public void setX1(int x1) {
    this.x1 = x1;
  }

  public int getX2() {
    return x2;
  }

  public void setX2(int x2) {
    this.x2 = x2;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public int getLength() {
    return length;
  }

  public void setLength(int length) {
    this.length = length;
  }

  public void setResistance(double resistance) {
    this.resistance = resistance;
  }

  /*XML API*/

  public Scales build() {
    super.build();

    if (platto1 != null) {
      remove(platto1);
    }
    if (platto2 != null) {
      remove(platto2);
    }

    ropeLeftX = x1 - bearingWidth / 2;
    ropeRightX = x2 + bearingWidth / 2;
    ropeTopY = height - bearingHeight / 2;
    double deltaX = Math.abs(x2 - x1) + bearingWidth + bearingHeight;
    double sideLength = length - deltaX;
    oneSideLength = sideLength / 2;

    platto1 = new Platto() {
      @Override
      public void move(double dx, double dy, double dz) {
        dy *= resistance;
        super.move(dx, dy, dz);
        dy1 = dy;
      }
    };
    platto1.setCount(6);
    platto1.build();
    platto1.setPosition(ropeLeftX - platto1.width() / 2, height + oneSideLength, 0);
    add(platto1);

    platto2 = new Platto() {
      @Override
      public void move(double dx, double dy, double dz) {
        dy *= resistance;
        super.move(dx, dy, dz);
        dy2 = dy;
      }
    };
    platto2.setCount(6);
    platto2.build();
    platto2.setPosition(ropeRightX - platto2.width() / 2, height + oneSideLength, 0);
    add(platto2);

    return this;
  }

  /*Area API*/

  @Override
  protected void onDrawSpriteIfVisible(Graphics graphics) {
    super.onDrawSpriteIfVisible(graphics);

    if (!graphics.containsDrawTexture()) {
      double bearingWidth = Sprites.Items.bearingWidth;
      double bearingHeight = Sprites.Items.bearingHeight;

      graphics.drawTexture(Sprites.Items.bearing(),
          ropeLeftX, ropeTopY, ropeLeftX + bearingWidth, ropeTopY + bearingHeight, null, 0, 0,
          Colors.white, false, false);

      graphics.drawTexture(Sprites.Items.bearing(),
          ropeRightX - bearingWidth, ropeTopY, ropeRightX, ropeTopY + bearingHeight, null, 0, 0,
          Colors.white, false, false);
    }

    // rope
    if (!graphics.containsFillRectangle()) {
      graphics.fillRectangle(ropeLeftX - 1, height, ropeLeftX + 1, platto1.position.y, ropeColor);
      graphics.fillRectangle(ropeRightX - 1, height, ropeRightX + 1, platto2.position.y, ropeColor);
      graphics.fillRectangle(x1, ropeTopY - 1, x2, ropeTopY + 1, ropeColor);
    }
  }

  @Override
  public void onTick(double elapsedTime) {
    if (dy1 > 0) {
      platto2.move(0, -dy1, 0);
    }
    if (dy2 > 0) {
      platto1.move(0, -dy2, 0);
    }
    dy1 = 0;
    dy2 = 0;
  }

  @Override
  public boolean shouldBeSwipedFromBuffer() {
    return platto1.shouldBeSwipedFromBuffer() || platto2.shouldBeSwipedFromBuffer();
  }

}
