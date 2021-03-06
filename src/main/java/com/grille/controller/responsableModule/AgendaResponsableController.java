package com.grille.controller.responsableModule;

import com.grille.dao.AgendaRespoRepository;
import com.grille.dao.DeadlineRepository;
import com.grille.dao.DomainRepository;
import com.grille.entities.Deadline;
import com.grille.entities.Domain;
import com.grille.entities.Skill;
import com.grille.entities.User;
import com.grille.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.*;
import java.text.*;

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

    @Secured({"ROLE_ADMIN", "ROLE_respoModule"})
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


    @Secured({"ROLE_ADMIN", "ROLE_respoModule"})
    @RequestMapping(value = "/new_deadline_insert", method = RequestMethod.POST)
    public void deadlineinsert (Model model, String date, String promo, String nom_domain, HttpServletResponse response){
        Deadline d = new Deadline();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = new Date();
        try {
            startDate = df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Domain dom = domainRepo.findOneByName(nom_domain);

        d.setDate(startDate);
        d.setPromo(promo);
        d.setDomain(dom);
        agendarespo.save(d);
        try{
            response.sendRedirect("/agenda-responsable");
        }catch (IOException i){
            i.printStackTrace();
        }

    }


    @Secured({"ROLE_ADMIN", "ROLE_respoModule"})
    @RequestMapping(value = "/delete_deadline", method = RequestMethod.GET)
    public void delete(HttpServletResponse response, @RequestParam("deadline") int id){
        Deadline d = deadlinerepo.findById(id);
        deadlinerepo.delete(d);

        String redirectPath = "/agenda-responsable";
        try {
            response.sendRedirect(redirectPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}