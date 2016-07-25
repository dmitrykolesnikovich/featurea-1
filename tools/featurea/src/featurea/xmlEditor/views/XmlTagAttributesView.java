package featurea.xmlEditor.views;

import featurea.app.MediaPlayer;
import featurea.swing.MyTable;
import featurea.util.ReflectionUtil;
import featurea.util.Selection;
import featurea.xml.XmlEditor;
import featurea.xml.XmlNode;
import featurea.xml.XmlPrimitive;
import featurea.xml.XmlTag;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class XmlTagAttributesView extends MyTable {

  private XmlTag xmlTag;
  private XmlPrimitive selectedPrimitive;

  public XmlTagAttributesView(final MediaPlayer mediaPlayer) {
    super(mediaPlayer);
    setBackground(Color.white);
    setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    setTableHeader(null);
    setModel(new DefaultTableModel() {
      @Override
      public boolean isCellEditable(int row, int column) {
        if (column == 0) {
          return false;
        }
        if (column == 1) {
          return true;
        }
        return super.isCellEditable(row, column);
      }
    });
    setColumnWidth(0, -1);

    getSelectionModel().addListSelectionListener(new ListSelectionListener() {
      @Override
      public synchronized void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
          int index = getSelectedRow();
          String key = null;
          if (index != -1) {
            key = (String) getValueAt(index, 0);
          }
          selectAttribute(key);
        }
      }
    });
  }

  public void setXmlTag(XmlTag xmlTag) {
    clearSelection();
    this.xmlTag = xmlTag;
    setVisible(xmlTag != null);
    if (xmlTag != null) {
      setAttributes(xmlTag);
    }
    selectAttribute("position");
  }

  public XmlPrimitive getSelectedPrimitive() {
    return selectedPrimitive;
  }

  private void setAttributes(XmlTag xmlTag) {
    List<String> schemaAttributes = mediaPlayer.getXmlSchema().getAttributes(xmlTag.name);
    Object[][] data = new Object[schemaAttributes.size()][2];
    int rowIndex = 0;
    for (String attribute : schemaAttributes) {
      data[rowIndex][0] = attribute;
      data[rowIndex][1] = mediaPlayer.project.xmlFormatter.format(xmlTag, attribute, xmlTag.getAttribute(attribute));
      rowIndex++;
    }
    setData(data, new Object[]{"Key", "Value"});
  }

  public void selectAttribute(String key) {
    XmlPrimitive primitive = newXmlPrimitive(key);
    if (this.selectedPrimitive != null) {
      this.selectedPrimitive.onDetach();
    }
    this.selectedPrimitive = primitive;
    if (this.selectedPrimitive != null) {
      this.selectedPrimitive.onAttach();
    }
    for (int i = 0; i < getModel().getRowCount(); i++) {
      String currentKey = (String) getValueAt(i, 0);
      if (currentKey.equals(key)) {
        setRowSelectionInterval(i, i);
        return;
      }
    }
    clearSelection();
  }

  private XmlPrimitive newXmlPrimitive(String key) {
    String field = xmlTag.name + "." + key;
    String className = mediaPlayer.project.xmlPrimitives.getValue(field);
    if (className != null) {
      Class<XmlPrimitive> klass = mediaPlayer.getClassLoader().loadClass(className);
      // >>

      return ReflectionUtil.newInstance(klass, mediaPlayer, xmlTag, key);
      // <<
    }
    return null;
  }

  public void put(String key, String value) {
    DefaultTableModel model = getModel();
    for (int i = 0; i < model.getRowCount(); i++) {
      String currentKey = (String) getValueAt(i, 0);
      if (currentKey.equals(key)) {
        model.setValueAt(value, i, 1);
        break;
      }
    }
  }

  private XmlEditor getXmlModel() {
    return xmlTag.context.resources.editor;
  }

}
