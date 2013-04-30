package xiatian.pim.component.tree;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

public class MyTreeCellRenderer extends DefaultTreeCellRenderer {

    private static final long serialVersionUID = 4929586928874777833L;

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        MyTreeNode node = (MyTreeNode) value;
        NodeData nodeData = (NodeData) node.getUserObject();

        try {
            this.setIcon(nodeData.getIcon());
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.setText(nodeData.getName());
        this.setOpaque(true);
        if (selected) {
            this.setBackground(new Color(178, 180, 191));
        } else {
            this.setBackground(new Color(255, 255, 255));
        }
        return this;
    }
}