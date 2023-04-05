package sg.edu.nus.iss.day22workshop.controller;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import sg.edu.nus.iss.day22workshop.model.RSVP;
import sg.edu.nus.iss.day22workshop.service.RSVPService;

@RestController
@RequestMapping(path="/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class RSVPRestController {
    
    @Autowired
    RSVPService rsvpService;

    @GetMapping(path="/rsvps")
    public ResponseEntity<String> getAllRSVPs() {
        List<RSVP> RSVPs = rsvpService.getAllRSVPs();
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        for (RSVP rsvp : RSVPs) {
            jsonArrayBuilder.add(rsvp.toJsonObject());
        }
        return ResponseEntity.status(HttpStatus.OK)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(jsonArrayBuilder.build().toString());
    }

    @GetMapping(path="/rsvp")
    public ResponseEntity<String> getRSVPsByName(@RequestParam(name="q") String name) {
        List<RSVP> RSVPs = rsvpService.getRSVPsByName(name);
        if (RSVPs.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Json.createObjectBuilder().add("error code", "name not found").build().toString());
        }

        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        for (RSVP rsvp : RSVPs) {
            jsonArrayBuilder.add(rsvp.toJsonObject());
        }
        JsonArray result = jsonArrayBuilder.build();
        return ResponseEntity.status(HttpStatus.OK)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(result.toString());
    }

    @PostMapping(path="/rsvp", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> insertUpdateRSVP(@RequestBody String json) throws ParseException {
        RSVP rsvp = rsvpService.jsonStringToJsonObject(json);
        RSVP result = rsvpService.createRSVP(rsvp); // this 'result' holds just the rsvp id

        JsonObject jsonObject = Json.createObjectBuilder()
                                    .add("rsvpID", result.getId())
                                    .build();
        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(jsonObject.toString());
    }

    @PutMapping(path="/rsvp/{email}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateExistingRSVP(@PathVariable String email, @RequestBody String json) throws ParseException {
        boolean success = rsvpService.updateRSVP(email, json);
        if (!success) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Json.createObjectBuilder().add("error code", "email not found").build().toString());
        }
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(Json.createObjectBuilder().add("message", "successful update").build().toString());
    }

    @GetMapping(path="/rsvps/count")
    public ResponseEntity<String> getTotalRSVPCounts() {
        long count = rsvpService.getTotalRSVPCount();
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(Json.createObjectBuilder().add("total RSVP count", count).build().toString());
    }
}
