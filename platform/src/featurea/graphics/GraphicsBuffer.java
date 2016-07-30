package featurea.graphics;

import featurea.app.Area;
import featurea.app.Layer;

public abstract class GraphicsBuffer {

  public final Area area;

  public GraphicsBuffer(Area area) {
    this.area = area;
  }

  public abstract Layer getLayer();

  public void clearDrawLine() {
    Canvas canvas = getCanvas();
    if (canvas != null) {
      canvas.clearDrawLinesAndDrawRectangle(area);
    }
  }

  public void clearDrawRectangle() {
    clearDrawLine(); // todo separate
  }

  public void clearFillRectangle() {
    Canvas canvas = getCanvas();
    if (canvas != null) {
      canvas.clearFillRectangle(area);
    }
  }

  public void clearFillShape() {
    Canvas canvas = getCanvas();
    if (canvas != null) {
      canvas.clearFillShape(area);
    }
  }

  public void clearDrawTexture() {
    Canvas canvas = getCanvas();
    if (canvas != null) {
      canvas.clearDrawTexture(area);
    }
  }

  public void clearAll() {
    clearDrawLine();
    clearDrawRectangle();
    clearFillRectangle();
    clearFillShape();
    clearDrawTexture();
  }

  /*private API*/

  private Canvas getCanvas() {
    Layer layer = getLayer();
    if (layer != null) {
      return layer.getCanvas();
    } else {
      return null;
    }
  }


}
