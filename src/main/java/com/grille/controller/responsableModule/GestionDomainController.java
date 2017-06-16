package com.grille.controller.responsableModule;

import com.grille.dao.DomainRepository;
import com.grille.dao.GridRepository;
import com.grille.entities.Domain;

import com.grille.entities.Grid;
import com.grille.entities.User;
import com.grille.service.UserService;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by Thomas on 09/05/2017.
 */

@Controller
public class GestionDomainController {

    @Autowired
    private DomainRepository domainrepo;

    @Autowired
    private GridRepository gridRepository;

    @Autowired
    private UserService userService;

    @Secured({"ROLE_ADMIN", "ROLE_respoModule"})
    @GetMapping("/gestion_grille")
    public String index (Model model, HttpSession session){
    	User currentUser = userService.getLogedUser(session);
    	List<Domain> listDomain = domainrepo.findAll();
        model.addAttribute("listDomain", listDomain);

        Map<String, Set<Domain>> domainByPromo = new HashMap<>();

        String currentSemester = userService.getCurrentSemester();
        String[] semester = currentSemester.split(" - ");

        if (semester[1] == "S1"){
            Integer year = Integer.parseInt(semester[0]);
            domainByPromo.put("I1", gridRepository.findByPromo(Integer.toString(year + 5)).getListDomain());
            domainByPromo.put("I2", gridRepository.findByPromo(Integer.toString(year + 4)).getListDomain());
            domainByPromo.put("A1", gridRepository.findByPromo(Integer.toString(year + 3)).getListDomain());
            domainByPromo.put("A2", gridRepository.findByPromo(Integer.toString(year + 2)).getListDomain());
            domainByPromo.put("A3", gridRepository.findByPromo(Integer.toString(year + 1)).getListDomain());
        }
        else {
            Integer year = Integer.parseInt(semester[0]);
            for (int i = 4; i > 0; i--){
                if (!(gridRepository.findByPromo(Integer.toString(year + i)) == null)){
                    switch (i){
                        case 4:
                            domainByPromo.put("I1", gridRepository.findByPromo(Integer.toString(year + 4)).getListDomain());
                            break;

                        case 3:
                            domainByPromo.put("I2", gridRepository.findByPromo(Integer.toString(year + 3)).getListDomain());
                            break;

                        case 2:
                            domainByPromo.put("A1", gridRepository.findByPromo(Integer.toString(year + 2)).getListDomain());
                            break;

                        case 1:
                            domainByPromo.put("A2", gridRepository.findByPromo(Integer.toString(year + 1)).getListDomain());
                            break;

                        default:
                            domainByPromo.put("A3", gridRepository.findByPromo(Integer.toString(year)).getListDomain());
                            break;
                    }
                }
                else {
                    switch (i){
                        case 4:
                            domainByPromo.put("I1", new HashSet<>());
                            break;

                        case 3:
                            domainByPromo.put("I2", new HashSet<>());
                            break;

                        case 2:
                            domainByPromo.put("A1", new HashSet<>());
                            break;

                        case 1:
                            domainByPromo.put("A2", new HashSet<>());
                            break;

                        default:
                            domainByPromo.put("A3", new HashSet<>());
                            break;
                    }

                }
            }

        }

        model.addAttribute("domainByPromo", domainByPromo);

        List<Domain> allDomain = domainrepo.findAll();
        model.addAttribute("allDomain", allDomain);

        model.addAttribute("currentUser", currentUser);

        return "respoModule/gestion_grille";
    }


    @Secured({"ROLE_ADMIN", "ROLE_respoModule"})
    @RequestMapping(value = "modifGrille", method = RequestMethod.GET)
    public void editGrille(HttpServletResponse response, @RequestParam(name = "promo") String promo, HttpServletRequest request){

        String postBody = request.getQueryString();

        Set<Domain> domains = new HashSet<>();

        String[] tabPost = postBody.split("&");
        for (String part:tabPost) {
            if (part.startsWith("promo")){
                promo = userService.getPromoFromClasse(promo);
            }
            else if (part.startsWith("listDomain")){
                Integer idDomain = Integer.parseInt(part.substring(11));
                domains.add(domainrepo.findById(idDomain));
            }
        }

        Grid grille = gridRepository.findByPromo(promo);

        if (grille.equals(null)){
            grille = new Grid();
            grille.setPromo(promo);
        }

        grille.setListDomain(domains);

        gridRepository.save(grille);

        try {
            response.sendRedirect("/gestion_grille");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Secured({"ROLE_ADMIN", "ROLE_respoModule"})
    @RequestMapping(value = "/delete-domain", method = RequestMethod.GET)
    public void delete(HttpServletResponse response, @RequestParam("domain") int id){
        Domain d = domainrepo.findById(id);
        domainrepo.delete(d);

        String redirectPath = "/gestion_grille";
        try {
            response.sendRedirect(redirectPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Secured({"ROLE_ADMIN", "ROLE_respoModule"})
    @RequestMapping(value = "/deleteGrille", method = RequestMethod.GET)
    public void deleteGrille(HttpServletResponse response, @RequestParam("promo") String promo, HttpServletRequest request){
        String postBody = request.getQueryString();

        String[] tabPost = postBody.split("&");
        for (String part:tabPost) {
            if (part.startsWith("promo")) {
                promo = userService.getPromoFromClasse(promo);
            }
        }

        Grid grille = gridRepository.findByPromo(promo);

        gridRepository.delete(grille.getId());

        try {
            response.sendRedirect("/gestion_grille");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
