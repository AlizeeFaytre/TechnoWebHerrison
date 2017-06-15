package com.grille.controller.tuteurClient;

import com.fasterxml.jackson.core.util.InternCache;
import com.grille.dao.DomainRepository;
import com.grille.dao.GroupeRepository;
import com.grille.entities.Domain;
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
    @Autowired
    private DomainRepository domainRepository;



    @RequestMapping(value = "/dashboard-tuteur", method = RequestMethod.GET)
    public String index(Model model, HttpSession session, @RequestParam("groupe") int id) {

        //selection tous les groupe
        List<Groupe> listGroupe = groupeRepository.findAll();

        //recup le logges user
        User logedUser = userService.getLogedUser(session);
        int logedUserId = logedUser.getId();
        
        User currentUser = userService.getLogedUser(session);
    	model.addAttribute("currentUser", currentUser);


        //recuperation des groupes dont l'utilisateur logger est le tuteur
        //et recuperation des groupes dont l'utilisateur logger est le client
        ArrayList<Groupe> tuteurListGroupe = new ArrayList<>();
        ArrayList<Groupe> clientListGroupe = new ArrayList<>();
        for (Groupe g : listGroupe) {
            for (User tuteur: g.getListTuteur()) {
                if (tuteur.getId() == logedUserId) {
                    tuteurListGroupe.add(g);
                }
            }

            if (g.getClient().getId() == logedUserId) {
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


        if (tuteurListGroupe.contains(selectedGroupe) || clientListGroupe.contains(selectedGroupe)){
            model.addAttribute("selectedGroupe", selectedGroupe);
            model.addAttribute("listGroupeUsers", listGroupeUsers);
        }else {
            listGroupeUsers = new ArrayList<>();
            User videUser = new User();
            videUser.setPrenom("Sorry : Access Denied !");
            listGroupeUsers.add(videUser);
            model.addAttribute("listGroupeUsers", listGroupeUsers);

            selectedGroupe = new Groupe();
            selectedGroupe.setNom("Error");
            model.addAttribute("selectedGroupe", selectedGroupe);
        }

        String mapping = "dashboard-tuteur";
        model.addAttribute("redirection", mapping);

        List<Domain> listDomain = domainRepository.findAll();
        String firstDomainId = "";
        try {
            firstDomainId = String.valueOf(listDomain.get(0).getId());
        }catch (NullPointerException e){

        }
        model.addAttribute("domainId",firstDomainId);
        return "/tuteur-client/dashboard-tuteur";

    }

    @RequestMapping(value = "/dashboard-tuteur-recherche", method = RequestMethod.POST)
    public void collectMotCle(HttpServletResponse response, @RequestParam("groupe") int id, String motCle){
        //En cas de soumission de champ vide dans la barre de recherche on redirige vers le controller dashboard-tuteur GET
        //sinon redirection vers la page pour affichage des resultats
        String redirectPath = "/dashboard-tuteur?groupe=0";
        if (motCle != "") {
            redirectPath = "/dashboard-tuteur-resultat?groupe="+id+"&recherche="+motCle;
        }
        try {
            response.sendRedirect(redirectPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @RequestMapping(value = "/dashboard-tuteur-resultat", method = RequestMethod.GET)
    public String dashResultRecherche(Model model, HttpSession session, @RequestParam("groupe") int id,@RequestParam("recherche") String motCle) {


        //selection tous les groupe
        List<Groupe> listGroupe = groupeRepository.findAll();

        //recup le logges user
        User logedUser = userService.getLogedUser(session);
        int logedUserId = logedUser.getId();

        User currentUser = userService.getLogedUser(session);
        model.addAttribute("currentUser", currentUser);


        //list contenant les resultats de la recherche par motCle
        ArrayList<Groupe> listResultat = new ArrayList<>();
        //ajout des resultat matching avec groupe nom
        listResultat.add(groupeRepository.findByNom(motCle));
        //ajout des resultat matching avec groupe Promo
        listResultat.addAll(groupeRepository.findByPromo(motCle));
        //ajout des resultat matching avec groupe semester
        listResultat.addAll(groupeRepository.findBySemester(motCle));

        //recherche par user : nom, prenom, identifiant
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

        //recuperation des groupes dont l'utilisateur logger est le tuteur
        //et recuperation des groupes dont l'utilisateur logger est le client
        ArrayList<Groupe> tuteurListGroupe = new ArrayList<>();
        ArrayList<Groupe> clientListGroupe = new ArrayList<>();

        try {
            for (Groupe g : listResultat) {
                for (User tuteur:g.getListTuteur()) {
                    if (tuteur.getId() == logedUserId) {
                        tuteurListGroupe.add(g);
                    }
                }

                if (g.getClient().getId() == logedUserId) {
                    clientListGroupe.add(g);
                }

            }
        } catch (NullPointerException e) {
            e.printStackTrace();
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

        if (tuteurListGroupe.contains(selectedGroupe) || clientListGroupe.contains(selectedGroupe)){
            model.addAttribute("selectedGroupe", selectedGroupe);
            model.addAttribute("listGroupeUsers", listGroupeUsers);
        }else {
            listGroupeUsers = new ArrayList<>();
            User videUser = new User();
            videUser.setPrenom("Sorry : Access Denied !");
            listGroupeUsers.add(videUser);
            model.addAttribute("listGroupeUsers", listGroupeUsers);

            selectedGroupe = new Groupe();
            selectedGroupe.setNom("Error");
            model.addAttribute("selectedGroupe", selectedGroupe);
        }

        model.addAttribute("motCle",motCle);

        String mapping = "dashboard-tuteur-resultat";
        model.addAttribute("redirection", mapping);

        return "/tuteur-client/dashboard-tuteur";
    }

}