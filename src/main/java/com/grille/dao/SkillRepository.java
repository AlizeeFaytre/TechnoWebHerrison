package com.grille.dao;

import com.grille.entities.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

/**
<<<<<<< HEAD
 * Created by jiawei on 16/05/2017.
 */
public interface SkillRepository extends JpaRepository<Skill, Integer> {
    public Skill findByName(String motCle);
}
