package com.grille.controller.eleve;

import com.grille.dao.RoleRepository;
import com.grille.dao.UserRepository;
import com.grille.entities.*;
import com.grille.service.GridService;
import com.grille.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by alizeefaytre on 07/05/2017.
 */
@Controller
public class grilleEleveController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private GridService gridService;

    @GetMapping("/grille")
    public String grille(@AuthenticationPrincipal UserDetails userDetails, Model model){
        User currentUser = userRepository.findByIdentifiant(userDetails.getUsername());
        Groupe group = userService.getCurrentGroupe(currentUser);
        Set<User> listUser = group.getListUser();

        Map<User, Set<Evaluate>> listStudentEvaluation = new HashMap<>();

        for (User user:listUser) {
            Set<Role> listRoles = user.getRoles();
            if(listRoles.contains(roleRepository.findByName("ROLE_STUDENT"))){
                listStudentEvaluation.put(user, userService.getLastEvaluate(user));
            }
        }

        model.addAttribute("listStudentEvaluation", listStudentEvaluation);

        Grid studentGrid = gridService.getGrid(currentUser);

        model.addAttribute("studentGrid", studentGrid);

        return "eleves/grilleEleve";
    }
}
