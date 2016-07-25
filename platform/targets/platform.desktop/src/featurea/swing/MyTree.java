package featurea.swing;

import javax.swing.*;
import javax.swing.tree.*;

public class MyTree extends JTree {

  public MyTree() {
    setExpandsSelectedPaths(true);
    setModel(new DefaultTreeModel(null));
    DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) getCellRenderer();
    renderer.setLeafIcon(null);
    renderer.setClosedIcon(null);
    renderer.setOpenIcon(null);
    setDragEnabled(true);
    setDropMode(DropMode.ON_OR_INSERT);
    setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
  }

  @Override
  public void setModel(TreeModel newModel) {
    super.setModel(newModel);
    FeatureaSwingUtil.expandTree(this);
  }

  public void setSelectedUserObject(Object userObject) {
    DefaultTreeModel model = getModel();
    DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) model.getRoot();
    selectUserObjectRecursively(rootNode, userObject);
  }

  private void selectUserObjectRecursively(DefaultMutableTreeNode parent, Object userObject) {
    DefaultTreeModel model = getModel();
    if (parent.getUserObject() == userObject) {
      setSelectionPath(new TreePath(model.getPathToRoot(parent)));
    } else {
      int count = model.getChildCount(parent);
      for (int i = 0; i < count; i++) {
        DefaultMutableTreeNode child = (DefaultMutableTreeNode) model.getChild(parent, i);
        selectUserObjectRecursively(child, userObject);
      }
    }
  }

  @Override
  public DefaultTreeModel getModel() {
    return (DefaultTreeModel) super.getModel();
  }
}
