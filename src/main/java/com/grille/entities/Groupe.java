package com.grille.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by alizeefaytre on 30/04/2017.
 */
@Entity
public class Groupe implements Serializable{

        @Id
        @GeneratedValue
        @Column(name = "id_group")
    private int id;

    private String promo;
    private String semester;
    private String nom;

    @ManyToMany
    @JoinTable(name = "tuteur_groupe", joinColumns = @JoinColumn(name = "id_groupe"), inverseJoinColumns = @JoinColumn(name="id_user"))
    private Set<User> listTuteur;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User client;

    @ManyToMany
    @JoinTable(name = "distributionGroup", joinColumns = @JoinColumn(name = "id_group"), inverseJoinColumns = @JoinColumn(name="id_user"))
    private Set<User> listUser = new HashSet<User>();


    public Groupe() {
    }

    public Groupe(String promo, String semester, String nom, Set<User> listUser) {
        this.promo = promo;
        this.semester = semester;
        this.nom = nom;
        this.listUser = listUser;
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

    public void setSemester(String semestre) {
        this.semester = semestre;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Set<User> getListUser() {
        return listUser;
    }

    public void setListUser(Set<User> listUser) {
        this.listUser = listUser;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Set<User> getListTuteur() {
        return listTuteur;
    }

    public void setListTuteur(Set<User> listTuteur) {
        this.listTuteur = listTuteur;
    }

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }
}
