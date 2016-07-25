package featurea.builder.ui;

import featurea.app.Project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

public class Main {

  private static final String TITLE = "Builder";
  private JPanel contentPanel;
  private JTextField projectFileTextField;
  private JButton updateButton;
  private JCheckBox packCheckBox;
  private final TrayIconHelper trayIconHelper;
  private final JFrame frame = new JFrame(TITLE);

  public static void main(String[] args) {
    try {
      UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
    } catch (Exception e) {
      e.printStackTrace();
    }
    UIManager.put("swing.boldMetal", Boolean.FALSE);
    Main main = new Main();
    main.start();
  }

  public Main() {
    trayIconHelper = new TrayIconHelper(frame);
    updateButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent event) {
        build();
      }
    });
  }

  private void build() {
    String text = projectFileTextField.getText();
    File file = new File(text);
    boolean isTexturePack = packCheckBox.isSelected();
    Project project = new Project();
    project.setFile(file);
    project.build(isTexturePack);
  }

  public void start() {
    frame.setContentPane(contentPanel);
    frame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("gen.png")));
    frame.getContentPane().setPreferredSize(new Dimension(400, 60));
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
    frame.setAlwaysOnTop(true);
    frame.requestFocus();
    frame.getContentPane().requestFocus();
    frame.addWindowListener(new WindowAdapter() {
      @Override
      public void windowIconified(WindowEvent e) {
        frame.setVisible(false);
        trayIconHelper.create();
      }
    });
  }

}
