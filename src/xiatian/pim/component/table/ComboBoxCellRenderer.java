package xiatian.pim.component.table;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class ComboBoxCellRenderer extends JComboBox implements TableCellRenderer {
    private static final long serialVersionUID = 5637081034558752631L;

    public ComboBoxCellRenderer(Object[] items, String[] descriptions) {
        super(items);
        this.setRenderer(new ComboBoxRenderer(items, descriptions));
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        if (isSelected) {
            setForeground(table.getSelectionForeground());
            super.setBackground(table.getSelectionBackground());
        } else {
            setForeground(table.getForeground());
            setBackground(table.getBackground());
        }

        setSelectedItem(value);
        return this;
    }

    class ComboBoxRenderer extends DefaultListCellRenderer {
        private static final long serialVersionUID = -247404896988845365L;

        private Object[] items;
        private String[] descriptions;

        public ComboBoxRenderer(Object[] items, String[] descriptions) {
            setOpaque(true);
            //setHorizontalAlignment(CENTER);
            setVerticalAlignment(CENTER);
            this.items = items;
            this.descriptions = descriptions;
        }

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                                                      boolean cellHasFocus) {
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }

            for (int i = 0; i < items.length; i++) {
                if (items[i].equals(value)) {
                    setText(descriptions[i]);
                }
            }

            return this;
        }
    }

}
