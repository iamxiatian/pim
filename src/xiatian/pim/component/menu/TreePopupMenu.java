package xiatian.pim.component.menu;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import xiatian.pim.component.dialog.JMessageBox;
import xiatian.pim.component.doc.EditPane;
import xiatian.pim.component.tree.ContentTree;
import xiatian.pim.component.tree.MyTreeNode;
import xiatian.pim.component.tree.NodeData;
import xiatian.pim.domain.Journal;
import xiatian.pim.io.PimDb;
import xiatian.pim.listener.PimController;

public class TreePopupMenu extends JPopupMenu {

  private static final long serialVersionUID = 681556764384962228L;

  public TreePopupMenu(final ContentTree tree){
    setSize(new Dimension(100,50));
    PopupMenuListener popupMenuListener = new MyPopupMenuListener();

    addPopupMenuListener(popupMenuListener);

    JMenuItem newMenuItem = new JMenuItem("新建记录");
    add(newMenuItem);
    
    newMenuItem.addActionListener(new ActionListener() {
      
      @Override
      public void actionPerformed(ActionEvent e) {
        if(tree.getSelectionPath()==null) return;
        MyTreeNode node = (MyTreeNode)tree.getSelectionPath().getLastPathComponent();
        if(node == null) return;
                
        NodeData data = (NodeData)node.getUserObject();
        Journal parentJournal = data.getJournal();
        String title = "我的新纪录";   
        Journal journal = new Journal(parentJournal.getId(), node.getChildCount(), title, "");
        PimDb.getInstance().addJournal(journal);
        NodeData newData = new NodeData(journal);
        node.insert(new MyTreeNode(newData), node.getChildCount());
        
        tree.getMyTreeModel().nodeStructureChanged(node);
      }
    });
    
    JMenuItem removeMenuItem = new JMenuItem("删除当前记录");
    add(removeMenuItem);
    
    removeMenuItem.addActionListener(new ActionListener() {
      
      @Override
      public void actionPerformed(ActionEvent e) {
        if(tree.getSelectionPath()==null) return;
        MyTreeNode node = (MyTreeNode)tree.getSelectionPath().getLastPathComponent();
        if(node == null) return;
        
        NodeData data = (NodeData)node.getUserObject();
        
        if(data.getChildrenCount()>0) {
          //JOptionPane.showMessageDialog(PimController.getInstance().getJFrame(),  "仅叶子节点允许直接删除", "删除提示", JOptionPane.WARNING_MESSAGE);
          JMessageBox.show(PimController.getInstance().getJFrame(), "警告", "仅叶子节点允许直接删除.");
          return;
        }
        
        Object[] options = {"Yes, please",
        "No, thanks"};
        int n = JOptionPane.showOptionDialog(PimController.getInstance().getJFrame(),
        "Are you sure to remove " + data.getName() + "?",
        "Confirm",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE,
        null,
        options,
        options[1]);
    
        if(n!=0) return;
        
        PimDb.getInstance().removeJournal(data.getJournal().getId());
        MyTreeNode parent = (MyTreeNode)node.getParent();
        parent.remove(parent.getIndex(node));        
        tree.getMyTreeModel().nodeStructureChanged(parent);
      }
    });
    
    JMenuItem recursiveRemoveMenuItem = new JMenuItem("递归删除(慎用)");
    add(recursiveRemoveMenuItem);
    
    recursiveRemoveMenuItem.addActionListener(new ActionListener() {
      
      @Override
      public void actionPerformed(ActionEvent e) {
        if(tree.getSelectionPath()==null) return;
        MyTreeNode node = (MyTreeNode)tree.getSelectionPath().getLastPathComponent();
        if(node == null) return;
        
        NodeData data = (NodeData)node.getUserObject();
                
        Object[] options = {"Yes, please",
        "No, thanks"};
        int n = JOptionPane.showOptionDialog(PimController.getInstance().getJFrame(),
        "Are you sure to remove " + data.getName() + "?",
        "Confirm",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE,
        null,
        options,
        options[1]);
    
        if(n!=0) return;
        
        PimDb.getInstance().removeRecursiveJournal(data.getJournal().getId());
        MyTreeNode parent = (MyTreeNode)node.getParent();
        parent.remove(parent.getIndex(node));        
        tree.getMyTreeModel().nodeStructureChanged(parent);
      }
    });
    
    this.addSeparator();
    
    ItemListener styleListener = new ItemListener(){

        @Override
        public void itemStateChanged(ItemEvent e) {
            JRadioButtonMenuItem item = (JRadioButtonMenuItem)e.getItem();
            String styleName = item.getText();
            
            if(tree.getSelectionPath()==null) return;
            MyTreeNode node = (MyTreeNode)tree.getSelectionPath().getLastPathComponent();
            if(node == null) return;
            
            NodeData data = (NodeData)node.getUserObject();
            Journal journal = data.getJournal();
            journal.setProperty(Journal.Property_Style, styleName);
            
            PimDb.getInstance().updateJournalProperties(journal.getId(), Journal.Property_Style, styleName);
            PimController.getInstance().getDocView().getEditPane().setSyntaxEditingStyle(EditPane.STYLES.get(styleName));
        }
        
    };
    
    ButtonGroup styleGroup = new ButtonGroup();
    JMenu styleMenu = new JMenu("阅读格式");
    add(styleMenu);
    
    for(String name:EditPane.STYLES.keySet()) {
        JRadioButtonMenuItem styleItem = new JRadioButtonMenuItem(name);
        styleMenu.add(styleItem);
        styleItem.setName(name);
        styleItem.addItemListener(styleListener);
        styleGroup.add(styleItem);        
    }
    
    this.addSeparator();
    
    JMenuItem upMoveMenuItem = new JMenuItem("上移记录");
    add(upMoveMenuItem);
    upMoveMenuItem.addActionListener(new NodeMoveActionListener(tree, true));
    
    JMenuItem downMoveMenuItem = new JMenuItem("下移记录");
    add(downMoveMenuItem);
    downMoveMenuItem.addActionListener(new NodeMoveActionListener(tree, false));
    
    this.addSeparator();
    JMenuItem setPasswordMenuItem = new JMenuItem("设置密码");
    add(setPasswordMenuItem);
    setPasswordMenuItem.addActionListener(new ActionListener() {
      
      @Override
      public void actionPerformed(ActionEvent e) {        
        if(tree.getSelectionPath()==null) return;
        MyTreeNode node = (MyTreeNode)tree.getSelectionPath().getLastPathComponent();
        if(node == null) return;
        NodeData data = (NodeData)node.getUserObject();
        Journal journal = data.getJournal();
        if(!journal.isOpened()) {
          JMessageBox.show(PimController.getInstance().getJFrame(), "提示", "当前记录未打开");
          return;
        } else {
          PasswordDialog dialog = new PasswordDialog(journal);
          dialog.setVisible(true);
        }
        
      }
    });
    
  }
  
  class NodeMoveActionListener implements ActionListener {
    private ContentTree tree;
    private boolean up;
    public NodeMoveActionListener(ContentTree tree, boolean up){
      this.tree = tree;
      this.up = up;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
      if(tree.getSelectionPath()==null) return;
      MyTreeNode node = (MyTreeNode)tree.getSelectionPath().getLastPathComponent();
      if(node == null) return;
      NodeData data = (NodeData)node.getUserObject();
      Journal journal = data.getJournal();
      MyTreeNode parent = (MyTreeNode)node.getParent();     
      MyTreeNode neighbor = null;
      if(up){
        neighbor = (MyTreeNode)parent.getChildBefore(node);
      }else{
        neighbor = (MyTreeNode)parent.getChildAfter(node);
      }
      if(neighbor==null) return;
      
      Journal neighborJournal = neighbor.getJournal();
      int position = parent.getIndex(node);
      int neighborPosition = parent.getIndex(neighbor);
      journal.setPosition(neighborPosition);
      neighborJournal.setPosition(position);      
      PimDb.getInstance().updateJournal(neighborJournal);
      PimDb.getInstance().updateJournal(journal);
      node.setUserObject(neighbor.getUserObject());
      neighbor.setUserObject(data);
      
      tree.getMyTreeModel().nodeStructureChanged(parent);      
    }
    
  }
  
  private class MyPopupMenuListener implements PopupMenuListener {
    public void popupMenuCanceled(PopupMenuEvent popupMenuEvent) {
      //System.out.println("Canceled");
    }

    public void popupMenuWillBecomeInvisible(PopupMenuEvent popupMenuEvent) {
      //System.out.println("Becoming Invisible");
    }

    public void popupMenuWillBecomeVisible(PopupMenuEvent popupMenuEvent) {
      //System.out.println("Becoming Visible");
    }
  }
  
  private class PasswordDialog extends JDialog {
    private static final long serialVersionUID = -4348550326095715620L;
    
    public PasswordDialog(final Journal journal){   
      this.setTitle("设置密码");      
      this.setModal(true);
      this.setLocationRelativeTo(PimController.getInstance().getJFrame());
      
      final JPasswordField passwordField1 = new JPasswordField(20);
      final JPasswordField passwordField2 = new JPasswordField(20);
      Container pane = this.getContentPane();
      pane.setLayout(new GridLayout(3, 1));
      JPanel line = new JPanel();
      line.setLayout(new FlowLayout());
      line.add(new JLabel("输入密码: "));
      line.add(passwordField1);
      pane.add(line);
      
      line = new JPanel();
      line.setLayout(new FlowLayout());
      line.add(new JLabel("确认密码: "));
      line.add(passwordField2);
      pane.add(line);
      
      line = new JPanel();
      line.setLayout(new FlowLayout());
      JButton cancelButton = new JButton("取消");
      line.add(cancelButton);
      JButton okButton = new JButton("确认");
      line.add(okButton);
      pane.add(line);
      
      this.pack();
      
      cancelButton.addActionListener(new ActionListener() {
        
        @Override
        public void actionPerformed(ActionEvent e) {
          PasswordDialog.this.setVisible(false);
        }
      });
      
      okButton.addActionListener(new ActionListener() {
        
        @Override
        public void actionPerformed(ActionEvent e) {
          String p1 = new String(passwordField1.getPassword());
          String p2 = new String(passwordField2.getPassword());
          if(p1.equals(p2)){
            journal.setPassword(p1);
            PimDb.getInstance().updateJournal(journal);
            PasswordDialog.this.setVisible(false);
          }else{
            //JOptionPane.showMessageDialog(PimController.getInstance().getJFrame(), "两次密码输入不一致");
            JMessageBox.show(PimController.getInstance().getJFrame(), "警告", "两次密码输入不一致");
          }          
        }
      });
    }
  }
}

