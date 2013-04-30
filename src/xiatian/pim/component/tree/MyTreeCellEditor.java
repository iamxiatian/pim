package xiatian.pim.component.tree;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;

import xiatian.pim.domain.Journal;
import xiatian.pim.io.PimDb;

public class MyTreeCellEditor extends DefaultTreeCellEditor {

    public MyTreeCellEditor(JTree tree, DefaultTreeCellRenderer renderer) {
        super(tree, renderer);
    }

    public Object getCellEditorValue() {
        MyTreeNode node = (MyTreeNode) tree.getLastSelectedPathComponent();
        NodeData nodeData = (NodeData) node.getUserObject();
        Journal journal = nodeData.getJournal();
        journal.setTitle(realEditor.getCellEditorValue().toString());

        //保存Jourlnal
        PimDb.getInstance().updateJournal(journal);

        return nodeData;
    }

}
