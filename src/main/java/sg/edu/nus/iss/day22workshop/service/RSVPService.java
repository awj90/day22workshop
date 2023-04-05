package sg.edu.nus.iss.day22workshop.service;

import java.io.StringReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import sg.edu.nus.iss.day22workshop.model.RSVP;
import sg.edu.nus.iss.day22workshop.repository.RSVPRepository;

@Service
public class RSVPService {
    
    @Autowired
    RSVPRepository rsvpRepository;
    
    public List<RSVP> getAllRSVPs() {
        return rsvpRepository.getAllRSVPs();
    }

    public List<RSVP> getRSVPsByName(String name) {
        return rsvpRepository.getRSVPsByName(name);
    }

    public RSVP createRSVP(RSVP rsvp) {
        return rsvpRepository.createRSVP(rsvp);
    }

    public RSVP jsonStringToJsonObject(String json) throws ParseException {
        RSVP rsvp = new RSVP();
        StringReader sr = new StringReader(json);
        JsonReader jsonReader = Json.createReader(sr);
        JsonObject jsonObject = jsonReader.readObject();
        // rsvp.setId(jsonObject.getInt("id")); // not needed
        rsvp.setName(jsonObject.getString("name"));
        rsvp.setEmail(jsonObject.getString("email"));
        rsvp.setPhone(jsonObject.getString("phone"));
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        rsvp.setConfirmationDate(formatter.parse(jsonObject.getString("confirmation_date")));
        rsvp.setComments(jsonObject.getString("comments"));
        return rsvp;
    }

    public boolean updateRSVP(String email, String json) throws ParseException {
        RSVP rsvp = jsonStringToJsonObject(json);
        return rsvpRepository.updateRSVP(email, rsvp);
    }

    public Long getTotalRSVPCount() {
        return rsvpRepository.getTotalRSVPCount();
    }
}
