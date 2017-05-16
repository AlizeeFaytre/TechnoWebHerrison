package com.grille.controller.responsableModule;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AgendaResponsableController {


    @GetMapping("/agenda-responsable")
    public String index (Model model){
                return "respoModule/agenda-responsable";
    }
}