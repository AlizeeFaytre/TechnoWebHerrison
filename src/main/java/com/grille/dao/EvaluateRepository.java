package com.grille.dao;

import com.grille.entities.Evaluate;
import com.grille.entities.Skill;
import com.grille.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by jiawei on 11/06/2017.
 */
public interface EvaluateRepository extends JpaRepository<Evaluate,Integer> {
    public Evaluate findByUserAndSkill(User user, Skill skill);
}
