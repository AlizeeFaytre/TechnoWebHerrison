package com.grille.controller.eleve;

import com.grille.dao.DomainRepository;
import com.grille.dao.RoleRepository;
import com.grille.dao.UserRepository;
import com.grille.dto.LineGrid;
import com.grille.entities.*;
import com.grille.service.GridService;
import com.grille.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by alizeefaytre on 07/05/2017.
 */
@Controller
public class grilleEleveController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private DomainRepository domainRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private GridService gridService;

    @GetMapping("/grille")
    public String grille(HttpSession session, Model model, @RequestParam(name="domain")String domainName){
        User currentUser = userService.getLogedUser(session);
        Groupe group = userService.getCurrentGroupe(currentUser);
        Set<User> listUser = group.getListUser();
        //on enlève de la liste les tuteurs et professeurs
        for (User user:listUser) {
            Set<Role> listRoles = user.getRoles();
            if(!(listRoles.contains(roleRepository.findByName("eleve")))){
                listUser.remove(user);
            }
        }

        Domain domain = domainRepository.findOneByName(domainName);

        Set<Skill> listSkill = domain.getListSkill();

        Map<LineGrid, ArrayList<LineGrid>> listeLigne = new HashMap<>();

        for (Skill skill:listSkill) {
            LineGrid firstLine = new LineGrid();
            firstLine.setCompetenceName(skill.getName());
            firstLine.setCompetenceDescription(skill.getDescription());
            firstLine.setUserName(currentUser.getPrenom() + " " + currentUser.getNom());
            Set<Evaluate> lastEvaluateUser = userService.getLastEvaluate(currentUser);

            //on recupere la liste de toutes les competence evaluée
            Set<Skill> listEvaluateSkill =  new HashSet<>();
            for (Evaluate e:lastEvaluateUser) {
               listEvaluateSkill.add(e.getSkill());
            }

            //On verifie que la competence dans ce domain soit bien evaluée
            if (listEvaluateSkill.contains(skill)){
                for (Evaluate e:lastEvaluateUser) {
                    if (e.getSkill().equals(skill)){
                        firstLine.setGeneralObservation(e.getGroupObservation());
                        firstLine.setIndividualObservation(e.getIndividualObservation());
                        firstLine.setCompetenceEvaluate(e.getLevel());
                    }
                }
            }
            else{
                firstLine.setGeneralObservation(" ");
                firstLine.setIndividualObservation(" ");
                firstLine.setCompetenceEvaluate(" ");
            }


            ArrayList<LineGrid> listLine = new ArrayList<>();
            for (User user:listUser) {
                if (!(user.equals(currentUser))){
                    LineGrid nextLine = new LineGrid();
                    nextLine.setUserName(user.getPrenom() + " " + user.getNom());
                    Set<Evaluate> listUserEvaluate = userService.getLastEvaluate(user);
                    Set<Skill> userEvaluatedSkill = new HashSet<>();
                    for (Evaluate e :listUserEvaluate) {
                        userEvaluatedSkill.add(e.getSkill());
                    }

                    if(userEvaluatedSkill.contains(skill)){
                        for (Evaluate e:listUserEvaluate) {
                            if(e.getSkill().equals(skill)){
                                nextLine.setIndividualObservation(e.getIndividualObservation());
                                nextLine.setCompetenceEvaluate(e.getLevel());
                            }
                        }
                    }
                    else{
                        nextLine.setIndividualObservation(" ");
                        nextLine.setCompetenceEvaluate(" ");
                        nextLine.setUserName(user.getPrenom() + " " + user.getNom());
                    }
                    listLine.add(nextLine);
                }

            }

            listeLigne.put(firstLine, listLine);
        }


        model.addAttribute("ligneGrille", listeLigne);

        return "eleves/grilleEleve";
    }
}
