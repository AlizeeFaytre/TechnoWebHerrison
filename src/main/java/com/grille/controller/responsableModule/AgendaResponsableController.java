package com.grille.controller.responsableModule;

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
public class AgendaResponsableController {

    @Autowired
    private AgendaRespoRepository agendarespo;

    @Autowired
    private DeadlineRepository deadlinerepo;

    @Autowired
    private DomainRepository domainRepo;
    
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/agenda-responsable", method = RequestMethod.GET)
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
        Map<String, List<DeadlineAndNameDo>> listdeadlinepromo = new HashMap<>();
        for (String s : listpromo){
            List<DeadlineAndNameDo> listDeadlineAndNameDo = new ArrayList<>();
            for (Deadline d : deadlinerepo.findByPromo(s)){
                DeadlineAndNameDo deadlineAndNameDo = new DeadlineAndNameDo();
                deadlineAndNameDo.setDeadline(d);
                deadlineAndNameDo.setNameDo(d.getDomain().getName());
                listDeadlineAndNameDo.add(deadlineAndNameDo);
            }
            listdeadlinepromo.put(s, listDeadlineAndNameDo);
        }

        model.addAttribute("listpromo", listpromo);
        model.addAttribute("listdeadlinepromo", listdeadlinepromo);
        
        model.addAttribute("currentUser", currentUser);

        return "respoModule/agenda-responsable";
    }

    public class DeadlineAndNameDo{
        private Deadline deadline;
        private String nameDo;

        public Deadline getDeadline() {
            return deadline;
        }

        public void setDeadline(Deadline deadline) {
            this.deadline = deadline;
        }

        public String getNameDo() {
            return nameDo;
        }

        public void setNameDo(String nameDo) {
            this.nameDo = nameDo;
        }
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