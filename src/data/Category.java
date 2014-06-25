package data;

public class Category {

	private int id;

	private String name;

	private String localisation;

	private String family;

	public Category(int id, String name, String localisation, String family) {

		this.id = id;

		this.name = name;

		this.localisation = localisation;

		this.family = family;
	}

	public String toString() {

		return "Categorie : " + id + " " + name + " " + localisation + " "
				+ family;
	}
}
