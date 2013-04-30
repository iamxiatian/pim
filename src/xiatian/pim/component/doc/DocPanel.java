package xiatian.pim.component.doc;

import xiatian.pim.domain.Journal;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class DocPanel extends JTabbedPane {
    private static final long serialVersionUID = -7243631498293051815L;

    DocPanel(final DocView docView) {
        this.addTab("EDIT", new JScrollPane(docView.getEditPane()));
        this.addTab("VIEW", new JScrollPane(docView.getViewPane()));
        this.setTabPlacement(JTabbedPane.BOTTOM);
        this.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                switch (DocPanel.this.getSelectedIndex()) {
                    case 0:
                        docView.toState(Journal.STATE_EDIT);
                        break;
                    case 1:
                        docView.toState(Journal.STATE_READ);
                        break;
                }

            }
        });
    }
}
