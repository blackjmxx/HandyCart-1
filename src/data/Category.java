package data;

public class Category {

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocalisation() {
        return localisation;
    }

    public String getFamily() {
        return family;
    }

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

        return "Catégorie : " + id + " " + name + " " + localisation + " "
                + family;
    }
}
