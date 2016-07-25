package featurea.swing;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class TextAreaRenderer implements TableCellRenderer {

  private final DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
  private final TableResizeUtil resizeUtil = new TableResizeUtil();

  public TextAreaRenderer(boolean isAutoResize) {
    resizeUtil.isAutoResize = isAutoResize;
  }

  @Override
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                 boolean hasFocus, int row, int column) {
    JTextArea textArea = new JTextArea();
    cellRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    textArea.setLineWrap(true);
    textArea.setWrapStyleWord(true);
    textArea.setBackground(cellRenderer.getBackground());
    textArea.setForeground(cellRenderer.getForeground());
    textArea.setBorder(cellRenderer.getBorder());
    textArea.setFont(cellRenderer.getFont());

    Object currentValue = table.getValueAt(row, column);
    if (currentValue == null) {
      currentValue = "";
    }
    String string = currentValue.toString();

    if (string == null) {
      string = value != null ? value.toString() : "";
    }
    textArea.setText(string);
    resizeUtil.setColumnSize(textArea, table, row, column);
    return textArea;
  }

}
