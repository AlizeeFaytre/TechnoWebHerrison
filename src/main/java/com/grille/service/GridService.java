package com.grille.service;

import com.grille.dao.GridRepository;
import com.grille.entities.Grid;
import com.grille.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Created by alizeefaytre on 08/05/2017.
 */

@Service
public class GridService {

    @Autowired
    private GridRepository gridRepository;

    @Autowired
    private UserService userService;

    public Grid getGrid(User user){ //a revoir pour recuperer la grille de l'année complete

        Grid grid = gridRepository.findByPromo(user.getClasse());


        return grid;
    }
}
