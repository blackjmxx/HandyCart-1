package data;

public class Family {
	String Libelle;

	public String getLibelle() {
		return Libelle;
	}

	public void setLibelle(String libelle) {
		Libelle = libelle;
	}
	public Family(String Libelle){
		this.Libelle = Libelle;
	}
	public String toString() {

		return ""+Libelle ;
	}

}
