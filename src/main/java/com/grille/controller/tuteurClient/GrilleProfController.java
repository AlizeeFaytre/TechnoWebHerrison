package com.grille.controller.tuteurClient;

import com.grille.dao.DomainRepository;
import com.grille.dao.EvaluateRepository;
import com.grille.dao.GroupeRepository;
import com.grille.dao.SkillRepository;
import com.grille.entities.*;
import com.grille.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by jiawei on 15/05/2017.
 */

@Controller
public class GrilleProfController {

    @Autowired
    private DomainRepository domainRepository;
    @Autowired
    private GroupeRepository groupeRepository;
    @Autowired
    private SkillRepository skillRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private EvaluateRepository evaluateRepository;

    //---------1 ---un mapping pour le visionnage qui map sur la vue grilleEleve
    //---------2 ---un mapping pour la modification (remplisage)

    @Secured({"ROLE_ADMIN", "ROLE_prof"})
    @RequestMapping(value = "/grilleprof", method = RequestMethod.GET)
    public String grilleProf(Model model, @RequestParam("groupe") int idGr, @RequestParam("domain") int idDom, @RequestParam("mode") String mode, HttpSession session) {

        User currentUser = userService.getLogedUser(session);
        model.addAttribute("currentUser", currentUser);

        //selection tous les domains
        List<Domain> listDomain = domainRepository.findAll();
        model.addAttribute("listDomain", listDomain);


        //Selectionner le domain d'url dans tous les domain
        Domain selectedDomain = domainRepository.findById(idDom);
        model.addAttribute("selectedDomain", selectedDomain);


        //Selectionner le groupe d'url parmi tous les groupes
        Groupe selectedGroupe = groupeRepository.findById(idGr);
        ArrayList<User> selectedGroupelistEleve = new ArrayList<>();
        List<Integer> listIdTuteur = new ArrayList<>();
        for (User tuteur : selectedGroupe.getListTuteur()) {
            listIdTuteur.add(tuteur.getId());
        }
        for (User u : selectedGroupe.getListUser()) {
            if (!(listIdTuteur.contains(u.getId())) && u.getId() != selectedGroupe.getClient().getId()) {
                selectedGroupelistEleve.add(u);
            }
        }
        Collections.sort(selectedGroupelistEleve, new Comparator<User>() {
            @Override
            public int compare(User u1, User u2) {
                String o1 = u1.getIdentifiant();
                String o2 = u2.getIdentifiant();
                if (o1.compareTo(o2) < 0) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });

        model.addAttribute("selectedGroupelistEleve", selectedGroupelistEleve);
        model.addAttribute("selectedGroupe", selectedGroupe);


        //Selection skill du domain selected
        Set<Skill> setSkill = new HashSet<>();
        try {
            setSkill = selectedDomain.getListSkill();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        List<Skill> selectedDomainlistSkill = new ArrayList<Skill>(setSkill);

        try {
            Collections.sort(selectedDomainlistSkill, new Comparator<Skill>() {
                @Override
                public int compare(Skill s1, Skill s2) {
                    String o1 = s1.getName();
                    String o2 = s1.getName();
                    if (o1.compareTo(o2) < 0) {
                        return -1;
                    } else {
                        return 1;
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


        //Selection des evluates pour ce groupe et les skills du domain selected
        List<Evaluate> listEvaluate = evaluateRepository.findAll();

        //map contenant les skills et une liste d'eleves et de evaluation de cet eleve pour le skill donne
        Map<SkillAndObservation, List<UserAndEval>> mapSkill = new HashMap<>();
        for (Skill s : selectedDomainlistSkill) {
            System.out.println(s.getName());
            List<UserAndEval> listUserAndEval = new ArrayList<>();
            for (User u : selectedGroupelistEleve) {
                UserAndEval userAndEval = new UserAndEval();
                userAndEval.setUser(u);
                userAndEval.setEvaluate(evaluateRepository.findByUserAndSkill(u, s));
                /*
                for (Evaluate e : listEvaluate){
                    if (e.getSkill() == s && e.getUser() == u){
                        userAndEval.setEvaluate(e);
                        break;
                    }
                }
                */
                if (userAndEval.getEvaluate() == null) {
                    Evaluate evalVide = new Evaluate();
                    evalVide.setLevel("Vide");
                    userAndEval.setEvaluate(evalVide);
                }
                listUserAndEval.add(userAndEval);
            }
            String groupObservation = "";
            String tuteurClientObservation = "";
            try {
                for (int i = 0; i < listUserAndEval.size(); i++) {
                    if (listUserAndEval.get(i).getEvaluate().getGroupObservation() != null) {
                        groupObservation = listUserAndEval.get(i).getEvaluate().getGroupObservation();
                        tuteurClientObservation = listUserAndEval.get(i).getEvaluate().getTuteurClientObservation();
                        break;
                    }
                }
            } catch (NullPointerException e) {

            }
            SkillAndObservation skillAndObservation = new SkillAndObservation();
            skillAndObservation.setSkill(s);
            skillAndObservation.setGroupObservation(groupObservation);
            skillAndObservation.setTuteurClientObservation(tuteurClientObservation);
            mapSkill.put(skillAndObservation, listUserAndEval);
        }

        Map<SkillAndObservation, List<UserAndEval>> sortMapSkill = doSort(mapSkill);
        model.addAttribute("mapSkill", sortMapSkill);

        if (mode.equalsIgnoreCase("modification")) {
            return ("tuteur-client/grillProfModif");
        } else {
            return ("tuteur-client/grilleProf");
        }

    }

    @Secured({"ROLE_ADMIN", "ROLE_prof"})
    @RequestMapping(value = "/grilleprofmodif", method = RequestMethod.POST)
    public void grilleProfmodif(HttpServletResponse response, Model model, @RequestParam("groupe") int idGr, @RequestParam("domain") int idDom, String groupeObservation, String tuteurClientObservation, String individualObservation, String motCle) {

        Domain selectedDomain = domainRepository.findById(idDom);

        Set<Skill> setSkill = new HashSet<>();
        try {
            setSkill = selectedDomain.getListSkill();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        List<Skill> sdl = new ArrayList<Skill>(setSkill);
        List<String> sl = new ArrayList<>();
        for (Skill s: sdl){
            sl.add(s.getName());
        }

        Collections.sort(sl);

        ArrayList<Skill> selectedDomainlistSkill = new ArrayList<>();
        for (String s : sl){
            for (Skill sk : sdl){
                if (sk.getName().equalsIgnoreCase(s)){
                    selectedDomainlistSkill.add(sk);
                }
            }
        }

        /*
        Collections.sort(selectedDomainlistSkill, new Comparator<Skill>() {
            @Override
            public int compare(Skill s1, Skill s2) {
                String o1 = s1.getName();
                String o2 = s1.getName();
                return o1.compareTo(o2);
            }
        });
        */



        Groupe selectedGroupe = groupeRepository.findById(idGr);
        ArrayList<User> selectedGroupelistEleve = new ArrayList<>();

        List<Integer> listIdTuteur = new ArrayList<>();
        for (User tuteur : selectedGroupe.getListTuteur()) {
            listIdTuteur.add(tuteur.getId());
        }

        for (User u : selectedGroupe.getListUser()) {
            if (!(listIdTuteur.contains(u.getId())) && u.getId() != selectedGroupe.getClient().getId()) {
                selectedGroupelistEleve.add(u);
            }
        }
        Collections.sort(selectedGroupelistEleve, new Comparator<User>() {
            @Override
            public int compare(User u1, User u2) {
                String o1 = u1.getIdentifiant();
                String o2 = u2.getIdentifiant();
                if (o1.compareTo(o2) < 0) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });

        int nombreTotalEval = selectedDomainlistSkill.size() * selectedGroupelistEleve.size();

        ArrayList<Evaluate> listEvaluate = new ArrayList<>();

        for (Skill s : selectedDomainlistSkill) {
            for (User u : selectedGroupelistEleve) {
                Evaluate e = new Evaluate();
                e.setSkill(s);
                e.setUser(u);
                e.setDate(new Date());
                listEvaluate.add(e);
            }
        }

        //group Oberservation
        ArrayList<String> listgroupeObservation = new ArrayList<>();
        String[] groupeObservationParts = groupeObservation.split(",");
        for (String s : groupeObservationParts) {
            for (User u : selectedGroupelistEleve) {
                listgroupeObservation.add(s);
            }
        }
        for (int i = 0 ; i < listgroupeObservation.size(); i++){
            listEvaluate.get(i).setGroupObservation(listgroupeObservation.get(i));
        }

        //tuteurclient Obeservation
        ArrayList<String> listTuteurClientObservation = new ArrayList<>();
        String[] tuteurClientObservationParts = tuteurClientObservation.split(",");
        for (String s : groupeObservationParts) {
            for (User u : selectedGroupelistEleve) {
                listTuteurClientObservation.add(s);
            }
        }
        for (int i = 0 ; i < listTuteurClientObservation.size(); i++){
            listEvaluate.get(i).setTuteurClientObservation(listTuteurClientObservation.get(i));
        }

        //individual obersvation
        String[] individualObservationParts = individualObservation.split(",");
        for (int i = 0 ; i < individualObservationParts.length; i++){
            listEvaluate.get(i).setIndividualObservation(individualObservationParts[i]);
        }

        //Eval
        String[] motCleParts = motCle.split(",");
        for (String s : motCleParts){
            if (!s.equalsIgnoreCase("")){
                String[] subParts = s.split("-");
                for (Evaluate e : listEvaluate){
                    if (e.getSkill().getId() == Integer.parseInt(subParts[0]) && e.getUser().getId() == Integer.parseInt(subParts[1])){
                        if (subParts.length == 3){
                            e.setLevel(subParts[2]);
                        }else
                        {
                            e.setLevel(subParts[2]+"-"+subParts[3]);
                        }

                    }
                }
            }
        }

        //ajout ou update dans bdd
        for (Evaluate e : listEvaluate){
            if (evaluateRepository.findByUserAndSkill(e.getUser(), e.getSkill()) != null){
                Evaluate eTrouve = evaluateRepository.findByUserAndSkill(e.getUser(), e.getSkill());
                try {
                    if (!e.getLevel().equalsIgnoreCase("")){
                        eTrouve.setLevel(e.getLevel());
                    }
                }catch (NullPointerException n){

                }
                eTrouve.setIndividualObservation(e.getIndividualObservation());
                eTrouve.setTuteurClientObservation(e.getTuteurClientObservation());
                eTrouve.setGroupObservation(e.getGroupObservation());
                evaluateRepository.saveAndFlush(eTrouve);
            }else{
                evaluateRepository.save(e);
            }
        }


        String redirectPath = "/grilleprof?groupe="+idGr+"&domain="+idDom+"&mode=vue";
        try {
            response.sendRedirect(redirectPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<SkillAndObservation, List<UserAndEval>> doSort(Map<SkillAndObservation, List<UserAndEval>> map) {
        Comparator<SkillAndObservation> comparator = new KeyComparator<SkillAndObservation>();
        Map<SkillAndObservation, List<UserAndEval>> sortedMap = new TreeMap<>(comparator);
        sortedMap.putAll(map);
        return sortedMap;
    }

    public class KeyComparator<SkillAndObservation> implements Comparator<GrilleProfController.SkillAndObservation> {

        @Override
        public int compare(GrilleProfController.SkillAndObservation sourceKey, GrilleProfController.SkillAndObservation targetKey) {
            String o1 = sourceKey.getSkill().getName();
            String o2 = targetKey.getSkill().getName();
            return o1.compareTo(o2);
        }

    }

    public class UserAndEval {
        private User user;
        private Evaluate evaluate;

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public Evaluate getEvaluate() {
            return evaluate;
        }

        public void setEvaluate(Evaluate evaluate) {
            this.evaluate = evaluate;
        }
    }

    public class SkillAndObservation {
        private Skill skill;
        private String groupObservation;
        private String tuteurClientObservation;

        public Skill getSkill() {
            return skill;
        }

        public void setSkill(Skill skill) {
            this.skill = skill;
        }

        public String getGroupObservation() {
            return groupObservation;
        }

        public void setGroupObservation(String groupObservation) {
            this.groupObservation = groupObservation;
        }

        public String getTuteurClientObservation() {
            return tuteurClientObservation;
        }

        public void setTuteurClientObservation(String tuteurClientObservation) {
            this.tuteurClientObservation = tuteurClientObservation;
        }
    }

}
