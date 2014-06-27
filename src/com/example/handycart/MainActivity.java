    package com.example.handycart;

    import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import dao.DatabaseAdapter;
import data.Product;

import static com.example.handycart.Constant.PRICE;
import static com.example.handycart.Constant.TOTAL;



    public class MainActivity extends TabActivity {

        private String[] parts;
        private ArrayList<HashMap> list = new ArrayList<HashMap>();
        private ArrayList<HashMap> listTotal;
        double RealPrice = 0;
        ArrayList<ListeCourses> results = new ArrayList<ListeCourses>();
        ArrayList<ListeCourses> liste_achat;
        ArrayList<Promotions> resultsPromo = new ArrayList<Promotions>();
        ArrayList<Promotions> liste_promo= new ArrayList<Promotions>();



        private ArrayList<HashMap> listPromo;
        public ExtractFunctions extract = new ExtractFunctions();
        private ListView maListViewPerso;
        private TextView t = null;
        public static Context mContext;
        public static final int CODE_RETOUR = 7;
        final Messenger mMessenger = new Messenger(new IncomingHandler());
        DatabaseAdapter databaseAdapter;
        /**
         * Messenger used for communicating with service.
         */
        Messenger mService = null;
        boolean mServiceConnected = false;

        ListView lview;
        ListView lviewTotal;


        //OnCreate
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //les intent
            MainActivityIntent mainIntent= new MainActivityIntent();

            registerReceiver(mainIntent, new IntentFilter("com.example.handycart.mainIntent"));

            bindService(new Intent(this, BluetoothService.class), mConn, Context.BIND_AUTO_CREATE);
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
            mContext = getBaseContext();
            setContentView(R.layout.main);
            Intent intent = getIntent();
            databaseAdapter = DatabaseAdapter
                    .getInstanceOfDatabaseAdapter(this);
            String[] idRayon = null;
            if (null != intent) {
                String headline = intent.getStringExtra("data");
                t = (TextView) findViewById(R.id.NameCli);
                parts = headline.split(":");
                t.append("BIENVENUE: " + parts[0]);


                //appeller la fonction pour obtenir la liste des porduits

                String[] part2 = parts[1].split(",");
                idRayon = new String[part2.length];
                for (int i = 0; i < part2.length; i++) {
                    String[] parts3 = part2[i].split("=");
                    Product product = databaseAdapter.getProductByID(Integer.parseInt(parts3[0]));
                    idRayon[i] = i + "_B";
                    liste_achat = AddList(product.getName(), parts3[1], String.valueOf(product.getPrice()));
                }

            }


            /// la gestion et l'initialisation des onglets -- debut
            Resources ressources = getResources();
            TabHost tabHost = getTabHost();

            // Android tab
            Intent intentAndroid = new Intent().setClass(this, NavigationActivity.class);
            intentAndroid.putExtra("id Rayon", idRayon);
            TabSpec tabSpecAndroid = tabHost
                    .newTabSpec("Android")
                    .setIndicator("Navigation")
                    .setContent(intentAndroid);

            // Apple tab
            Intent intentApple = new Intent().setClass(this, SearchActivity.class);
            TabSpec tabSpecApple = tabHost
                    .newTabSpec("Apple")
                    .setIndicator("Recherche de produit")
                    .setContent(intentApple);


            Intent intentAssistance = new Intent().setClass(this, AssistanceActivity.class);
            TabSpec tabSpecAssistance = tabHost
                    .newTabSpec("Windows")
                    .setIndicator("Assistance")
                    .setContent(intentAssistance);

            tabHost.addTab(tabSpecAndroid);
            tabHost.addTab(tabSpecApple);
            tabHost.addTab(tabSpecAssistance);
            /// la gestion et l'initialisation des onglets -- fin


            //		TextView text = (TextView)findViewById(R.id.textview5);
            //		text.setText(" FrameLayout Promotions!!!");

            //set Windows tab as default (zero based)
            tabHost.setCurrentTab(0);

            maListViewPerso = (ListView) findViewById(R.id.listview3);
            //Cr?ation de la ArrayList qui nous permettra de remplire la listView
            ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> map = new HashMap<String, String>();

            //on ins?re la r?f?rence ? l'image (convertit en String car normalement c'est un int) que l'on r?cup?rera dans l'imageView cr?? dans le fichier affichageitem.xml
            map.put("img", String.valueOf(R.drawable.biere));
            map.put("img", String.valueOf(R.drawable.bledina));
            map.put("img", String.valueOf(R.drawable.fromage));
            map.put("img", String.valueOf(R.drawable.vaisselle));
            map.put("img", String.valueOf(R.drawable.lait));


            //enfin on ajoute cette hashMap dans la arrayList
            listItem.add(map);

            lview = (ListView) findViewById(R.id.listview);
            lviewTotal = (ListView) findViewById(R.id.listview2);
            ListView lviewPromotions = (ListView) findViewById(R.id.listview3);

          /*  liste_achat = ScanProduct("Pomme de terre", "1", "1.45");
            liste_achat = ScanProduct("fromage", "1", "2.5");*/


            lview.setAdapter(new listViewCourses(this, liste_achat));


            listviewAdapterPrice adapterTotal = new listviewAdapterPrice(this, listTotal);
            lviewTotal.setAdapter(adapterTotal);


            liste_promo = AddPromotions();
            lviewPromotions.setAdapter(new PromotionAdapter(this, liste_promo));




        }


        //la fonction qui permet d'ajouter les produits scannÈes
        public ArrayList<ListeCourses> ScanProduct(String Name, String Quantite, String Price) {
            ListeCourses listeCourse = new ListeCourses();
            listeCourse.setName(Name);
            listeCourse.setItemQuantite(Quantite);
            listeCourse.setPrice(Price);
            listeCourse.setImageNumber(0);
            results.add(listeCourse);

            RealPrice += Double.parseDouble(Price);
            CalculTotal(String.valueOf(RealPrice));
            return results;
        }

        //la fonction qui permet d'ajouter la liste de courses prÈdÈfini par le client
        public ArrayList<ListeCourses> AddList(String Name, String Quantite, String Price) {

            ListeCourses listeCourse = new ListeCourses();
            listeCourse.setName(Name);
            listeCourse.setItemQuantite(Quantite);
            listeCourse.setPrice(Price);
            listeCourse.setImageNumber(1);
            results.add(listeCourse);
            CalculTotal(String.valueOf(RealPrice));
            return results;
        }

        public ArrayList<Promotions> AddPromotions(){
            Promotions promo = new Promotions();
            promo.setImageNumber1(1);
            promo.setImageNumber2(2);
            promo.setImageNumber3(3);
            promo.setImageNumber4(4);
            promo.setImageNumber5(5);
            resultsPromo.add(promo);
            return resultsPromo;
        }


        // la fonction qui permet de mettre ‡ jour le total
        public void CalculTotal(String chaine) {
            listTotal = new ArrayList<HashMap>();

            HashMap<String, String> temp = new HashMap<String, String>();
            temp.put(TOTAL, "Total :");
            temp.put(PRICE, chaine);
            listTotal.add(temp);

        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }

        class IncomingHandler extends Handler {
            @Override
            public void handleMessage(Message msg) {


                if (msg.what == BluetoothService.MESSAGE_LOC) {
                    Bundle b = msg.getData();
                    CharSequence text = null;
                    if (b != null) {
                        text = b.getCharSequence("data");

                        Intent i = new Intent();
                        i.putExtra("LOC", text);
                        i.setAction("com.example.handycart.NavigationActivity.DO_SOME");
                        sendBroadcast(i);

                    }
                    Log.d("MessengerActivity", "Response: " + text);
                }

                if (msg.what == BluetoothService.MESSAGE_SCAN) {
                    Bundle b = msg.getData();
                    CharSequence text = null;
                    if (b != null) {
                        text = b.getCharSequence("data");

                        Product product = databaseAdapter.getProductByBarCode(text.toString());
                        liste_achat = Scanner(product.getName(), "1", String.valueOf(product.getPrice()));

                        lview.setAdapter(new listViewCourses(mContext, liste_achat));
                        listviewAdapterPrice adapterTotal = new listviewAdapterPrice(MainActivity.this, listTotal);
                        lviewTotal.setAdapter(adapterTotal);

                        Intent i = new Intent();
                        i.putExtra("scan", "6_H");
                        i.setAction("com.example.handycart.navigIntent2");
                        sendBroadcast(i);
                    }
                    Log.d("MessengerActivity", "Response: " + text);
                } else {
                    super.handleMessage(msg);
                }
            }
        }

        private ServiceConnection mConn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName className, IBinder service) {
                Log.d("MessengerActivity", "Connected to service. Registering our Messenger in the Service...");
                mService = new Messenger(service);
                mServiceConnected = true;

                // Register our messenger also on Service side:
                Message msg = Message.obtain(null, BluetoothService.MESSAGE_TYPE_REGISTER_MAIN);
                msg.replyTo = mMessenger;
                try {
                    mService.send(msg);
                } catch (RemoteException e) {
                    // We always have to trap RemoteException (DeadObjectException
                    // is thrown if the target Handler no longer exists)
                    e.printStackTrace();
                }
            }

            /**
             * Connection dropped.
             */
            @Override
            public void onServiceDisconnected(ComponentName className) {
                Log.d("MessengerActivity", "Disconnected from service.");
                mService = null;
                mServiceConnected = false;
            }
        };

        @Override
        protected void onStop() {
            super.onStop();
            if (mServiceConnected) {
                unbindService(mConn);
                // stopService(new Intent(this, BluetoothService.class));
                mServiceConnected = false;
            }
        }

       /* private void updateView(int itemIndex){
            int visiblePosition = lview.getFirstVisiblePosition();
            View v = lview.getChildAt(itemIndex - visiblePosition);
            // Do something fancy with your listitem view
            TextView someTextView = (TextView) v.findViewById(R.id.sometextview);
            someTextView.setText("");
        }*/


        public static Context getContext() {
            return mContext;
        }

        public void RemoveElements(ArrayList<ListeCourses> recherche, int x) {
            recherche.remove(x);
        }

        // à ajouter dans le main
        public void IncreseQuantite(ArrayList<ListeCourses> recherche, int i) {
            int quantite;
            recherche.get(i).getItemQuantite();
            quantite = Integer.valueOf(recherche.get(i).getItemQuantite());
            quantite++;

            recherche.get(i).setItemQuantite(String.valueOf(quantite));

        }

        public int RechercheElements(ArrayList<ListeCourses> recherche, String element) {
            final int size = recherche.size();
            for (int i = 0; i < size; i++) {
                final String label = recherche.get(i).getName();
                if (label != null && label.equals(element)) {

                    return i;
                }
            }
            return -1;
        }

        public ArrayList<ListeCourses> Scanner(String Name, String Quantite, String Price) {
            int x = RechercheElements(results, Name);
            ListeCourses listeCourse = new ListeCourses();
            if (x != -1) {
                Log.i("informations", " la recherche est différente de -1" + x);

                if (results.get(x).getImageNumber() == 1) {
                    Log.i("informations", " le produit existe mais n'a pas été scanné");

                    RemoveElements(results, x);

                    listeCourse.setName(Name);
                    listeCourse.setItemQuantite(Quantite);
                    listeCourse.setPrice(Price);
                    listeCourse.setImageNumber(0);
                    results.add(listeCourse);

                    RealPrice += Double.parseDouble(Price);
                    CalculTotal(String.valueOf(RealPrice));

                } else {
                    IncreseQuantite(results, x);
                    RealPrice += Double.parseDouble(Price);
                    CalculTotal(String.valueOf(RealPrice));

                }

            } else {
                Log.i("informations", " la recherche est égale a -1");
                listeCourse.setName(Name);
                listeCourse.setItemQuantite(Quantite);
                listeCourse.setPrice(Price);
                listeCourse.setImageNumber(0);
                results.add(listeCourse);
                RealPrice += Double.parseDouble(Price);
                CalculTotal(String.valueOf(RealPrice));

            }


            return results;
        }

        public class MainActivityIntent extends BroadcastReceiver {

            @Override
            public void onReceive(Context context, Intent intent) {
                // TODO Auto-generated method stub
                if (intent.getAction().equalsIgnoreCase("com.example.handycart.mainIntent")) {


                    String Price = intent.getStringExtra(SearchActivity.intentPrice);
                    Log.i(" intent !!!", Price);
                    String Name = intent.getStringExtra(SearchActivity.intentName);
                    Log.i(" intent !!!", Name);
                    liste_achat = AddList(Name, "1", Price);

                }


            }
        }
    }
