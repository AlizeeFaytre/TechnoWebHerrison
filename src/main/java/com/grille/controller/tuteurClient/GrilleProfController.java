package com.grille.controller.tuteurClient;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by jiawei on 15/05/2017.
 */

@Controller
public class GrilleProfController {


    //---------1 ---un mapping pour le visionnage qui map sur la vue grilleEleve
    //---------2 ---un mapping pour la modification (remplisage)

    @RequestMapping(value = "/grilleprofvue", method = RequestMethod.GET)
    public String grilleProfvue(){



        return("tuteur-client/grilleProf");
    }

    @RequestMapping(value = "/grilleprofmodif", method = RequestMethod.POST)
    public String grilleProfmodif(){



        return("tuteur-client/grilleProf");
    }

}
