package featurea.app;

import featurea.graphics.Canvas;
import featurea.input.InputListener;
import featurea.opengl.Render;
import featurea.util.Color;
import featurea.util.Colors;
import featurea.util.Size;
import featurea.xml.XmlResource;

import java.util.ArrayList;
import java.util.List;

public class Screen implements XmlResource {

  public boolean isVisible = true;
  public final List<InputListener> inputListeners = new ArrayList<>();
  private final List<Layer> layers = new ArrayList<>();
  public Color background = Colors.background;

  public void onResume() {
    // no op
  }

  public void onPause() {
    // no op
  }

  public void onResize(int width, int height) {
    // no op
  }

  public void onTick(double elapsedTime) {
    for (Layer layer : layers) {
      layer.onTick(elapsedTime);
    }
  }

  public void onTraverseTick() {
    for (Layer layer : layers) {
      layer.onTraverseTick();
    }
  }

  public void onTraverseDraw() {
    for (Layer layer : layers) {
      layer.onTraverseDraw();
    }
  }

  public void onDrawBackground() {
    // todo make use of background
  }

  public void onDraw(Render render) {
    if (isVisible) {
      double maxX = 0, maxY = 0;
      for (Layer layer : layers) {
        if (layer.isVisible) {
          Canvas canvas = layer.getCanvas();
          canvas.onDrawBatches(layer);
          canvas.onDrawBuffers(layer);
          canvas.clearCaches();
          Camera camera = layer.getCamera();
          maxX = Math.max(maxX, camera.zoom.x);
          maxY = Math.max(maxY, camera.zoom.y);
        }
      }
      if (!render.isScreenMode) {
        // todo
      }
    }
  }

  public Screen add(Layer layer, int index) {
    layers.add(index, layer);
    layer.onAttachToScreen(this);
    return this;
  }

  public Screen add(Layer layer) {
    layers.add(layer);
    layer.onAttachToScreen(this);
    return this;
  }

  public Screen remove(Layer layer) {
    layers.remove(layer);
    layer.onDetachFromScreen();
    return this;
  }

  /**
   * IMPORTANT OpenGL is initialized already
   */
  public void onCreate() {
    // no op
  }

  public void onResize(Size size) {
    for (Layer layer : layers) {
      layer.onResize(size);
    }
  }

  public void onZoom() {
    for (Layer layer : layers) {
      layer.onZoom();
    }
  }

  @Override
  public Screen build() {
    return this;
  }

  public List<Layer> getLayers() {
    return layers;
  }

}
