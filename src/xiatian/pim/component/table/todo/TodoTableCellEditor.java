package xiatian.pim.component.table.todo;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;

public class TodoTableCellEditor extends DefaultCellEditor {
    private static final long serialVersionUID = -412956441778539L;

    public TodoTableCellEditor(JComboBox comboBox) {
        super(comboBox);
    }

}
