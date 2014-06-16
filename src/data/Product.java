package data;

public class Product {

	private int id;

	private String barCode;

	private String name;

	private double price;

	private String description;

	private String brand;

	private String category;

	private String localisation;

	private String family;

	public Product(int id, String barCode, String name, double price,
			String description, String brand, String category,
			String localisation, String family) {

		this.id = id;

		this.barCode = barCode;

		this.name = name;

		this.price = price;

		this.description = description;

		this.brand = brand;

		this.category = category;

		this.localisation = localisation;

		this.family = family;
	}

	public String toString() {

		return "Produit : " + id + " " + barCode + " " + name + " " + price
				+ " " + description + " " + brand + " " + category + " "
				+ localisation + " " + family;
	}

    public int getId() {
        return id;
    }

    public String getBarCode() {
        return barCode;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getBrand() {
        return brand;
    }

    public String getCategory() {
        return category;
    }

    public String getLocalisation() {
        return localisation;
    }

    public String getFamily() {
        return family;
    }
}
