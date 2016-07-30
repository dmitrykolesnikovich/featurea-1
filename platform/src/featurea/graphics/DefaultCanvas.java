package featurea.graphics;

import featurea.app.Area;
import featurea.app.Layer;
import featurea.opengl.batches.DrawLineAndDrawRectangleBatch;
import featurea.opengl.batches.DrawTextureBatch;
import featurea.opengl.batches.FillRectangleBatch;
import featurea.opengl.batches.FillShapeBatch;

public class DefaultCanvas extends Canvas {

  public Graphics graphics;

  public DefaultCanvas setLayer(Layer layer) {
    super.setLayer(layer);
    graphics = new DefaultGraphics().setLayer(layer).build();
    return this;
  }

  @Override
  public Graphics getGraphics(Area area) {
    return graphics;
  }

  @Override
  public void onDrawBuffers(Layer layer) {
    DrawLineAndDrawRectangleBatch drawLineAndDrawRectangleBatch = graphics.getDrawLineAndDrawRectangleBach();
    if (drawLineAndDrawRectangleBatch != null) {
      drawLineAndDrawRectangleBatch.drawBuffers(graphics);
    }
    FillRectangleBatch fillRectangleBatch = graphics.getFillRectangleBatch();
    if (fillRectangleBatch != null) {
      fillRectangleBatch.drawBuffers(graphics);
    }
    FillShapeBatch fillShapeBatch = graphics.getFillShapeBatch();
    if (fillShapeBatch != null) {
      fillShapeBatch.drawBuffers(graphics);
    }
    DrawTextureBatch drawTextureBatch = graphics.getDrawTextureBatch();
    if (drawTextureBatch != null) {
      drawTextureBatch.drawBuffers(graphics);
    }
  }

}
