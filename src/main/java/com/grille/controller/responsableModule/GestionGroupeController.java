package com.grille.controller.responsableModule;

import com.grille.dao.GroupeRepository;
import com.grille.entities.Groupe;
import com.grille.entities.User;
import com.grille.service.GroupeService;
import com.grille.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by alizeefaytre on 11/06/2017.
 */

@Controller
public class GestionGroupeController {

    @Autowired
    private UserService userService;

    @Autowired
    private GroupeRepository groupeRepository;

    @Autowired
    private GroupeService groupeService;

    @GetMapping("/gestionGroupes")
    public String groupeAccueil(HttpSession session, Model model, @RequestParam(name="promo")String promo, @RequestParam(name="page")Integer page){
        User currentUser = userService.getLogedUser(session);
        model.addAttribute("currentUser", currentUser);

        Page<Groupe> listGroupe = groupeRepository.findByPromo(promo, new PageRequest(page, 3));

        Map<String, Set<User>> groupes = new HashMap<>();

        for (Groupe g:listGroupe) {
            groupes.put(g.getNom(), groupeService.getStudent(g));
        }

        model.addAttribute("listeGroupe", groupes);

        List<Groupe> allGroupe = groupeRepository.findAll();

        Integer totalPage = 0;
        if (allGroupe.size()%3 != 0) {
            totalPage = (allGroupe.size()/3) + 1;
        }
        else {
            totalPage = (allGroupe.size()/3);
        }

        Integer[] nbPage = new Integer[totalPage-1];

        for (int i =0; i<totalPage-1; i++){
            nbPage[i] = i ;
        }


        model.addAttribute("totalPage", nbPage);
        model.addAttribute("actualPage", page);

        Map<String, String> listPromo = new HashMap<>();
        String currentSemester = userService.getCurrentSemester();
        String[] semester = currentSemester.split(" - ");

        if (semester[1] == "S1"){
            Integer year = Integer.parseInt(semester[0]);
            listPromo.put("I1", Integer.toString(year + 5));
            listPromo.put("I2", Integer.toString(year + 4));
            listPromo.put("A1", Integer.toString(year + 3));
            listPromo.put("A2", Integer.toString(year + 2));
            listPromo.put("A3", Integer.toString(year + 1));
        }
        else {
            Integer year = Integer.parseInt(semester[0]);
            listPromo.put("I1", Integer.toString(year + 4));
            listPromo.put("I2", Integer.toString(year + 3));
            listPromo.put("A1", Integer.toString(year + 2));
            listPromo.put("A2", Integer.toString(year + 1));
            listPromo.put("A3", Integer.toString(year + 0));

        }

        model.addAttribute("listPromo", listPromo);
        model.addAttribute("promo", promo);

        return "respoModule/gestion_groupes";
    }
}
