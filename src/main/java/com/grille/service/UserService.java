package com.grille.service;

import com.grille.dao.UserRepository;
import com.grille.entities.*;
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
                if(!evaluate.equals(e)){
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
            semester = "S2";
        }
        else{
            semester = "S1";
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

    public String getGrade(User user, Domain domain){
        Set<Grade> gradeSet = user.getListGrade();
        String currentSemester = getCurrentSemester();

        double note = new Double(0);

        for (Grade grade:gradeSet) {
            if(grade.getListDomain().contains(domain) && grade.getListDomain().size() == 1 && grade.getSemester().equals(currentSemester)){
                note = grade.getGrade();
            }
        }

        return Double.toString(note);
    }

    public String getPromoFromClasse(String promo){
        String currentSemester = getCurrentSemester();
        String semester = currentSemester.split(" - ")[1];
        Integer year = Integer.parseInt(currentSemester.split(" - ")[0]);
        if (semester.equals("S1")){
            switch (promo){
                case "I1":
                    promo = Integer.toString(year + 5);
                    break;
                case "I2":
                    promo = Integer.toString(year + 4);
                    break;
                case "A1":
                    promo = Integer.toString(year + 3);
                    break;
                case "A2":
                    promo = Integer.toString(year + 2);
                    break;
                case "A3":
                    promo = Integer.toString(year + 1);
                    break;
            }
        }
        else {
            switch (promo){
                case "I1":
                    promo = Integer.toString(year + 4);
                    break;
                case "I2":
                    promo = Integer.toString(year + 3);
                    break;
                case "A1":
                    promo = Integer.toString(year + 2);
                    break;
                case "A2":
                    promo = Integer.toString(year + 1);
                    break;
                case "A3":
                    promo = Integer.toString(year);
                    break;
            }
        }

        return promo;

    }
}
