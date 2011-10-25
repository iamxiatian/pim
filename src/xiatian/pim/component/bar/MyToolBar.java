package xiatian.pim.component.bar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JToolBar;

import xiatian.pim.component.OpenListener;
import xiatian.pim.listener.PimController;
import xiatian.pim.util.ImageUtils;

public class MyToolBar extends JToolBar {
  private static final long serialVersionUID = 8084521537992925964L;  
  
  public MyToolBar(){
    super();
    JButton saveButton = new JButton("Save");
    saveButton.setIcon(ImageUtils.createImageIcon("/images/small/save.png", "Save"));
    saveButton.addActionListener(new ActionListener() {
      
      @Override
      public void actionPerformed(ActionEvent e) {
        PimController.getInstance().getDocView().save();        
      }
    });
    this.add(saveButton);
    
    JButton openButton = new JButton("Open");
    openButton.setIcon(ImageUtils.createImageIcon("/images/small/open.png", "Open Database"));
    this.add(openButton);
    openButton.addActionListener(new OpenListener());    
  }
  
}
