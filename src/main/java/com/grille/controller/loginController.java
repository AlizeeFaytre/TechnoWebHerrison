package com.grille.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by alizeefaytre on 01/05/2017.
 */

@Controller
public class loginController {

    @GetMapping("/login")
    public String index (){

        return "login";
    }
}
