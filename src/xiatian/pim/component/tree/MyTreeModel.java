package xiatian.pim.component.tree;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

public class MyTreeModel extends DefaultTreeModel {

    private static final long serialVersionUID = 4696771173453934444L;

    public MyTreeModel(TreeNode root, boolean asksAllowsChildren) {
        super(root, asksAllowsChildren);

    }
}
