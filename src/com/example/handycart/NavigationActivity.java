package com.example.handycart;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewTreeObserver;
import android.widget.GridLayout;
import android.widget.TextView;

import data.Carte;
import data.Noeud;
import data.Point;
import utils.NavigationUtil;

import java.util.ArrayList;


public class NavigationActivity extends Activity {

    private GridLayout gridLayout;
    private int width;
    private int height;
    private Carte carte;
    private Handler handler;
    private NavigationListener listener = null;
    private String[] idRayon;
    private ArrayList<Point> points, pointsTemp;
    private ArrayList<TextView> casesAReinitialiser;
    private int positionCarte = 0;
    private Thread aEtoileThread;
    private boolean isRunning = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation);
        points = new ArrayList<Point>();
        pointsTemp = new ArrayList<Point>();
        casesAReinitialiser = new ArrayList<TextView>();
        listener = new NavigationListener();
        carte = new Carte();
        handler = new Handler();
        idRayon = getIntent().getStringArrayExtra("id Rayon");
        for(int i = 0; i < idRayon.length; i++){
            Point p = convertirIDRayonEnPoint(idRayon[i]);
            if(p != null){
                points.add(p);
                pointsTemp.add(p);
            }
        }
        registerReceiver(listener, new IntentFilter("com.example.handycart.NavigationActivity.DO_SOME"));


        gridLayout = (GridLayout) findViewById(R.id.tablayout);
        gridLayout.setRowCount(carte.getNbLignes());
        gridLayout.setColumnCount(carte.getNbColonnes());

        ViewTreeObserver vto = gridLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressWarnings("deprecation")
            public void onGlobalLayout() {
                width = gridLayout.getWidth();
                height = gridLayout.getHeight();
                System.out.println("Width : " + width + ", Height : " + height);
                ViewTreeObserver obs = gridLayout.getViewTreeObserver();
                if(android.os.Build.VERSION.SDK_INT>=16) {
                    obs.removeOnGlobalLayoutListener(this);
                }
                else {
                    obs.removeGlobalOnLayoutListener(this);
                }

                for (int i = 0; i < carte.getModeleCarte().length(); i++) {
                    TextView tv = new TextView(NavigationActivity.this.getApplicationContext());
                    tv.setWidth(width/carte.getNbColonnes());
                    tv.setHeight(height/carte.getNbLignes());
                    if (carte.getModeleCarte().charAt(i)=='1') {
                        tv.setBackgroundColor(Color.argb(255, 255, 215, 0));
                    }
                    gridLayout.addView(tv, i);
                }
                aEtoileThread = new Thread(new AEtoileRunabble());
                aEtoileThread.start();
               // testAetoile(new Point(0,0), plusProche(new Point(0,0)),0);
            }
        });
    }

    private Point plusProche(Point depart){
        Point p = null;
        double distanceMin = Double.MAX_VALUE;
        for(Point pTemp : pointsTemp){
            double distance = NavigationUtil.distanceEuclidienne(depart, pTemp);
            if(distance < distanceMin) {
                p = pTemp;
                distanceMin = distance;
            }
        }
        if(p!=null) {
            pointsTemp.remove(p);
        }
        return p;
    }

    private Point convertirIDRayonEnPoint(String s) {
        if(s.equalsIgnoreCase("0_B")){
            return new Point(0,20);
        }if (s.equalsIgnoreCase("1_H")){
            return new Point(1,5);
        }if (s.equalsIgnoreCase("1_B")){
            return new Point(2,5);
        }if (s.equalsIgnoreCase("2_H")){
            return new Point(2,20);
        }if (s.equalsIgnoreCase("2_B")){
            return new Point(3,20);
        }if (s.equalsIgnoreCase("3_H")){
            return new Point(4,5);
        }if (s.equalsIgnoreCase("3_B")){
            return new Point(5,5);
        }if (s.equalsIgnoreCase("4_H")){
            return new Point(5,20);
        }if (s.equalsIgnoreCase("4_B")){
            return new Point(6,20);
        }if (s.equalsIgnoreCase("5_H")){
            return new Point(7,5);
        }if (s.equalsIgnoreCase("5_B")){
            return new Point(8,5);
        }if (s.equalsIgnoreCase("6_H")){
            return new Point(8,20);
        }if (s.equalsIgnoreCase("6_B")){
            return new Point(9,5);
        }if (s.equalsIgnoreCase("7_H")){
            return new Point(10,5);
        }if (s.equalsIgnoreCase("7_B")){
            return new Point(11,5);
        }if (s.equalsIgnoreCase("8_H")){
            return new Point(11,20);
        }if (s.equalsIgnoreCase("8_B")){
            return new Point(12,20);
        }if (s.equalsIgnoreCase("9_H")){
            return new Point(13,5);
        }if (s.equalsIgnoreCase("9_B")){
            return new Point(14,5);
        }if (s.equalsIgnoreCase("10_H")){
            return new Point(14,20);
        }if (s.equalsIgnoreCase("10_B")){
            return new Point(15,20);
        }if (s.equalsIgnoreCase("11_H")){
            return new Point(16,5);
        }if (s.equalsIgnoreCase("11_B")){
            return new Point(17,20);
        }if (s.equalsIgnoreCase("12_H")){
            return new Point(17,5);
        }if (s.equalsIgnoreCase("12_B")){
            return new Point(18,20);
        }if (s.equalsIgnoreCase("13_H")){
            return new Point(19,5);
        }if (s.equalsIgnoreCase("13_B")){
            return new Point(20,5);
        }if (s.equalsIgnoreCase("14_H")){
            return new Point(20,20);
        }
        return null;
    }

    protected class NavigationListener extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            // No need to check for the action unless the listener will
            // will handle more than one - let's do it anyway
            if (intent.getAction().equals("com.example.handycart.NavigationActivity.DO_SOME")) {
                    if (positionCarte >= 0) {
                        reinitialiserCases();

                    }

                    String location = intent.getStringExtra("LOC");
                    Point p = convertirIDRayonEnPoint(location);
                    if (p != null) {
                        copierVersPointsTemp();
                        positionCarte = NavigationUtil.convertirPointEnPosition(p.getX(), p.getY(), carte.getNbColonnes());
                        ((TextView) gridLayout.getChildAt(positionCarte)).setCompoundDrawablesWithIntrinsicBounds(
                                R.drawable.chemin_aetoile, 0, 0, 0);
                        casesAReinitialiser.add(((TextView) gridLayout.getChildAt(positionCarte)));
                        aEtoileThread.interrupt();
                        aEtoileThread = new Thread(new AEtoileRunabble());
                        aEtoileThread.start();
                    }
            }
        }
    }

    private void execAetoile(Point courant, final Point arrivee, final int positionCourante) {
        if(courant.getId().equalsIgnoreCase(arrivee.getId())){
            ((TextView) gridLayout.getChildAt(positionCarte)).setText("" + positionCourante);
            pointsTemp.remove(arrivee);
            execAetoile(arrivee, plusProche(arrivee), positionCourante + 1);
        }
        Noeud depart = new Noeud();
        depart.setParent(courant);
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
                final int nbElements = aetoile.getChemin().size();
                Point precedent = null;
                for(final Point p : aetoile.getChemin()){
                    final int position = NavigationUtil.convertirPointEnPosition(p.getX(), p.getY(), carte.getNbColonnes());
                    final Point finalCourant = courant;
                    final Point finalPrecedent = precedent;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            if(!p.getId().equalsIgnoreCase(arrivee.getId())){
                                if(positionCourante==0) {
                                    ((TextView) gridLayout.getChildAt(position)).setCompoundDrawablesWithIntrinsicBounds(
                                            directionImage(p, finalPrecedent), 0, 0, 0);
                                }

                            } else {
                                ((TextView) gridLayout.getChildAt(position)).setText("" + positionCourante);
                                ((TextView) gridLayout.getChildAt(position)).setTextColor(Color.BLACK);
                                ((TextView) gridLayout.getChildAt(position)).setGravity(Gravity.CENTER);
                            }
                            casesAReinitialiser.add((TextView) gridLayout.getChildAt(position));
                        }
                    });
                    precedent = p;
                }
                if (pointsTemp.size() > 0) {
                    execAetoile(arrivee, plusProche(arrivee), positionCourante + 1);
                }
            }
        }
    }

    private int directionImage(Point p, Point precedent){
        int x = p.getX() - precedent.getX();
        int y = p.getY() - precedent.getY();
        if(x == -1 && y == -1){
            return R.drawable.footsteps_rd;
        }
        if(x == 1 && y == 1){
            return R.drawable.footsteps_lu;
        }
        if(x == 1 && y == 0){
            return R.drawable.footsteps_u;
        }
        if(x == 0 && y == 1){
            return R.drawable.footsteps_l;
        }
        if(x == 0 && y == -1){
            return R.drawable.footsteps_r;
        }
        if(x == -1 && y == 0){
            return R.drawable.footsteps_d;
        }
        if(x == 1 && y == -1){
            return R.drawable.footsteps_ru;
        }
        if(x == -1 && y == 1){
            return R.drawable.footsteps_ld;
        }
        return R.drawable.chemin_aetoile;
    }

    private void reinitialiserCases(){
        for(TextView t : casesAReinitialiser){
            t.setText("");
            t.setBackgroundColor(Color.WHITE);
            t.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }
        casesAReinitialiser.clear();
    }

    private  void copierVersPointsTemp(){
        for(Point p : points){
            pointsTemp.add(p);
        }
    }
    
    class AEtoileRunabble implements Runnable {
        @Override
        public void run() {
            int[] coordonnees = NavigationUtil.convertirPositionEnPoint(positionCarte, carte.getNbColonnes());
            Point p = new Point(coordonnees[0], coordonnees[1]);
            execAetoile(p, plusProche(p), 0);
        }
    }
}