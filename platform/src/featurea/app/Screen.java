package featurea.app;

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

  public void onTraverse() {
    for (Layer layer : layers) {
      layer.onTraverse();
    }
  }

  public void onDrawBackground() {
    if (background != null) {
      Context.getRender().clearBackground(background);
    } else {
      Context.getRender().clearBackground(Colors.black);
    }
  }

  public void onDraw(Render render) {
    if (isVisible) {
      double maxX = Double.MAX_VALUE, maxY = Double.MAX_VALUE;
      for (Layer layer : layers) {
        if (layer.isVisible) {
          layer.onDraw(layer.getGraphics());
          Camera camera = layer.getCamera();
          maxX = Math.max(maxX, camera.zoom.x);
          maxY = Math.max(maxY, camera.zoom.y);
        }
      }
      if (!Context.getRender().isScreenMode) {
        Context.getRender().cropWithCamera(maxX, maxY);
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

  public void onLayout(Size size) {
    for (Layer layer : layers) {
      layer.onLayout(size);
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
