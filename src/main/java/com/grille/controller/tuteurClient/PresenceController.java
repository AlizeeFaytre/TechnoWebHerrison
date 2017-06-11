package com.grille.controller.tuteurClient;


import com.grille.dao.*;
import com.grille.entities.*;
import com.grille.service.UserService;

import javafx.scene.control.Alert;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
    @Autowired
    private AttendanceRepository attendanceRepository;

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

        //Map des groupes by promo et liste de tous les groupes dont le currentUser possede les droits
        Map<String, ArrayList<Groupe>> mapGroupeByPromo = new HashMap<>();
        ArrayList<Groupe> AllCurrentUserGroupes = new ArrayList<>();
        for (String p : listPromo) {
            ArrayList<Groupe> tempListGroupe = new ArrayList<>();
            for (Groupe g : listGroupe) {
                if (g.getPromo().equalsIgnoreCase(p)) {
                    if (g.getIdClient() == currentUser.getId() || g.getIdTuteur() == currentUser.getId()) {
                        tempListGroupe.add(g);
                        AllCurrentUserGroupes.add(g);
                    }
                }
            }
            mapGroupeByPromo.put(p, tempListGroupe);
        }
        model.addAttribute("mapGroupeByPromo", mapGroupeByPromo);


        //Recuperation de la liste des Utilisateurs du groupe selected
        //et identification du groupe selected
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

        String modaltitle = "Ajouter des présences";

        //verifier que le currentUser possede les droits sur le groupe selected
        //si oui : recup de la liste des users du groupe selected les eleves seulement
        //si non : envoie message error
        if (AllCurrentUserGroupes.contains(selectedGroupe)) {
            ArrayList<User> listGroupeEleve = new ArrayList<>();
            for (User u : listGroupeUsers) {
                if (u.getId() != selectedGroupe.getIdTuteur() && u.getId() != selectedGroupe.getIdClient()) {
                    listGroupeEleve.add(u);
                }

            }
            model.addAttribute("modalTitle", modaltitle);
            model.addAttribute("listGroupeEleve", listGroupeEleve);
        } else {
            modaltitle = "Error : Sorry Access Denied !";
            model.addAttribute("modalTitle", modaltitle);
        }


        //recuperation de la date du jour
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String dateAffichage = dt.format(date);
        model.addAttribute("date", dateAffichage);

        String mapping = "presence";
        model.addAttribute("redirection", mapping);

        return "/tuteur-client/presence-tuteur";
    }

    @RequestMapping(value = "/presence-submit", method = RequestMethod.POST)
    public void presenceSubmit(HttpServletResponse response, Model model, String motCle) {

        ArrayList<Attendance> listAttendanceByDate = attendanceRepository.findByDate(new Date());

        String[] parts = motCle.split(",");
        for (String s : parts) {
            String[] subParts = s.split("-");
            Attendance attendance = new Attendance();
            User u = userRepository.findById(Integer.parseInt(subParts[0]));
            attendance.setUser(u);
            Boolean state = false;
            if (Integer.parseInt(subParts[1]) == 1) {
                state = true;
            }
            attendance.setState(state);
            attendance.setDate(new Date());

            boolean existe = false;
            for (Attendance a : listAttendanceByDate) {
                if (a.getUser() == u) {
                    a.setState(state);
                    attendanceRepository.saveAndFlush(a);
                    existe = true;
                    break;
                }
            }
            if (!existe) {
                attendanceRepository.save(attendance);
            }
        }


        String redirectPath = "/presence?groupe=0";
        try {
            response.sendRedirect(redirectPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/presence-groupe-recherche", method = RequestMethod.POST)
    public void collectMotCle(HttpServletResponse response, @RequestParam("groupe") int id, String motCle){
        //En cas de soumission de champ vide dans la barre de recherche on redirige vers le controller dashboard-tuteur GET
        //sinon redirection vers la page pour affichage des resultats
        String redirectPath = "/presence?groupe=0";
        if (motCle != "") {
            redirectPath = "/presence-recherche-resultat?groupe="+id+"&recherche="+motCle;
        }
        try {
            response.sendRedirect(redirectPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/presence-recherche-resultat", method = RequestMethod.GET)
    public String dashResultRecherche(Model model, HttpSession session,@RequestParam("groupe") int id,@RequestParam("recherche") String motCle) {

        User currentUser = userService.getLogedUser(session);
        model.addAttribute("currentUser", currentUser);

        model.addAttribute("motCle", motCle);
        //selection du groupe rechercher
        Groupe groupeRecherche = groupeRepository.findByNom(motCle);

        //Map des groupes by promo et groupe recherche ici 1 element
        Map<String, ArrayList<Groupe>> mapGroupeByPromo = new HashMap<>();
        ArrayList<Groupe> tempListGroupe = new ArrayList<>();

        //si le currentUser possede les droits : remplissage du map
        //sinon : renvoie map vide = rien est affiche
        try {
            if (groupeRecherche.getIdClient() == currentUser.getId() || groupeRecherche.getIdTuteur() == currentUser.getId()){
                tempListGroupe.add(groupeRecherche);
                mapGroupeByPromo.put(groupeRecherche.getPromo(), tempListGroupe);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        model.addAttribute("mapGroupeByPromo", mapGroupeByPromo);

        //Utilisateur du groupe recherche
        Set<User> groupeRechercheUser = new HashSet<>();
        try {
            groupeRechercheUser = groupeRecherche.getListUser();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        ArrayList<User> listGroupeUsers = new ArrayList<>(groupeRechercheUser);

        String modaltitle = "Ajouter des présences";

        //selection des eleves only dans la list de tous les utilisateur du groupe recherche
        try {
            if (groupeRecherche.getIdClient() == currentUser.getId() || groupeRecherche.getIdTuteur() == currentUser.getId()){
                ArrayList<User> listGroupeEleve = new ArrayList<>();
                for (User u : listGroupeUsers) {
                    if (u.getId() != groupeRecherche.getIdTuteur() && u.getId() != groupeRecherche.getIdClient()) {
                        listGroupeEleve.add(u);
                    }

                }
                model.addAttribute("modalTitle", modaltitle);
                model.addAttribute("listGroupeEleve", listGroupeEleve);
            } else {
                modaltitle = "Error : Sorry Access Denied !";
                model.addAttribute("modalTitle", modaltitle);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            modaltitle = "Error : Sorry Access Denied !";
            model.addAttribute("modalTitle", modaltitle);
        }

        //recuperation de la date du jour
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String dateAffichage = dt.format(date);
        model.addAttribute("date", dateAffichage);

        String mapping = "/presence-recherche-resultat";
        model.addAttribute("redirection", mapping);

        return "/tuteur-client/presence-tuteur";
    }

}
