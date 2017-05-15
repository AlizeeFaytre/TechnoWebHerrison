package com.grille.dao;

import com.grille.entities.Domain;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by alizeefaytre on 14/05/2017.
 */
public interface DomainRepository extends JpaRepository<Domain, Integer> {

    public Domain findOneByName(String name);
}
