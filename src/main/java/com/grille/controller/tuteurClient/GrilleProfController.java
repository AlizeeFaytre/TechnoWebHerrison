package com.grille.controller.tuteurClient;

import com.grille.dao.DomainRepository;
import com.grille.dao.EvaluateRepository;
import com.grille.dao.GroupeRepository;
import com.grille.dao.SkillRepository;
import com.grille.entities.*;
import com.grille.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

import javax.servlet.http.HttpSession;

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
    @Autowired
    private UserService userService;
    @Autowired
    private EvaluateRepository evaluateRepository;

    //---------1 ---un mapping pour le visionnage qui map sur la vue grilleEleve
    //---------2 ---un mapping pour la modification (remplisage)

    @RequestMapping(value = "/grilleprofvue", method = RequestMethod.GET)
    public String grilleProfvue(Model model, @RequestParam("groupe")int idGr, @RequestParam("domain")int idDom, HttpSession session){

        User currentUser = userService.getLogedUser(session);
        model.addAttribute("currentUser", currentUser);

        //selection tous les domains
        List<Domain> listDomain = domainRepository.findAll();
        model.addAttribute("listDomain", listDomain);


        //Selectionner le domain d'url dans tous les domain
        Domain selectedDomain = domainRepository.findById(idDom);
        model.addAttribute("selectedDomain",selectedDomain);


        //Selectionner le groupe d'url parmi tous les groupes
        Groupe selectedGroupe = groupeRepository.findById(idGr);
        ArrayList<User> selectedGroupelistEleve = new ArrayList<>();
        List<Integer> listIdTuteur = new ArrayList<>();
        for (User tuteur:selectedGroupe.getListTuteur()) {
            listIdTuteur.add(tuteur.getId());
        }
        for (User u : selectedGroupe.getListUser()) {
            if ( !(listIdTuteur.contains(u.getId())) && u.getId() != selectedGroupe.getClient().getId()) {
                selectedGroupelistEleve.add(u);
            }

        }
        model.addAttribute("selectedGroupelistEleve", selectedGroupelistEleve);
        model.addAttribute("selectedGroupe",selectedGroupe);


        //Selection skill du domain selected
        Set<Skill> setSkill = new HashSet<>();
        try {
            setSkill = selectedDomain.getListSkill();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        ArrayList<Skill> selectedDomainlistSkill = new ArrayList<Skill>(setSkill);
        model.addAttribute("selectedDomainListSkill", selectedDomainlistSkill);

        //Selection des evluates pour ce groupe et les skills du domain selected
        List<Evaluate> listEvaluate = evaluateRepository.findAll();

        //map contenant les skills et une liste d'eleves et de evaluation de cet eleve pour le skill donne
        Map<SkillAndObservation, List<UserAndEval>> mapSkill = new HashMap<>();
        for (Skill s : selectedDomainlistSkill){
            List<UserAndEval> listUserAndEval = new ArrayList<>();
            for (User u : selectedGroupelistEleve){
                UserAndEval userAndEval = new UserAndEval();
                userAndEval.setUser(u);
                userAndEval.setEvaluate(evaluateRepository.findByUserAndSkill(u,s));
                /*
                for (Evaluate e : listEvaluate){
                    if (e.getSkill() == s && e.getUser() == u){
                        userAndEval.setEvaluate(e);
                        break;
                    }
                }
                */
                if (userAndEval.getEvaluate() == null){
                    Evaluate evalVide = new Evaluate();
                    evalVide.setLevel("Vide");
                    userAndEval.setEvaluate(evalVide);
                }
                listUserAndEval.add(userAndEval);
            }
            String groupObservation = "";
            String tuteurClientObservation = "";
            try{
                groupObservation = listUserAndEval.get(0).getEvaluate().getGroupObservation();
                tuteurClientObservation =listUserAndEval.get(0).getEvaluate().getTuteurClientObservation();
            }catch (NullPointerException e){

            }
            SkillAndObservation skillAndObservation = new SkillAndObservation();
            skillAndObservation.setSkill(s);
            skillAndObservation.setGroupObservation(groupObservation);
            skillAndObservation.setTuteurClientObservation(tuteurClientObservation);
            mapSkill.put(skillAndObservation, listUserAndEval);
        }
        model.addAttribute("mapSkill", mapSkill);

        return("tuteur-client/grilleProf");
    }

    @RequestMapping(value = "/grilleprofmodif", method = RequestMethod.POST)
    public String grilleProfmodif(){



        return("tuteur-client/grilleProf");
    }

    public class UserAndEval{
        private User user;
        private Evaluate evaluate;

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public Evaluate getEvaluate() {
            return evaluate;
        }

        public void setEvaluate(Evaluate evaluate) {
            this.evaluate = evaluate;
        }
    }

    public class SkillAndObservation{
        private Skill skill;
        private String groupObservation;
        private String tuteurClientObservation;

        public Skill getSkill() {
            return skill;
        }

        public void setSkill(Skill skill) {
            this.skill = skill;
        }

        public String getGroupObservation() {
            return groupObservation;
        }

        public void setGroupObservation(String groupObservation) {
            this.groupObservation = groupObservation;
        }

        public String getTuteurClientObservation() {
            return tuteurClientObservation;
        }

        public void setTuteurClientObservation(String tuteurClientObservation) {
            this.tuteurClientObservation = tuteurClientObservation;
        }
    }

}
