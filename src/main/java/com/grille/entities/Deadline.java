package com.grille.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by alizeefaytre on 30/04/2017.
 */

@Entity
public class Deadline implements Serializable {


    @Id
    @GeneratedValue
    @Column(name = "id_deadline")
    private int id;

    private String promo;

    private Date date;

    @ManyToOne
    @JoinColumn(name = "id_domain")
    private Domain domain;

    public Deadline() {
    }

    public Deadline(String promo, Date date) {
        this.promo = promo;
        this.date = date;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
