package sg.edu.nus.iss.day22workshop.repository;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.day22workshop.model.RSVP;

import static sg.edu.nus.iss.day22workshop.repository.DBQueries.*;

@Repository
public class RSVPRepository {
    
    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<RSVP> getAllRSVPs() {
        List<RSVP> RSVPs = new ArrayList<>();
        SqlRowSet rs = jdbcTemplate.queryForRowSet(SELECT_ALL_RSVPS);
        while (rs.next()) {
            RSVPs.add(RSVP.create(rs));
        }
        return RSVPs;
    }

    public List<RSVP> getRSVPsByName(String name) {
        List<RSVP> RSVPs = new ArrayList<>();
        // for MySQL instructions wrapped between single quotes, pass in through as an array of Objects
        // String query = "'%" + name + "%'"; // this does not work
        // SqlRowSet rs = jdbcTemplate.queryForRowSet(SELECT_RSVP_BY_NAME, query); // this does not work
        SqlRowSet rs = jdbcTemplate.queryForRowSet(SELECT_RSVP_BY_NAME, new Object[] {"%" + name + "%"});
        while (rs.next()) {
            RSVPs.add(RSVP.create(rs));
        }
        return RSVPs;
    }

    // private method called only by createRSVP method
    private RSVP getRSVPByEmail(String email) {
        List<RSVP> RSVPs = new ArrayList<>();
        SqlRowSet rs = jdbcTemplate.queryForRowSet(SELECT_RSVP_BY_EMAIL, email);
        while (rs.next()) {
            RSVPs.add(RSVP.create(rs));
        }
        return RSVPs.isEmpty() ? null : RSVPs.get(0);
    } 

    public RSVP createRSVP(RSVP rsvp) { 
        // Keyholders are for getting the auto-generated id key after inserting entities 
        KeyHolder keyHolder = new GeneratedKeyHolder();       
        
        RSVP existingRSVP = getRSVPByEmail(rsvp.getEmail());

        if (Objects.isNull(existingRSVP)) {
            // The method isNull is a static method of the Objects class in java that checks whether the input object reference supplied to it is null or not
            // if the passed object is null, then the method returns true
            // i.e. RSVP does not exist yet, create the RSVP
            jdbcTemplate.update(connection -> {
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_RSVP, Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, rsvp.getName());
                preparedStatement.setString(2, rsvp.getEmail());
                preparedStatement.setString(3, rsvp.getPhone());
                preparedStatement.setDate(4, new java.sql.Date(rsvp.getConfirmationDate().getTime()));
                preparedStatement.setString(5, rsvp.getComments());
                return preparedStatement;
            }, keyHolder);
            
            // BigInteger represents immutable arbitrary-precision integers. It is similar to the primitive integer types but allows arbitrary large values. It is used when integers involved are larger than the limit of long type
            BigInteger primaryKey = (BigInteger) keyHolder.getKey();
            if (primaryKey!= null) {
                rsvp.setId(primaryKey.intValue());
            }
        } else {
            // RSVP exists, update the RSVP
            existingRSVP.setName(rsvp.getName());
            existingRSVP.setPhone(rsvp.getPhone());
            existingRSVP.setConfirmationDate(rsvp.getConfirmationDate());
            existingRSVP.setComments(rsvp.getComments());

            boolean isUpdated = upsertRSVP(existingRSVP);

            if(isUpdated){
                rsvp.setId(existingRSVP.getId());
            }
        }
        return rsvp;
    }

    private boolean upsertRSVP(RSVP existingRSVP) {
        return jdbcTemplate.update(UPDATE_RSVP_BY_EMAIL, 
                existingRSVP.getName(),    
                existingRSVP.getPhone(), 
                new java.sql.Date(existingRSVP.getConfirmationDate().getTime()), 
                existingRSVP.getComments(),
                existingRSVP.getEmail()) > 0;
    }

    public boolean updateRSVP(String email, RSVP updatedRSVP) {
        return jdbcTemplate.update(UPDATE_RSVP_BY_EMAIL, 
                updatedRSVP.getName(),    
                updatedRSVP.getPhone(), 
                new java.sql.Date(updatedRSVP.getConfirmationDate().getTime()), 
                updatedRSVP.getComments(),
                email) > 0;
    }

    public Long getTotalRSVPCount() {
        List<Map<String,Object>> rows = jdbcTemplate.queryForList(SELECT_TOTAL_RSVP_COUNT); // returns a list that contains a map of <"total_count", count>
        return (long) rows.get(0).get("total_count");
    }
}
