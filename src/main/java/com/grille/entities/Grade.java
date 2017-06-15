package com.grille.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by alizeefaytre on 30/04/2017.
 */

@Entity
public class Grade implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "id_grade")
    private int id;

    private double grade;

    private String semester;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

    @ManyToMany
    @JoinTable(name = "gradeDomain", joinColumns = @JoinColumn(name = "id_grade"), inverseJoinColumns = @JoinColumn(name="id_domain"))
    private Set<Domain> listDomain = new HashSet<Domain>();

    public Grade() {
    }

    public Grade(double grade, String semester) {
        this.grade = grade;
        this.semester = semester;
    }

    public int getId() {
        return id;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Domain> getListDomain() {
        return listDomain;
    }

    public void setListDomain(Set<Domain> listDomain) {
        this.listDomain = listDomain;
    }
}
