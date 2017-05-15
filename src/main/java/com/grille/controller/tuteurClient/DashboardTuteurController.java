package com.grille.controller.tuteurClient;

import com.grille.dao.GroupeRepository;
import com.grille.entities.Groupe;
import com.grille.entities.User;
import com.grille.service.UserRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
public class DashboardTuteurController {

    @Autowired
    private UserRestService userRestService;
    @Autowired
    private GroupeRepository groupeRepository;

    @GetMapping("/dashboard-tuteur")
    public String index (Model model, HttpSession session){

        //selection tous les groupe
        List<Groupe> listGroupe = groupeRepository.findAll();

        Map<String, Object> logedUser = userRestService.getLogedUser(session);
        String userName = logedUser.get("username").toString();

        //--A suivre----------------------------------------------------------------------------------
        //Suggestion : ajouter un champ tuteur et un champ client dans la tab des groupe
        for (Groupe g : listGroupe){
            Set<User> setUser = g.getListUser();
            ArrayList<User> listUser = new ArrayList<User>(setUser);
            for (User u : listUser){

            }
        }
        //-------------------------------------------------------------------------------------


        ArrayList<ArrayList<User>> listListUser = new ArrayList<ArrayList<User>>();
        for (Groupe g : listGroupe){
            Set<User> setUser = g.getListUser();
            ArrayList<User> listUser = new ArrayList<User>(setUser);
            listListUser.add(listUser);
        }


        model.addAttribute("listGroupe",listGroupe);
        model.addAttribute("listListUser", listListUser);


        return "tuteur-client/dashboard-tuteur";

    }

}