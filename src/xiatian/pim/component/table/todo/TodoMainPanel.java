package xiatian.pim.component.table.todo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JCheckBox;
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

import net.sf.nachocalendar.components.DateField;
import net.sf.nachocalendar.table.DateFieldTableEditor;
import net.sf.nachocalendar.table.DateRendererDecorator;
import xiatian.pim.component.border.DoubleLineBorder;
import xiatian.pim.domain.Todo;
import xiatian.pim.io.PimDb;
import xiatian.pim.listener.PimController;
import xiatian.pim.listener.PimLog;

public class TodoMainPanel extends JPanel {
  private static final long serialVersionUID = 765272757506L;
  
  public TodoMainPanel(){
    this.setLayout(new BorderLayout());
    this.setBorder(new DoubleLineBorder(Color.GRAY));
    TodoTableModel model = new TodoTableModel();
    JTable table = new TodoTable(model);
//    table = autoResizeColWidth(table, model);
    final TableRowSorter<TodoTableModel> sorter = new TableRowSorter<TodoTableModel>(model);
    sorter.setRowFilter(new TodoRowFilter("", false));
    table.setRowSorter(sorter);
    
    //设置Cell
    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    table.setDefaultEditor(Date.class, new DateFieldTableEditor(new DateField(format)));
    table.setDefaultRenderer(Date.class, new DateRendererDecorator(table.getDefaultRenderer(String.class), format));    
    JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,new CommandPanel(table, model, sorter), new JScrollPane(table) );    
    this.add(splitPane, BorderLayout.CENTER);
  }
  
  class CommandPanel extends JPanel {
    private static final long serialVersionUID = -6005728225508824421L;
    
    public CommandPanel(final JTable table, final TodoTableModel model, final TableRowSorter<TodoTableModel> sorter){     
      final JCheckBox showFinishCheckBox = new JCheckBox("Show Finished Items");
      this.add(showFinishCheckBox);      
      
      this.add(new JLabel("Filter:"));
      final JTextField filterTextField = new JTextField();
      filterTextField.setColumns(20);
      this.add(filterTextField);      
      this.add(new JLabel("      "));
      
      JButton addButton = new JButton("Add");
      this.add(addButton);
      
      showFinishCheckBox.addActionListener(new ActionListener() {
        
        @Override
        public void actionPerformed(ActionEvent e) {          
          sorter.setRowFilter(new TodoRowFilter(filterTextField.getText().toLowerCase(), showFinishCheckBox.isSelected()));          
        }
      });

      filterTextField.getDocument().addDocumentListener(new DocumentListener() {
        public void changedUpdate(DocumentEvent e) {
          sorter.setRowFilter(new TodoRowFilter(filterTextField.getText().toLowerCase(), showFinishCheckBox.isSelected()));
        }

        public void removeUpdate(DocumentEvent e) {
          sorter.setRowFilter(new TodoRowFilter(filterTextField.getText().toLowerCase(), showFinishCheckBox.isSelected()));
        }

        public void insertUpdate(DocumentEvent e) {
          sorter.setRowFilter(new TodoRowFilter(filterTextField.getText().toLowerCase(), showFinishCheckBox.isSelected()));
        }
      });   
      
      addButton.addActionListener(new ActionListener() {
        
        @Override
        public void actionPerformed(ActionEvent e) {
          Todo todo = new Todo("新事项", "");
          int id = PimDb.getInstance().addTodo(todo);
          todo.setId(id);
          model.add(todo);
          table.updateUI();
          filterTextField.setText("新事项");
          sorter.setRowFilter(new TodoRowFilter(filterTextField.getText().toLowerCase(), showFinishCheckBox.isSelected()));
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
              if(f.isDirectory() || f.getAbsolutePath().toLowerCase().endsWith(".xml")){
                return true;
              }else{
                return false;
              }
            }
          });
          chooser.showOpenDialog(PimController.getInstance().getJFrame());
          File choosedFile = chooser.getSelectedFile();
          try {
            int count = PimDb.getInstance().importTodolist(choosedFile);
            PimLog.getInstance().showStatusMessage(count + " todo items imported.");
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
            PimDb.getInstance().exportTodolist(choosedFile);
            PimLog.getInstance().showStatusMessage("export todo list completed.");
          } catch (Exception e1) {
            PimLog.getInstance().showStatusMessage(e1.getMessage());
          }
        }
      });    
      
  }
    
  }
}
