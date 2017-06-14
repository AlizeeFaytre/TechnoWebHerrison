package com.grille.controller.responsableModule;

import com.grille.dao.DomainRepository;
import com.grille.dao.SkillRepository;
import com.grille.entities.Domain;
import com.grille.entities.Skill;
import com.grille.entities.User;
import com.grille.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@Controller
public class GestionSkillsController {

    @Autowired
    private SkillRepository skillrepo;
    
    @Autowired
    private UserService userService;


    @RequestMapping(value="/gestion-skills", method = RequestMethod.GET)
    public String indexbis (Model model, HttpSession session){
    	User currentUser = userService.getLogedUser(session);
    	List<Skill> listSkill = skillrepo.findAll();
        model.addAttribute("listSkill", listSkill);
        model.addAttribute("skill", new Skill());
        model.addAttribute("currentUser", currentUser);
        return "respoModule/gestion-skills";
    }

    @RequestMapping(value = "/gestion-skill-recherche", method = RequestMethod.POST)
    public void collectMotCle(HttpServletResponse response, String motCle){

        try {
            response.sendRedirect("/gestion-skills-resultat?recherche="+motCle);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @RequestMapping(value="/gestion-skills-resultat", method = RequestMethod.GET)
    public String resultat (Model model, @RequestParam("recherche") String motCle, HttpSession session){
        List<Skill> listSkill = skillrepo.findAll();
        model.addAttribute("skill", new Skill());

        Skill resultatRecherche = skillrepo.findByName(motCle);
        model.addAttribute("listSkill", resultatRecherche);
        User currentUser = userService.getLogedUser(session);
        model.addAttribute("currentUser", currentUser);
        return "respoModule/gestion-skills";
    }

    @RequestMapping(value = "/new_skill_insert", method = RequestMethod.POST)
    public void grille (Model model, Skill s, HttpServletResponse response){

        skillrepo.save(s);
        try{
            response.sendRedirect("/gestion-skills");
        }catch (IOException i){
            i.printStackTrace();
        }

    }



}