package featurea.graphics;

import featurea.app.Area;
import featurea.app.Layer;
import featurea.opengl.Batch;
import featurea.opengl.batches.DrawLineAndDrawRectangleBatch;
import featurea.opengl.batches.DrawTextureBatch;
import featurea.opengl.batches.FillRectangleBatch;
import featurea.opengl.batches.FillShapeBatch;
import featurea.xml.XmlResource;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

// lifecycle: onDrawBatches() -> onDrawBuffers()
public abstract class Canvas implements XmlResource {

  private final Set<Batch> dirtyBatches = new HashSet<>();
  private final Map<Area, Graphics> currentGraphics = new HashMap<>();

  /*cache*/
  private final Map<Graphics, Boolean> graphicsVisibility = new HashMap<>();
  private final Set<Batch> visibleBatches = new HashSet<>();
  private final Set<Graphics> visibleGraphics = new HashSet<>();

  public abstract Graphics getGraphics(Area area);

  public abstract void onDrawBuffers(Layer layer);

  public boolean isStatic(Batch batch) {
    return !dirtyBatches.contains(batch);
  }

  public void onDrawBatches(Layer layer) {
    if (isDirty()) {
      rebuildBatch(layer);
      for (Area area : layer.projection) {
        rebuildBatch(area);
      }
    }
    // validate
    for (Batch batch : visibleBatches) {
      batch.build();
    }
    for (Graphics graphics : visibleGraphics) {
      graphics.setDirty(false);
    }
  }

  public void rebuildBatch(Area area) {
    Graphics graphics = getGraphics(area);
    if (graphics.isDirty()) {
      if (isVisible(graphics)) {
        area.onDraw(graphics);
        currentGraphics.put(area, graphics);
        visibleGraphics.add(graphics);
        visibleBatches.addAll(graphics.getBatches());
      }
    }
  }

  public void clearCaches() {
    dirtyBatches.removeAll(visibleBatches);
    visibleBatches.clear();
    visibleGraphics.clear();
    graphicsVisibility.clear();
  }

  public boolean isVisible(Graphics graphics) {
    Boolean result = graphicsVisibility.get(graphics);
    if (result == null) {
      result = graphics.isShown();
    }
    return result;
  }

  public boolean isDirty() {
    return !dirtyBatches.isEmpty();
  }

  public void clearDrawLinesAndDrawRectangle(Area area) {
    clearGraphicsDrawLineAndRectangle(currentGraphics.get(area));
    clearGraphicsDrawLineAndRectangle(getGraphics(area));
  }

  public void clearFillRectangle(Area area) {
    clearGraphicsFillRectangle(currentGraphics.get(area));
    clearGraphicsFillRectangle(getGraphics(area));
  }

  public void clearFillShape(Area area) {
    clearGraphicsFillShape(currentGraphics.get(area));
    clearGraphicsFillShape(getGraphics(area));
  }

  public void clearDrawTexture(Area area) {
    clearGraphicsDrawTexture(currentGraphics.get(area));
    clearGraphicsDrawTexture(getGraphics(area));
  }

  /*private API*/

  public void clearGraphicsDrawLineAndRectangle(Graphics graphics) {
    if (graphics != null) {
      DrawLineAndDrawRectangleBatch drawLineAndDrawRectangleBatch = graphics.getDrawLineAndDrawRectangleBach();
      if (drawLineAndDrawRectangleBatch != null) {
        dirtyBatches.add(drawLineAndDrawRectangleBatch);
        drawLineAndDrawRectangleBatch.clear();
        graphics.setDirty(true);
      }
    }
  }

  public void clearGraphicsFillRectangle(Graphics graphics) {
    if (graphics != null) {
      FillRectangleBatch fillRectangleBatch = graphics.getFillRectangleBatch();
      if (fillRectangleBatch != null) {
        dirtyBatches.add(fillRectangleBatch);
        fillRectangleBatch.clear();
        graphics.setDirty(true);
      }
    }
  }

  public void clearGraphicsFillShape(Graphics graphics) {
    if (graphics != null) {
      FillShapeBatch fillShapeBatch = graphics.getFillShapeBatch();
      if (fillShapeBatch != null) {
        dirtyBatches.add(fillShapeBatch);
        fillShapeBatch.clear();
        graphics.setDirty(true);
      }
    }
  }

  public void clearGraphicsDrawTexture(Graphics graphics) {
    if (graphics != null) {
      DrawTextureBatch drawTextureBatch = graphics.getDrawTextureBatch();
      if (drawTextureBatch != null) {
        dirtyBatches.add(drawTextureBatch);
        drawTextureBatch.clear();
        graphics.setDirty(true);
      }
    }
  }

  public void clearGraphicsAll(Graphics graphics) {
    clearGraphicsDrawLineAndRectangle(graphics);
    clearGraphicsFillRectangle(graphics);
    clearGraphicsFillShape(graphics);
    clearGraphicsDrawTexture(graphics);
  }

  @Override
  public Canvas build() {
    return this;
  }

}
