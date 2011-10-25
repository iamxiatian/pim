package xiatian.pim.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * Journal代表一条日记，其中的state具有的含义为：
 * <ul>
 * 	<li>state=0: 编辑显示模式，用户可以输入修改内容</li>
 * 	<li>state=1: 阅读显示模式，已HTML方式阅读内容</li>
 * </ul>
 * @author xiatian
 *
 */
public class Journal extends PropertyHandler implements Serializable {
  private static final long serialVersionUID = 8101176120454406561L;
  
  public static final int STATE_EDIT = 0;
  public static final int STATE_READ = 1;
  
  public static final String Property_Style = "style";
  
  /** 内部Id */
  private int id;
  /** 父亲Id*/
  private int parentId;
  /** 在字节点的位置，排序时使用 */
  private int position;
  /** 标题 */
  private String title;
  private String tags = "";
  /** 内容 */
  private String content;
  /** 创建时间 */
  private Date createDate = new Date();
  
  private String password = "";
  
  private int state = 0;
  
  private boolean opened = false;
  
  private String properties;
  
  public Journal(){}
  
  public Journal(String title){
    this.title = title;
    this.content = "";
    this.id = 0;
  }  
  
  public Journal(int parentId, int position, String title, String content) {
    this.parentId = parentId;
    this.position = position;
    this.title = title;
    this.content = content;
  }
  
  public int getId() {
    return id;
  }
  public void setId(int id) {
    this.id = id;
  }
  public int getParentId() {
    return parentId;
  }
  public void setParentId(int parentId) {
    this.parentId = parentId;
  }
  public int getPosition() {
    return position;
  }
  public void setPosition(int position) {
    this.position = position;
  }
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }
 
  public String getContent() {
    return content==null?"":content;
  }
  public void setContent(String content) {
    this.content = content;
  }
  public Date getCreateDate() {
    return createDate;
  }
  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }
  public String getPassword() {
    return password;
  }
  public void setPassword(String password) {
    this.password = password;
  }
  public int getState() {
    return state;
  }
  public void setState(int state) {
    this.state = state;
  }
  
  public String getTags() {
    return tags;
  }

  public void setTags(String tags) {
    this.tags = tags;
  }

  public String toString(){
    StringBuilder sb = new StringBuilder();
    sb.append("id=" + id);
    sb.append(", parentId=" + parentId);
    sb.append(", title=" + title);
    sb.append(", tags=" + tags);
    sb.append(", content=" + content);
    return sb.toString();
  }

  public void setOpened(boolean opened) {
    this.opened = opened;
  }

  public boolean isOpened() {
    return opened;
  }
}
