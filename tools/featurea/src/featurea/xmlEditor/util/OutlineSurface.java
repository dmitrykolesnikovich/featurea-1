package featurea.xmlEditor.util;

import featurea.app.Layer;
import featurea.graphics.Graphics;
import featurea.util.Color;
import featurea.util.Colors;
import featurea.util.Selection;
import featurea.xml.XmlNode;
import featurea.xmlEditor.XmlEditorView;

import java.util.Map;

public class OutlineSurface {

  private final XmlEditorView xmlEditorView;
  private final Selection selection = new Selection();

  public OutlineSurface(XmlEditorView xmlEditorView) {
    this.xmlEditorView = xmlEditorView;
  }

  public void onDraw() {
    if (xmlEditorView.getMediaPlayer().render.isOutline) {
      Map<XmlNode, Layer> xmlNodes = xmlEditorView.retrieveXmlNodes();
      for (XmlNode xmlNode : xmlNodes.keySet()) {
        Layer layer = xmlNodes.get(xmlNode);
        xmlNode.getSelection(selection.reset(), null);
        Color color = selection.color;
        if (xmlNode == xmlEditorView.getSelectedXmlTag().getResource()) {
          color = Colors.focus;
        }
        Graphics graphics = layer.getGraphics();

        double x1 = selection.left();
        double y1 = selection.top();
        double x2 = selection.right();
        double y2 = selection.bottom();

        if (color == Colors.focus) {
          SelectionRender.draw(graphics, x1, y1, x2, y2);
        } else {
          graphics.drawRectangle(x1, y1, x2, y2, color);
        }
      }
    }
  }

}
