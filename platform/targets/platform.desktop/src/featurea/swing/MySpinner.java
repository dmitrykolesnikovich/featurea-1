package featurea.swing;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;

public final class MySpinner extends JSpinner.NumberEditor {

  public static MySpinner newInstance() {
    final SpinnerNumberModel model = new SpinnerNumberModel();
    JSpinner spinner = new JSpinner(model);
    MySpinner result = new MySpinner(spinner, model);
    return result;
  }

  private final JSpinner spinner;

  private MySpinner(JSpinner spinner, final SpinnerNumberModel model) {
    super(spinner);
    this.spinner = spinner;
    getFormat().setGroupingUsed(false);

    final JFormattedTextField textField = getTextField();
    textField.setColumns(5);
    textField.setText("0");

    Action upAction = new AbstractAction() {

      @Override
      public void actionPerformed(ActionEvent e) {
        model.setValue(model.getNextValue());
      }
    };
    Action dnAction = new AbstractAction() {

      @Override
      public void actionPerformed(ActionEvent e) {
        model.setValue(model.getPreviousValue());
      }
    };

    InputMap inputMap = getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    inputMap.put(KeyStroke.getKeyStroke("UP"), "up");
    inputMap.put(KeyStroke.getKeyStroke("DOWN"), "dn");

    ActionMap actionMap = getActionMap();
    actionMap.put("up", upAction);
    actionMap.put("dn", dnAction);

    getTextField().getDocument().addDocumentListener(new DocumentListener() {

      @Override
      public void insertUpdate(DocumentEvent e) {
        updateModel();
      }

      @Override
      public void removeUpdate(DocumentEvent e) {
        updateModel();
      }

      @Override
      public void changedUpdate(DocumentEvent e) {
        updateModel();
      }

      private void updateModel() {
        Integer newValue = (Integer) model.getValue();
        String text = textField.getText().trim();
        if (!text.isEmpty()) {
          try {
            newValue = Integer.valueOf(text);
          } catch (NumberFormatException nfe) {
            // if desired, uncomment
            //Toolkit.getDefaultToolkit().beep();
          }
        }
        final Integer finalValue = newValue;
        if (!model.getValue().equals(newValue)) {
          SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
              model.setValue(finalValue);
            }
          });
        }
      }
    });
  }

  public void setValue(Object value) {
    spinner.setValue(value);
  }


  public void addChangeListener(ChangeListener changeListener) {
    spinner.addChangeListener(changeListener);
  }

  public Object getValue() {
    return spinner.getValue();
  }

  public JSpinner.DefaultEditor getEditor() {
    return (JSpinner.DefaultEditor) spinner.getEditor();
  }
}