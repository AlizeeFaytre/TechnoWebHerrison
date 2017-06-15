package com.grille.controller.responsableModule;

import com.grille.dao.GroupeRepository;
import com.grille.dao.RoleRepository;
import com.grille.dao.UserRepository;
import com.grille.entities.Groupe;
import com.grille.entities.User;
import com.grille.service.GroupeService;
import com.grille.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Secured({"ROLE_ADMIN", "ROLE_respoModule"})
    @GetMapping("/gestionGroupes")
    public String groupeAccueil(HttpSession session, Model model, @RequestParam(name="promo")String promo, @RequestParam(name="page")Integer page){
        User currentUser = userService.getLogedUser(session);
        model.addAttribute("currentUser", currentUser);

        Page<Groupe> listGroupe = groupeRepository.findByPromo(promo, new PageRequest(page, 3));

        Map<Groupe, Set<User>> groupes = new HashMap<>();

        for (Groupe g:listGroupe) {
            groupes.put(g, g.getListUser());
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

        model.addAttribute("groupe", new Groupe());

        Set<User> listeEleve = userRepository.findByClasse(promo);

        model.addAttribute("listeEleve", listeEleve);

        Set<User> listProf = roleRepository.findByName("prof").getListUser();

        model.addAttribute("listProf", listProf);

        return "respoModule/gestion_groupes";
    }

    @Secured({"ROLE_ADMIN", "ROLE_respoModule"})
    @RequestMapping(value = "/groupeInsert", method = RequestMethod.POST)
    public void inserte(HttpServletResponse response,@RequestBody String postbody, @RequestParam(name="promo")String promo){
        System.out.println(postbody);

        Groupe groupe = new Groupe();

        String[] tab = postbody.split("&");

        Set<User> listUser = new HashSet<>();

        Set<User> listTuteur = new HashSet<>();

        for (int i= 0; i<tab.length; i++){
            if (tab[i].startsWith("studentList")){
                String userstr = tab[i].substring(12);
                User user = userRepository.findByIdentifiant(userstr);
                listUser.add(user);
            }
            else if (tab[i].startsWith("nom")) {
                String nom = tab[i].substring(4).replace('+', ' ');
                groupe.setNom(nom);
            }
            else if (tab[i].startsWith("listTuteur")){
                String userstr = tab[i].substring(11);
                User user = userRepository.findByIdentifiant(userstr);
                listTuteur.add(user);
            }
            else {
                String userstr = tab[i].substring(7);
                User user = userRepository.findByIdentifiant(userstr);
                groupe.setClient(user);
            }

        }

        groupe.setListUser(listUser);

        groupe.setListTuteur(listTuteur);

        groupe.setPromo(promo);

        groupeRepository.save(groupe);

        try{
            response.sendRedirect("/gestionGroupes?promo=" + promo + "&page=0");
        }catch (IOException i){
            i.printStackTrace();
        }
    }

    @Secured({"ROLE_ADMIN", "ROLE_respoModule"})
    @RequestMapping(value = "/deleteGroupe", method = RequestMethod.GET)
    public void deleteGroupe( @RequestParam(name="promo")String promo, @RequestParam(name = "groupeID") Integer groupeID, HttpServletResponse response){
       groupeRepository.delete(groupeID);

        try{
            response.sendRedirect("/gestionGroupes?promo=" + promo + "&page=0");
        }catch (IOException i){
            i.printStackTrace();
        }
    }
}
