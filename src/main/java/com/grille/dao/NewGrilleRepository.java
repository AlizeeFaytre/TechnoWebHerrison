package com.grille.dao;

import com.grille.entities.Domain;
import com.grille.entities.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Thomas on 14/05/2017.
 */
public interface NewGrilleRepository extends JpaRepository<Domain, String> {

}
