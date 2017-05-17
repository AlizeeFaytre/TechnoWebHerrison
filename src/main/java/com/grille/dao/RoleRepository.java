package com.grille.dao;

import com.grille.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
<<<<<<< HEAD
 * Created by alizeefaytre on 07/05/2017.
 */
public interface RoleRepository extends JpaRepository<Role, Integer>{

    public Role findByName(String name);

}
