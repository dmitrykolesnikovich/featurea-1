package featurea.platformer.util;

import featurea.app.Camera;
import featurea.app.Context;
import featurea.graphics.Graphics;
import featurea.opengl.Texture;
import featurea.platformer.Animation;
import featurea.platformer.physics.WorldLayer;
import featurea.util.ArrayUtil;
import featurea.util.Colors;
import featurea.util.StringUtil;
import featurea.util.Vector;

import java.util.HashMap;
import java.util.Map;

public class Background extends Animation {

  private Vector step;
  private final Map<String, int[][]> sprites = new HashMap<>();

  public Background() {
    position.z = -100;
  }

  public Vector getStep() {
    return step;
  }

  public void setStep(Vector step) {
    this.step = step;
  }

  public Background setValue(String string) {
    String[] tokens = StringUtil.split(string, ";");
    for (String token : tokens) {
      String[] spriteAndCoordinates = StringUtil.split(token, ",");
      String sprite = spriteAndCoordinates[0];
      int size = 2;
      if (sprite.endsWith(".png.3")) {
        sprite = sprite.replaceAll(".3", "");
        size = 3;
      }

      int[][] chunks = ArrayUtil.twoDimensions(spriteAndCoordinates, size, 1, spriteAndCoordinates.length - 1);
      sprites.put(sprite, chunks);
    }
    return this;
  }

  @Override
  public void onDrawSpriteIfVisible(Graphics graphics) {
    if (!graphics.containsFillRectangle()) {
      onFillBackground(graphics);
    }
    if (!graphics.containsDrawTexture()) {
      for (Map.Entry<String, int[][]> entry : sprites.entrySet()) {
        String sprite = entry.getKey();
        int[][] coordinates = entry.getValue();
        String file = getFile(graphics, sprite);
        drawSprite(graphics, coordinates, file);
      }
    }
  }

  protected void onFillBackground(Graphics graphics) {
    // no op
  }

  private void drawSprite(Graphics graphics, int[][] coordinates, String file) {
    Texture texture = Context.getResources().getTexture(file);
    if (texture != null) {
      double width = texture.getWidth();
      double height = texture.getHeight();
      WorldLayer layer = (WorldLayer) graphics.getLayer();
      Camera camera = layer.getCamera();
      double right = Context.isFeaturea() ? layer.getSize().width : camera.right();
      for (double counter = 0, x1 = 0, y2 = 0; x1 < right && y2 < camera.bottom(); counter++) {
        for (int i = 0; i < coordinates.length; i++) {
          int numberOfCopies = 1;
          if (coordinates[i].length == 3) {
            numberOfCopies = coordinates[i][2];
          }
          for (int n = 0; n < numberOfCopies; n++) {
            x1 = coordinates[i][0] + step.x * counter;
            y2 = coordinates[i][1] + step.y * counter;
            x1 += n * width;
            double x2 = x1 + width;
            double y1 = y2 - height;
            // >> IMPORANT apply positioin
            x1 += position.x;
            x2 += position.x;
            y1 += position.y;
            y2 += position.y;
            // <<
            if (x1 < right && y2 < camera.bottom()) {
              graphics.drawTexture(file, x1, y1, x2, y2, null, 0, 0, Colors.white, false, false);
            }
          }
        }
      }
    } else {
      Context.getLoader().load(file);
    }
  }

  protected String getFile(Graphics graphics, String sprite) {
    return sprite;
  }

}

