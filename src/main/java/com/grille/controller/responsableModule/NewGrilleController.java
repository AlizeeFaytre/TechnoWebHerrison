package com.grille.controller.responsableModule;

import com.grille.dao.DomainRepository;
import com.grille.dao.SkillRepository;
import com.grille.entities.Domain;
import com.grille.entities.Skill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by Thomas on 09/05/2017.
 */

@Controller
public class NewGrilleController {

    @Autowired
    private DomainRepository domainrepo;

    @Autowired
    private SkillRepository skillrepo;

    @RequestMapping(value = "/new_domain", method = RequestMethod.GET)
    public String index (Model model){

        //selection tous les domain
        List<Skill> listSkill = skillrepo.findAll();
        model.addAttribute("listSkill", listSkill);
        model.addAttribute("domain", new Domain());
        return "respoModule/new_grille";
    }

    @RequestMapping(value = "/new_domain_insert", method = RequestMethod.POST)
    public void grille (Model model, Domain d, HttpServletResponse response){

        domainrepo.save(d);
        try{
            response.sendRedirect("/gestion_grille");
        }catch (IOException i){
            i.printStackTrace();
        }

    }


}
