package com.grille.controller.eleve;

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

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by alizeefaytre on 30/04/2017.
 */

@Controller
public class AccueilEleveController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private GridService gridService;

    @GetMapping("/accueilEleve")
    public String accueil(HttpSession session, Model model){
        User currentUser = userService.getLogedUser(session);
        Set<Evaluate> listEvaluate = userService.getLastEvaluate(currentUser);


        Grid userGrid = gridService.getGrid(currentUser);

        Set<Skill> listEvaluateSkill = new HashSet<>();
        for (Evaluate evaluate:listEvaluate) {
            listEvaluateSkill.add(evaluate.getSkill());
        }

        Map<String[], ArrayList<LineGrid>> listeLigne = new HashMap<>();//manque juste le grade
        Set<Domain> listDomaine = userGrid.getListDomain();
        for (Domain domaine:listDomaine) {
            String[] oneDomaine = new String[2];
            ArrayList<LineGrid> lines = new ArrayList<>();
            oneDomaine[0] = domaine.getName();
            for (Skill skill:domaine.getListSkill()) {
                if (listEvaluateSkill.contains(skill)){
                    for (Evaluate evaluate:listEvaluate) {
                        if(evaluate.getSkill().equals(skill)){
                            LineGrid line = new LineGrid();
                            line.setCompetenceName(skill.getName());
                            line.setCompetenceWeight(skill.getPonderation());
                            line.setCompetenceEvaluate(evaluate.getLevel());
                            line.setIndividualObservation(evaluate.getIndividualObservation());
                            line.setUserName(currentUser.getPrenom() + " " + currentUser.getNom());
                            lines.add(line);
                        }
                    }
                }
                else{
                    LineGrid line = new LineGrid();
                    line.setCompetenceName(skill.getName());
                    line.setCompetenceWeight(skill.getPonderation());
                    line.setCompetenceEvaluate(" ");
                    line.setIndividualObservation(" ");
                    line.setUserName(currentUser.getPrenom() + " " + currentUser.getNom());
                    lines.add(line);
                }

            }
            listeLigne.put(oneDomaine, lines);
        }

        model.addAttribute("listeLigne", listeLigne);
        model.addAttribute("currentUser", currentUser);

        return "eleves/accueilEleve";
    }
}
