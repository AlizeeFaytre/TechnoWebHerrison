package com.grille.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by alizeefaytre on 30/04/2017.
 */

@Entity
public class Domain implements Serializable {

    @Id
    @GeneratedValue
    @Column(name="id_domain")
    private int id;

    private String name;

    @ManyToMany
    @JoinTable(name = "skillDomain", joinColumns = @JoinColumn(name = "id_domain"), inverseJoinColumns = @JoinColumn(name="id_skill"))
    private Set<Skill> listSkill = new HashSet<Skill>();

    @ManyToMany(mappedBy = "listDomain")
    private Set<Grid> listGrid;

    @OneToMany(mappedBy = "domain")
    private Set<Deadline> listDeadline = new HashSet<Deadline>();

    @ManyToMany(mappedBy = "listDomain")
    private Set<Grade> listGrade;

    public Domain() {
    }

    public Domain(String name) {
        this.name = name;
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
