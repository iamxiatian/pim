package xiatian.pim.domain;

import java.io.Serializable;
import java.util.Date;

import xiatian.pim.io.PimDb;

public class Todo implements Serializable {
  private static final long serialVersionUID = -4708505696437989010L;
  
  public static final int STATE_WAITING = 0;
  public static final int STATE_INPROGRESS = 1;
  public static final int STATE_FINISHED = 2;
  
  private int id;
  private String name;
  private Date addTime = new Date();
  private Date startTime = new Date();
  private Date endTime = new Date();
  private Date finishTime = new Date();
  private int state;
  private String memo;
  private String category;
  
  public Todo(){}
    
  public Todo(String name, String memo){
    this.name = name;    
    this.memo = memo;
  }  
    
  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Date getAddTime() {
    return addTime;
  }

  public String getAddTimeString(){
    return PimDb.getDateString(addTime);
  }
  
  public void setAddTime(Date addTime) {
    this.addTime = addTime;
  }

  public Date getStartTime() {
    return startTime;
  }

  public String getStartTimeString(){
    return PimDb.getDateString(startTime);
  }
  
  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }

  public Date getEndTime() {
    return endTime;
  }
  
  public String getEndTimeString(){
    return PimDb.getDateString(endTime);
  }

  public void setEndTime(Date endTime) {
    this.endTime = endTime;
  }

  public Date getFinishTime() {
    return finishTime;
  }
  
  public String getFinishTimeString(){
    return PimDb.getDateString(finishTime);
  }

  public void setFinishTime(Date finishTime) {
    this.finishTime = finishTime;
  }

  public int getState() {
    return state;
  }

  public void setState(int state) {
    this.state = state;
  }

  public String getMemo() {
    return memo;
  }

  public void setMemo(String memo) {
    this.memo = memo;
  }
  
  public String toString(){
    StringBuilder sb = new StringBuilder();
    sb.append("id=" + id);
    sb.append(", name=" + name);    
    return sb.toString();
  }
}
