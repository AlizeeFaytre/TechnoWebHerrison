package com.grille.service;

import com.grille.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by alizeefaytre on 01/05/2017.
 */

@RestController
public class UserRestService {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/getLogedUser")
    public Map<String, Object> getLogedUser(HttpSession session){
        SecurityContext securityContext = (SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");
        String username = securityContext.getAuthentication().getName();
        List<String> roles = new ArrayList<>();

        for (GrantedAuthority ga:securityContext.getAuthentication().getAuthorities()) {
            roles.add(ga.getAuthority());
        }

        Map<String, Object> params = new HashMap<>();
        params.put("username", username);
        params.put("roles", roles);
        return params;

    }
}
