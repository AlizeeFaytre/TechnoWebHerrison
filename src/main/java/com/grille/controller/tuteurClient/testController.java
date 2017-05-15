package com.grille.controller.tuteurClient;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by jiawei on 14/05/2017.
 */

@Controller
public class testController {

    @RequestMapping(value = "/testjia", method = RequestMethod.GET)
    public String test(@RequestParam(name = "page") int page){
        return "tuteur-client/test";

    }
}
