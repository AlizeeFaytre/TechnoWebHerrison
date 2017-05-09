package com.grille.dao;

import com.grille.entities.Domain;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Thomas on 09/05/2017.
 */
public interface DomainRepository extends JpaRepository<Domain, String> {


}
