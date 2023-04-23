package sg.edu.nus.iss.day22workshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import sg.edu.nus.iss.day22workshop.model.RSVP;

@Controller
public class RSVPController {

    @GetMapping(path="/")
    public String renderForm(Model model) {
        model.addAttribute("rsvp", new RSVP());
        return "index";
    }

}
