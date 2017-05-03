package com.grille.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by alizeefaytre on 29/04/2017.
 */

@Controller
public class HomeController {


    @GetMapping("/home")
    public String index (Model model){
        model.addAttribute("hello", "Hello Alizee");
        return "eleves/hello";
    }
}
