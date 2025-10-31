package sa3.fijiimpulse.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sa3.fijiimpulse.entity.User;
import sa3.fijiimpulse.service.UserService;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private UserService userService;

    @PostMapping("/register")
    private ResponseEntity<?> register(@RequestBody Map<String, String> req) {
        boolean success = userService.register(
                req.get("username"),
                req.get("email"),
                req.get("password")
        );
        if (!success) return(ResponseEntity.badRequest().body("Email already exists"));
        return ResponseEntity.ok("Register Successfully");
    }

    //temp
    @PostMapping("/register/admin")
    private ResponseEntity<?> registerAdmin(@RequestBody Map<String, String> req) {
        boolean success = userService.registerAdmin(
                req.get("username"),
                req.get("email"),
                req.get("password")
        );
        if (!success) return(ResponseEntity.badRequest().body("Email already exists"));
        return ResponseEntity.ok("Register Successfully");
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> req, HttpSession session) {
        User user = userService.login(req.get("username"), req.get("password"));

        if (user == null) return(ResponseEntity.status(401).body("Invalid credentials"));

        session.setAttribute("user", user);
        return(ResponseEntity.ok("Login success"));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return ResponseEntity.status(200).body(Map.of(
                    "authenticated", false
            ));
        }

        return ResponseEntity.ok(Map.of(
                "authenticated", true,
                "user", Map.of(
                        "id", user.getUserId(),
                        "username", user.getUsername(),
                        "email", user.getEmail(),
                        "role", user.getRole()
                )
        ));
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return(ResponseEntity.ok("logged out"));
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
