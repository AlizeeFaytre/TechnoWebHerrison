package com.grille.controller.responsableModule;

import com.grille.dao.DomainRepository;
import com.grille.dao.SkillRepository;
import com.grille.entities.Domain;
import com.grille.entities.Skill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class GestionSkillsController {

    @Autowired
    private SkillRepository skillrepo;

    @GetMapping("/new_grille")
    public String index (Model model){

        List<Skill> listSkill = skillrepo.findAll();
        model.addAttribute("listSkill", listSkill);
        return "respoModule/new_grille";
    }

    @GetMapping("/new_grille2")
    public String formgrille(Model model){
        model.addAttribute("domain", new Domain());
        return "respoModule/new_grille";
    }


    /*@GetMapping(value="/new_grille3", method = RequestMethod.POST)
    public String savegrille(Model model, Domain d){
        DomainRepository.save(d);
        model.addAttribute("domain", d);
        return "respoModule/gestion_grille";
    }*/

}