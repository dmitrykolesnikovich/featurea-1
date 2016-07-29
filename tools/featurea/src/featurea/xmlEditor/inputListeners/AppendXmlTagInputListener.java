package featurea.xmlEditor.inputListeners;

import com.sun.istack.internal.Nullable;
import featurea.app.Area;
import featurea.app.Layer;
import featurea.graphics.Graphics;
import featurea.input.InputAdapter;
import featurea.util.Selection;
import featurea.util.Settings;
import featurea.util.Vector;
import featurea.xml.XmlTag;
import featurea.xml.XmlUtil;
import featurea.xmlEditor.XmlEditorView;
import featurea.xmlEditor.util.SelectionRender;

public class AppendXmlTagInputListener extends InputAdapter {

  private static final String POSITION = "position";
  private final XmlEditorView xmlEditorView;
  private Vector position;
  @Nullable
  private XmlTag xmlTag;

  public XmlTag getXmlTag() {
    return xmlTag;
  }

  public AppendXmlTagInputListener(XmlEditorView xmlEditorView) {
    this.xmlEditorView = xmlEditorView;
  }

  @Override
  public boolean onTouchDown(double x, double y, int id) {
    if (id == 0) {
      editPosition(x, y);
      save();
    }
    return false;
  }

  @Override
  public boolean onTouchMove(double x, double y, int id) {
    if (id == 0) {
      editPosition(x, y);
    }
    return false;
  }

  public void onDraw() {
    if (xmlTag != null) {
      if (position != null) {
        Object resource = xmlTag.getResource();
        if (resource instanceof Area) {
          Area area = (Area) resource;
          Layer selectedLayer = xmlEditorView.getSelectedLayer();
          if (selectedLayer != null) {
            Graphics graphics = xmlEditorView.graphics;
            drawRecursively(graphics, area);
            Selection selection = xmlEditorView.getSelection(xmlTag);
            SelectionRender.draw(graphics, selection.left(), selection.top(), selection.right(), selection.bottom());
          }
        }
      }
    }
  }

  private void drawRecursively(Graphics graphics, Area area) {
    area.onDraw(graphics);
    for (Area child : area.listAreas()) {
      drawRecursively(graphics, child);
    }
  }

  private void editPosition(double x, double y) {
    if (position == null) {
      position = new Vector();
    }
    Settings settings = xmlEditorView.getProject().settings;
    x = settings.roundValue(x);
    y = settings.roundValue(y);
    position.setValue(x, y);
    if (xmlTag != null) {
      XmlUtil.setAttribute(xmlTag, POSITION, position.x + ", " + position.y);
    }
  }

  private void save() {
    XmlTag selectedXmlTag = xmlEditorView.getSelectedXmlTag();
    if (xmlTag != null && selectedXmlTag != null) {
      xmlEditorView.appendXmlTag(selectedXmlTag, xmlTag);
      xmlEditorView.setXmlTagToAppend(xmlTag.name);
      xmlEditorView.selectXmlTag(selectedXmlTag);
    }
  }

  public void onInput() {
    if (xmlTag != null) {
      Layer selectedLayer = xmlEditorView.getSelectedLayer();
      if (selectedLayer != null) {
        xmlEditorView.getMediaPlayer().input.update(this, selectedLayer);
      }
    }
  }

  public void selectArea(@Nullable String name) {
    if (name != null) {
      xmlTag = new XmlTag(xmlEditorView.getXmlContext(), name, xmlEditorView.getProject().getFiles().getRelativePath(xmlEditorView.getXmlFile()));
    } else {
      xmlTag = null;
    }
  }

}