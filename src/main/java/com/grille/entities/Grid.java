package com.grille.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by alizeefaytre on 30/04/2017.
 */

@Entity
public class Grid implements Serializable{

    @Id
    @GeneratedValue
    @Column(name="id_grid")
    private int id;

    private String promo;

    private String semester;

    @ManyToMany
    @JoinTable(name = "gridDomain", joinColumns = @JoinColumn(name = "id_grid"), inverseJoinColumns = @JoinColumn(name="id_domain"))
    private Set<Domain> listDomain = new HashSet<Domain>();

    public Grid() {
    }

    public Grid(String promo) {
        this.promo = promo;
    }

    public int getId() {
        return id;
    }


    public String getPromo() {
        return promo;
    }

    public void setPromo(String promo) {
        this.promo = promo;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }
}
