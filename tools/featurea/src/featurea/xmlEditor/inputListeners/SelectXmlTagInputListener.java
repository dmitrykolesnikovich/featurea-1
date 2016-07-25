package featurea.xmlEditor.inputListeners;

import featurea.app.Layer;
import featurea.input.InputAdapter;
import featurea.util.Selection;
import featurea.util.Vector;
import featurea.xml.XmlNode;
import featurea.xml.XmlResource;
import featurea.xml.XmlTag;
import featurea.xmlEditor.XmlEditorView;

import java.util.Map;

public class SelectXmlTagInputListener extends InputAdapter {

  private final Selection selection = new Selection();
  private final XmlEditorView xmlEditorView;

  public SelectXmlTagInputListener(XmlEditorView xmlEditorView) {
    this.xmlEditorView = xmlEditorView;
  }

  @Override
  public boolean onTouchDown(double x, double y, int id) {
    if (id == 0) {
      XmlNode xmlNode = findNode(x, y);
      if (xmlNode != null) {
        XmlTag xmlTag = xmlEditorView.getXmlContext().getXmlTagByResource((XmlResource) xmlNode);
        if (xmlTag != null) {
          xmlEditorView.selectXmlTag(xmlTag);
        }
      }
    }
    return false;
  }

  /*private API*/

  private XmlNode findNode(double screenX, double screenY) {
    Map<XmlNode, Layer> mapXmlNodes = xmlEditorView.retrieveXmlNodes();
    for (XmlNode xmlNode : mapXmlNodes.keySet()) {
      Layer layer = mapXmlNodes.get(xmlNode);
      double layerX = layer.toLayerX(screenX);
      double layerY = layer.toLayerY(screenY);
      xmlNode.getSelection(selection.reset(), new Vector(layerX, layerY));
      XmlTag xmlTag = xmlEditorView.getXmlContext().getXmlTagByResource((XmlResource) xmlNode);
      if (selection.isSelected && xmlTag != null) {
        return xmlNode;
      }
    }
    return null;
  }


}


