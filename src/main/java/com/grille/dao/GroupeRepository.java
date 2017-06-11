package com.grille.dao;

import com.grille.entities.Groupe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

/**
 * Created by jiawei on 09/05/2017.
 */
public interface GroupeRepository extends JpaRepository<Groupe, Integer> {

    public ArrayList<Groupe> findByPromo(String promo);
    public ArrayList<Groupe> findBySemester(String semester);
    public ArrayList<Groupe> findByNom(String nom);
    public Page<Groupe> findByPromo(String promo, Pageable pageable);

}
