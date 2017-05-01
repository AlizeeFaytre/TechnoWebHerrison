package com.grille.controller.eleve;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by alizeefaytre on 30/04/2017.
 */

@Controller
public class AccueilEleveController {

    @GetMapping("/accueilEleve")
    public String accueil(){
        return "eleves/accueilEleve";
    }
}
