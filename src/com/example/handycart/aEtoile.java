package com.example.handycart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import data.Carte;
import data.Noeud;
import data.Point;
import utils.NavigationUtil;


public class aEtoile {
	
	private Map<String, Noeud> listeOuverte;
	private Map<String, Noeud> listeFermee;
	private List<Point> chemin;
	private Noeud depart;
	private Point arrivee;
	private Carte carte;
	
	public aEtoile(Noeud depart, Point arrivee, Carte carte){
		chemin = new ArrayList<Point>();
		listeOuverte = new HashMap<String, Noeud>();
		listeFermee = new HashMap<String, Noeud>();
		this.depart = depart;
		this.arrivee = arrivee;
		this.carte = carte;
	}
	
	public void ajouterCasesAdjacentes(Point p){
		Noeud ntmp = new Noeud();
		for(int i = p.getX()-1; i<=p.getX()+1; i++){
			if(i < 0 || i >= carte.getNbLignes()) 
				continue;
			for(int j = p.getY() -1; j<=p.getY()+1; j++){
				if(j < 0 || j >= carte.getNbColonnes()) 
					continue;
				if(i==p.getX() && j==p.getY())
					continue;
				int position = NavigationUtil.convertirPointEnPosition(i, j, carte.getNbColonnes());
				if(carte.getModeleCarte().charAt(position)=='1')
					continue;
				Point ptmp = new Point(i, j);
				if(!NavigationUtil.dejaPresentDansListe(ptmp, listeFermee)){
					double distance = NavigationUtil.distanceEuclidienne(ptmp, p);
					float coutG = listeFermee.get(p.getId()).getCoutG() + (float)distance;
					ntmp.setCoutG(coutG);
					ntmp.setCoutH((float) NavigationUtil.distanceEuclidienne(ptmp, arrivee));
					ntmp.setCoutF(ntmp.getCoutG() + ntmp.getCoutH());
					ntmp.setParent(p);
					
					if(NavigationUtil.dejaPresentDansListe(ptmp, listeOuverte)){
						if(ntmp.getCoutF() < listeOuverte.get(ptmp.getId()).getCoutF()){
							listeOuverte.remove(ptmp.getId());
							listeOuverte.put(ptmp.getId(), ntmp);
						}
					} else 
						listeOuverte.put(ptmp.getId(), ntmp);
				}
			}
		}
	}
	
	public Point trouverMeilleurNoeud(){
		Point p = null;
		float coutF = Float.MAX_VALUE;
		int i = 0;
		for(Entry<String, Noeud> entry : listeOuverte.entrySet()){
			if(i == 0){
				p = NavigationUtil.convertirIDenPoint(entry.getKey());
				i++;
			}
			if(entry.getValue().getCoutF() < coutF){
				coutF = entry.getValue().getCoutF();
				p = NavigationUtil.convertirIDenPoint(entry.getKey());
			}
		}
		return p;
	}
	
	public void ajouterListeFermee(Point p){
		String id = p.getId();
		Noeud ntmp = listeOuverte.get(id);
		listeFermee.put(id, ntmp);
		listeOuverte.remove(id);
	}
	
	public void trouverChemin() {
		Noeud ntmp = listeFermee.get(arrivee.getX()+"-"+arrivee.getY());
		Point ptmp = new Point();
		ptmp.setX(arrivee.getX());
		ptmp.setY(arrivee.getY());
		ptmp.setId(arrivee.getId());
		Point prec = new Point();
		prec.setX(ntmp.getParent().getX());
		prec.setY(ntmp.getParent().getY());
		prec.setId(ntmp.getParent().getId());
		chemin.add(ptmp);
		
		while(prec.getId() != depart.getParent().getId()){
			ptmp = new Point();
			ptmp.setX(prec.getX());
			ptmp.setY(prec.getY());
			ptmp.setId(prec.getId());
			chemin.add(ptmp);
			ntmp = listeFermee.get(ntmp.getParent().getId());
			prec.setX(ntmp.getParent().getX());
			prec.setY(ntmp.getParent().getY());
			prec.setId(ntmp.getParent().getId());
		}
	}
	
	
	
	public Map<String, Noeud> getListeOuverte() {
		return listeOuverte;
	}

	public void setListeOuverte(Map<String, Noeud> listeOuverte) {
		this.listeOuverte = listeOuverte;
	}

	public Map<String, Noeud> getListeFermee() {
		return listeFermee;
	}

	public void setListeFermee(Map<String, Noeud> listeFermee) {
		this.listeFermee = listeFermee;
	}

	public List<Point> getChemin() {
		return chemin;
	}

	public void setChemin(List<Point> chemin) {
		this.chemin = chemin;
	}
	
	

	public static void main(String[] args) {
		Noeud depart = new Noeud();
		Point courant = new Point(0, 0);
		depart.setParent(courant);
		Point arrivee = new Point(20, 0);
		Carte carte = new Carte();
		aEtoile aetoile = new aEtoile(depart, arrivee, carte);
		
		aetoile.getListeOuverte().put(courant.getId(), depart);
		aetoile.ajouterListeFermee(courant);
		aetoile.ajouterCasesAdjacentes(courant);
		while(!((courant.getX()==arrivee.getX()) && (courant.getY()==arrivee.getY())&&(!aetoile.getListeOuverte().isEmpty()))){
			courant = aetoile.trouverMeilleurNoeud();
			aetoile.ajouterListeFermee(courant);
			aetoile.ajouterCasesAdjacentes(courant);
			if((courant.getX()==arrivee.getX()) && (courant.getY()==arrivee.getY())){
				aetoile.trouverChemin();
				for(Point p : aetoile.getChemin())
					System.out.println(p.getId());
			}
		}
	}	
}
