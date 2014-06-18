package com.example.handycart;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ViewTreeObserver;
import android.widget.GridLayout;
import android.widget.TextView;

import android.widget.Toast;
import data.Carte;
import data.Noeud;
import data.Point;
import utils.NavigationUtil;

import java.util.ArrayList;
import java.util.List;


public class NavigationActivity extends Activity {

    private GridLayout gridLayout;
    private int width;
    private int height;
    private Carte carte;
    private Handler handler;
    private NavigationListener listener = null;
    private Boolean MyListenerIsRegistered = false;
    private String[] idRayon;
    private List<Point> points;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation);
        points = new ArrayList<Point>();
        listener = new NavigationListener();
        carte = new Carte();
        handler = new Handler();
        idRayon = getIntent().getStringArrayExtra("id Rayon");
        for(int i = 0; i < idRayon.length; i++){
            Point p = convertirIDRayonEnPoint(idRayon[i]);
            if(p != null){
                points.add(p);
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
                testAetoile(new Point(0,0), points.get(0));
            }
        });
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
                // Do something
            }
        }
    }

    private void testAetoile(Point courant, Point arrivee) {
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
                for(Point p : aetoile.getChemin()){
                    final int position = NavigationUtil.convertirPointEnPosition(p.getX(), p.getY(), carte.getNbColonnes());
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            ((TextView) gridLayout.getChildAt(position)).setCompoundDrawablesWithIntrinsicBounds(
                                    R.drawable.chemin_aetoile, 0, 0, 0);
                        }
                    });
                }
            }
        }
    }

}