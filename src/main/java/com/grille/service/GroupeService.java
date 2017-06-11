package com.grille.service;

import com.grille.dao.RoleRepository;
import com.grille.entities.Groupe;
import com.grille.entities.Role;
import com.grille.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by alizeefaytre on 11/06/2017.
 */

@Service
public class GroupeService {

    @Autowired
    private RoleRepository roleRepository;

    public Set<User> getStudent(Groupe g){
        Set<User> users = g.getListUser();
        Set<User> student = new HashSet<>();
        for (User user:users) {
            Set<Role> roles = user.getRoles();
            if (roles.contains(roleRepository.findByName("eleve"))){
                student.add(user);
            }
        }

        return student;
    }
}
