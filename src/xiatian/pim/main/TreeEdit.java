package xiatian.pim.main;

import java.awt.BorderLayout;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellEditor;

public class TreeEdit {
    public static void main(String args[]) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Object array[] = {Boolean.TRUE, Boolean.FALSE, "Hello"};
        JTree tree = new JTree(array);
        tree.setEditable(true);
        tree.setRootVisible(true);


        DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) tree.getCellRenderer();
        String elements[] = {"A", "B", "C", "D"};
        JComboBox comboBox = new JComboBox(elements);
        comboBox.setEditable(true);
        TreeCellEditor comboEditor = new DefaultCellEditor(comboBox);
        TreeCellEditor editor = new DefaultTreeCellEditor(tree, renderer, comboEditor);
        tree.setCellEditor(editor);


        JScrollPane scrollPane = new JScrollPane(tree);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setSize(300, 150);
        frame.setVisible(true);
    }
}
