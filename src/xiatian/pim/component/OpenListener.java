package xiatian.pim.component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import xiatian.pim.component.dialog.OpenPimDialog;
import xiatian.pim.component.tree.ContentTree;
import xiatian.pim.conf.PimConf;
import xiatian.pim.io.PimDb;
import xiatian.pim.listener.PimController;

/**
 * Open Database listener
 *
 * @author <a href="mailto:iamxiatian@gmail.com">xiatian</a>
 */
public class OpenListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        List<String> lastOpenedDbs = PimConf.getInstance().getList("pim.db.lasts");
        String dbname = OpenPimDialog.show(PimController.getInstance().getJFrame(), lastOpenedDbs);
        if (dbname == null) return;
        File db = new File(dbname);
        db.getParentFile().mkdirs();
        try {
            PimDb pimDb = PimDb.getInstance();
            PimController.getInstance().getDocView().save();
            pimDb.close();
            pimDb.open(db.getAbsolutePath());
            //PimConf.getInstance().set("pim.db.name", db.getAbsolutePath());
            PimConf.getInstance().addList("pim.db.lasts", db.getAbsolutePath());
            PimController.getInstance().getJFrame().setTitle("PIM4XIATIAN-" + db.getAbsolutePath());
            ContentTree tree = PimController.getInstance().getTree();
            tree.reload();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
