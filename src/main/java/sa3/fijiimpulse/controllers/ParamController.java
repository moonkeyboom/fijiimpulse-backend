package sa3.fijiimpulse.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ParamController {

    @GetMapping("/testparam")
    public String mappingTest(
            @RequestParam(name = "name", required = false) String inputParam,
            @RequestParam(name = "age", required = false) Integer inputAge
            ) {
//        System.out.println("input form query: " + inputParam);
        return String.format("name = %s, age = %d", inputParam, inputAge);
    }

}
