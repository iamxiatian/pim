package xiatian.pim.component.tree;

import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import xiatian.pim.domain.Journal;
import xiatian.pim.io.PimDb;

public class MyTreeNode extends DefaultMutableTreeNode {

    private static final long serialVersionUID = 5315887193457648003L;

    public MyTreeNode() {

    }

    public MyTreeNode(NodeData data) {
        this.setUserObject(data);
    }

    public Journal getJournal() {
        return ((NodeData) getUserObject()).getJournal();
    }
//
//  public Object clone() {
//    MyTreeNode newNode = null;    
//    newNode = (MyTreeNode) super.clone();
//    newNode.setUserObject(this.getUserObject());
//    newNode.children = this.children;
//    newNode.parent = this.parent;
//    return newNode;
//  }

    public static MyTreeNode create() {
        int id = 0;
        PimDb pimDb = PimDb.getInstance();
        MyTreeNode root = new MyTreeNode();
        Journal journal = pimDb.getJourlnal(id);
        root.setUserObject(new NodeData(journal));

        int childIndex = 0;
        List<Journal> list = pimDb.getJourlnalList(id);

        for (Journal j : list) {
            root.insert(new MyTreeNode(new NodeData(j)), childIndex++);
        }

        return root;
    }

    public static void appendChildren(MyTreeNode parent, int parentId) {
        int childIndex = 0;
        List<Journal> list = PimDb.getInstance().getJourlnalList(parentId);

        for (Journal j : list) {
            parent.insert(new MyTreeNode(new NodeData(j)), childIndex++);
        }

    }

}
