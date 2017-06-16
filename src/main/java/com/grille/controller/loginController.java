package com.grille.controller;

import com.grille.dao.RoleRepository;
import com.grille.entities.Role;
import com.grille.entities.User;
import com.grille.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Set;

/**
 * Created by alizeefaytre on 01/05/2017.
 */

@Controller
public class loginController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/login")
    public String index (){

        return "login";
    }

    @GetMapping("/index")
    public void redirectIndex(HttpSession session, HttpServletResponse response){
        User currentUser = userService.getLogedUser(session);

        if (currentUser.getRoles().contains(roleRepository.findByName("eleve"))){
            try{
                response.sendRedirect("/accueilEleve");
            }catch (IOException i){
                i.printStackTrace();
            }
        }
        else if (currentUser.getRoles().contains(roleRepository.findByName("respoModule"))){
            try{
                response.sendRedirect("/gestion_grille");
            }catch (IOException i){
                i.printStackTrace();
            }
        }
        else {
            try{
                response.sendRedirect("/dashboard-tuteur?groupe=1");
            }catch (IOException i){
                i.printStackTrace();
            }
        }
    }
}
