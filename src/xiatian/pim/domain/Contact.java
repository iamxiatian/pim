package xiatian.pim.domain;

import java.io.Serializable;
import java.util.Date;

public class Contact implements Serializable {
    private static final long serialVersionUID = -4708505696437989010L;

    private int id;
    private String name;
    private String telephone;
    private String cellphone;
    private String email;
    private String category;
    private String organization;
    private Date addTime = new Date();
    private String memo;

    private int state;
    private int position;

    public Contact() {
    }

    public Contact(String name, String telephone, String mobile, String email, String category, String organization, String memo) {
        this.name = name;
        this.telephone = telephone;
        this.cellphone = mobile;
        this.email = email;
        this.category = category;
        this.organization = organization;
        this.memo = memo;
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

    public String getTelephone() {
        return telephone == null ? "" : telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getOrganization() {
        return organization == null ? "" : organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public String getMemo() {
        return memo == null ? "" : memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("id=" + id);
        sb.append(", name=" + name);
        sb.append(", cellphone=" + cellphone);
        sb.append(", telephone=" + telephone);
        sb.append(", email=" + email);
        sb.append(", category=" + category);
        return sb.toString();
    }
}
