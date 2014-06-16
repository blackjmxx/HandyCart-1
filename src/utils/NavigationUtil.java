package utils;


import java.util.Map;

import data.Noeud;
import data.Point;


public class NavigationUtil {

	public static double carreDistanceEuclidienne(Point p1, Point p2){
		double x2 = Math.pow(p1.getX() - p2.getX(), 2);
		double y2 = Math.pow(p1.getY() - p2.getY(), 2);
		return x2 + y2;
	}

	public static double distanceEuclidienne(Point p1, Point p2){
		return Math.sqrt(carreDistanceEuclidienne(p1, p2));
	}

	public static boolean dejaPresentDansListe(Point p, Map<String, Noeud> liste){
		return liste.containsKey(p.getId());
	}

	public static Point convertirIDenPoint(String id){
		String[] split = id.split("-");
		int x = Integer.parseInt(split[0]);
		int y = Integer.parseInt(split[1]);
		Point p = new Point(x,y);
		return p;
	}

	public static int convertirPointEnPosition(int x, int y, int nbLignes){
		return x*nbLignes + y;
	}

	public static int[] convertirPositionEnPoint(int position, int nbLignes){
		int x = position / nbLignes;
		int y = position % nbLignes;
		return new int[]{x,y};
	}

	

}
