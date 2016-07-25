package featurea.xmlEditor.views;

import featurea.app.MediaPlayer;
import featurea.swing.MyScrollPanel;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.awt.event.*;

public class AreaSuggestionView extends JDialog {

  public interface ChooseAreaListener {
    void choose(String area);
  }

  private JPanel contentPane = new JPanel(new BorderLayout());
  private JTextField textField = new JTextField();
  private final AreasView areasView;

  public AreaSuggestionView(MediaPlayer mediaPlayer) {
    areasView = new AreasView(mediaPlayer);
    setLayout(new BorderLayout());
    setModal(true);
    textField.addCaretListener(new CaretListener() {
      @Override
      public void caretUpdate(CaretEvent e) {
        areasView.filter(textField.getText());
      }
    });

    setContentPane(contentPane);
    contentPane.add(textField, BorderLayout.NORTH);
    contentPane.add(MyScrollPanel.wrap(areasView), BorderLayout.CENTER);
    contentPane.setMinimumSize(new Dimension(300, 0));
    contentPane.setMaximumSize(new Dimension(300, 500));
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        areasView.getChooseListener().choose(null);
        dispose();
      }
    });
    contentPane.registerKeyboardAction(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        dispose();
      }
    }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

    textField.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
          int index = areasView.getSelectedRow();
          if (index == -1) {
            areasView.requestFocus();
            areasView.setRowSelectionInterval(0, 0);
          }
        }
      }
    });
  }

  public void showDialog(ChooseAreaListener chooseAreaListener) {
    areasView.setChooseListener(chooseAreaListener);
    areasView.update();
    setUndecorated(true);
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    pack();
    setLocationRelativeTo(null);
    setVisible(true);
    requestFocus();
    getContentPane().requestFocus();
  }


}
