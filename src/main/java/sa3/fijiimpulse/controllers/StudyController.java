package sa3.fijiimpulse.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StudyController {

    @GetMapping("hello-world")
    public String helloWorld1() {
        return "Hello World 1";
    }
}
