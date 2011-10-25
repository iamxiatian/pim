package xiatian.pim.component.doc;

import javax.swing.JTextArea;

public class ViewPane extends JTextArea {
  private static final long serialVersionUID = 7675924741273914256L;
  private String srcText;
  public ViewPane(){
  	super();
  	this.setEditable(false);
  	//让长文本自动换行
//  	this.setEditorKit(new StyledEditorKit());
//  	this.setContentType("text/html");		
  }
  
  @Override
  public void setText(String text){
  	this.srcText = text;
  	int pos = text.toLowerCase().indexOf("<body>");
  	if(pos<0){
  		super.setText(text.replaceAll("\\n", "<br/>"));
  	}else{
  		super.setText(text);
  	}
  }
  
  @Override
  public String getText(){
  	return this.srcText;
  }
}
