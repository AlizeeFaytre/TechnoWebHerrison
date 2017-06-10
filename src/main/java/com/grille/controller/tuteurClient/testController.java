package com.grille.controller.tuteurClient;

import com.grille.dao.DomainRepository;
import com.grille.dao.UserRepository;
import com.grille.entities.Domain;
import com.grille.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by jiawei on 14/05/2017.
 */

@Controller
public class testController {

    @Autowired
    private DomainRepository domainrepos;
    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/testjia", method = RequestMethod.GET)
    public String test(Model model){

        List<User> listUser = userRepository.findAll();
        model.addAttribute("listUser", listUser);
        model.addAttribute("simpleChaine", new simpleChaine());
        return "tuteur-client/test";

    }

    @RequestMapping(value = "/chercher", method = RequestMethod.POST)
    public String chercher(Model model, String motCle){

        model.addAttribute("presence", motCle);

        return "tuteur-client/test2";

    }

    public class simpleChaine{
        public List<String> chaine;

        public List<String> getChaine() {
            return chaine;
        }

        public void setChaine(List<String> chaine) {
            this.chaine = chaine;
        }
    }
}
