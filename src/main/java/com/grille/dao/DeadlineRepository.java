package com.grille.dao;

import com.grille.entities.Deadline;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

/**
 * Created by Thomas on 08/06/2017.
 */
public interface DeadlineRepository extends JpaRepository<Deadline, Integer> {
    public ArrayList<Deadline> findByPromo(String promo);
}
