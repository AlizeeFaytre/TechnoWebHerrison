package com.grille.controller.tuteurClient;

import com.grille.dao.DomainRepository;
import com.grille.entities.Domain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;


/**
 * Created by jiawei on 14/05/2017.
 */

@Controller
public class testController {

    @Autowired
    private DomainRepository domainrepos;

    @RequestMapping(value = "/testjia", method = RequestMethod.GET)
    public String test(Model model){
        model.addAttribute("date", new Date());
        return "tuteur-client/test";

    }

    @RequestMapping(value = "/chercher", method = RequestMethod.POST)
    public String chercher(Model model, Date d){

        model.addAttribute("date", d);
        System.out.print(d);

        return "tuteur-client/test2";

    }
}
