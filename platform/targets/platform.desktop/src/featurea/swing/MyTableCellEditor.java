package featurea.swing;

import featurea.app.Config;
import featurea.util.ArrayUtil;
import featurea.util.StringUtil;
import featurea.xml.XmlEditor;
import featurea.xml.XmlTag;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class MyTableCellEditor extends DefaultCellEditor {

  private final DefaultTableCellRenderer adaptee = new DefaultTableCellRenderer();
  public final Map<Integer, JComponent> rows = new HashMap<>();
  private final MyTable table;

  public MyTableCellEditor(final MyTable table) {
    super(new JTextField());
    this.table = table;
  }

  @Override
  public Component getTableCellEditorComponent(final JTable skip, Object value, boolean isSelected, final int row, int column) {
    if (column == 0) {
      throw new IllegalArgumentException("column == 0");
    } else {
      String key = getSelectedKey(table, row);
      XmlTag xmlTag = this.table.mediaPlayer.project.resources.editor.getSelectedXmlTag();
      boolean isEnum = this.table.mediaPlayer.getXmlSchema().isEnum(xmlTag, key);
      String canonicalClassName = this.table.mediaPlayer.getXmlSchema().getCanonicalClassName(xmlTag.name + "." + key);
      if (!"id".equals(key) && !"link".equals(key)) {
        if ("int".equals(canonicalClassName)) {
          MySpinner spinner = (MySpinner) rows.get(row);
          if (spinner == null) {
            spinner = MySpinner.newInstance();
            spinner.addChangeListener(new ChangeListener() {
              @Override
              public void stateChanged(ChangeEvent e) {
                JSpinner s = (JSpinner) e.getSource();
                String value = s.getValue().toString();
                String key = getSelectedKey(table, row);
                getEditor().editAttribute(key, value);
              }
            });
            rows.put(row, spinner);
          } else {
            value = spinner.getValue();
            table.setValueAt(value, row, column); // IMPORTANT
          }
          int intValue = 0;
          try {
            String string = value != null ? value.toString() : "";
            intValue = Integer.valueOf(string);
          } catch (NumberFormatException e) {
            e.printStackTrace();
          }
          spinner.setValue(intValue);
          return spinner;
        } else if ("boolean".equals(canonicalClassName)) {
          JComboBox<String> comboBox = (JComboBox<String>) rows.get(row);
          if (comboBox == null) {
            comboBox = new MyComboBox(row);
            rows.put(row, comboBox);
          } else {
            value = comboBox.getSelectedItem().toString();
            table.setValueAt(value, row, column); // IMPORTANT
          }
          comboBox.setModel(new DefaultComboBoxModel<>(new String[]{"", "false", "true"}));
          comboBox.setSelectedItem(value);
          return comboBox;
        } else if (isEnum) {
          JComboBox<String> comboBox = (JComboBox<String>) rows.get(row);
          if (comboBox == null) {
            comboBox = new MyComboBox(row);
            rows.put(row, comboBox);
          } else {
            value = comboBox.getSelectedItem().toString();
            table.setValueAt(value, row, column); // IMPORTANT
          }

          Config enums = new Config(table.mediaPlayer.project, "enums.properties");
          String[] tokens = StringUtil.split(enums.getValue(xmlTag.name + "." + key), ",");
          tokens = ArrayUtil.add(tokens, "", 0);
          comboBox.setModel(new DefaultComboBoxModel<>(tokens));
          comboBox.setSelectedItem(value);
          return comboBox;
        }
      }
      JTextArea textArea = (JTextArea) rows.get(row);
      if (textArea == null) {
        textArea = new MyTextArea(row);
        rows.put(row, textArea);
      } else {
        value = textArea.getText();
        table.setValueAt(value, row, column); // IMPORTANT
      }

      delegate.setValue(value);
      textArea.setWrapStyleWord(true);
      textArea.setLineWrap(true);
      textArea.setForeground(adaptee.getForeground());
      textArea.setBorder(adaptee.getBorder());
      textArea.setFont(adaptee.getFont());
      JScrollPane scrollPane = MyScrollPanel.wrap(textArea);
      scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
      scrollPane.setBorder(null);
      textArea.addKeyListener(new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
          if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            stopCellEditing();
          }
        }
      });
      String string = value != null ? value.toString() : "";
      string = this.table.mediaPlayer.project.xmlFormatter.format(xmlTag, key, string);
      textArea.setText(string);
      return scrollPane;
    }
  }

  private class MyTextArea extends JTextArea {

    public MyTextArea(final int row) {
      getDocument().addDocumentListener(new MyDocumentListener(this, getSelectedKey(table, row)));
    }
  }

  private class MyDocumentListener implements DocumentListener {

    private final JTextComponent textComponent;
    private final String key;

    public MyDocumentListener(JTextComponent textComponent, String key) {
      this.textComponent = textComponent;
      this.key = key;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
      updateSelf();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
      updateSelf();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
      updateSelf();
    }

    private void updateSelf() {
      String string = textComponent.getText();
      if (string == null) {
        string = "";
      }
      getEditor().editAttribute(key, string);
    }
  }


  private class MyComboBox extends JComboBox {
    public MyComboBox(final int row) {
      addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          String key = getSelectedKey(table, row);
          Object selectedItem = getSelectedItem();
          if (selectedItem != null) {
            String value = selectedItem.toString();
            MyTableCellEditor.this.getEditor().editAttribute(key, value);
          }
        }
      });
      addPopupMenuListener(new PopupMenuListener() {
        @Override
        public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
          JComboBox comboBox = (JComboBox) e.getSource();
          BasicComboPopup popup = (BasicComboPopup) comboBox.getAccessibleContext().getAccessibleChild(0);
          JList list = popup.getList();
          list.setSelectedIndex(comboBox.getSelectedIndex());
        }

        @Override
        public void popupMenuCanceled(PopupMenuEvent e) {
          // no op
        }

        @Override
        public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
          // no op
        }
      });
    }
  }

  private String getSelectedKey(JTable table, int row) {
    return (String) table.getValueAt(row, 0);
  }

  private XmlEditor getEditor() {
    return table.mediaPlayer.project.resources.editor;
  }

}
