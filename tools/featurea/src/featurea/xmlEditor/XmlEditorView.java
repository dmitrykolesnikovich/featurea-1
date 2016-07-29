package featurea.xmlEditor;

import featurea.app.*;
import featurea.desktop.Simulator;
import featurea.graphics.DefaultGraphics;
import featurea.graphics.Graphics;
import featurea.opengl.OpenGLManager;
import featurea.swing.MyScrollPanel;
import featurea.swing.TreeTransferHandler;
import featurea.util.AppendXmlTag;
import featurea.util.FileUtil;
import featurea.util.Selection;
import featurea.util.Vector;
import featurea.xml.*;
import featurea.xmlEditor.configs.DefaultsConfig;
import featurea.xmlEditor.inputListeners.*;
import featurea.xmlEditor.util.OutlineSurface;
import featurea.xmlEditor.views.ErrTextView;
import featurea.xmlEditor.views.XmlTagAttributesView;
import featurea.xmlEditor.views.XmlTagChildrenTreeView;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class XmlEditorView extends JPanel implements XmlEditor {

  private XmlTagChildrenTreeView tree;
  private XmlTagAttributesView table;
  private final ErrTextView errTextView = new ErrTextView();
  private final JLabel infoLabel = new JLabel();
  private final Simulator simulator;
  private final JSplitPane splitPanel = new JSplitPane();
  private final JSplitPane splitPane2 = new JSplitPane();
  private final File xmlFile;
  private final AppendXmlTagInputListener appendXmlTagInputListener = new AppendXmlTagInputListener(this);
  private final SelectXmlTagInputListener selectXmlTagInputListener = new SelectXmlTagInputListener(this);
  private final OutlineSurface outlineSurface = new OutlineSurface(this);
  private final TrackCoordinatesInputListener trackCoordinatesInputListener = new TrackCoordinatesInputListener(this);
  private final ZoomInputListener zoomInputListener = new ZoomInputListener(this);
  public final Graphics graphics = new DefaultGraphics().build();


  /*configs*/
  private DefaultsConfig defaults;

  private AppendXmlTag appendXmlTagConfig;

  public XmlEditorView(File xmlFile) {
    this.xmlFile = xmlFile;

    //
    setLayout(new BorderLayout());
    splitPane2.setOrientation(JSplitPane.VERTICAL_SPLIT);
    splitPane2.setTopComponent(new JPanel());
    splitPane2.setBottomComponent(new JPanel());
    splitPanel.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
    add(splitPanel, BorderLayout.CENTER);
    splitPanel.setLeftComponent(splitPane2);
    setupSizes();

    this.simulator = new Simulator() {
      @Override
      public void onCreate(MediaPlayer mediaPlayer) {
        setupLeftComponent(mediaPlayer);
        mediaPlayer.getResources().editor = XmlEditorView.this;
        appendXmlTagConfig = new AppendXmlTag(mediaPlayer.project);
        mediaPlayer.getFiles().err = errTextView.err;
        super.onCreate(mediaPlayer);
        simulator.getWindow().addKeyListener(new CopyPasteXmlTagListener(mediaPlayer));
      }

      @Override
      public void onDrawBackground() {
        super.onDrawBackground();
        if (getMediaPlayer().isPause()) {
          XmlEditorView.this.onDrawBackground();
        }
      }

      @Override
      public void onDraw(OpenGLManager gl) {
        if (errTextView.isError()) {
          splitPanel.setRightComponent(MyScrollPanel.wrap(errTextView));
          errTextView.flush();
          setupSizes();
        }
        super.onDraw(gl);
        if (getMediaPlayer().isPause()) {
          XmlEditorView.this.onDraw();
        }
        outlineSurface.onDraw();
      }

      @Override
      public void onInput() {
        if (getMediaPlayer().isPause()) {
          XmlEditorView.this.onInput();
        } else {
          super.onInput();
        }
        zoomInputListener.onInput();
        trackCoordinatesInputListener.onInput();
      }

      @Override
      public void onDoubleClick() {
        if (getMediaPlayer().isPause()) {
          tree.centering();
        } else {
          // no op
        }
      }
    };
    simulator.setXmlFile(xmlFile);
    simulator.createWindow();
    simulator.getWindow().mediaPlayer.render.isScreenMode = true;
    simulator.getWindow().mediaPlayer.render.isOutline = true;
    simulator.getMediaPlayer().pause();
    setupSimulator();
  }

  /*XmlFilePreview API*/

  private void updateTree() {
    XmlTag rootXmlTag = getXmlContext().xmlTag;
    tree.setRootXmlTag(rootXmlTag);
    selectXmlTag(rootXmlTag);
  }

  private void onDrawBackground() {
    // no op
  }

  private void onDraw() {
    appendXmlTagInputListener.onDraw();
    Layer selectedLayer = getSelectedLayer();
    XmlPrimitive selectedPrimitive = getSelectedPrimitive();
    if (selectedLayer != null && selectedPrimitive != null) {
      selectedPrimitive.onDraw(graphics);
    }
  }

  private void onInput() {
    if (appendXmlTagInputListener.getXmlTag() != null) {
      appendXmlTagInputListener.onInput();
    } else {
      if (getSelectedPrimitive() == null || "position".equals(getSelectedPrimitive().key)) {
        getMediaPlayer().input.update(selectXmlTagInputListener);
      }
      Layer selectedLayer = getSelectedLayer();
      XmlPrimitive selectedPrimitive = getSelectedPrimitive();
      if (selectedLayer != null && selectedPrimitive != null) {
        getMediaPlayer().input.update(selectedPrimitive, selectedLayer);
      }
    }
  }

  /*XmlModel API*/

  @Override
  public void selectXmlTag(XmlTag xmlTag) {
    tree.setSelectedXmlTag(xmlTag);
  }

  @Override
  public void replaceXmlTag(XmlTag parentXmlTag, int index, XmlTag childXmlTag) {
    XmlTag prevParentXmlTag = childXmlTag.getParent();
    prevParentXmlTag.removeChild(childXmlTag);
    parentXmlTag.addChild(index, childXmlTag);
    updateTree();
    save();
  }

  @Override
  public void editAttribute(String key, String value) {
    XmlTag selectedXmlTag = getSelectedXmlTag();
    if (selectedXmlTag != null) {
      table.put(key, value);
      XmlUtil.setAttribute(selectedXmlTag, key, value);
      save();
    }
  }

  @Override
  public XmlTag getSelectedXmlTag() {
    return tree.getSelectedXmlTag();
  }

  @Override
  public void setXmlTagToAppend(String name) {
    if (name == null) {
      appendXmlTagInputListener.selectArea(name);
    } else {
      XmlTag xmlTag = appendXmlTagConfig.getXmlTag(name);
      if (xmlTag != null) {
        selectXmlTag(xmlTag);
      }
      if (getMediaPlayer().getXmlSchema().getAttributes(name).contains("position")) {
        appendXmlTagInputListener.selectArea(name);
        defaults.inflate(appendXmlTagInputListener.getXmlTag());
      } else {
        XmlTag childXmlTag;
        if (getXmlContext().xmlTag != null) {
          XmlTag selectedXmlTag = getSelectedXmlTag();
          childXmlTag = new XmlTag(getXmlContext(), name, selectedXmlTag.getFile());
          appendXmlTag(selectedXmlTag, childXmlTag);
          selectXmlTag(childXmlTag);
        } else {
          childXmlTag = new XmlTag(getXmlContext(), name, getXmlFile().getAbsolutePath());
          appendXmlTag(null, childXmlTag);
        }
        defaults.inflate(childXmlTag);
        selectXmlTag(childXmlTag);
      }
    }
  }

  public XmlTag getXmlTagToAppend() {
    return appendXmlTagInputListener.getXmlTag();
  }

  @Override
  public void appendXmlTag(XmlTag parentXmlTag, XmlTag childXmlTag) {
    if (parentXmlTag != null) {
      parentXmlTag.addChild(childXmlTag);
      updateTree();
      save();
    } else {
      String absoluteId = getProject().resources.getIdByFile(xmlFile);
      String id = FileUtil.getName(absoluteId);
      getXmlContext().xmlTag = childXmlTag;
      childXmlTag.setAttribute("id", id);
      updateTree();
      save();
      reload();
    }
  }

  @Override
  public void removeXmlTag(XmlTag xmlTag) {
    XmlTag parentXmlTag = xmlTag.getParent();
    if (parentXmlTag != null) {
      parentXmlTag.removeChild(xmlTag);
      updateTree();
      selectXmlTag(parentXmlTag);
      save();
    }
  }

  @Override
  public XmlPrimitive getSelectedPrimitive() {
    return table.getSelectedPrimitive();
  }

  @Override
  public Layer getSelectedLayer() {
    return XmlUtil.getLayer(getSelectedXmlTag(), getMediaPlayer().app.screen);
  }

  @Override
  public XmlContext getXmlContext() {
    return simulator.xmlContext;
  }

  @Override
  public MediaPlayer getMediaPlayer() {
    return simulator.getMediaPlayer();
  }

  @Override
  public Project getProject() {
    return simulator.getMediaPlayer().project;
  }

  @Override
  public File getXmlFile() {
    return xmlFile;
  }

  @Override
  public void save() {
    XmlUtil.save(tree.getRootXmlTag(), getXmlFile());
  }

  @Override
  public void updatePointerCoordinates(double x, double y) {
    String localPointPostfix = "";
    XmlTag selectedXmlTag = getSelectedXmlTag();
    if (selectedXmlTag != null) {
      String positionAttribute = selectedXmlTag.getAttribute("position");
      if (positionAttribute != null) {
        Vector origin = Vector.valueOf(positionAttribute);
        localPointPostfix = "; " + (int) (x - origin.x) + ", " + (int) (y - origin.y);
      }
    }
    String selectionPointPostfix = "";
    Selection currentSelection = getCurrentSelection();
    if (currentSelection != null) {
      selectionPointPostfix = "; " + currentSelection.toString();
    }

    infoLabel.setText(" " + (int) x + ", " + (int) y + localPointPostfix + selectionPointPostfix);
  }

  @Override
  public Selection getCurrentSelection() {
    XmlTag selectedXmlTag = getSelectedXmlTag();
    return getSelection(selectedXmlTag);
  }

  @Override
  public Selection getSelection(XmlTag xmlTag) {
    XmlResource resource = xmlTag.getResource();
    if (resource instanceof XmlNode) {
      XmlNode selectedXmlNode = (XmlNode) resource;
      Selection result = new Selection();
      selectedXmlNode.getSelection(result, null);
      return result;
    }
    return null;
  }

  @Override
  public void reload() {
    setupConfigs(getProject());
    if (errTextView.isError()) {
      setupSimulator();
    }
    setXmlTagToAppend(null);
    simulator.reload();
    updateTree();
  }

  public void changeMode() {
    if (getMediaPlayer().isPause()) {
      getMediaPlayer().play();
    } else {
      getMediaPlayer().pause();
    }
  }

  private void setupSimulator() {
    splitPanel.setRightComponent(simulator.getContentPane());
    setupSizes();
  }

  private void setupLeftComponent(final MediaPlayer mediaPlayer) {
    tree = new XmlTagChildrenTreeView(mediaPlayer);
    table = new XmlTagAttributesView(mediaPlayer);

    // tree
    tree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
      @Override
      public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        if (node != null) {
          XmlTag xmlTag = (XmlTag) node.getUserObject();
          table.setXmlTag(xmlTag);
        }
        getMediaPlayer().input.keyboard.clear(); // IMPORTANT for xmlPrimitive logic
      }
    });
    tree.setTransferHandler(new TreeTransferHandler() {
      @Override
      public void onTransfer(DefaultMutableTreeNode parent, int index, DefaultMutableTreeNode child) {
        XmlTag parentXmlTag = (XmlTag) parent.getUserObject();
        XmlTag childXmlTag = (XmlTag) child.getUserObject();
        replaceXmlTag(parentXmlTag, index, childXmlTag);
      }
    });

    splitPane2.setTopComponent(MyScrollPanel.wrap(tree));
    JPanel panel = new JPanel(new BorderLayout());
    panel.add(MyScrollPanel.wrap(table), BorderLayout.CENTER);
    panel.add(infoLabel, BorderLayout.SOUTH);
    splitPane2.setBottomComponent(panel);

    setupSizes();
  }

  private void setupSizes() {
    splitPane2.setDividerLocation(300);
    splitPanel.setDividerLocation(300);
  }

  public void clearSelectedPrimitive() {
    table.selectAttribute(null);
  }

  public Map<XmlNode, Layer> retrieveXmlNodes() {
    Map<XmlNode, Layer> result = new HashMap<>();
    Screen screen = getMediaPlayer().app.screen;
    if (screen != null) {
      java.util.List<Layer> layers = screen.getLayers();
      for (Layer layer : layers) {
        if (layer.isVisible) {
          layer.onTraverse();
          for (Area area : layer.projection) {
            if (area instanceof XmlNode) {
              XmlNode node = (XmlNode) area;
              result.put(node, layer);
            }
          }
          if (layer instanceof XmlNode) {
            XmlNode node = (XmlNode) layer;
            result.put(node, layer);
          }
        }
      }
    }
    return result;
  }

  private void setupConfigs(Project project) {
    defaults = new DefaultsConfig(project);
  }

}
