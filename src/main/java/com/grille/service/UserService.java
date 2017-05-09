package com.grille.service;

import com.grille.dao.UserRepository;
import com.grille.entities.Evaluate;
import com.grille.entities.Groupe;
import com.grille.entities.Skill;
import com.grille.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by alizeefaytre on 07/05/2017.
 */

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    //retourne la liste des competence ainsi que les notes associees de l'eleves, pour chaque competence
    //la notes la plus recente est recuperee
    public Set<Evaluate> getLastEvaluate(User user){
        Set<Evaluate> listEvaluate = user.getListEvaluate();
        for (Evaluate e:listEvaluate) {
            Skill skill2 = e.getSkill();
            for (Evaluate evaluate:listEvaluate) {
                Skill skill1 = evaluate.getSkill();
                if(skill1.equals(skill2)){
                    if (e.getDate().after(evaluate.getDate())){
                        listEvaluate.remove(evaluate);
                    }
                    else{
                        listEvaluate.remove(e);
                    }
                }

            }
        }
        return listEvaluate;
    }

    public Groupe getCurrentGroupe(User user){
        String currentSemester = getCurrentSemester();
        Groupe currentGroup = new Groupe();
        Set<Groupe> listGroupe = user.getGroupes();
        for (Groupe g:listGroupe) {
            if(g.getSemester().equals(currentSemester)){
                currentGroup = g;
            }
        }

        return currentGroup;
    }

    public String getCurrentSemester(){
        Calendar cal = Calendar.getInstance();
        Integer year = cal.get(Calendar.YEAR);
        String yearStr = Integer.toString(year);
        int month = cal.get(Calendar.MONTH);
        String semester = new String();
        if(month>=2 && month<=8){
            semester = "S1";
        }
        else{
            semester = "S2";
        }

        String currentSemester = yearStr + " - " + semester;

        return currentSemester;
    }

    /*public Map<String, Object> getLogedUser(HttpSession session){
        SecurityContext securityContext = (SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");
        String username = securityContext.getAuthentication().getName();

        List<String> roles = new ArrayList<>();

        for (GrantedAuthority ga:securityContext.getAuthentication().getAuthorities()) {
            roles.add(ga.getAuthority());
        }

        Map<String, Object> params = new HashMap<>();
        params.put("username", username);
        params.put("roles", roles);
        return params;


    }*/

    public User getLogedUser(HttpSession session){
        SecurityContext securityContext = (SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");
        String username = securityContext.getAuthentication().getName();

        User connectedUser = userRepository.findByIdentifiant(username);

        return connectedUser;

    }
}
