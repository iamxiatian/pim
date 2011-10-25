package xiatian.pim.component.dialog;

import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class JMessageBox extends JDialog {  
  private static final long serialVersionUID = 90969199438185L;
  
  public JMessageBox(Component parent, String caption, String message) {
    this.setTitle(caption);      
    this.setModal(true);
    this.setLocationRelativeTo(parent);
    
    Container pane = this.getContentPane();
    pane.setLayout(new GridLayout(3, 1));
    JPanel line = new JPanel();
    line.setLayout(new FlowLayout());
    line.add(new JLabel(message));
    pane.add(line);
    
    
    line = new JPanel();
    line.setLayout(new FlowLayout());    
    JButton okButton = new JButton("确认");
    line.add(okButton);
    pane.add(line);
    
    this.pack();    
    
    okButton.addActionListener(new ActionListener() {
      
      @Override
      public void actionPerformed(ActionEvent e) {        
        JMessageBox.this.setVisible(false);
      }
    });
  }
  
  public static void show(Component parent, String caption, String message){
    JMessageBox box = new JMessageBox(parent, caption, message);
    box.setVisible(true);
  }

}
