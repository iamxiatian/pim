package xiatian.pim.component.dialog;

import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.KeyStroke;

public class JPasswordInputBox extends JDialog {  
  private static final long serialVersionUID = 909697785L;
  private String value = null;
  public JPasswordInputBox(Component parent, String caption, String title, String defaultValue) {
    this.setTitle(caption);      
    this.setModal(true);    
    
    final JPasswordField field = new JPasswordField(20);  
    field.setText(defaultValue);
    Container pane = this.getContentPane();
    pane.setLayout(new GridLayout(3, 1));
    JPanel line = new JPanel();
    line.setLayout(new FlowLayout());
    line.add(new JLabel(title));
    pane.add(line);
    
    line = new JPanel();
    line.setLayout(new FlowLayout());
    line.add(new JLabel("     "));
    line.add(field);
    pane.add(line);
    
    line = new JPanel();
    line.setLayout(new FlowLayout());
    JButton cancelButton = new JButton("取消");
    line.add(cancelButton);
    JButton okButton = new JButton("确认");
    line.add(okButton);
    pane.add(line);
    
    this.pack();
    this.setLocationRelativeTo(parent);    
    
    getRootPane().setDefaultButton(okButton);
    ActionListener okListener = new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        value = new String(field.getPassword());
        JPasswordInputBox.this.setVisible(false);
      }
    };
    okButton.addActionListener(okListener);
    okButton.registerKeyboardAction(okListener, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
        JComponent.WHEN_IN_FOCUSED_WINDOW);

    ActionListener cancelListener = new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        value = null;
        JPasswordInputBox.this.setVisible(false);
      }
    };
    cancelButton.addActionListener(cancelListener);
    cancelButton.registerKeyboardAction(cancelListener, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
        JComponent.WHEN_IN_FOCUSED_WINDOW);
  }
  
  public String getInputValue(){
    return value;
  }
  
  public static String show(Component parent, String caption, String title, String defaultValue){
    JPasswordInputBox box = new JPasswordInputBox(parent, caption, title, defaultValue);
    box.setVisible(true);
    return box.getInputValue();
  }
}
