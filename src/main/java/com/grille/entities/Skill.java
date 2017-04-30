package com.grille.entities;


import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by alizeefaytre on 30/04/2017.
 */

@Entity
public class Skill implements Serializable{

    @Id
    @GeneratedValue
    @Column(name = "id_skill")
    private int id;

    private String name;
    private double ponderation;
    private String description;
    private String levelMin;

    @ManyToMany(mappedBy = "listSkill")
    private Set<Domain> listDomain;

    @OneToMany(mappedBy = "skill")
    private Set<Evaluate> listEvaluate = new HashSet<Evaluate>();

    public Skill() {
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

    public double getPonderation() {
        return ponderation;
    }

    public void setPonderation(double ponderation) {
        this.ponderation = ponderation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLevelMin() {
        return levelMin;
    }

    public void setLevelMin(String levelMin) {
        this.levelMin = levelMin;
    }
}
