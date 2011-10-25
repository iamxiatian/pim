package xiatian.pim.component.tree;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**/
/* Drop Transferable */
public class DragAndDropTransferable implements Transferable {
  private MyTreeNode treeNode;
  // 创建自己的DataFlavor
  public final static DataFlavor TREENODE_FLAVOR = new DataFlavor(MyTreeNode.class, "TreeNode instance");
  public DataFlavor[] flavors = new DataFlavor[] { TREENODE_FLAVOR };

  public DragAndDropTransferable(MyTreeNode treeNode) {
    this.treeNode = treeNode;
  }

  @Override
  public DataFlavor[] getTransferDataFlavors() {
    return flavors;
  }

  @Override
  public boolean isDataFlavorSupported(DataFlavor flavor) {
    for (DataFlavor df : flavors) {
      if (df.equals(flavor)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public Object getTransferData(DataFlavor df) throws UnsupportedFlavorException, IOException {
    if (df.equals(TREENODE_FLAVOR)) {      
      return treeNode;
    }
    throw new UnsupportedFlavorException(df);
  }
}
