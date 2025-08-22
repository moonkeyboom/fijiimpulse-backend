package sa3.fijiimpulse.controllers;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import sa3.fijiimpulse.entity.User;

@RestController
public class FormSubmitDataController {
    @PostMapping("login")
    public String login(
            @ModelAttribute User inputFormSubmit
            ) {
        System.out.println("username: " + inputFormSubmit.getUsername());
        System.out.println("password: " + inputFormSubmit.getPassword());
        return String.format("username = %s, password = %s", inputFormSubmit.getUsername(), inputFormSubmit.getPassword());
    }

}
