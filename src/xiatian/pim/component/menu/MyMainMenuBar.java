package xiatian.pim.component.menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JColorChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import xiatian.pim.component.OpenListener;
import xiatian.pim.component.chooser.JFontChooser;
import xiatian.pim.conf.PimConf;
import xiatian.pim.listener.PimController;
import xiatian.pim.listener.PimLog;

public class MyMainMenuBar extends JMenuBar {
  private static final long serialVersionUID = 553678038354615907L;  
  
  public MyMainMenuBar(JMenu... menus){
    JMenu fileMenu = new JMenu("File");  
    fileMenu.setMnemonic(KeyEvent.VK_F);  
    this.add(fileMenu);  
    
    JMenu viewMenu = new JMenu("View");  
    viewMenu.setMnemonic(KeyEvent.VK_V);  
    this.add(viewMenu);  
    
    if(menus!=null){
//      for(JMenu m:menus){
//        this.add(m);
//      }
    }
    
    JMenu helpMenu = new JMenu("Help");  
    viewMenu.setMnemonic(KeyEvent.VK_H);  
    this.add(helpMenu); 
    
    JMenuItem openMenuItem = new JMenuItem("Open", KeyEvent.VK_O);  
    fileMenu.add(openMenuItem);  
    openMenuItem.setAccelerator(KeyStroke.getKeyStroke("ctrl O"));
    openMenuItem.addActionListener(new OpenListener());
    
    JMenuItem saveMenuItem = new JMenuItem("Save", KeyEvent.VK_S);  
    fileMenu.add(saveMenuItem);  
    saveMenuItem.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
    saveMenuItem.addActionListener(new ActionListener() {      
      @Override
      public void actionPerformed(ActionEvent e) {
        PimController.getInstance().getDocView().save();        
      }
    });
    
    
    JMenuItem newMenuItem = new JMenuItem("New", KeyEvent.VK_N);  
    fileMenu.add(newMenuItem);  
    JCheckBoxMenuItem caseMenuItem = new JCheckBoxMenuItem("Case Sensitive");  
    caseMenuItem.setMnemonic(KeyEvent.VK_C);  
    fileMenu.add(caseMenuItem);  
    
    JCheckBoxMenuItem showToolBarItem = new JCheckBoxMenuItem("显示工具栏", PimConf.getInstance().getBoolean("toolbar.show", false));
    viewMenu.add(showToolBarItem);   
    showToolBarItem.addItemListener(new ItemListener() {
      
      @Override
      public void itemStateChanged(ItemEvent e) {
        AbstractButton button = (AbstractButton) e.getItem();  
        PimConf.getInstance().setBoolean("toolbar.show", button.isSelected());        
        PimLog.getInstance().showToolBar(button.isSelected());        
      }
    });
    
    JCheckBoxMenuItem showStatusBarItem = new JCheckBoxMenuItem("显示状态栏", PimLog.getInstance().isShowStatusBar());
    viewMenu.add(showStatusBarItem);   
    showStatusBarItem.addItemListener(new ItemListener() {
      
      @Override
      public void itemStateChanged(ItemEvent e) {
        AbstractButton button = (AbstractButton) e.getItem();  
        PimLog.getInstance().showStatusBar(button.isSelected());        
      }
    });
    
    JCheckBoxMenuItem showLineNumberItem = new JCheckBoxMenuItem("显示行号", PimConf.getInstance().getBoolean("show.linenumber", true));
    viewMenu.add(showLineNumberItem);   
    showLineNumberItem.addItemListener(new ItemListener() {
      
      @Override
      public void itemStateChanged(ItemEvent e) {
        AbstractButton button = (AbstractButton) e.getItem();  
        PimController.getInstance().getDocView().getEditPane().showLineNumber(button.isSelected());    
      }
    });    
    viewMenu.addSeparator();
    JMenuItem globalFontMenuItem = new JMenuItem("全局字体设置");
    viewMenu.add(globalFontMenuItem);
    globalFontMenuItem.addActionListener(new ActionListener() {
      
      @Override
      public void actionPerformed(ActionEvent e) {
        Font font = PimConf.getInstance().getFont("global.font", new Font("STXinwei", 0, 14));
        font = JFontChooser.showDialog(PimController.getInstance().getJFrame(), "Choose Global Font(need restart)", "Sample Font Text 示例字体", font);
        PimConf.getInstance().setFont("global.font", font);
        PimConf.getInstance().save();
        //Start.InitGlobalFont(font);
      }
    }); 
    
    
    viewMenu.addSeparator();
    
    JMenuItem fontMenuItem = new JMenuItem("编辑器字体设置");
    viewMenu.add(fontMenuItem);
    fontMenuItem.addActionListener(new ActionListener() {
      
      @Override
      public void actionPerformed(ActionEvent e) {
        Font font = JFontChooser.showDialog(PimController.getInstance().getJFrame(), "Choose Editor Font", "Sample Font Text 示例字体", PimController.getInstance().getFont());
        PimController.getInstance().setFont(font);        
      }
    }); 
    
    JMenuItem forecolorItem = new JMenuItem("编辑器前景色");
    viewMenu.add(forecolorItem);
    forecolorItem.addActionListener(new ActionListener() {      
      @Override
      public void actionPerformed(ActionEvent e) {
        Color newColor = JColorChooser.showDialog(null, "Choose Editor Forground Color", PimController.getInstance().getForeground());
        PimController.getInstance().setForeground(newColor);
        
      }
    });
    
    JMenuItem backColorItem = new JMenuItem("编辑器背景颜色");
    viewMenu.add(backColorItem);
    backColorItem.addActionListener(new ActionListener() {      
      @Override
      public void actionPerformed(ActionEvent e) {
        Color newColor = JColorChooser.showDialog(null, "Choose Editor Background Color", PimController.getInstance().getBackground());
        PimController.getInstance().setBackground(newColor);
        
      }
    });
    
    viewMenu.addSeparator();
    
    JMenuItem treeFontMenuItem = new JMenuItem("目录树字体设置");
    viewMenu.add(treeFontMenuItem);
    treeFontMenuItem.addActionListener(new ActionListener() {
      
      @Override
      public void actionPerformed(ActionEvent e) {
        Font font = JFontChooser.showDialog(PimController.getInstance().getJFrame(), "Choose Editor Font", "Sample Font Text 示例字体", PimController.getInstance().getTree().getFont());
        if(font!=null){
          PimController.getInstance().getTree().setFont(font);
          PimConf.getInstance().setFont("content.tree.font", font);
        }
      }
    }); 
    
    JMenuItem treeForecolorItem = new JMenuItem("目录树前景色");
    viewMenu.add(treeForecolorItem);
    treeForecolorItem.addActionListener(new ActionListener() {      
      @Override
      public void actionPerformed(ActionEvent e) {
        Color newColor = JColorChooser.showDialog(null, "Choose Tree Forground Color", PimController.getInstance().getTree().getForeground());
        if(newColor!=null){
          PimController.getInstance().getTree().setForeground(newColor);
          PimConf.getInstance().setColor("content.tree.color", newColor);
        }
        
      }
    });
    
    JMenuItem treeBackColorItem = new JMenuItem("目录树背景色");
    viewMenu.add(treeBackColorItem);
    treeBackColorItem.addActionListener(new ActionListener() {      
      @Override
      public void actionPerformed(ActionEvent e) {
        Color newColor = JColorChooser.showDialog(null, "Choose Tree Background Color", PimController.getInstance().getTree().getForeground());
        if(newColor!=null){
          PimController.getInstance().getTree().setBackground(newColor);
          PimConf.getInstance().setColor("content.tree.bgcolor", newColor);
        }
        
      }
    });
    
    /////////////////////////////////////
    //about
    JMenuItem aboutMenuItem = new JMenuItem("About", KeyEvent.VK_A);
    helpMenu.add(aboutMenuItem);
  }
  
}
