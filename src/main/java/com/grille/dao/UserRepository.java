package com.grille.dao;

import com.grille.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by alizeefaytre on 29/04/2017.
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    public User findById(Integer id);

    public User findByIdentifiant(String identifiant);
}
