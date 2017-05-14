package com.grille.dto;

import java.io.Serializable;

/**
 * Created by alizeefaytre on 13/05/2017.
 */
public class LineGrid implements Serializable{

    private String competenceName;
    private double competenceWeight;
    private String competenceEvaluate;
    private String individualObservation;
    private String userName;

    public LineGrid() {
    }

    public LineGrid(String competenceName, double competenceWeight, String competenceEvaluate, String individualObservation, String userName) {
        this.competenceName = competenceName;
        this.competenceWeight = competenceWeight;
        this.competenceEvaluate = competenceEvaluate;
        this.individualObservation = individualObservation;
        this.userName = userName;
    }

    public String getCompetenceName() {
        return competenceName;
    }

    public void setCompetenceName(String competenceName) {
        this.competenceName = competenceName;
    }

    public double getCompetenceWeight() {
        return competenceWeight;
    }

    public void setCompetenceWeight(double competenceWeight) {
        this.competenceWeight = competenceWeight;
    }

    public String getCompetenceEvaluate() {
        return competenceEvaluate;
    }

    public void setCompetenceEvaluate(String competenceEvaluate) {
        this.competenceEvaluate = competenceEvaluate;
    }

    public String getIndividualObservation() {
        return individualObservation;
    }

    public void setIndividualObservation(String individualObservation) {
        this.individualObservation = individualObservation;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
