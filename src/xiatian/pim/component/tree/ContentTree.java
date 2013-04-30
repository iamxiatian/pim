package xiatian.pim.component.tree;

import java.awt.Color;
import java.awt.Event;
import java.awt.Font;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceContext;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import xiatian.pim.component.dialog.JMessageBox;
import xiatian.pim.component.dialog.JPasswordInputBox;
import xiatian.pim.component.menu.TreePopupMenu;
import xiatian.pim.conf.PimConf;
import xiatian.pim.domain.Journal;
import xiatian.pim.listener.PimController;
import xiatian.pim.util.BlankUtils;

/**
 * 目录树
 *
 * @author <a href="mailto:iamxiatian@gmail.com">xiatian</a>
 */
public class ContentTree extends JTree {
    private static final long serialVersionUID = 4165263193501648251L;

    private MyTreeNode root = null;

    private MyTreeModel treeModel = null;

    //拖动的树节点
    MyTreeNode dragTreeNode = null;

    public void reload() {
        root = MyTreeNode.create();
        this.setComponentPopupMenu(new TreePopupMenu(this));
        this.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        this.treeModel = new MyTreeModel(root, true);
        this.setModel(treeModel);
        this.updateUI();
    }

    public ContentTree() {
        this.setFont(PimConf.getInstance().getFont("content.tree.font", new Font("SimHei", 0, 12)));
        Color color = PimConf.getInstance().getColor("content.tree.color", null);
        if (color != null) this.setForeground(color);
        color = PimConf.getInstance().getColor("content.tree.bgcolor", null);
        if (color != null) this.setBackground(color);

        root = MyTreeNode.create();

        this.setComponentPopupMenu(new TreePopupMenu(this));
        this.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        MouseListener mouseListener = new MouseAdapter() {
            //      public void mousePressed(MouseEvent e) {
//        int selRow = getRowForLocation(e.getX(), e.getY());// 返回节点所在的行，-1表示鼠标定位不在显示的单元边界内
//        TreePath selPath = getPathForLocation(e.getX(), e.getY());// 返回指定节点的树路径
//        if (selRow != -1) {// 如果选中
//          if (e.getClickCount() == 2) {// 如果是双击
//            //myDoubleClick(selRow, selPath);
//            System.out.println("double clicked");
//          } else if (e.getButton() == MouseEvent.BUTTON2) {
//            System.out.println("right clicked");
//            // 右键单击（好像是这个）
//            setSelectionPath(selPath); 
//          }
//        }
//      }
//      
            public void mouseReleased(MouseEvent e) {
                if (e.getModifiers() == Event.META_MASK) {
                    TreePath treePath = getPathForLocation(e.getX(), e.getY());
                    if (treePath != null) {
                        setSelectionPath(treePath);
                    }
                    return;
                }
            }
        };
        this.addMouseListener(mouseListener);

        this.treeModel = new MyTreeModel(root, true);
        this.setModel(treeModel);

        MyTreeCellRenderer treeCellRender = new MyTreeCellRenderer();
        this.setCellRenderer(treeCellRender);
        this.setEditable(true);

        this.setCellEditor(new MyTreeCellEditor(this, treeCellRender));

        this.addTreeWillExpandListener(new TreeWillExpandListener() {
            //折叠时的处理
            public void treeWillCollapse(TreeExpansionEvent event) {
                TreePath path = event.getPath();
                MyTreeNode node = (MyTreeNode) path.getLastPathComponent();
                NodeData data = (NodeData) node.getUserObject();
                data.setExpanded(false);
                //node.removeAllChildren();
            }

            //打开时的处理
            public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {

                TreePath path = event.getPath();
                MyTreeNode node = (MyTreeNode) path.getLastPathComponent();
                NodeData data = (NodeData) node.getUserObject();
                Journal journal = data.getJournal();
                if (!BlankUtils.isBlank(journal.getPassword()) && !journal.isOpened()) {
                    String password = JPasswordInputBox.show(PimController.getInstance().getJFrame(), "输入提示", "请输入密码：", "");
                    //取消操作
                    if (password == null) {
                        setExpandedState(path, false);// 父亲节点可展开，但孩子节点都不行
                        throw new ExpandVetoException(event);
                    }
                    if (!password.equalsIgnoreCase(journal.getPassword())) {
                        setExpandedState(path, false);// 父亲节点可展开，但孩子节点都不行

                        JMessageBox.show(PimController.getInstance().getJFrame(), "错误提示", "密码输入错误！");
                        throw new ExpandVetoException(event);
                    }
                }

                //设置已经打开记录标记
                journal.setOpened(true);
                //展开了，则更换图标
                data.setExpanded(true);
                //设置docText的内容
                if (journal.equals(PimController.getInstance().getDocView().getCurrentJournal())) {
                    PimController.getInstance().getDocView().setJournal(journal);
                }

                if (node.getChildCount() == 0) {
                    //加入孩子节点
                    MyTreeNode.appendChildren(node, data.getJournal().getId());
                }
            }
        });

        this.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {

                MyTreeNode node = (MyTreeNode) getLastSelectedPathComponent();
                if (node == null) {
                    return;
                }

                NodeData data = (NodeData) node.getUserObject();
                Journal journal = data.getJournal();
                if (BlankUtils.isBlank(journal.getPassword())) {
                    journal.setOpened(true);
                } else if (!journal.isOpened()) {
                    String password = JPasswordInputBox.show(PimController.getInstance().getJFrame(), "输入提示", "请输入密码：", "");
                    if (password != null) {
                        if (password.equalsIgnoreCase(journal.getPassword())) {
                            journal.setOpened(true);
                        } else {
                            JMessageBox.show(PimController.getInstance().getJFrame(), "错误提示", "密码输入错误！");
                        }
                    }
                }

                PimController.getInstance().treeNodeSelected(data);
            }

        });

        //拖动处理
        DragSource dragSource = DragSource.getDefaultDragSource();
        //设置drap(拖)数据源对应的对象jtr，并且添加监听器
        dragSource.createDefaultDragGestureRecognizer(this,
                DnDConstants.ACTION_MOVE, new DragGestureListener() {

            @Override
            public void dragGestureRecognized(DragGestureEvent dge) {
                ContentTree tree = (ContentTree) dge.getComponent();
                TreePath path = tree.getSelectionPath();
                if (path != null) {
                    dragTreeNode = (MyTreeNode) path.getLastPathComponent();
                    if (dragTreeNode != null) {
                        DragAndDropTransferable dragAndDropTransferable = new DragAndDropTransferable(dragTreeNode);
                        //启动拖动操作，dragAndDropTransferable为封装移动、复制或连接的数据
                        //DragAndDropDragSourceListener实例十跟踪操作进程和完成操作启动者的任务
                        dge.startDrag(DragSource.DefaultMoveDrop, dragAndDropTransferable, new DragAndDropDragSourceListener());
                    }
                }

            }

        });
        //设置放置目标jtr，并且添加监听器
        //DropTarget dropTarget = new DropTarget(this, new DragAndDropDropTargetListener());
        new DropTarget(this, new DragAndDropDropTargetListener());
    }

    public MyTreeModel getMyTreeModel() {
        return this.treeModel;
    }

    // 因为要JTreee既为拖动源又是放置目标，所以把DragGestureListener作为一个内部类比较好
    class DragAndDropDragSourceListener implements DragSourceListener {

        // dragEnter(),dragExit(),dragOver(),dropActionChanged()这几个方法只有在调用放置目标监听器中
        // 的对应方法并且防止目标不拒绝操作后，才调用这个拖动源的方法。

        /**
         * 在光标进入放置组件的显示区时调用
         *
         * @param e DragSourceDragEvent
         */
        public void dragEnter(DragSourceDragEvent e) {
            // 设置光标
            DragSourceContext context = e.getDragSourceContext();
            int dropAction = e.getDropAction();
            if ((dropAction & DnDConstants.ACTION_COPY) != 0) {
                context.setCursor(DragSource.DefaultCopyDrop);
            } else if ((dropAction & DnDConstants.ACTION_MOVE) != 0) {
                context.setCursor(DragSource.DefaultMoveDrop);
            } else {
                context.setCursor(DragSource.DefaultCopyNoDrop);
            }
        }

        /**
         * 在光标退出放置组件的显示区时发生
         *
         * @param e DragSourceEvent
         */
        public void dragExit(DragSourceEvent e) {
        }

        /**
         * 在光标进入放置组件显示区之后移动时调用
         *
         * @param e DragSourceDragEvent
         */
        public void dragOver(DragSourceDragEvent e) {
        }

        /**
         * 选择放置操作的修饰键的状态变化
         *
         * @param e DragSourceDragEvent
         */
        public void dropActionChanged(DragSourceDragEvent e) {
        }

        /**
         * 放置发生并调用DropTargetListener的drop()方法后，调用dragDropEnd()方法， 告诉拖动源放置已经完成
         *
         * @param e DragSourceDropEvent
         */
        public void dragDropEnd(DragSourceDropEvent e) {
            // getDropSuccess()对应DropTargetListener的drop()方法调用dropcomplete()时指目标指定的值
            // getDropAction()对应DropTargetListener的drop()方法调用acceptDrop()时指定的操作
            if (!e.getDropSuccess() || e.getDropAction() != DnDConstants.ACTION_MOVE) {
                return;
            }

            DragSourceContext context = e.getDragSourceContext();
            Object comp = context.getComponent();
            if (comp == null || !(comp instanceof ContentTree)) {
                return;
            }
            MyTreeNode dragTreeNode = getDragTreeNode();

            if (dragTreeNode != null) {
                ((DefaultTreeModel) ContentTree.this.getModel()).removeNodeFromParent(dragTreeNode);
                // 设置拖动的树节点为空
                setDragTreeNode(null);
            }

        }
    }

    public MyTreeNode getDragTreeNode() {
        return dragTreeNode;
    }

    public void setDragTreeNode(MyTreeNode dragTreeNode) {
        this.dragTreeNode = dragTreeNode;
    }
}
