package sa3.fijiimpulse.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HeaderController {

    @GetMapping("test-header")
    public String getHeader(
            @RequestHeader("Accept-Encoding") String headerFromRequest,
            @RequestHeader("myHeader") String headerFromRequest2
    ) {
        System.out.println(headerFromRequest);
        System.out.println(headerFromRequest2);
    return headerFromRequest + " " + headerFromRequest2;
    }

    @GetMapping("test-head-2")
    public String testHeader2(
            @RequestHeader Map<String, String> header
            ) {

        return header.get("user-Agent".toLowerCase());
//        return header.get("user-Agent");
    }
}
