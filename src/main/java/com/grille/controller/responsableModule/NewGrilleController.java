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
        List<Domain> listDomain = domainrepo.findAll();

        //selection max id
        int maxId = 0;
        for (Domain d : listDomain){
            if (d.getId()> maxId){
                maxId = d.getId();
            }
        }
        maxId = maxId + 1;
        Domain test = new Domain();
        test.setId(maxId);
        test.setName("Italie");
        domainrepo.save(test);

        List<Skill> listSkill = skillrepo.findAll();
        model.addAttribute("listSkill", listSkill);
        return "respoModule/new_grille";
    }
/*
    @RequestMapping(value = "/new_domain", method = RequestMethod.POST)
    public ResponseEntity<Void> processXml(@RequestBody String requestBody){

        //selection tous les domain
        List<Domain> listDomain = domainrepo.findAll();

        //selection max id
        int maxId = 0;
        for (Domain d : listDomain){
            if (d.getId()> maxId){
                maxId = d.getId();
            }
        }
        maxId = maxId + 1;
        Domain test = new Domain();
        test.setId(maxId);
        test.setName(requestBody);
        domainrepo.save(test);

        return new ResponseEntity<Void>(HttpStatus.CONFLICT);

    }*/


}
