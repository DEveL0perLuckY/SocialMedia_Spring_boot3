package com.insta.my_app.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.insta.my_app.domain.User;
import com.insta.my_app.model.UserDTO;
import com.insta.my_app.repos.UserRepository;
import com.insta.my_app.service.UserService;

import jakarta.servlet.http.HttpSession;

import org.springframework.ui.Model;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

@Controller
@RequestMapping("/feed")
public class FeedController {
    @Autowired
    UserRepository u;

    @Autowired
    UserService userService;

    @GetMapping()
    public String getFeed(Model m, HttpSession session) {
        String temporaryPassword = (String) session.getAttribute("password");
        if (temporaryPassword != null) {
            m.addAttribute("temporaryPassword", temporaryPassword);
            session.removeAttribute("temporaryPassword");
        }
        return "UserHome/userHome";
    }

    @GetMapping("/profile")
    public String getProfile(Model m) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<User> user = u.findByEmail(username);
        UserDTO userD = new UserDTO();
        userD = userService.mapToDTO(user.get(), userD);
        if (userD.getCreatedAt() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d");
            String formattedDate = userD.getCreatedAt().format(formatter);
            m.addAttribute("formattedDate", formattedDate);
            formatter = DateTimeFormatter.ofPattern("h:mm a");
            String formattedTime = userD.getCreatedAt().format(formatter);
            m.addAttribute("formattedTime", formattedTime);
        }
        m.addAttribute("username", username);
        m.addAttribute("user", userD);
        return "UserHome/userProfile";
    }

    @GetMapping("/friends")
    public String getFrinds() {
        return "UserHome/userFriends";
    }

    @GetMapping("/posts")
    public String getPost() {
        return "UserHome/userPosts";
    }

    @GetMapping("/notifications")
    public String getNotifications() {
        return "UserHome/userNotifications";
    }
}
