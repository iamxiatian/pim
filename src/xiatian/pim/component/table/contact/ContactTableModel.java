package xiatian.pim.component.table.contact;

import java.io.Serializable;
import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import xiatian.pim.domain.Contact;
import xiatian.pim.io.PimDb;

public class ContactTableModel implements TableModel, Serializable {

  private static final long serialVersionUID = -85914360996159919L;
  private List<Contact> contacts = null;

  public static final String[] COLUMN_NAMES = new String[] { "Name", "Cellphone", "Telephone", "Email", "Category",
      "Organization", "Memo" };

  public ContactTableModel() {
    // super();
    this.contacts = PimDb.getInstance().getContactList();
  }
  
  public void reload(){
    this.contacts = PimDb.getInstance().getContactList();
  }

  public void add(Contact c) {
    contacts.add(c);
  }

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    return String.class;
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
    return contacts.size();
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    Contact c = contacts.get(rowIndex);
    switch (columnIndex) {
    case 0:
      return c.getName();
    case 1:
      return c.getCellphone();
    case 2:
      return c.getTelephone();
    case 3:
      return c.getEmail();
    case 4:
      return c.getCategory();
    case 5:
      return c.getOrganization();
    case 6:
      return c.getMemo();
    }
    return null;
  }

  @Override
  public boolean isCellEditable(int rowIndex, int columnIndex) {
    return true;
  }

  @Override
  public void setValueAt(Object value, int rowIndex, int columnIndex) {
    Contact c = contacts.get(rowIndex);
    boolean changed = false;
    switch (columnIndex) {
    case 0:
      if (!c.getName().equals(value.toString())) {
        c.setName(value.toString());
        changed = true;
      }
      break;
    case 1:
      if (!c.getCellphone().equals(value.toString())) {
        c.setCellphone(value.toString());
        changed = true;
      }
      break;
    case 2:
      if (!c.getTelephone().equals(value.toString())) {
        c.setTelephone(value.toString());
        changed = true;
      }
      break;
    case 3:
      if (!c.getEmail().equals(value.toString())) {
        c.setEmail(value.toString());
        changed = true;
      }
      break;
    case 4:
      if (!c.getCategory().equals(value.toString())) {
        c.setCategory(value.toString());
        changed = true;
      }
      break;
    case 5:
      if (!c.getOrganization().equals(value.toString())) {
        c.setOrganization(value.toString());
        changed = true;
      }
      break;
    case 6:
      if (!c.getMemo().equals(value.toString())) {
        c.setMemo(value.toString());
        changed = true;
      }
      break;
    }
    if (changed) {
      System.out.println("modify:" + c);
      PimDb.getInstance().updateContact(c);
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
