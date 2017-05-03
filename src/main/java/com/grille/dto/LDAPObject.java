package com.grille.dto;

import java.io.Serializable;

public class LDAPObject implements Serializable {

	private static final long serialVersionUID = 1L;
	public String nom;
	public String nomFamille;
	public String prenom;
	private String employeeType; 
	private String employeeNumber;
	public String login;
	public String password;
	public String mail;
	public String promo;
	
	public LDAPObject(String login, String password, String nom, String nomFamille, String prenom, String type, String numero, String mail, String promo) {
		this.nom = nom;
		this.nomFamille = nomFamille;
		this.prenom = prenom;
		this.employeeType = type;
		this.employeeNumber = numero;
		this.login = login;
		this.password = password;
		this.mail = mail;
		this.promo = promo;
	}
	
	public LDAPObject() {
		super();
	}

	public String getNom()
	{
		return nom;
	}

	public void setNom(String nom)
	{
		this.nom = nom;
	}

	public String getLogin()
	{
		return login;
	}

	public String getType()
	{
		return employeeType;
	}

	public String getNumber()
	{
		return employeeNumber;
	}

	public String getMail() {
		return mail;
	}

	public String getPassword() {
		return password;
	}

	public String getPromo() {
		return promo;
	}

	public void setPromo(String promo) {
		this.promo = promo;
	}

	public String toString() {
		return "login = " + login + " nom = " + nom + " type = " + employeeType + " id = " + employeeNumber;
 	}

	public String getNomFamille() {
		return nomFamille;
	}

	public void setNomFamille(String nomFamille) {
		this.nomFamille = nomFamille;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

}
