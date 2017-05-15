package com.grille.dao;

import com.grille.entities.Deadline;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Thomas on 15/05/2017.
 */
public interface AgendaRespoRepository extends JpaRepository<Deadline, String> {

}
