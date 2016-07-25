package featurea.app;

import featurea.graphics.Graphics;
import featurea.input.InputListener;
import featurea.util.Size;
import featurea.util.ZOrder;
import featurea.xml.XmlResource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Lifecycle: onTraverse -> onTick -> onDraw
public class Layer implements Area, XmlResource {

  private final Graphics graphics = new Graphics(this);
  public final List<InputListener> inputListeners = new ArrayList<>();
  private Screen screen;
  private Camera camera;
  public boolean isVisible = true;
  private final List<Area> listAreas = new ArrayList<>();
  private ZOrder zOrder;
  public final Traverse traverse = new Traverse(this);
  public Projection<? extends Area> projection = new Projection<>();

  public void removeSelf() {
    if (screen != null) {
      screen.remove(this);
    }
  }

  @Override
  public void onDraw(Graphics graphics) {
    if (zOrder != null) {
      Collections.sort(projection, zOrder);
    }
    graphics.drawProjection(projection);
  }

  public void onTraverse() {
    traverse.onTraverse(projection);
  }

  @Override
  public void onTick(double elapsedTime) {
    for (Area area : projection) {
      area.onTick(elapsedTime);
    }
  }

  public void onLayout(Size size) {
    if (camera != null) {
      camera.onLayout(size);
    }
  }

  public final Layer setCamera(Camera camera) {
    this.camera = camera;
    return this;
  }

  public Camera getCamera() {
    return camera;
  }

  // like java.io.File.listFiles(FileFilter)
  @Override
  public List<? extends Area> listAreas() {
    return listAreas;
  }

  public Layer add(Area area) {
    listAreas.add(area);
    return this;
  }

  public Layer add(Area area, int index) {
    listAreas.add(index, area);
    return this;
  }

  public final Layer add(List<? extends Area> areas) {
    for (Area area : areas) {
      add(area);
    }
    return this;
  }

  public Layer remove(Area area) {
    listAreas.remove(area);
    return this;
  }

  public Graphics getGraphics() {
    return graphics;
  }

  public Screen getScreen() {
    return screen;
  }

  protected void onAttachToScreen(Screen screen) {
    this.screen = screen;
  }

  protected void onDetachFromScreen() {
    // no op
  }

  @Override
  public Layer build() {
    return this;
  }

  public ZOrder getzOrder() {
    return zOrder;
  }

  public void setzOrder(ZOrder zOrder) {
    this.zOrder = zOrder;
  }

  public double toScreenX(double layerX) {
    if (!Context.getRender().isScreenMode) {
      return camera.zoom.x + (layerX - camera.left()) * camera.zoom.scale;
    } else {
      return Context.getRender().zoom.x + layerX * Context.getRender().zoom.scale;
    }
  }

  public double toScreenY(double layerY) {
    if (!Context.getRender().isScreenMode) {
      return camera.zoom.y + (layerY - camera.top()) * camera.zoom.scale;
    } else {
      return Context.getRender().zoom.y + layerY * Context.getRender().zoom.scale;
    }
  }

  public double toScreenLength(double layerLength) {
    if (!Context.getRender().isScreenMode) {
      return layerLength * camera.zoom.scale;
    } else {
      return layerLength * Context.getRender().zoom.scale;
    }
  }

  public double toLayerX(double screenX) {
    if (!Context.getRender().isScreenMode) {
      return camera.left() + (screenX - camera.zoom.x) / camera.zoom.scale;
    } else {
      return (screenX - Context.getRender().zoom.x) / Context.getRender().zoom.scale;
    }
  }

  public double toLayerY(double screenY) {
    if (!Context.getRender().isScreenMode) {
      return camera.top() + (screenY - camera.zoom.y) / camera.zoom.scale;
    } else {
      return (screenY - Context.getRender().zoom.y) / Context.getRender().zoom.scale;
    }
  }

  public double toLayerLength(double screenLength) {
    if (!Context.getRender().isScreenMode) {
      return screenLength / camera.zoom.scale;
    } else {
      return screenLength / Context.getRender().zoom.scale;
    }
  }


}
