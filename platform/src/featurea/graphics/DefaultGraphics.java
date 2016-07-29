package featurea.graphics;

import featurea.opengl.Batches;

public class DefaultGraphics extends Graphics {

  public DefaultGraphics() {
    setDrawLineAndDrawRectangleBach(Batches.newDrawLineAndDrawRectangleBatch());
    setFillRectangleBatch(Batches.newFillRectangleBatch());
    setFillShapeBatch(Batches.newFillShapeBatch());
    setDrawTextureBatch(Batches.newDrawTextureBatch());
  }

}
