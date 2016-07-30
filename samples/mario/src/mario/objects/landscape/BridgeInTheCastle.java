package mario.objects.landscape;

import featurea.app.Context;
import featurea.graphics.Graphics;
import featurea.platformer.Animation;
import featurea.platformer.overlap.Overlap;
import featurea.platformer.physics.Body;
import featurea.util.Colors;
import mario.Sprites;
import mario.config.Gameplay;
import mario.objects.hero.Hero;

public class BridgeInTheCastle extends Body {

  private int length;
  private boolean isOpenProgress;

  private double currentOpenTime;
  private static final double width = Sprites.Items.dragonBridgeWidth;
  private static final double height = Sprites.Items.dragonBridgeHeight;
  private static final String texture = Sprites.Items.dragonBridge();

  public BridgeInTheCastle() {
    setSolid(true);
  }

  public int getLength() {
    return length;
  }

  public void setLength(int length) {
    this.length = length;
    setRectangle(0, 0, width * length, height);
    redraw();
  }

  @Override
  public void onDrawSpriteIfVisible(Graphics graphics) {
    if (!graphics.containsDrawTexture()) {
      double x = position.x;
      double y = position.y;
      for (int i = 0; i < length; i++) {
        double x1 = x + width * i;
        double x2 = x1 + width;
        double y1 = y;
        double y2 = y1 + height;
        graphics.drawTexture(texture, x1, y1, x2, y2, null, x, y, Colors.white, false, false);
      }
    }
  }

  public void open() {
    isOpenProgress = true;
  }

  @Override
  public void onTick(double elapsedTime) {
    super.onTick(elapsedTime);
    if (isOpenProgress && !isOpen()) {
      currentOpenTime += elapsedTime;
      if (currentOpenTime >= Gameplay.bridgeOpenPeriod) {
        currentOpenTime %= Gameplay.bridgeOpenPeriod;
        length--;
        setLength(length);
      }
    }
    if (isOpen()) {
      Context.getTimer().delay(new Runnable() {
        @Override
        public void run() {
          removeSelf();
        }
      });
    }
  }

  @Override
  public void onIntersect(Animation animation, Overlap.X horizontal, Overlap.Y vertical) {
    if (horizontal == Overlap.X.right) {
      if (!isOpenProgress) {
        if (animation instanceof Hero) {
          open();
        }
      }
    }
  }

  public boolean isOpen() {
    return length <= 0;
  }

}
