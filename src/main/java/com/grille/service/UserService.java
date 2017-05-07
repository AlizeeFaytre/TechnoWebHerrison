package com.grille.service;

import com.grille.entities.Evaluate;
import com.grille.entities.Groupe;
import com.grille.entities.Skill;
import com.grille.entities.User;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Set;

/**
 * Created by alizeefaytre on 07/05/2017.
 */

@Service
public class UserService {

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
}
