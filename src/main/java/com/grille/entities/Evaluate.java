package com.grille.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/**
 * Created by alizeefaytre on 30/04/2017.
 */

@Entity
public class Evaluate implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "id_Evalute")
    private int id;

    private String groupObservation;

    private String individualObservation;

    private String level;

    @Temporal(TemporalType.DATE)
    private Date date;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

    @ManyToOne
    @JoinColumn(name = "id_skill")
    private Skill skill;

    public Evaluate() {
    }

    public Evaluate(String groupObservation, String individualObservation, String level) {
        this.groupObservation = groupObservation;
        this.individualObservation = individualObservation;
        this.level = level;
    }

    public int getId() {
        return id;
    }


    public String getGroupObservation() {
        return groupObservation;
    }

    public void setGroupObservation(String groupObservation) {
        this.groupObservation = groupObservation;
    }

    public String getIndividualObservation() {
        return individualObservation;
    }

    public void setIndividualObservation(String individualObservation) {
        this.individualObservation = individualObservation;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
