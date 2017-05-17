package com.grille.controller.responsableModule;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by Thomas on 09/05/2017.
 */

@Controller
public class NewGrilleController {


    @GetMapping("/new_grille")
    public String index (Model model){
        return "respoModule/new_grille";
    }
}
