package featurea.swing;

import javax.swing.tree.DefaultMutableTreeNode;

public interface TreeTransferListener {

  void onTransfer(DefaultMutableTreeNode parent, int index, DefaultMutableTreeNode child);

}
