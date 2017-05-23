package com.grille.controller.tuteurClient;

import com.grille.dao.GroupeRepository;
import com.grille.entities.Groupe;
import com.grille.entities.User;
import com.grille.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.acl.Group;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
public class DashboardTuteurController {

    @Autowired
    private UserService userService;
    @Autowired
    private GroupeRepository groupeRepository;


    @RequestMapping(value = "/dashboard-tuteur", method = RequestMethod.GET)
    public String index(Model model, HttpSession session, @RequestParam("groupe") int id) {

        //selection tous les groupe
        List<Groupe> listGroupe = groupeRepository.findAll();

        //recup le logges user
        User logedUser = userService.getLogedUser(session);
        int logedUserId = logedUser.getId();


        //recuperation des groupes dont l'utilisateur logger est le tuteur
        //et recuperation des groupes dont l'utilisateur logger est le client
        ArrayList<Groupe> tuteurListGroupe = new ArrayList<>();
        ArrayList<Groupe> clientListGroupe = new ArrayList<>();
        for (Groupe g : listGroupe) {
            if (g.getIdTuteur() == logedUserId) {
                tuteurListGroupe.add(g);
            }
            if (g.getIdClient() == logedUserId) {
                clientListGroupe.add(g);
            }

        }
        model.addAttribute("tuteurListGroupe", tuteurListGroupe);
        model.addAttribute("clientListGroupe", clientListGroupe);


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
        model.addAttribute("listGroupeUsers", listGroupeUsers);
        model.addAttribute("selectedGroupe", selectedGroupe);


        return "/tuteur-client/dashboard-tuteur";

    }

    @RequestMapping(value = "/dashboard-tuteur-recherche", method = RequestMethod.POST)
    public String dashResultRecherche(Model model, HttpSession session, HttpServletResponse response, @RequestParam("groupe") int id, String motCle) {

        //En cas de soumission de champ vide dans la barre de recherche on redirige vers le controller dashboard-tuteur GET
        if (motCle == "") {
            try {
                response.sendRedirect("/dashboard-tuteur?groupe=0");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //selection tous les groupe
        List<Groupe> listGroupe = groupeRepository.findAll();

        //recup le logges user
        User logedUser = userService.getLogedUser(session);
        int logedUserId = logedUser.getId();


        //list contenant les resultats de la recherche par motCle
        ArrayList<Groupe> listResultat = new ArrayList<>();
        //ajout des resultat matching avec groupe nom
        listResultat.addAll(groupeRepository.findByNom(motCle));
        //ajout des resultat matching avec groupe Promo
        listResultat.addAll(groupeRepository.findByPromo(motCle));
        //ajout des resultat matching avec groupe semester
        listResultat.addAll(groupeRepository.findBySemester(motCle));

        //recherche par user : nom, prenom, identifiant
        //----------! à revoir ------------------
        for (Groupe g : listGroupe) {
            Set<User> setUser = g.getListUser();
            ArrayList<User> listUser = new ArrayList<>(setUser);
            for (User u : listUser) {
                if (u.getNom().equalsIgnoreCase(motCle) || u.getPrenom().equalsIgnoreCase(motCle) || u.getIdentifiant().equalsIgnoreCase(motCle) || u.getClasse().equalsIgnoreCase(motCle)) {
                    if (!listResultat.contains(g)){
                        listResultat.add(g);
                        break;
                    }
                }
            }
        }
        //----------------------------------------

        //recuperation des groupes dont l'utilisateur logger est le tuteur
        //et recuperation des groupes dont l'utilisateur logger est le client
        ArrayList<Groupe> tuteurListGroupe = new ArrayList<>();
        ArrayList<Groupe> clientListGroupe = new ArrayList<>();
        for (Groupe g : listResultat) {
            if (g.getIdTuteur() == logedUserId) {
                tuteurListGroupe.add(g);
            }
            if (g.getIdClient() == logedUserId) {
                clientListGroupe.add(g);
            }

        }
        model.addAttribute("tuteurListGroupe", tuteurListGroupe);
        model.addAttribute("clientListGroupe", clientListGroupe);


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
        model.addAttribute("listGroupeUsers", listGroupeUsers);
        model.addAttribute("selectedGroupe", selectedGroupe);


        return "/tuteur-client/dashboard-tuteur";
    }

}