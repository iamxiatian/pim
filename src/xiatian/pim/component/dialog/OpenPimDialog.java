package xiatian.pim.component.dialog;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;

import xiatian.pim.util.ImageUtils;

/**
 * Select & Open PIM database
 *
 * @author <a href="mailto:iamxiatian@gmail.com">xiatian</a>
 */
public class OpenPimDialog extends JDialog {
    private static final long serialVersionUID = 9096977100185L;
    private String value = null;

    public OpenPimDialog(Component parent, List<String> lastOpenedDbs) {
        this.setTitle("打开个人信息管理数据库");
        ImageIcon image = ImageUtils.createImageIcon("/images/medium/pim.png", "pim");
        this.setIconImage(image.getImage());
        this.setModal(true);
        String title = "选择要打开的数据库文件";

        Vector<ComboBoxItem> items = new Vector<ComboBoxItem>();
        for (int i = 0; i < lastOpenedDbs.size(); i++) {
            items.add(new ComboBoxItem(lastOpenedDbs.get(i), i));
        }
        final JComboBox comboBox = new JComboBox(items);
        comboBox.setMinimumSize(new Dimension(200, 25));
        comboBox.setMaximumSize(new Dimension(200, 25));
        comboBox.setPreferredSize(new Dimension(200, 25));

        ListCellRenderer comboBoxRenderer = new DefaultListCellRenderer() {
            private static final long serialVersionUID = -7208337553092329837L;

            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                                                          boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof ComboBoxItem) {
                    ComboBoxItem item = (ComboBoxItem) value;
                    this.setText(item.getTitle());
                    if (item.getPosition() % 2 == 0) {
                        this.setBackground(Color.YELLOW);
                    } else {
                        this.setBackground(Color.RED);
                    }
                }
                return this;
            }
        };
        comboBox.setRenderer(comboBoxRenderer);

        Container pane = this.getContentPane();
        pane.setLayout(new GridLayout(3, 1));
        JPanel line = new JPanel();
        line.setLayout(new FlowLayout());
        line.add(new JLabel(title));
        pane.add(line);

        line = new JPanel();
        line.setLayout(new FlowLayout());
        line.add(new JLabel("   "));
        line.add(comboBox);
        JButton newFileButton = new JButton("浏览");
        line.add(newFileButton);
        line.add(new JLabel("   "));
        pane.add(line);
        newFileButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setDialogTitle("选择要打开的文件或直接输入文件名创建新文件");
                chooser.showSaveDialog(OpenPimDialog.this);
                File choosedFile = chooser.getSelectedFile();
                if (choosedFile != null) {
                    comboBox.addItem(new ComboBoxItem(choosedFile.getAbsolutePath(), 0));
                }
            }
        });

        line = new JPanel();
        line.setLayout(new FlowLayout());
        JButton cancelButton = new JButton(" 取消 ");
        line.add(cancelButton);
        JButton okButton = new JButton(" 确认 ");
        line.add(okButton);
        pane.add(line);

        this.pack();
        // this.setMinimumSize(new Dimension(300,120));
        // this.setMaximumSize(new Dimension(300,120));
        // this.setPreferredSize(new Dimension(300,120));
        this.setLocationRelativeTo(parent);

        getRootPane().setDefaultButton(okButton);
        ActionListener okListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ComboBoxItem item = (ComboBoxItem) comboBox.getSelectedItem();
                if (item != null) {
                    value = item.getFilename();
                } else {
                    value = null;
                }
                OpenPimDialog.this.setVisible(false);
            }
        };
        okButton.addActionListener(okListener);
        okButton.registerKeyboardAction(okListener, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);

        ActionListener cancelListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                value = null;
                OpenPimDialog.this.setVisible(false);
            }
        };
        cancelButton.addActionListener(cancelListener);
        cancelButton.registerKeyboardAction(cancelListener, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    public String getSelectedValue() {
        return value;
    }

    public static String show(Component parent, List<String> lastOpenedDbs) {
        OpenPimDialog box = new OpenPimDialog(parent, lastOpenedDbs);
        box.setVisible(true);
        return box.getSelectedValue();
    }

    class ComboBoxItem {
        private String filename;
        private String title;
        private int position;

        public ComboBoxItem(String filename, int index) {
            this.filename = filename;
            this.title = new File(filename).getName();
            this.position = index;
        }

        public String getFilename() {
            return filename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

    }
}
