package xiatian.pim.component.table.todo;

import xiatian.pim.component.table.MyRowFilter;
import xiatian.pim.domain.Todo;

public class TodoRowFilter extends MyRowFilter {
  private boolean showFinished = false;
  public TodoRowFilter(String regex, boolean showFinished) {
    super(regex, 1);    
    this.showFinished = showFinished;
  }  
  
  @Override
  protected boolean include(Entry<? extends Object, ? extends Object> value, int index) {
    if(!showFinished){
      int state = (Integer)value.getValue(6);
      if(state==Todo.STATE_FINISHED){
        return false;
      }
    }
    
    return super.include(value, index);
  }
}