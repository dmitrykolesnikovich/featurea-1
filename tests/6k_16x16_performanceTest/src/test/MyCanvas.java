package test;

import featurea.app.Area;
import featurea.app.Context;
import featurea.app.Layer;
import featurea.graphics.Canvas;
import featurea.graphics.Graphics;
import featurea.opengl.batches.DrawTextureBatch;

public class MyCanvas extends Canvas {

  private Graphics graphics;
  private int capacity;

  @Override
  public Graphics getGraphics(Area area) {
    return graphics;
  }

  public MyCanvas setCapacity(int capacity) {
    this.capacity = capacity;
    return this;
  }

  @Override
  public MyCanvas build() {
    super.build();
    DrawTextureBatch drawTextureBatch = new DrawTextureBatch().setCapacity(capacity);
    graphics = new Graphics().setDrawTextureBatch(drawTextureBatch).setLayer(getLayer()).build();
    return this;
  }

  @Override
  public void onDrawBatches(Layer layer) {
    super.onDrawBatches(layer);
    System.out.println("FPS: " + Context.getPerformance().fps);
  }

  @Override
  public void onDrawBuffers(Layer layer) {
    DrawTextureBatch drawTextureBatch = graphics.getDrawTextureBatch();
    if (drawTextureBatch != null) {
      drawTextureBatch.drawBuffers(graphics);
    }
  }

  public Graphics getGraphics() {
    return graphics;
  }
}
