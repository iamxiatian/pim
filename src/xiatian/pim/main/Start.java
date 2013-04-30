package xiatian.pim.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import xiatian.pim.component.bar.JStatusBar;
import xiatian.pim.component.bar.MyToolBar;
import xiatian.pim.component.border.DoubleLineBorder;
import xiatian.pim.component.dialog.OpenPimDialog;
import xiatian.pim.component.doc.DocView;
import xiatian.pim.component.doc.example.CaretListenerLabel;
import xiatian.pim.component.menu.MyMainMenuBar;
import xiatian.pim.component.table.contact.ContactMainPanel;
import xiatian.pim.component.table.todo.TodoMainPanel;
import xiatian.pim.component.tree.ContentTree;
import xiatian.pim.conf.PimConf;
import xiatian.pim.io.PimDb;
import xiatian.pim.listener.PimController;
import xiatian.pim.listener.PimLog;
import xiatian.pim.util.ImageUtils;

public class Start extends JFrame implements WindowListener {
    private static final long serialVersionUID = -6626036399395306880L;

    private DocView docView = null;
    private ContentTree tree = null;
    private JStatusBar statusBar = null;
    private MyToolBar toolBar = null;
    private static PimConf conf = PimConf.getInstance();

    public Start() {
        this.setTitle("PIM4XIATIAN");
        this.setSize(700, 500);
        this.setLocation(50, 50);
        //this.setBackground(Color.GRAY);
        ImageIcon image = ImageUtils.createImageIcon("/images/medium/pim.png", "pim");
        this.setIconImage(image.getImage());

        Container pane = this.getContentPane();
        pane.setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        pane.add(tabbedPane, BorderLayout.CENTER);

        JPanel editorPanel = new JPanel();
        editorPanel.setLayout(new BorderLayout());
        editorPanel.setBorder(new DoubleLineBorder(Color.GRAY));
        tabbedPane.add("Notes", editorPanel);

        tabbedPane.add("Contacts", new ContactMainPanel());

        tabbedPane.add("Todo", new TodoMainPanel());

        // Create the main component
        CaretListenerLabel messageLabel = new CaretListenerLabel("PIM by XIATIAN");
        messageLabel.setToolTipText("PIM");
        // Create the list of secondary components
        List<JComponent> secondaryComponents = new ArrayList<JComponent>();
        JLabel cylonBar = new JLabel("      ");
        secondaryComponents.add(cylonBar);
        statusBar = new JStatusBar(messageLabel, secondaryComponents);
        statusBar.setPreferredSize(new Dimension(0, 25));
        statusBar.setVisible(conf.getBoolean("statusbar.show", true));
        pane.add(statusBar, BorderLayout.SOUTH);

        PimLog pimLog = PimLog.getInstance();
        pimLog.setStatusMessageLabel(messageLabel);
        pimLog.setStatusBar(statusBar);

        tree = new ContentTree();
        docView = new DocView();
        docView.getEditPane().addCaretListener(messageLabel);
        Dimension minimumSize = new Dimension(200, 200);
        JScrollPane treePane = new JScrollPane(tree);
        treePane.setMinimumSize(minimumSize);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treePane, docView.getDocPanel());
        editorPanel.add(splitPane, BorderLayout.CENTER);

        PimController pimController = PimController.getInstance();
        pimController.setUp(PimDb.getInstance(), this, tree, docView);

        toolBar = new MyToolBar();
        pane.add(toolBar, BorderLayout.NORTH);
        if (!PimConf.getInstance().getBoolean("toolbar.show", false)) {
            toolBar.setVisible(false);
        }
        pimLog.setToolBar(toolBar);

        MyMainMenuBar menubar = new MyMainMenuBar();
        this.setJMenuBar(menubar);

        //设置从配置文件中读取的配置信息
        docView.getEditPane().setForeground(conf.getColor("doc.color.foreground", Color.BLACK));
        docView.getEditPane().setBackground(conf.getColor("doc.color.background", Color.WHITE));
        Font font = conf.getFont("doc.font");
        if (font != null) docView.getEditPane().setFont(font);

        this.addWindowListener(this);

        this.pack();
        setExtendedState(MAXIMIZED_BOTH);
    }

    @Override
    public void windowActivated(WindowEvent e) {


    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        try {
            PimDb.getInstance().close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        conf.setColor("doc.color.foreground", docView.getEditPane().getForeground());
        conf.setColor("doc.color.background", docView.getEditPane().getBackground());
        conf.setFont("doc.font", docView.getEditPane().getFont());
        conf.setBoolean("statusbar.show", statusBar.isVisible());
        conf.setBoolean("toolbar.show", toolBar.isVisible());
//    conf.set("skin.name", SubstanceSkinComboSelector.getCurrentSkinName());
        conf.save();
        this.setVisible(false);
        this.dispose();
        System.exit(0);
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void windowIconified(WindowEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void windowOpened(WindowEvent e) {
        // TODO Auto-generated method stub

    }

    public static void InitGlobalFont(Font font) {
        FontUIResource fontRes = new FontUIResource(font);
        for (Enumeration<Object> keys = UIManager.getDefaults().keys(); keys.hasMoreElements(); ) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, fontRes);
            }
        }
    }

    public static void main(String[] args) {
        JFrame.setDefaultLookAndFeelDecorated(true);


        EventQueue.invokeLater(new Runnable() {
            public void run() {
                Font font = PimConf.getInstance().getFont("global.font", null);
                if (font != null) {
                    InitGlobalFont(font);
                }

                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

                List<String> lastOpenedDbs = conf.getList("pim.db.lasts");
                String dbname = OpenPimDialog.show(null, lastOpenedDbs);

                if (dbname == null) {
                    System.exit(0);
                }
                File db = new File(dbname);
                db.getParentFile().mkdirs();
                PimDb pimDb = PimDb.getInstance();
                try {
                    pimDb.open(db.getAbsolutePath());
                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(0);
                }
                //保存上次打开的文件
                conf.addList("pim.db.lasts", db.getAbsolutePath());
                Start start = new Start();
                start.setVisible(true);
                start.setTitle("PIM4XIATIAN - " + db.getAbsolutePath());

                //预处理

            }

        });
    }

}
