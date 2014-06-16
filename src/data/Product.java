package data;

public class Product {

	private String barCode;

	private String libelle;

	private String marque;

	private double prix;

	public Product(String barCode, String libelle, String marque, double prix) {

		this.barCode = barCode;

		this.libelle = libelle;

		this.marque = marque;

		this.prix = prix;
	}

	public String toString() {

		return "Produit : " + barCode + " " + libelle + " " + marque + " "
				+ prix;
	}
}
