package com.example.handycart;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.ViewTreeObserver;
import android.widget.GridLayout;
import android.widget.TextView;

import data.Carte;
import data.Noeud;
import data.Point;
import utils.NavigationUtil;


public class NavigationActivity extends Activity {

    private GridLayout gridLayout;
    private int width;
    private int height;
    private Carte carte;
    private Handler handler;
    private NavigationListener listener = null;
    private Boolean MyListenerIsRegistered = false;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation);
        listener = new NavigationListener();
        carte = new Carte();
        handler = new Handler();
        String id = getIntent().getStringExtra("id Rayon");

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
                testAetoile();
            }
        });
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

    private void testAetoile() {
        Noeud depart = new Noeud();
        Point courant = new Point(0, 0);
        depart.setParent(courant);
        Point arrivee = new Point(carte.getNbLignes()-1,0);
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