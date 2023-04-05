package sg.edu.nus.iss.day22workshop.repository;

public class DBQueries {
    // public static final String SELECT_ALL_RSVPS = "select id, name, email, phone, DATE_FORMAT(confirmation_date,\"%d/%m/%y\") as confirmation_date, comments from rsvps";
    // public static final String SELECT_RSVP_BY_NAME = "select id, name, email, phone, DATE_FORMAT(confirmation_date,\"%d/%m/%y\") as confirmation_date, comments from rsvps where name like ?"; 
    public static final String SELECT_ALL_RSVPS = "select id, name, email, phone, confirmation_date, comments from rsvps";
    public static final String SELECT_RSVP_BY_NAME = "select id, name, email, phone, confirmation_date, comments from rsvps where name like ?"; 
    public static final String SELECT_RSVP_BY_EMAIL = "select * from rsvps where email = ?";
    public static final String INSERT_RSVP = "insert into rsvps (name, email, phone, confirmation_date, comments) values (?, ?, ?, ?, ?)";
    public static final String UPDATE_RSVP_BY_EMAIL = "update rsvps set name = ?, phone = ?, confirmation_date = ?, comments = ? where email = ? ";
    public static final String SELECT_TOTAL_RSVP_COUNT = "select count(*) as total_count from rsvps";
}
