package xiatian.pim.component.table.contact;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import xiatian.pim.component.table.PimTable;
import xiatian.pim.component.table.TextDialog;

/**
 * http://java.sun.com/docs/books/tutorial/uiswing/components/table.html
 * editrender
 *
 * @author <a href="mailto:iamxiatian@gmail.com">xiatian</a>
 */
public class ContactTable extends PimTable {
    private static final long serialVersionUID = -8547164778704781841L;
    private MemoRenderer memoRender = new MemoRenderer();

    public ContactTable(TableModel model) {
        super(model);
        TableColumn memoColumn = this.getColumnModel().getColumn(this.getColumnCount() - 1);
        memoColumn.setCellEditor(new TextEditor());

    }

    public TableCellRenderer getCellRenderer(int row, int column) {
        if (column == (this.getColumnCount() - 1)) {
            return memoRender;
        } else {
            return super.getCellRenderer(row, column);
        }
        // else...
        // return super.getCellRenderer(row, column);
    }

    static class MemoRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 3000694167604172597L;

        public MemoRenderer() {
            super();
        }

        public void setValue(Object value) {
            if (value == null) {
                setText("");
            } else {
                String s = value.toString();
                if (s.length() < 10) {
                    setText(s);
                } else {
                    setText(s.substring(0, 10) + "...");
                }
            }
        }
    }

    public class TextEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
        private static final long serialVersionUID = 5530679258884301756L;
        String currentValue;
        JButton button;
        JColorChooser colorChooser;
        TextDialog dialog;
        protected static final String EDIT = "edit";
        int row, col;

        public TextEditor() {
            button = new JButton();
            button.setActionCommand(EDIT);
            button.addActionListener(this);
            button.setBorderPainted(false);

            // Set up the dialog that the button brings up.
            dialog = new TextDialog(ContactTable.this, this);
        }

        public void actionPerformed(ActionEvent e) {
            if (EDIT.equals(e.getActionCommand())) {
                // The user has clicked the cell, so
                // bring up the dialog.
//        button.setBackground(currentColor);
//        colorChooser.setColor(currentColor);
                dialog.setText(currentValue);
                dialog.setVisible(true);
                dialog.setModal(true);

                fireEditingStopped(); // Make the renderer reappear.

            } else { // User pressed dialog's "OK" button.
                currentValue = dialog.getText();
                dialog.setVisible(false);
                fireEditingStopped();
                ContactTable.this.setValueAt(currentValue, row, col);
                ContactTable.this.updateUI();
                //System.out.println("V:" + currentValue);
            }
        }

        // Implement the one CellEditor method that AbstractCellEditor doesn't.
        public Object getCellEditorValue() {
            return currentValue;
        }

        // Implement the one method defined by TableCellEditor.
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            currentValue = value.toString();
            this.row = row;
            this.col = column;
            return button;
        }
    }

}
