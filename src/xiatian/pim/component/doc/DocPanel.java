package xiatian.pim.component.doc;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

public class DocPanel extends JTabbedPane {
  private static final long serialVersionUID = -7243631498293051815L;

	DocPanel(final DocView docView){
		this.addTab("My Journal", new JScrollPane(docView.getEditPane()));
//		this.addTab("HTML View", new JScrollPane(docView.getViewPane()));
		this.setTabPlacement(JTabbedPane.BOTTOM);
//		this.addChangeListener(new ChangeListener() {
//			
//			@Override
//			public void stateChanged(ChangeEvent e) {
//				switch(DocPanel.this.getSelectedIndex()){
//				case 0:
//					docView.toState( Journal.STATE_EDIT);
//					break;
//				case 1:
//					docView.toState( Journal.STATE_READ);
//					break;
//				}
//				
//			}
//		});
	}
}
