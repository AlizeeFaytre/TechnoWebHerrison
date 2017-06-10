package com.grille.controller.tuteurClient;


import com.grille.dao.DomainRepository;
import com.grille.dao.GroupeRepository;
import com.grille.dao.RoleRepository;
import com.grille.dao.UserRepository;
import com.grille.entities.Domain;
import com.grille.entities.Groupe;
import com.grille.entities.Role;
import com.grille.entities.User;
import com.grille.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.*;


/**
 * Created by jiawei on 09/05/2017.
 */

@Controller
public class PresenceController {

    @Autowired
    private GroupeRepository groupeRepository;
    @Autowired
    private DomainRepository domainRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/presence", method = RequestMethod.GET)
    public String presence(Model model, @RequestParam("groupe") int id, HttpSession session) {

        User currentUser = userService.getLogedUser(session);
        model.addAttribute("currentUser", currentUser);
        //selection tous les groupe
        List<Groupe> listGroupe = groupeRepository.findAll();

        //Selection des differents promo
        Set<String> setPromo = new HashSet<>();
        for (Groupe g : listGroupe) {
            setPromo.add(g.getPromo());
        }
        //Promo sorting by ordre decroissant
        List<String> listPromo = new ArrayList(setPromo);
        try {
            Collections.sort(listPromo, new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    int i1 = Integer.parseInt(o1);
                    int i2 = Integer.parseInt(o2);
                    if (i1 > i2) {
                        return -1;
                    } else {
                        return 1;
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("listPromo", listPromo);

        //Liste des groupes by promo et groupe selectionner dans url
        Map<String, ArrayList<Groupe>> mapGroupeByPromo = new HashMap<>();
        for (String p : setPromo) {
            ArrayList<Groupe> tempListGroupe = new ArrayList<>();
            for (Groupe g : listGroupe) {
                if (g.getPromo().equalsIgnoreCase(p)) {
                    if (g.getIdClient() == currentUser.getId() || g.getIdTuteur() == currentUser.getId()) {
                        tempListGroupe.add(g);
                    }
                }
            }
            mapGroupeByPromo.put(p, tempListGroupe);
        }
        model.addAttribute("mapGroupeByPromo", mapGroupeByPromo);


        //Utilisateur du groupe selected
        ArrayList<User> listGroupeUsers = new ArrayList<>();
        Groupe selectedGroupe = new Groupe();
        for (Groupe g : listGroupe) {
            if (g.getId() == id) {
                selectedGroupe = g;
                Set<User> groupeUsers = g.getListUser();
                listGroupeUsers = new ArrayList<>(groupeUsers);
                break;
            }
        }

        ArrayList<User> listGroupeEleve = new ArrayList<>();
        for (User u : listGroupeUsers){
            if (u.getId() != selectedGroupe.getIdTuteur() && u.getId() != selectedGroupe.getIdClient()){
                listGroupeEleve.add(u);
            }

        }
        model.addAttribute("listGroupeEleve", listGroupeEleve);

        String mapping = "presence";
        model.addAttribute("redirection", mapping);




        /* Affichage data test
    for (ArrayList<Groupe> l : mapGroupeByPromo){
        System.out.println();
        for (Groupe g : l){
            System.out.print(g.getNom());
        }
    }

    System.out.println("-----------------------------------------------");
        System.out.println(selectedGroupe.getNom());
        for (User u : listGroupeUsers){
            System.out.print(u.getNom());
        }
        System.out.println();
        System.out.println("-----------------------------------------------");

        System.out.println("-----------------------------------------------");
        System.out.println(selectedGroupe.getNom());
        for (User u : listGroupeEleve){
            System.out.print(u.getNom());
        }
        System.out.println();
        System.out.println("-----------------------------------------------");
    */
        return "/tuteur-client/presence-tuteur";
    }

    @RequestMapping(value = "/presence-submit", method = RequestMethod.POST)
    public void presenceSubmit(HttpServletResponse response,Model model, String motCle){

        System.out.println(motCle);

        //10/06/17 -------- A faire : Exploitation des data motCle (separation en substring par ",") et insertion dans bdd

    }


}
