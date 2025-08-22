package sa3.fijiimpulse.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sa3.fijiimpulse.entity.User;

@RestController
public class JsonController {

    @PostMapping("login-with-json")
    public String loginWithJson(
            @RequestBody User inputJsonFromClient
    ) {
        return String.format("username = %s, password = %s", inputJsonFromClient.getUsername(), inputJsonFromClient.getPassword());
    }
}
