package com.grille.controller;

import com.grille.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * Created by alizeefaytre on 29/04/2017.
 */

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;
}
