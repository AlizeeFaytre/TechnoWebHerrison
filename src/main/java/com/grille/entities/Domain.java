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

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public Set<Skill> getListSkill() {
        return listSkill;
    }

    public void setListSkill(Set<Skill> listSkill) {
        this.listSkill = listSkill;
    }

    public Set<Grid> getListGrid() {
        return listGrid;
    }

    public void setListGrid(Set<Grid> listGrid) {
        this.listGrid = listGrid;
    }

    public Set<Deadline> getListDeadline() {
        return listDeadline;
    }

    public void setListDeadline(Set<Deadline> listDeadline) {
        this.listDeadline = listDeadline;
    }

    public Set<Grade> getListGrade() {
        return listGrade;
    }

    public void setListGrade(Set<Grade> listGrade) {
        this.listGrade = listGrade;


    public Set<Skill> getListSkill() {
        return listSkill;
    }
}
