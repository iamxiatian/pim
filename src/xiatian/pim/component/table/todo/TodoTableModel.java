package xiatian.pim.component.table.todo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import xiatian.pim.domain.Todo;
import xiatian.pim.io.PimDb;

public class TodoTableModel implements TableModel, Serializable {

    private static final long serialVersionUID = -85910999919L;
    private List<Todo> todolist = null;

    public static final String[] COLUMN_NAMES = new String[]{"Id", "Name", "Category", "StartTime", "EndTime", "FinishTime", "State", "Memo"};

    Class<?>[] types = new Class[]{
            String.class, String.class, String.class, Date.class, Date.class, Date.class, Integer.class, String.class
    };

    public TodoTableModel() {
        // super();
        this.todolist = PimDb.getInstance().getTodoList();
    }

    public void reload() {
        this.todolist = PimDb.getInstance().getTodoList();
    }

    public void add(Todo c) {
        todolist.add(c);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return types[columnIndex];
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return COLUMN_NAMES[columnIndex];
    }

    @Override
    public int getRowCount() {
        return todolist.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Todo c = todolist.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return c.getId();
            case 1:
                return c.getName();
            case 2:
                return c.getCategory();
            case 3:
                return c.getStartTime();
            case 4:
                return c.getEndTime();
            case 5:
                return c.getFinishTime();
            case 6:
                return c.getState();
            case 7:
                return c.getMemo();
        }
        return null;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex != 0;
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        Todo todo = todolist.get(rowIndex);
        boolean changed = false;
        Date d = null;
        switch (columnIndex) {
            case 1:
                if (!todo.getName().equals(value.toString())) {
                    todo.setName(value.toString());
                    changed = true;
                }
                break;
            case 2:
                if (!value.toString().equals(todo.getCategory())) {
                    todo.setCategory(value.toString());
                    changed = true;
                }
                break;
            case 3:
                d = (Date) value;
                if (d.getTime() != todo.getStartTime().getTime()) {
                    todo.setStartTime(d);
                    changed = true;
                }
                break;
            case 4:
                d = (Date) value;
                if (d.getTime() != todo.getEndTime().getTime()) {
                    todo.setEndTime(d);
                    changed = true;
                }
                break;
            case 5:
                d = (Date) value;
                if (d.getTime() != todo.getFinishTime().getTime()) {
                    todo.setFinishTime(d);
                    changed = true;
                }
                break;
            case 6:
                if (todo.getState() != (Integer) value) {
                    todo.setState((Integer) value);
                    changed = true;
                }
                break;
            case 7:
                if (!value.toString().equals(todo.getMemo())) {
                    todo.setMemo(value.toString());
                    changed = true;
                }
                break;
        }


        if (changed) {
            System.out.println("modify:" + todo);
            PimDb.getInstance().updateTodo(todo);
        }
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
        // TODO Auto-generated method stub

    }

}
