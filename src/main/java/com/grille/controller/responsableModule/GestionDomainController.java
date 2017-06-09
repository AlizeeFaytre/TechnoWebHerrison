package com.grille.controller.responsableModule;

import com.grille.dao.DomainRepository;
import com.grille.entities.Domain;
import com.grille.entities.User;
import com.grille.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

import javax.servlet.http.HttpSession;

/**
 * Created by Thomas on 09/05/2017.
 */

@Controller
public class GestionDomainController {

    @Autowired
    private DomainRepository domainrepo;
    
    @Autowired
    private UserService userService;

    @GetMapping("/gestion_grille")
    public String index (Model model, HttpSession session){
    	User currentUser = userService.getLogedUser(session);
    	List<Domain> listDomain = domainrepo.findAll();
        model.addAttribute("listDomain", listDomain);
        model.addAttribute("currentUser", currentUser);
        return "respoModule/gestion_grille";
    }
}
