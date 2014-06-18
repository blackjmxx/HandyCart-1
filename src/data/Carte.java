package data;

public class Carte {
	
	private String modeleCarte;
	private int nbLignes;
	private int nbColonnes;
	
	public Carte() {
		modeleCarte = new String();
		chargerCarte("");
	}
	
	private void chargerCarte(String nomCarte){
		//TODO chargement dynamique de la carte
		modeleCarte = 		 "011111111111"+"000"+"000000000000" +
					   		 "000000000000"+"000"+"111111111110" +
					   		 "000000000000"+"000"+"000000000000" +
					   		 "011111111111"+"000"+"000000000000" +
					   		 "000000000000"+"000"+"111111111110" +
					   		 "000000000000"+"000"+"000000000000" +
					   		 "011111111111"+"000"+"000000000000" +
					   		 "000000000000"+"000"+"111111111110" +
					   		 "000000000000"+"000"+"000000000000" +
					   		 "011111111111"+"000"+"000000000000" +
					   		 "000000000000"+"000"+"111111111110" +
					   		 "000000000000"+"000"+"000000000000" +
					   		 "011111111111"+"000"+"000000000000" +
					   		 "000000000000"+"000"+"111111111110" +
					   		 "000000000000"+"000"+"000000000000" +
					   	     "011111111111"+"000"+"000000000000" +
					   		 "000000000000"+"000"+"111111111110" +
					   		 "000000000000"+"000"+"000000000000" +
					   	     "011111111111"+"000"+"000000000000" +
					   		 "000000000000"+"000"+"111111111110" +
					   		 "000000000000"+"000"+"000000000000";
		nbLignes = 21;
		nbColonnes = 27;
	}

	public String getModeleCarte() {
		return modeleCarte;
	}

	public void setModeleCarte(String modeleCarte) {
		this.modeleCarte = modeleCarte;
	}

	public int getNbLignes() {
		return nbLignes;
	}

	public void setNbLignes(int nbLignes) {
		this.nbLignes = nbLignes;
	}

	public int getNbColonnes() {
		return nbColonnes;
	}

	public void setNbColonnes(int nbColonnes) {
		this.nbColonnes = nbColonnes;
	}
	
	

}
