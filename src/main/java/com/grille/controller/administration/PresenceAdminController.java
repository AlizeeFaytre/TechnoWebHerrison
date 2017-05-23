package com.grille.controller.administration;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PresenceAdminController {
    @GetMapping("/presence-admin")
    public String index (Model model){
                return "administration/presence-admin";
    }
}
