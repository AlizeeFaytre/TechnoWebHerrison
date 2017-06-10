package com.grille.controller.tuteurClient;

import com.grille.dao.AgendaRespoRepository;
import com.grille.dao.DeadlineRepository;
import com.grille.dao.DomainRepository;
import com.grille.entities.Deadline;
import com.grille.entities.Domain;
import com.grille.entities.User;
import com.grille.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.*;

@Controller
public class AgendaTuteurController {

    @Autowired
    private AgendaRespoRepository agendarespo;

    @Autowired
    private DeadlineRepository deadlinerepo;

    @Autowired
    private DomainRepository domainRepo;
    
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/agenda-tuteur", method = RequestMethod.GET)
    public String index (Model model, HttpSession session){
        /* List<Deadline> listDeadline = agendarespo.findAll();
        model.addAttribute("listDeadline", listDeadline);
        model.addAttribute("deadline", new Deadline()); */
    	User currentUser = userService.getLogedUser(session);
    	Set<String> listpromo = new HashSet<>();
        List<Deadline> listdeadline = deadlinerepo.findAll();
        for (Deadline d : listdeadline){
            listpromo.add(d.getPromo());
        }

        // ArrayList<ArrayList<Deadline>> listdeadlinepromo = new ArrayList<>();
        Map<String, List<Deadline>> listdeadlinepromo = new HashMap<>();
        for (String s : listpromo){
            listdeadlinepromo.put(s, deadlinerepo.findByPromo(s));
        }
        model.addAttribute("listpromo", listpromo);
        model.addAttribute("listdeadlinepromo", listdeadlinepromo);

        String nameDo = domainRepo.findById(1).getName();
        model.addAttribute("domainRepo", domainRepo);

        model.addAttribute("test", nameDo);
        
        model.addAttribute("currentUser", currentUser);

        return "tuteur-client/agenda-tuteur";
    }


    
}