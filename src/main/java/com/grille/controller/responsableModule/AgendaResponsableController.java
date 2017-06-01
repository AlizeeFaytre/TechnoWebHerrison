package com.grille.controller.responsableModule;

import com.grille.dao.AgendaRespoRepository;
import com.grille.entities.Deadline;
import com.grille.entities.Domain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
public class AgendaResponsableController {

    @Autowired
    private AgendaRespoRepository agendarespo;

    @RequestMapping(value = "/agenda-responsable", method = RequestMethod.GET)
    public String index (Model model){
        List<Deadline> listDeadline = agendarespo.findAll();
        model.addAttribute("listDeadline", listDeadline);
        model.addAttribute("deadline", new Deadline());
        return "respoModule/agenda-responsable";
    }


    @RequestMapping(value = "/new_deadline_insert", method = RequestMethod.POST)
    public void grille (Model model, Deadline d, HttpServletResponse response){

        agendarespo.save(d);
        try{
            response.sendRedirect("/agenda-responsable");
        }catch (IOException i){
            i.printStackTrace();
        }

    }


}