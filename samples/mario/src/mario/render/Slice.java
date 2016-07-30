package mario.render;

import featurea.graphics.Graphics;

public abstract class Slice {

  public void draw(WorldCanvas worldCanvas) {
    for (Graphics graphics : worldCanvas.listGraphics()) {
      if (graphics.isShown) {
        draw(graphics);
      }
    }
  }

  public abstract void draw(Graphics graphics);

}
