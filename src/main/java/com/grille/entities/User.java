package com.grille.entities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

/**
 * Created by alizeefaytre on 29/04/2017.
 */

@Entity
public class User implements UserDetails {

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

    public void setGroupes(Set<Groupe> groupes) {
        this.groupes = groupes;
    }

    public Set<Evaluate> getListEvaluate() {
        return listEvaluate;
    }

    public void setListEvaluate(Set<Evaluate> listEvaluate) {
        this.listEvaluate = listEvaluate;
    }

    public Set<Attendance> getListAttendance() {
        return listAttendance;
    }

    public void setListAttendance(Set<Attendance> listAttendance) {
        this.listAttendance = listAttendance;
    }

    public Set<Grade> getListGrade() {
        return listGrade;
    }

    public void setListGrade(Set<Grade> listGrade) {
        this.listGrade = listGrade;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role:roles) {
            authorities.add(new SimpleGrantedAuthority("ELEVE"));

        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.identifiant;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
