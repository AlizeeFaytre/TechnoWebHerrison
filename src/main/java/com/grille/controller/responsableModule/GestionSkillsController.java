package com.grille.controller.responsableModule;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GestionSkillsController {


    @GetMapping("/gestion-skills")
    public String index (Model model){
                return "responsable/gestion-skills";
    }
}