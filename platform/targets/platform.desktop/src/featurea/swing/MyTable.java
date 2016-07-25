package featurea.swing;

import featurea.app.MediaPlayer;
import featurea.xml.XmlEditor;
import featurea.xml.XmlTag;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.util.HashMap;
import java.util.Map;

/**
 * with this class you should use setData() method for setting data
 */
public class MyTable extends JTable {

  private static final double CHARACTER_WIDTH_IN_PIXELS = 8;
  private final Map<Integer, Integer> columnWidthMap = new HashMap<>();
  public boolean isAutoResize = true;
  public final MediaPlayer mediaPlayer;

  public MyTable(MediaPlayer mediaPlayer) {
    this.mediaPlayer = mediaPlayer;
  }

  public void setData(Object[][] dataVector, Object[] columnNames) {
    getModel().setDataVector(dataVector, columnNames);

    // multi-line cell support
    TextAreaRenderer textAreaRenderer = new TextAreaRenderer(isAutoResize);
    MyTableCellEditor textEditor = new MyTableCellEditor(this);
    TableColumnModel columnModel = getColumnModel();
    for (int i = 0; i < columnModel.getColumnCount(); i++) {
      TableColumn column = columnModel.getColumn(i);
      column.setCellRenderer(textAreaRenderer);
      column.setCellEditor(textEditor);
    }

    setupKeyColumnWidth();
  }

  private void setupKeyColumnWidth() {
    for (int j = 0; j < getColumnModel().getColumnCount(); j++) {
      TableColumn column = getColumnModel().getColumn(j);
      Integer columnWidth = columnWidthMap.get(j);
      if (columnWidth != null) {
        int fixedSize = columnWidth;
        if (fixedSize == -1) {
          double max = 0;
          for (int i = 0; i < getModel().getRowCount(); i++) {
            String text = getValueAt(i, 0).toString();
            max = Math.max(max, text.length());
          }
          max *= CHARACTER_WIDTH_IN_PIXELS;
          if (max > 80) {
            max *= 0.8;
          }
          fixedSize = (int) (max + 0.5);
        }
        column.setMinWidth(fixedSize);
        column.setMaxWidth(fixedSize);
      }
    }
  }

  @Override
  public DefaultTableModel getModel() {
    return (DefaultTableModel) super.getModel();
  }

  public void setColumnWidth(int columnIndex, int width) {
    columnWidthMap.put(columnIndex, width);
  }

  @Override
  public Object getValueAt(int row, int column) {
    if (column == 1) {
      String key = (String) getModel().getValueAt(row, 0);
      XmlEditor editor = mediaPlayer.project.resources.editor;
      if (editor != null) {
        XmlTag xmlTag = editor.getSelectedXmlTag();
        TableCellEditor cellRenderer = getCellEditor(row, column);
        if (cellRenderer instanceof MyTableCellEditor) {
          MyTableCellEditor tableCellEditor = (MyTableCellEditor) cellRenderer;
          JComponent rowComponent = tableCellEditor.rows.get(row);
          if (rowComponent instanceof JTextArea) {
            JTextArea textArea = (JTextArea) rowComponent;
            String string = textArea.getText();
            string = mediaPlayer.project.xmlFormatter.format(xmlTag, key, string);
            return string;
          } else if (rowComponent instanceof JComboBox) {
            JComboBox comboBox = (JComboBox) rowComponent;
            Object selectedItem = comboBox.getSelectedItem();
            if (selectedItem != null) {
              String string = selectedItem.toString();
              string = mediaPlayer.project.xmlFormatter.format(xmlTag, key, string);
              return string;
            } else {
              return "";
            }
          } else if (rowComponent instanceof JSpinner) {
            JSpinner spinner = (JSpinner) rowComponent;
            return spinner.getValue() + "";
          }
        }
      }
    }
    return super.getValueAt(row, column);
  }

}
