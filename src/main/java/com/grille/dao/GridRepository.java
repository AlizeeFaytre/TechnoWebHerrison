package com.grille.dao;

import com.grille.entities.Grid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

/**
 * Created by alizeefaytre on 08/05/2017.
 */
public interface GridRepository extends JpaRepository<Grid, Integer> {

    public Set<Grid> findByPromo(String promo);
}
