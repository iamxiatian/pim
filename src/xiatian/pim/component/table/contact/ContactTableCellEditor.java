package xiatian.pim.component.table.contact;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;
import javax.swing.text.JTextComponent;

public class ContactTableCellEditor extends AbstractCellEditor implements TableCellEditor {
    private static final long serialVersionUID = -4129564833241778539L;
    JTextComponent component = null;

    // This method is called when a cell value is edited by the user.
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int rowIndex,
                                                 int vColIndex) {
        // 'value' is value contained in the cell located at (rowIndex, vColIndex)
        if (ContactTableModel.COLUMN_NAMES[vColIndex].equalsIgnoreCase("Memo")) {
            component = new JTextArea();
        } else {
            component = new JTextField();
        }
        if (isSelected) {
            // cell (and perhaps other cells) are selected
        }

        // Configure the component with the specified value
        component.setText((String) value);

        // Return the configured component
        return component;
    }

    // This method is called when editing is completed.
    // It must return the new value to be stored in the cell.
    public Object getCellEditorValue() {
        return ((JTextComponent) component).getText();
    }
}
