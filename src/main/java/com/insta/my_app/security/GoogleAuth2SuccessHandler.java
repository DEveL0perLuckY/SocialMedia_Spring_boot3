package com.insta.my_app.security;

import com.insta.my_app.model.FinalRolesConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import com.insta.my_app.domain.Role;
import com.insta.my_app.domain.User;
import com.insta.my_app.repos.RoleRepository;
import com.insta.my_app.repos.UserRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class GoogleAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    final private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;

        String email = token.getPrincipal().getAttributes().get("email").toString();

        if (userRepository.findByEmail(email).isEmpty()) {
            User newUser = new User();
            Set<Role> rolesHashSet = new HashSet<>();
            Optional<Role> optionalRole = roleRepository.findById(FinalRolesConstants.Roles.USER);
            if (optionalRole.isPresent()) {
                rolesHashSet.add(optionalRole.get());
            } else {
                throw new IllegalStateException("role not found");
            }

            newUser.setUsername(token.getPrincipal().getAttributes().get("given_name").toString() + " "
                    + token.getPrincipal().getAttributes().get("family_name").toString());
            newUser.setEmail(email);
            newUser.setRoleId(rolesHashSet);
            newUser.setCreatedAt(LocalDateTime.now());
            newUser.setBio("Hello World");
            String password = getPasswordGenerator();
            System.out.println("Your new generated password is " + password);
            newUser.setPassword(passwordEncoder.encode(password));
            request.getSession().setAttribute("password", password);
            userRepository.save(newUser);
        }

        UserDetails userDetails = getUserDetails(email);
        Authentication updatedAuthentication = new UsernamePasswordAuthenticationToken(userDetails,
                authentication.getCredentials(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(updatedAuthentication);
        redirectStrategy.sendRedirect(request, response, "/feed");
    }

    private UserDetails getUserDetails(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            User u = user.get();
            List<GrantedAuthority> authorities = u.getRoleId().stream()
                    .map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());

            System.out.println("USER Details if user is present : " + u);
            System.out.println("Name : " + u.getPassword());
            System.out.println("Authority : " + authorities);
            System.out.println("Password is : " + u.getPassword());

            return new org.springframework.security.core.userdetails.User(u.getEmail(), u.getPassword(), authorities);
        } else {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
    }

    private static final String ALLOWED_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789~!@#$%^&*()_-+=<>?/";

    private static String getPasswordGenerator() {
        char[] password = new char[12];
        for (int i = 0; i < 12; i++) {
            int randomIndex = (int) Math.floor(Math.random() * ALLOWED_CHARACTERS.length());
            password[i] = ALLOWED_CHARACTERS.charAt(randomIndex);
        }
        return password.toString();
    }
}
