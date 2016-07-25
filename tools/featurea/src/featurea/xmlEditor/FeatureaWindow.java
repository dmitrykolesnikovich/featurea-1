package featurea.xmlEditor;

import featurea.desktop.FeatureaMenuBar;
import featurea.desktop.LwjglNatives;
import featurea.desktop.Simulator;
import featurea.desktop.SingleInstanceServer;
import featurea.swing.FeatureaSwingUtil;
import featurea.util.FileUtil;
import featurea.util.Preferences;
import featurea.xml.XmlPrimitive;
import featurea.xml.XmlTag;
import featurea.xmlEditor.views.AreaSuggestionView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;

public class FeatureaWindow extends JFrame implements SingleInstanceServer.Program {

  static {
    Simulator.initOnlyOnce();
  }

  private static final String TITLE = "Featurea";
  private final JTabbedPane contentPanel = new JTabbedPane();
  private final FeatureaMenuBar menubar = new FeatureaMenuBar(this);

  public FeatureaWindow() {
    setTitle(TITLE);
    setIconImage(FeatureaSwingUtil.getBufferedImage("featurea/studio/xcode.png"));

    contentPanel.setBorder(BorderFactory.createEmptyBorder());
    setContentPane(contentPanel);

    inflateActionMap();
    setJMenuBar(menubar);
  }

  @Override
  public void launch() {
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    pack();
    setSize(800, 600);
    setLocationRelativeTo(null);
    setVisible(true);
    setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
  }

  @Override
  public void openFile(File file) {

    XmlEditorView xmlEditorView = new XmlEditorView(file);
    contentPanel.addTab(file.getName(), xmlEditorView);
    contentPanel.setSelectedComponent(xmlEditorView);
    menubar.onProjectSwitch(xmlEditorView.getMediaPlayer());
  }

  public XmlEditorView getSelectedEditor() {
    return (XmlEditorView) contentPanel.getSelectedComponent();
  }

  private void inflateActionMap() {
    ActionMap actionMap = contentPanel.getActionMap();
    InputMap inputMap = contentPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
    // DELETE
    {
      String actionKey = "deleteSelectedXmlTag";
      inputMap.put(KeyStroke.getKeyStroke("DELETE"), actionKey);
      actionMap.put(actionKey, new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
          XmlEditorView editor = getSelectedEditor();
          if (editor != null) {
            XmlPrimitive primitive = editor.getSelectedPrimitive();
            if (primitive == null || "position".equals(primitive.key)) {
              XmlTag selectedXmlTag = editor.getSelectedXmlTag();
              editor.removeXmlTag(selectedXmlTag);
            }
          }
        }
      });
    }


    // SHIFT+F9
    {
      String actionKey = "startNewProcess";
      inputMap.put(KeyStroke.getKeyStroke("shift F9"), actionKey);
      actionMap.put(actionKey, new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
          XmlEditorView xmlEditorView = getSelectedEditor();
          if (xmlEditorView != null) {
            FeatureaSingleInstanceServer.startNewProcess(false, xmlEditorView.getXmlFile());
          }
        }
      });
    }

    // F2
    {
      String actionKey = "stopSimulator";
      inputMap.put(KeyStroke.getKeyStroke("F4"), actionKey);
      actionMap.put(actionKey, new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
          XmlEditorView xmlEditorView = getSelectedEditor();
          if (xmlEditorView != null) {
            xmlEditorView.changeMode();
          }
        }
      });
    }

    // CTRL+W
    {
      String KEY = "closeSelectedTab";
      inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_MASK), KEY);
      actionMap.put(KEY, new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
          int selectedIndex = contentPanel.getSelectedIndex();
          if (selectedIndex != -1) {
            contentPanel.removeTabAt(selectedIndex);
            repaint();
            resize(getWidth(), getHeight() + 1);
          }
        }
      });
    }

    // CTRL+N
    {
      String KEY = "searchArea";
      inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK), KEY);
      actionMap.put(KEY, new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
          final XmlEditorView xmlEditorView = getSelectedEditor();
          if (xmlEditorView != null) {
            final AreaSuggestionView dialog = new AreaSuggestionView(xmlEditorView.getMediaPlayer());
            dialog.showDialog(new AreaSuggestionView.ChooseAreaListener() {
              @Override
              public void choose(String area) {
                xmlEditorView.setXmlTagToAppend(area);
                dialog.dispose();
              }
            });
          }
        }
      });
    }

    // ESCAPE
    {
      String KEY = "escape";
      inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), KEY);
      actionMap.put(KEY, new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
          XmlEditorView xmlEditorView = getSelectedEditor();
          if (xmlEditorView != null) {
            if (xmlEditorView.getXmlTagToAppend() != null) {
              xmlEditorView.setXmlTagToAppend(null);
            } else {
              xmlEditorView.clearSelectedPrimitive();
            }
          }
        }
      });
    }

    // CTRL+UP
    {
      String KEY = "up";
      inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, InputEvent.CTRL_MASK), KEY);
      actionMap.put(KEY, new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
          XmlEditorView editor = getSelectedEditor();
          if (editor != null) {
            XmlTag xmlTag = editor.getSelectedXmlTag();
            if (xmlTag != null) {
              XmlTag parent = xmlTag.getParent();
              if (parent != null) {
                editor.selectXmlTag(parent);
              }
            }
          }
        }
      });
    }
  }

}
