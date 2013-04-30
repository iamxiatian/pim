package xiatian.pim.component.table.contact;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.TableRowSorter;

import xiatian.pim.component.border.DoubleLineBorder;
import xiatian.pim.component.table.MyRowFilter;
import xiatian.pim.domain.Contact;
import xiatian.pim.io.PimDb;
import xiatian.pim.listener.PimController;
import xiatian.pim.listener.PimLog;

public class ContactMainPanel extends JPanel {
    private static final long serialVersionUID = 7652727575104982706L;

    public ContactMainPanel() {
        this.setLayout(new BorderLayout());
        this.setBorder(new DoubleLineBorder(Color.GRAY));
        ContactTableModel model = new ContactTableModel();
        JTable table = new ContactTable(model);
//    table = autoResizeColWidth(table, model);
        final TableRowSorter<ContactTableModel> sorter = new TableRowSorter<ContactTableModel>(model);
        //sorter.setRowFilter(RowFilter.regexFilter(null));
        table.setRowSorter(sorter);
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new CommandPanel(table, model, sorter), new JScrollPane(table));
        this.add(splitPane, BorderLayout.CENTER);
    }

    class CommandPanel extends JPanel {
        private static final long serialVersionUID = -6005728225508824421L;

        public CommandPanel(final JTable table, final ContactTableModel model, final TableRowSorter<ContactTableModel> sorter) {
            this.add(new JLabel("Filter:"));
            final JTextField filterTextField = new JTextField();
            filterTextField.setColumns(20);
            this.add(filterTextField);
            filterTextField.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                    sorter.setRowFilter(new MyRowFilter(filterTextField.getText().toLowerCase(), 0));
                }

                public void removeUpdate(DocumentEvent e) {
                    sorter.setRowFilter(new MyRowFilter(filterTextField.getText().toLowerCase(), 0));
                }

                public void insertUpdate(DocumentEvent e) {
                    sorter.setRowFilter(new MyRowFilter(filterTextField.getText().toLowerCase(), 0));
                }
            });
            this.add(new JLabel("        "));

            JButton addButton = new JButton("Add");
            this.add(addButton);
            addButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    Contact c = new Contact("New Contact", "", "", "", "", "", "");
                    int id = PimDb.getInstance().addContact(c);
                    c.setId(id);
                    model.add(c);
                    table.updateUI();
                    filterTextField.setText("New Contact");
                    //sorter.setRowFilter(RowFilter.regexFilter(filterTextField.getText().trim()));
                    sorter.setRowFilter(new MyRowFilter(filterTextField.getText().toLowerCase(), 0));
                }
            });

            JButton importButton = new JButton("Import");
            this.add(importButton);
            importButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    JFileChooser chooser = new JFileChooser();
                    chooser.setFileFilter(new FileFilter() {

                        @Override
                        public String getDescription() {
                            return null;
                        }

                        @Override
                        public boolean accept(File f) {
                            if (f.isDirectory() || f.getAbsolutePath().toLowerCase().endsWith(".xml")) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                    chooser.showOpenDialog(PimController.getInstance().getJFrame());
                    File choosedFile = chooser.getSelectedFile();
                    try {
                        int count = PimDb.getInstance().importContacts(choosedFile);
                        PimLog.getInstance().showStatusMessage(count + " contacts imported.");
                        //重新刷新
                        model.reload();
                        table.updateUI();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        PimLog.getInstance().showStatusMessage(e1.getMessage());
                    }
                }
            });

            JButton exportButton = new JButton("Export");
            this.add(exportButton);
            exportButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    JFileChooser chooser = new JFileChooser();
                    chooser.setFileFilter(new FileFilter() {

                        @Override
                        public String getDescription() {
                            return null;
                        }

                        @Override
                        public boolean accept(File f) {
                            if (f.isDirectory() || f.getAbsolutePath().toLowerCase().endsWith(".xml")) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                    chooser.showSaveDialog(PimController.getInstance().getJFrame());
                    File choosedFile = chooser.getSelectedFile();
                    try {
                        PimDb.getInstance().exportContacts(choosedFile);
                        PimLog.getInstance().showStatusMessage("export contacts completed.");
                    } catch (Exception e1) {
                        PimLog.getInstance().showStatusMessage(e1.getMessage());
                    }
                }
            });
        }

    }
}
