package xiatian.pim.component.table;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class TextDialog extends JDialog {
  private static final long serialVersionUID = -2231816274380128333L;

  private JTextPane textArea = new JTextPane();
  public TextDialog(Component parent, ActionListener okListener){
    super();
    this.setTitle("PIM");
    this.setModal(true);
    this.setSize(400, 350);
    this.setLocationRelativeTo(parent);
    this.setLayout(new BorderLayout());   
    this.add(new JScrollPane(textArea), BorderLayout.CENTER);
    JButton okButton = new JButton("OK");
    this.add(okButton, BorderLayout.SOUTH);
    okButton.addActionListener(okListener);
  }
  
  public void setText(String v){
    this.textArea.setText(v);
  }
  
  public String getText(){
    return textArea.getText();
  }
}
