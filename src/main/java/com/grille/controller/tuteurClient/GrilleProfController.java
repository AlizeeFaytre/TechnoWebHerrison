package com.grille.controller.tuteurClient;

import com.grille.dao.DomainRepository;
import com.grille.dao.GroupeRepository;
import com.grille.dao.SkillRepository;
import com.grille.entities.Domain;
import com.grille.entities.Groupe;
import com.grille.entities.Skill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by jiawei on 15/05/2017.
 */

@Controller
public class GrilleProfController {

    @Autowired
    private DomainRepository domainRepository;
    @Autowired
    private GroupeRepository groupeRepository;
    @Autowired
    private SkillRepository skillRepository;

    //---------1 ---un mapping pour le visionnage qui map sur la vue grilleEleve
    //---------2 ---un mapping pour la modification (remplisage)

    @RequestMapping(value = "/grilleprofvue", method = RequestMethod.GET)
    public String grilleProfvue(Model model, @RequestParam("groupe")int idGr, @RequestParam("domain")int idDom){

        //selection tous les domains
        List<Domain> listDomain = domainRepository.findAll();
        model.addAttribute("listDomain", listDomain);

        //selection tous les groupes
        List<Groupe> listGroupe = groupeRepository.findAll();



        //Selectionner le domain d'url dans tous les domain
        Domain selectedDomain = new Domain();
        for (Domain d : listDomain){
            if (d.getId() == idDom){
                selectedDomain = d;
            }
        }
        model.addAttribute("selectedDomain",selectedDomain);



        //Selectionner le groupe d'url parmi tous les groupes
        Groupe selectedGroupe = new Groupe();
        for (Groupe p : listGroupe){
            if (p.getId() == idGr){
                selectedGroupe = p;
            }
        }
        model.addAttribute("selectedGroupe", selectedGroupe);

        //Selection skill du domain selected
        Set<Skill> setSkill = selectedDomain.getListSkill();
        ArrayList<Skill> listSkill = new ArrayList<Skill>(setSkill);
        model.addAttribute("listSkill", listSkill);


        return("tuteur-client/grilleProf");
    }

    @RequestMapping(value = "/grilleprofmodif", method = RequestMethod.POST)
    public String grilleProfmodif(){



        return("tuteur-client/grilleProf");
    }

}
