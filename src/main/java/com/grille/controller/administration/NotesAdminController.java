package com.grille.controller.administration;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NotesAdminController {
    @GetMapping("/notes-admin")
    public String index (Model model){
                return "administration/notes-admin";
    }
}
