package mario.render;

import featurea.graphics.Graphics;

public final class Slices {

  private Slices() {
    // no op
  }

  public static final Slice shapes = new Slice() {
    @Override
    public void draw(Graphics graphics) {
      graphics.getDrawLineAndDrawRectangleBach().drawBuffers(graphics);
    }
  };

  public static final Slice textures = new Slice() {
    @Override
    public void draw(Graphics graphics) {
      graphics.getFillRectangleBatch().drawBuffers(graphics);
      graphics.getDrawTextureBatch().drawBuffers(graphics);
    }
  };

}
