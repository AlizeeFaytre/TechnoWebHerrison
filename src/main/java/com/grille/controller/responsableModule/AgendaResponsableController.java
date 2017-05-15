package com.grille.controller.responsableModule;

import com.grille.dao.AgendaRespoRepository;
import com.grille.entities.Deadline;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class AgendaResponsableController {

    @Autowired
    private AgendaRespoRepository agendarespo;

    @GetMapping("/agenda-responsable")
    public String index (Model model){
        List<Deadline> listDeadline = agendarespo.findAll();
        model.addAttribute("listDeadline", listDeadline);
        return "respoModule/agenda-responsable";
    }


}