package featurea.xmlEditor.inputListeners;

import featurea.app.Layer;
import featurea.app.MediaPlayer;
import featurea.input.InputAdapter;
import featurea.xml.XmlEditor;
import featurea.xml.XmlTag;
import featurea.xmlEditor.XmlEditorView;

public class TrackCoordinatesInputListener extends InputAdapter {

  private final XmlEditorView xmlEditorView;

  public TrackCoordinatesInputListener(XmlEditorView xmlEditorView) {
    this.xmlEditorView = xmlEditorView;
  }

  @Override
  public boolean onTouchDown(double x, double y, int id) {
    trackMouseCoordinates(x, y);
    return false;
  }

  @Override
  public boolean onTouchDrag(double x, double y, int id) {
    trackMouseCoordinates(x, y);
    return false;
  }

  @Override
  public boolean onTouchMove(double x, double y, int id) {
    trackMouseCoordinates(x, y);
    return false;
  }

  @Override
  public boolean onTouchUp(double x, double y, int id) {
    trackMouseCoordinates(x, y);
    return false;
  }

  private void trackMouseCoordinates(double x, double y) {
    Layer selectedLayer = xmlEditorView.getSelectedLayer();
    if (selectedLayer != null) {
      x = selectedLayer.toLayerX(x);
      y = selectedLayer.toLayerY(y);
      xmlEditorView.updatePointerCoordinates(x, y);
    }
  }

  public void onInput() {
    XmlTag xmlTag = xmlEditorView.getXmlContext().xmlTag;
    if (xmlTag != null) {
      Layer selectedLayer = xmlEditorView.getSelectedLayer();
      if (selectedLayer != null) {
        xmlEditorView.getMediaPlayer().input.update(this, selectedLayer);
      }
    }
  }

}
