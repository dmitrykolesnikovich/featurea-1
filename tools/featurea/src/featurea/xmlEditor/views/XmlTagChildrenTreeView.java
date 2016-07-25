package featurea.xmlEditor.views;

import featurea.app.Context;
import featurea.app.Layer;
import featurea.app.MediaPlayer;
import featurea.swing.MyTree;
import featurea.util.Selection;
import featurea.xml.XmlNode;
import featurea.xml.XmlResource;
import featurea.xml.XmlTag;
import featurea.xml.XmlUtil;
import featurea.xmlEditor.inputListeners.CopyPasteXmlTagListener;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class XmlTagChildrenTreeView extends MyTree {

  private final MediaPlayer mediaPlayer;
  private final Selection selection = new Selection();
  private XmlTag rootXmlTag;

  public XmlTagChildrenTreeView(final MediaPlayer mediaPlayer) {
    this.mediaPlayer = mediaPlayer;
    setToggleClickCount(0);
    addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
          centering();
        }
      }
    });
    addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
          centering();
        }
        /*if (e.getKeyCode() == KeyEvent.VK_DELETE) {
          TreePath[] treePaths = getSelectionPaths();
          if (treePaths != null) {
            for (TreePath treePath : treePaths) {
              DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePath.getLastPathComponent();
              XmlTag xmlTag = (XmlTag) node.getUserObject();
              mediaPlayer.getResources().editor.removeXmlTag(xmlTag);
            }
          }
        }*/
      }
    });

    addKeyListener(new CopyPasteXmlTagListener(mediaPlayer));
  }

  public void centering() {
    try {
      if (Context.getMediaPlayer() != mediaPlayer) {
        System.out.println("breakpoint");
      }
      DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) getLastSelectedPathComponent();
      if (treeNode != null) {
        XmlTag xmlTag = (XmlTag) treeNode.getUserObject();
        XmlResource resource = xmlTag.getResource();
        if (resource instanceof XmlNode) {
          XmlNode node = (XmlNode) resource;
          node.getSelection(selection.reset(), null);
          Layer layer = XmlUtil.getLayer(xmlTag, mediaPlayer.app.screen);
          double x1 = layer.toScreenX(selection.ox());
          double y1 = layer.toScreenY(selection.oy());
          double x2 = mediaPlayer.render.size.width / 2;
          double y2 = mediaPlayer.render.size.height / 2;
          double dx = x2 - x1;
          double dy = y2 - y1;
          mediaPlayer.render.zoom.move(dx, dy);
          System.out.println("centering: " + dx + ", " + dy);
          System.out.println("move: " + x1 + ", " + y1 + ", " + x2 + ", " + y2);
          System.out.println("context: " + mediaPlayer.hashCode());
          double startScale = mediaPlayer.render.zoom.scale;
          double finishScale = mediaPlayer.project.settings.getPointScale();
          double deltaScale = finishScale / startScale;
          mediaPlayer.render.zoom.scale(x2, y2, deltaScale);
        }
      }
    } catch (NullPointerException e) {
      e.printStackTrace();
    }
  }

  public void setRootXmlTag(XmlTag rootXmlTag) {
    this.rootXmlTag = rootXmlTag;
    if (this.rootXmlTag != null) {
      DefaultMutableTreeNode rootNode = inflateTreeModelRecursively(rootXmlTag);
      getModel().setRoot(rootNode);
    } else {
      getModel().setRoot(null);
    }
  }

  public XmlTag getRootXmlTag() {
    return rootXmlTag;
  }

  public void setSelectedXmlTag(XmlTag selectedXmlTag) {
    if (selectedXmlTag != null) {
      setSelectedUserObject(selectedXmlTag);
      TreePath selectionPath = getSelectionPath();
      scrollPathToVisible(selectionPath);
    } else {
      clearSelection();
    }
  }

  public XmlTag getSelectedXmlTag() {
    DefaultMutableTreeNode node = (DefaultMutableTreeNode) getLastSelectedPathComponent();
    if (node != null) {
      return (XmlTag) node.getUserObject();
    } else {
      return null;
    }
  }

  /**
   * @return root node
   */
  private DefaultMutableTreeNode inflateTreeModelRecursively(final XmlTag xmlTag) {
    DefaultMutableTreeNode treeNode = new XmlTagTreeNode(xmlTag);
    for (XmlTag child : xmlTag.getChildren()) {
      treeNode.add(inflateTreeModelRecursively(child));
    }
    return treeNode;
  }

}
