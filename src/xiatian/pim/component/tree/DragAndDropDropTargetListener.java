package xiatian.pim.component.tree;

import java.awt.Component;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import xiatian.pim.domain.Journal;
import xiatian.pim.io.PimDb;

//* Drop Target Listener */
public class DragAndDropDropTargetListener implements DropTargetListener {

    // e.rejectDrop()只可以在dragEnter()、dragOver()和dropActionChanged()中调用，不能在drop()中调用

    /**
     * 在光标进入放置组件的显示区时调用
     *
     * @param e DropTargetDragEvent
     */
    public void dragEnter(DropTargetDragEvent e) {
    }

    /**
     * 在光标进入放置组件显示区之后移动时调用
     *
     * @param e DropTargetDragEvent
     */
    public void dragOver(DropTargetDragEvent e) {
    }

    /**
     * 选择放置操作的修饰键的状态变化
     *
     * @param e DropTargetDragEvent
     */
    public void dropActionChanged(DropTargetDragEvent e) {
    }

    /**
     * 在光标退出放置组件的显示区时发生
     *
     * @param e DropTargetEvent
     */
    public void dragExit(DropTargetEvent e) {
    }

    /**
     * 在发生放置时调用，负责接受或拒绝放置请求和处理放置的数据
     *
     * @param e DropTargetDropEvent
     */
    public void drop(DropTargetDropEvent e) {
        // 获取要传递的数据
        Transferable transfer = e.getTransferable();
        MyTreeNode dragSource = null;
        // 是否支持树节点数据的传递
        if (transfer.isDataFlavorSupported(DragAndDropTransferable.TREENODE_FLAVOR)) {
            try {
                // 设置接受移动的操作
                e.acceptDrop(DnDConstants.ACTION_MOVE);
                // 获取传递的数据
                dragSource = (MyTreeNode) transfer.getTransferData(DragAndDropTransferable.TREENODE_FLAVOR);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        if (dragSource == null) { // 拖动的数据源为空则退出
            // 放置不成功
            e.dropComplete(false);
            return;
        }
        // 获取dropTo对象
        DropTarget dt = (DropTarget) e.getSource();
        Component comp = dt.getComponent();
        if (!(comp instanceof ContentTree)) {
            // 放置不成功
            e.dropComplete(false);
            return;
        }
        ContentTree jtr = (ContentTree) comp;
        TreePath treePath = jtr.getPathForLocation(e.getLocation().x, e.getLocation().y);
        if (treePath == null) {
            // 放置不成功
            e.dropComplete(false);
            return;
        }
        MyTreeNode treeNode = (MyTreeNode) treePath.getLastPathComponent();
        if (!isCanDrop(dragSource, treeNode, jtr)) {
            // 放置不成功
            e.dropComplete(false);
            return;
        }

        // 把节点添加到当前树节点下
        treeNode.add(dragSource);
        Journal journal = dragSource.getJournal();
        journal.setParentId(treeNode.getJournal().getId());
        journal.setPosition(treeNode.getChildCount());
        PimDb.getInstance().updateJournal(journal);
        // 更新树节点
        ((DefaultTreeModel) jtr.getModel()).reload(treeNode);

        // 设置放置成功
        e.dropComplete(true);
    }

    /**
     * 判断是否可以放置操作
     *
     * @param dragTreeNode DefaultMutableTreeNode 拖动源的树节点
     * @param dropTreeNode DefaultMutableTreeNode 放置目标的树节点
     * @return boolean
     */
    public boolean isCanDrop(DefaultMutableTreeNode dragTreeNode, DefaultMutableTreeNode dropTreeNode, JTree jtr) {
        if (dragTreeNode == null) { // 拖动源为空则退出
            return false;
        }
        // 设置放置目标为空时不可放置
        if (dropTreeNode == null) {
            return false;
        }
        // 放置目标是拖动源则退出
        if (dragTreeNode == dropTreeNode) {
            return false;
        }
        TreePath dragPath = new TreePath(((DefaultTreeModel) jtr.getModel()).getPathToRoot(dragTreeNode));
        TreePath dropPath = new TreePath(((DefaultTreeModel) jtr.getModel()).getPathToRoot(dropTreeNode));
        String strDragPath = dragPath.toString();
        String strDropPath = dropPath.toString();
        String subDragPath = strDragPath.substring(0, strDragPath.length() - 1);

        if (strDropPath.startsWith(subDragPath)) {// 放置目标是拖动源的子孙节点
            return false;
        }
        if (dragPath.getParentPath().toString().equals(strDropPath)) {// 放置目标是拖动源的父节点
            return false;
        }

        // 放置目标是拖动源的父节点则退出
        if (dragTreeNode.getParent().equals(dropTreeNode)) {
            return false;
        }

        return true;
    }
}
