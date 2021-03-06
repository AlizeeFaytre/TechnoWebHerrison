package com.grille.controller.responsableModule;

import com.grille.dao.DomainRepository;
import com.grille.dao.SkillRepository;
import com.grille.entities.Attendance;
import com.grille.entities.Domain;
import com.grille.entities.Skill;
import com.grille.entities.User;
import com.grille.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Thomas on 09/05/2017.
 */

@Controller
public class NewGrilleController {

    @Autowired
    private DomainRepository domainrepo;

    @Autowired
    private SkillRepository skillrepo;
    
    @Autowired
    private UserService userService;

    @Secured({"ROLE_ADMIN", "ROLE_respoModule"})
    @RequestMapping(value = "/new_domain", method = RequestMethod.GET)
    public String index (Model model, HttpSession session){
    	User currentUser = userService.getLogedUser(session);
        //selection tous les domain
        List<Skill> listSkill = skillrepo.findAll();
        model.addAttribute("listSkill", listSkill);
        model.addAttribute("domain", new Domain());
        model.addAttribute("currentUser", currentUser);
        return "respoModule/new_grille";
    }

    @Secured({"ROLE_ADMIN", "ROLE_respoModule"})
    @RequestMapping(value = "/new_domain_insert", method = RequestMethod.POST)
    public void grille (Model model, Domain d, String motcle, HttpServletResponse response){

        String[] parts = motcle.split(",");
        Set<Skill> setSkill = new HashSet<>();
        for (String s : parts) {
            if (!s.equalsIgnoreCase("")){
                setSkill.add(skillrepo.findById(Integer.parseInt(s)));
            }
        }
        d.setListSkill(setSkill);
        domainrepo.save(d);
        try{
            response.sendRedirect("/gestion_grille");
        }catch (IOException i){
            i.printStackTrace();
        }

    }


}
