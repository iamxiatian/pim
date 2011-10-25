package xiatian.pim.component.table;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

public class PimTable extends JTable {
  private static final long serialVersionUID = -8294318945600877183L;

  public PimTable(TableModel model){
    super(model);
    
    this.setRowHeight(28);    
  }
  
  /**
   * 根据给定的列号调整表格的列宽，此算法取自Swing hacker，不多说啦
   * @param table
   * @param col
   */
  public static void adjustColumnPreferredWidths(JTable table, int col) {
      // strategy - get max width for cells in column and
      // make that the preferred width
      TableColumnModel columnModel = table.getColumnModel();
      int maxwidth = 0;
      for (int row = 0; row < table.getRowCount(); row++) {
          TableCellRenderer rend =
                  table.getCellRenderer(row, col);
          Object value = table.getValueAt(row, col);
          Component comp =
                  rend.getTableCellRendererComponent(table,
                  value,
                  false,
                  false,
                  row,
                  col);
          maxwidth = Math.max(comp.getPreferredSize().width, maxwidth);
      } // for row
      TableColumn column = columnModel.getColumn(col);
      column.setPreferredWidth(maxwidth + 3);
  }


}
