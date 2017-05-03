package com.grille.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * Created by alizeefaytre on 02/05/2017.
 */

@Entity
public class Role implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "id_role")
    private int id;

    private String name;

    @ManyToMany(mappedBy = "roles")
    private Set<User> listUser;

    public Role() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
