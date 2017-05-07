package com.grille.controller.eleve;

import com.grille.dao.UserRepository;
import com.grille.entities.Evaluate;
import com.grille.entities.Groupe;
import com.grille.entities.Skill;
import com.grille.entities.User;
import com.grille.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Calendar;
import java.util.Set;

/**
 * Created by alizeefaytre on 30/04/2017.
 */

@Controller
public class AccueilEleveController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/accueilEleve")
    public String accueil(@AuthenticationPrincipal UserDetails userDetails, Model model){
        User currentUser = userRepository.findByIdentifiant(userDetails.getUsername());
        Set<Evaluate> listEvaluate = userService.getLastEvaluate(currentUser);

        model.addAttribute("listEvaluate", listEvaluate);

        Groupe currentGroup = userService.getCurrentGroupe(currentUser);

        model.addAttribute("groupe", currentGroup);

        return "eleves/accueilEleve";
    }
}
