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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


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
    public String presence(Model model, @RequestParam("groupe") int id, HttpSession session){

        /*

        //selection tous les domains
        List<Domain> listDomain = domainRepository.findAll();

        //selection tous les groupe
        List<Groupe> listGroupe = groupeRepository.findAll();

        //selection tous les user
        List<User> listUser = userRepository.findAll();

        //selection tous les roles
        List<Role> listRole = roleRepository.findAll();

        //Utilisateur du groupe selected
        ArrayList<User> listGroupeUsers = new ArrayList<User>();
        for (Groupe g : listGroupe){
            if (g.getId() == id){
                Set<User> groupeUsers = g.getListUser();
                listGroupeUsers = new ArrayList<User>(groupeUsers);
                break;
            }
        }

        ArrayList<User> groupeEleve = new ArrayList<User>();
        for(User u : listGroupeUsers) {
            Set<Role> uRoles = u.getRoles();
            List<Role> list = new ArrayList<>(uRoles);
            if (list.get(0).getName().equalsIgnoreCase("Eleve")){
                groupeEleve.add(u);
            }
        }
        model.addAttribute("groupeEleve", groupeEleve);
        model.addAttribute("listGroupe", listGroupe);
        model.addAttribute("listDomain", listDomain);
        */
    	User currentUser = userService.getLogedUser(session);
    	model.addAttribute("currentUser", currentUser);
        //selection tous les groupe
        List<Groupe> listGroupe = groupeRepository.findAll();

        //Selection des differents promo
        Set<String> setPromo = new HashSet<>();
        for (Groupe g : listGroupe){
            setPromo.add(g.getPromo());
        }

        model.addAttribute("setPromo", setPromo);

        //Liste des groupes by promo
        ArrayList<ArrayList<Groupe>> listGroupeByPromo = new ArrayList<>();
        for (String p : setPromo){
            ArrayList<Groupe> tempListGroupe = new ArrayList<>();
            for (Groupe g : listGroupe){
                if (g.getPromo().equalsIgnoreCase(p)){
                    tempListGroupe.add(g);
                }
            }
            listGroupeByPromo.add(tempListGroupe);
        }
        model.addAttribute("listGroupeByPromo", listGroupeByPromo);


        return "/tuteur-client/presence-tuteur";
    }

}
