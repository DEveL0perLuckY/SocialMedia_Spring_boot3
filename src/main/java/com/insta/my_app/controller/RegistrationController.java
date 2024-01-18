package com.insta.my_app.controller;

import com.insta.my_app.domain.Role;
import com.insta.my_app.domain.User;
import com.insta.my_app.model.FinalRolesConstants;
import com.insta.my_app.model.TempDTO;
import com.insta.my_app.repos.RoleRepository;
import com.insta.my_app.repos.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Controller
public class RegistrationController {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;

    @GetMapping("/unAuthorized")
    public String unAuthorized() {
        return "Pages/unAuthorized";
    }

    @GetMapping("/reg")
    public String register(Model m) {
        m.addAttribute("obj", new TempDTO());
        return "Pages/SingUp";
    }

    @PostMapping("/reg")
    public String registration(@Valid @ModelAttribute("obj") TempDTO x, BindingResult result, Model model) {

        if (userAlreadyRegistered(x.getEmail(), result)) {
            return "Pages/SingUp";
        }
        if (result.hasErrors()) {
            model.addAttribute("user", x);
            return "Pages/SingUp";
        }

        Optional<Role> role = roleRepository.findById(FinalRolesConstants.Roles.USER);

        if (role.isPresent()) {
            User user = new User();
            user.setUsername(x.getName());
            user.setEmail(x.getEmail());
            user.setPassword(passwordEncoder.encode(x.getPassword()));

            Set<Role> roles = new HashSet<>();
            roles.add(role.get());
            user.setRoleId(roles);
            user.setCreatedAt(LocalDateTime.now());
            user.setBio("Hello World");
            userRepository.save(user);

        } else {
            return "redirect:/reg?error=ROLES are not Found";
        }

        return "redirect:/reg?success";
    }

    private boolean userAlreadyRegistered(String email, BindingResult result) {
        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            result.rejectValue("email", null, "User already registered!");
            return true;
        }
        return false;
    }

}