package sa3.fijiimpulse.controllers;

import org.springframework.web.bind.annotation.*;

@RestController
public class MappingController {

    @GetMapping("/")
    public String display() {
        return "nanana";
    }

    @GetMapping("/page2")
    public String about() {
        return "nonono";
    }

    @RequestMapping(value = "hello3", method = {
            RequestMethod.GET,
            RequestMethod.POST
    })
    public String both() {
        return "get or post";
    }
}
