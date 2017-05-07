package com.grille.controller.eleve;

import com.grille.dao.UserRepository;
import com.grille.entities.Evaluate;
import com.grille.entities.Groupe;
import com.grille.entities.Skill;
import com.grille.entities.User;
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

    private UserRepository userRepository;

    @GetMapping("/accueilEleve")
    public String accueil(@AuthenticationPrincipal UserDetails userDetails, Model model){
        User currentUser = userRepository.findByIdentifiant(userDetails.getUsername());
        Set<Evaluate> listEvaluate = currentUser.getListEvaluate();
        for (Evaluate e:listEvaluate) {
            Skill skill2 = e.getSkill();
            for (Evaluate evaluate:listEvaluate) {
                Skill skill1 = evaluate.getSkill();
                if(skill1.equals(skill2)){
                    if (e.getDate().after(evaluate.getDate())){
                        listEvaluate.remove(evaluate);
                    }
                    else{
                        listEvaluate.remove(e);
                    }
                }

            }
        }

        model.addAttribute("listEvaluate", listEvaluate);

        Calendar cal = Calendar.getInstance();
        Integer year = cal.get(Calendar.YEAR);
        String yearStr = Integer.toString(year);
        int month = cal.get(Calendar.MONTH);
        String semester = new String();
        if(month>=2 && month<=8){
            semester = "S1";
        }
        else{
            semester = "S2";
        }

        String currentSemester = yearStr + " - " + semester;

        Groupe currentGroup = new Groupe();
        Set<Groupe> listGroupe = currentUser.getGroupes();
        for (Groupe g:listGroupe) {
            if(g.getSemester().equals(currentSemester)){
                currentGroup = g;
            }
        }

        model.addAttribute("groupe", currentGroup);

        return "eleves/accueilEleve";
    }
}
