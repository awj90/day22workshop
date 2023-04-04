package sg.edu.nus.iss.day22workshop.model;

import java.util.Date;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class RSVP {

    private Integer id;
    private String name;
    private String email;
    private String phone;
    private Date confirmationDate;
    private String comments;

    public RSVP() {
    }

    public RSVP(Integer id, String name, String email, String phone, Date confirmationDate, String comments) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.confirmationDate = confirmationDate;
        this.comments = comments;
    }
    
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public Date getConfirmationDate() {
        return confirmationDate;
    }
    public void setConfirmationDate(Date confirmationDate) {
        this.confirmationDate = confirmationDate;
    }
    public String getComments() {
        return comments;
    }
    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "RSVP [id=" + id + ", name=" + name + ", email=" + email + ", phone=" + phone + ", confirmationDate="
                + confirmationDate + ", comments=" + comments + "]";
    }

    public static RSVP create(SqlRowSet rs) {
        RSVP rsvp = new RSVP();
        rsvp.setId(rs.getInt("id"));
        rsvp.setName(rs.getString("name"));
        rsvp.setEmail(rs.getString("email"));
        rsvp.setPhone(rs.getString("phone"));
        rsvp.setConfirmationDate(rs.getDate("confirmation_date")); 
        rsvp.setComments(rs.getString("comments"));
        return rsvp;
    }

    public JsonObject toJsonObject() {
        return Json.createObjectBuilder().add("id", this.getId())
                                        .add("name", this.getName())
                                        .add("email", this.getEmail())
                                        .add("phone", this.getPhone())
                                        .add("confirmation_date", this.getConfirmationDate().toString())
                                        .add("comments", this.getComments())
                                        .build();
    }
}
