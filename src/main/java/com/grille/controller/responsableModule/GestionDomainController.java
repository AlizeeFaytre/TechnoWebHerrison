package com.grille.controller.responsableModule;

import com.grille.dao.DomainRepository;
import com.grille.dao.GridRepository;
import com.grille.entities.Domain;

import com.grille.entities.User;
import com.grille.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.*;

import javax.servlet.http.HttpSession;

/**
 * Created by Thomas on 09/05/2017.
 */

@Controller
public class GestionDomainController {

    @Autowired
    private DomainRepository domainrepo;
    
    @Autowired
    private UserService userService;

    @Autowired
    private GridRepository gridRepository;

    @Autowired
    private UserService userService;

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
            }

        }

        model.addAttribute("domainByPromo", domainByPromo);

        model.addAttribute("currentUser", currentUser);

        return "respoModule/gestion_grille";
    }
}
