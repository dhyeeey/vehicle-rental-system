package org.intech.vehiclerental.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.intech.vehiclerental.entities.User;
import org.intech.vehiclerental.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getUser(HttpServletRequest httpServletRequest){
        HttpSession httpSession = httpServletRequest.getSession(false);

        if (httpSession == null || httpSession.getAttribute("USER_ID") == null) {
            return ResponseEntity.status(401).build();
        }

        Long id = (Long) httpSession.getAttribute("USER_ID");

        String accountOwnerType = (String)httpSession.getAttribute("ACCOUNT_OWNER_TYPE");

        User user = userService.findUserById(id);

        return ResponseEntity.ok(user);
    }


}
