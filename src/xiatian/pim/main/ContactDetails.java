package xiatian.pim.main;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import xiatian.pim.layout.SWTGridData;
import xiatian.pim.layout.SWTGridLayout;

/**
 * @author Daniel Spiewak
 */
public class ContactDetails extends JFrame {
  private static final long serialVersionUID = -6633191495627654389L;

  public ContactDetails() {
		super("Contact Details");
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(400, 265);
		
		JPanel body = new JPanel(new SWTGridLayout(2, false));
		getContentPane().add(body);
		
		body.add(new JLabel("First Name:"));
		
		JTextField firstName = new JTextField();
		
		SWTGridData data = new SWTGridData();
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = SWTGridData.FILL;
		body.add(firstName, data);
		
		body.add(new JLabel("Last Name:"));
		
		JTextField lastName = new JTextField();
		
		data = new SWTGridData();
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = SWTGridData.FILL;
		body.add(lastName, data);
		
		JCheckBox address = new JCheckBox("Address");
		
		data = new SWTGridData();
		data.horizontalSpan = 2;
		data.horizontalIndent = 15;
		body.add(address, data);
		
		JPanel addressPanel = new JPanel(new SWTGridLayout(4, false));
		addressPanel.setBorder(BorderFactory.createEtchedBorder());
		
		data = new SWTGridData();
		data.grabExcessVerticalSpace = true;
		data.horizontalAlignment = SWTGridData.FILL;
		data.verticalAlignment = SWTGridData.BEGINNING;
		data.horizontalSpan = 2;
		body.add(addressPanel, data);
		
		addressPanel.add(new JLabel("Line 1:"));
		
		JTextField line1 = new JTextField();
		
		data = new SWTGridData();
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = SWTGridData.FILL;
		data.horizontalSpan = 3;
		addressPanel.add(line1, data);
		
		addressPanel.add(new JLabel("Line 2:"));
		
		JTextField line2 = new JTextField();
		
		data = new SWTGridData();
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = SWTGridData.FILL;
		data.horizontalSpan = 3;
		addressPanel.add(line2, data);
		
		addressPanel.add(new JLabel("City:"));
		
		JTextField city = new JTextField();
		
		data = new SWTGridData();
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = SWTGridData.FILL;
		addressPanel.add(city, data);
		
		addressPanel.add(new JLabel("State:"));
		
		JComboBox state = new JComboBox(new String[] {"MO", "MI", "IL", "WI", "WV"});
		state.setSelectedItem("WI");
		
		data = new SWTGridData();
		data.horizontalAlignment = SWTGridData.FILL;
		addressPanel.add(state, data);
		
		addressPanel.add(new JLabel("Zip:"));
		
		JTextField zip = new JTextField(10);
		addressPanel.add(zip);
		
		JPanel footer = new JPanel();
		getContentPane().add(footer, BorderLayout.SOUTH);
		
		JButton cancel = new JButton("Cancel");
		footer.add(cancel);
		
		JButton save = new JButton("Save");
		getRootPane().setDefaultButton(save);
		footer.add(save);
	}
	
	public static void main(String... args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
          UIManager.setLookAndFeel(UIManager.getLookAndFeel());
        } catch (Exception e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
				
				new ContactDetails().setVisible(true);
			}
		});
	}
}
