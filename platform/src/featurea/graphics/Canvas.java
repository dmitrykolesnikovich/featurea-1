package featurea.graphics;

import com.sun.istack.internal.Nullable;
import featurea.app.Area;
import featurea.app.Context;
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
public class Canvas implements XmlResource {

  private Layer layer;
  private final Set<Batch> dirtyBatches = new HashSet<Batch>() {
    @Override
    public boolean add(Batch o) {
      return super.add(o);
    }
  };
  private final Map<Area, Graphics> currentGraphics = new HashMap<>();

  /*cache*/
  private final Set<Batch> visibleBatches = new HashSet<>();
  private final Set<Graphics> visibleGraphics = new HashSet<>();
  private int lastRebuildBatchCount; // for performance debug info
  private final Map<Graphics, Boolean> visibilityMap = new HashMap<>();

  public Canvas setLayer(Layer layer) {
    this.layer = layer;
    return this;
  }

  public Layer getLayer() {
    return layer;
  }

  @Nullable
  public Graphics getGraphics(Area area) {
    return null;
  }

  public void onDrawBuffers(Layer layer) {
    // no op
  }

  public void onDrawBatches(Layer layer) {
    if (isDirty()) {
      rebuildBatch(layer);
      for (Area area : layer.drawProjection) {
        rebuildBatch(area);
      }
    }

    int rebuildBatchCount = visibleBatches.size();
    if (rebuildBatchCount != lastRebuildBatchCount) {
      lastRebuildBatchCount = rebuildBatchCount;
      System.out.println("Canvas.rebuildBatches: " + rebuildBatchCount + " (" + dirtyBatches.size() + "), FPS = " + Context.getPerformance().fps); // todo render this on performance info panel
    }
    // <<

    for (Batch batch : visibleBatches) {
      batch.build();
      batch.lastLayerPosition.setValue(layer.getCamera().getPosition());
    }
    for (Graphics graphics : visibleGraphics) {
      graphics.setDirty(false);
    }
  }

  public void rebuildBatch(Area area) {
    Graphics graphics = getGraphics(area);
    if (graphics != null) {
      if (graphics.isDirty()) {
        graphics.isShown = isVisible(graphics);
        if (graphics.isShown) {
          area.onDraw(graphics);
          graphics.onDraw(area);
          currentGraphics.put(area, graphics);
          visibleGraphics.add(graphics);
          visibleBatches.addAll(graphics.getBatches());
        }
      }
    }
  }

  private boolean isVisible(Graphics graphics) {
    Boolean result = visibilityMap.get(graphics);
    if (result == null) {
      result = graphics.isShown(graphics.getLayer().getCamera());
      visibilityMap.put(graphics, result);
    }
    return result;
  }

  public void clearCaches() {
    dirtyBatches.removeAll(visibleBatches);
    visibleBatches.clear();
    visibleGraphics.clear();
    visibilityMap.clear();
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
