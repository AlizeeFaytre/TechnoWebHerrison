package com.grille.entities;

import org.springframework.data.jpa.repository.*;

import javax.persistence.*;
import javax.persistence.Temporal;
import java.io.Serializable;
import java.util.Date;


/**
 * Created by alizeefaytre on 30/04/2017.
 */

@Entity
public class Attendance implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "id_attendance")
    private int id;

    @Temporal(TemporalType.DATE)
    private Date date;

    private boolean state;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

    public Attendance() {
    }

    public Attendance(Date date, boolean state) {
        this.date = date;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
