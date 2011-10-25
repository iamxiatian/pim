package xiatian.pim.component.table.todo;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.TextAttribute;
import java.util.Date;
import java.util.Map;

import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import xiatian.pim.component.table.ComboBoxCellRenderer;
import xiatian.pim.component.table.PimTable;
import xiatian.pim.component.table.TextDialog;
import xiatian.pim.domain.Todo;

/**
 * http://java.sun.com/docs/books/tutorial/uiswing/components/table.html
 * editrender
 * 
 * @author <a href="mailto:iamxiatian@gmail.com">xiatian</a>
 * 
 */
public class TodoTable extends PimTable {
  private static final long serialVersionUID = -8547164778704781841L;

  static final DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();

  private MemoRenderer memoRender = new MemoRenderer();
  private int COL_STATE = 6;
  private int COL_END_TIME = 4;
  
  public TodoTable(TableModel model) {
    super(model);
  
    //状态修改变化处理
    TableColumn stateColumn = this.getColumnModel().getColumn(COL_STATE);
    String[] descriptions = new String[]{"Waiting", "Inprogress", "Finished"};
    Integer[] items = new Integer[]{0, 1, 2};
    stateColumn.setCellRenderer(new ComboBoxCellRenderer(items, descriptions));
    stateColumn.setCellEditor(new DefaultCellEditor(new ComboBoxCellRenderer(items, descriptions)));

    this.getColumnModel().getColumn(0).setPreferredWidth(50);
    this.getColumnModel().getColumn(0).setMaxWidth(50);
    
    TableColumn nameColumn = this.getColumnModel().getColumn(1);
    nameColumn.setPreferredWidth(250);
        
    TableColumn memoColumn = this.getColumnModel().getColumn(this.getColumnCount() - 1);
    memoColumn.setCellRenderer(memoRender);
    memoColumn.setCellEditor(new TextEditor());    
  }

  public TableCellRenderer getCellRenderer(int row, int column) {
    TableCellRenderer renderer = super.getCellRenderer(row, column);
    int state = (Integer)this.getValueAt(row, COL_STATE);
    Date d = (Date)this.getValueAt(row, COL_END_TIME);
    long rest = d.getTime() - System.currentTimeMillis();
    long days = rest/86400000+1;
    return new StatedCellRenderer(renderer, state, days);
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
  
  class StatedCellRenderer implements TableCellRenderer {
    TableCellRenderer parent = null;
    int state;
    long days;
    public StatedCellRenderer(TableCellRenderer parent, int state, long days){
      this.parent = parent;
      this.state = state;       
      this.days = days;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
        int row, int column) {
      // TODO Auto-generated method stub
      Component c = parent.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
      if(state == Todo.STATE_FINISHED){
        //c.setBackground(Color.gray);
        Map map = table.getFont().getAttributes();
        map.put(TextAttribute.FOREGROUND, Color.GRAY);
        //map.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
        map.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
        c.setFont(new Font(map));
      }else{
        Map map = table.getFont().getAttributes();
        if(days>15){
          map.put(TextAttribute.FOREGROUND, Color.BLACK);
        }else if(days>7){
          map.put(TextAttribute.FOREGROUND, Color.BLUE);
        }else if(days>3){
          map.put(TextAttribute.FOREGROUND, Color.MAGENTA);
        }else{
          map.put(TextAttribute.FOREGROUND, Color.RED);
        }
        //map.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
        map.put(TextAttribute.STRIKETHROUGH, null);
        c.setFont(new Font(map));
      }
      
      return c;
    }
    
    class MyComponent extends JComponent {
      private static final long serialVersionUID = 134123233245961L;
      private Component parent = null;
      
      public MyComponent(Component parent){
        this.parent = parent;
      }
      
      @Override
      public void paintComponent(Graphics g )
      {
        parent.paint(g);          
        g.setColor( getForeground() );
        int midpoint = getHeight() / 2;
        g.drawLine( 0, midpoint, getWidth()-1, midpoint );      
      }

    }
    
  }

  class EvenOddRenderer implements TableCellRenderer {

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
        int row, int column) {
      Component renderer = DEFAULT_RENDERER.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
      Color foreground, background;
      if (isSelected) {
        foreground = Color.YELLOW;
        background = Color.GREEN;
      } else {
        if (row % 2 == 0) {
          foreground = Color.BLUE;
          background = Color.WHITE;
        } else {
          foreground = Color.WHITE;
          background = Color.BLUE;
        }
      }
      renderer.setForeground(foreground);
      renderer.setBackground(background);
      return renderer;
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
      dialog = new TextDialog(TodoTable.this, this);
    }

    public void actionPerformed(ActionEvent e) {
      if (EDIT.equals(e.getActionCommand())) {
        // The user has clicked the cell, so
        // bring up the dialog.
        // button.setBackground(currentColor);
        // colorChooser.setColor(currentColor);
        dialog.setText(currentValue);
        dialog.setVisible(true);

        fireEditingStopped(); // Make the renderer reappear.

      } else { // User pressed dialog's "OK" button.
        currentValue = dialog.getText();
        dialog.setVisible(false);
        fireEditingStopped();
        TodoTable.this.setValueAt(currentValue, row, col);
        TodoTable.this.updateUI();
        // System.out.println("V:" + currentValue);
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
