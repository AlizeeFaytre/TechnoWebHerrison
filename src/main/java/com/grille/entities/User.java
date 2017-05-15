package com.grille.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by alizeefaytre on 29/04/2017.
 */

@Entity
public class User implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "id_user")
    private int id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    private String classe;

    private String identifiant;

    @ManyToMany(mappedBy = "listUser")
    private Set<Groupe> groupes;

    @OneToMany(mappedBy = "user")
    private Set<Evaluate> listEvaluate = new HashSet<Evaluate>();

    @OneToMany(mappedBy = "user")
    private Set<Attendance> listAttendance = new HashSet<Attendance>();

    @OneToMany(mappedBy = "user")
    private Set<Grade> listGrade = new HashSet<Grade>();

    @ManyToMany
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "id_user"), inverseJoinColumns = @JoinColumn(name="id_role"))
    private Set<Role> roles = new HashSet<Role>();

    public User() {
    }

    public User(String nom, String prenom, String classe) {
        this.nom = nom;
        this.prenom = prenom;
        this.classe = classe;
    }

    public int getId() {
        return id;
    }


    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public Set<Groupe> getGroupes() {
        return groupes;
    }

    public String getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }

    public Set<Role> getRoles() {
        return roles;
    }


}
