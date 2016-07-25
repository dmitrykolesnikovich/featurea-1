package featurea.xmlEditor.inputListeners;

import featurea.input.InputAdapter;
import featurea.xml.XmlTag;
import featurea.xmlEditor.XmlEditorView;

public class ZoomInputListener extends InputAdapter {

  private double prevX;
  private double prevY;
  private final XmlEditorView xmlEditorView;

  public ZoomInputListener(XmlEditorView xmlEditorView) {
    this.xmlEditorView = xmlEditorView;
  }

  @Override
  public synchronized boolean onTouchDown(double x, double y, int id) {
    if (!xmlEditorView.getMediaPlayer().render.isScreenMode) {
      return false;
    }
    if (id == 1) {
      System.out.println("ZoomInputListener.onTouchDown");
      prevX = x;
      prevY = y;
      onTouchDrag(x, y, id);
    }
    return false;
  }

  @Override
  public synchronized boolean onTouchDrag(double x, double y, int id) {
    if (!xmlEditorView.getMediaPlayer().render.isScreenMode) {
      return false;
    }
    if (id == 1) {
      double dx = x - prevX;
      double dy = y - prevY;
      xmlEditorView.getMediaPlayer().render.zoom.move(dx, dy);
      prevX = x;
      prevY = y;
    }
    return false;
  }

  @Override
  public synchronized boolean onMouseWheel(int count, double x, double y) {
    if (!xmlEditorView.getMediaPlayer().render.isScreenMode) {
      return false;
    }
    double k = 1.05f;
    if (xmlEditorView.getMediaPlayer().input.keyboard.isShiftDown()) {
      k = Math.pow(k, 6);
    }
    double scale = count > 0 ? 1 / k : k;
    scale = Math.pow(scale, Math.abs(count));
    if (xmlEditorView.getMediaPlayer().render.zoom.scale * scale < xmlEditorView.getProject().settings.getMaxScale()) {
      xmlEditorView.getMediaPlayer().render.zoom.scale(x, y, scale);
    }
    return false;
  }

  public void onInput() {
    XmlTag xmlTag = xmlEditorView.getXmlContext().xmlTag;
    if (xmlTag != null) {
      xmlEditorView.getMediaPlayer().input.update(this);
    }
  }

}
