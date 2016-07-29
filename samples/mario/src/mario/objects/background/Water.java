package mario.objects.background;

import featurea.graphics.Graphics;
import featurea.platformer.Animation;
import featurea.platformer.config.Engine;
import featurea.util.Color;
import mario.Sprites;
import mario.Theme;

import static mario.Sprites.Background.Water.waveHeight;
import static mario.Sprites.Background.Water.waveWidth;

public class Water extends Animation {

  private static final Color overworldBackgroundColor = new Color(60f / 255f, 188f / 255f, 252f / 255f, 1);
  private static final Color castleBackgroundColor = new Color(216f / 255f, 40f / 255f, 0f / 255f, 1);
  private int length;

  public Water() {
    setLength(1);
  }

  public Water setLength(int length) {
    this.length = length;
    return this;
  }

  @Override
  public Water build() {
    setRectangle(0, 0, length * waveWidth, Engine.cameraHeight - position.y);
    return this;
  }

  @Override
  protected void onDrawSpriteIfVisible(Graphics graphics) {
    super.onDrawSpriteIfVisible(graphics);
    if (!graphics.containsDrawTexture() || !graphics.containsFillRectangle()) {
      Color backgroundColor;
      if (Sprites.theme == Theme.castle) {
        backgroundColor = castleBackgroundColor;
      } else {
        backgroundColor = overworldBackgroundColor;
      }
      double x1 = position.x;
      double x2 = x1 + length * waveWidth;
      double y1 = position.y;
      double y2 = graphics.getLayer().getCamera().bottom();
      graphics.fillRectangle(x1, y1 + waveHeight, x2, y2, backgroundColor);
      graphics.drawTile(Sprites.Background.Water.wave(), x1, y1, x2, y1 + waveHeight, null, 0, 0, null, false, false);
    }
  }

}
