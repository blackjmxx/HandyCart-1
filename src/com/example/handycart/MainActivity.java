    package com.example.handycart;

    import android.app.TabActivity;
    import android.content.ComponentName;
    import android.content.Context;
    import android.content.Intent;
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
    import android.widget.SimpleAdapter;
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
        private ArrayList<HashMap> list;
        private ArrayList<HashMap> listTotal;
        private ArrayList<HashMap> listPromo;
        public ExtractFunctions extract = new ExtractFunctions();
        private ListView maListViewPerso;
        private TextView t = null;
        public static Context mContext;
        public static final int CODE_RETOUR = 7;
        final Messenger mMessenger = new Messenger(new IncomingHandler());
        /**
         * Messenger used for communicating with service.
         */
        Messenger mService = null;
        boolean mServiceConnected = false;


        //OnCreate
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            bindService(new Intent(this,BluetoothService.class), mConn, Context.BIND_AUTO_CREATE);
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
            mContext= getBaseContext();
            setContentView(R.layout.main);
            Intent intent = getIntent();
            DatabaseAdapter databaseAdapter = DatabaseAdapter
                    .getInstanceOfDatabaseAdapter(this);
            if (null != intent) {
               String  headline = intent.getStringExtra("data");
                t = (TextView) findViewById(R.id.NameCli);
                String[] parts = headline.split(":");
                t.append("BIENVENUE: "+parts[0]);

                Product product1 = databaseAdapter.getProductByBarCode("1234567891248");


                //appeller la fonction pour obtenir la liste des porduits

                String[] part2= parts[1].split(",");

                for (int i=0; i <part2.length ; i++)
                {
                    String[] parts3 = part2[i].split("=");

                    Product product = databaseAdapter.getProductsbyId(parts3[0]);
                }

            }



            /// la gestion et l'initialisation des onglets -- debut
            Resources ressources = getResources();
            TabHost tabHost = getTabHost();

            // Android tab
            Intent intentAndroid = new Intent().setClass(this, NavigationActivity.class);
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
              //Cr?ation d'un SimpleAdapter qui se chargera de mettre les items pr?sent dans notre list (listItem) dans la vue affichageitem
                SimpleAdapter mSchedule = new SimpleAdapter (this.getBaseContext(), listItem, R.layout.promotions,
                       new String[] {"promo1", "promo2", "promo3","promo4","promo5"}, new int[] {R.id.list_image1, R.id.list_image2, R.id.list_image3,R.id.list_image4, R.id.list_image5});

                //On attribut ? notre listView l'adapter que l'on vient de cr?er
                maListViewPerso.setAdapter(mSchedule);


            ListView lview = (ListView) findViewById(R.id.listview);
            ListView lviewTotal = (ListView) findViewById(R.id.listview2);
            //ListView lviewPromotion = (ListView) findViewById(R.id.listview3);
            //populateList();
            DecodeProtocol("AUT LISTECOURSE-NomProduit1|Marque1,Prix1,Quantit?1;NomProduit2|Marque2,Prix2,Quantit?2;NomProduit3|Marque3,Prix3,Quantit?3;");
            CalculTotal();

            listviewAdapter adapter = new listviewAdapter(this, list);
            listviewAdapterPrice adapterTotal = new listviewAdapterPrice(this, listTotal);
            //listviewAdapterPromotions adapterPromo = new listviewAdapterPromotions(this,listPromo);
            lview.setAdapter(adapter);
            lviewTotal.setAdapter(adapterTotal);
            //lviewPromotion.setAdapter(adapterPromo);

        }

        public void DecodeProtocol(String chaine)
        {
            String[] parts = chaine.split("-");
            String part1 = parts[0];
            list = new ArrayList<HashMap>();

            if(part1.equals("AUT LISTECOURSE"))
            {
                String part2 = parts[1];
                String[] parts2 = part2.split(";");

                for(int i=0;i < parts2.length;i++)
                {
                    String[] s = parts2[i].split(",");
                    list.add(extract.getArticle(s));
                }

            }

            if (part1.equals("SCAN"))
            {


            }
            if(part1.equals("LOC"))
            {
                Log.d("EF-BTBee", "LOC :: "+part1);
            }
            if(part1.equals("TRAJET"))
            {
                Log.d("EF-BTBee", "TRAJET :: "+part1);
            }
            if(part1.equals("PUB"))
            {
                Log.d("EF-BTBee", "PUB :: "+part1);
            }
            if(part1.equals("MSG"))
            {
                Log.d("EF-BTBee", "MSG :: "+part1);
            }

        }


        public void CalculTotal(){
             listTotal = new ArrayList<HashMap>();

                HashMap temp = new HashMap();
                    temp.put(TOTAL,"Total :");
                    temp.put(PRICE, "10.3 ?" );
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
                if (msg.what == BluetoothService.MESSAGE_TYPE_TEXT) {
                    Bundle b = msg.getData();
                    CharSequence text = null;
                    if (b != null) {
                        text = b.getCharSequence("data");

                    } else {
                        text = "Service responded with empty message";
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


        public static Context getContext() {
            return mContext;
        }


    }
