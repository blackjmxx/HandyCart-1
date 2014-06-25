package com.example.handycart;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;

import dao.DatabaseAdapter;
import data.Category;
import data.Product;

public class SearchActivity extends Activity implements OnItemSelectedListener{
    ArrayAdapter<String> adapter1,adapter2;
    ArrayList<String> list1 = new ArrayList<String>();
    ArrayList<String> list2 = new ArrayList<String>();
    Product produit;

    static String intentPrice = "Price";
    static String intentName = "Name";


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spinner);
        final Spinner spinner1 = (Spinner) findViewById(R.id.spinner1);
        final Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
        final Context context = this;
        // DataBase
        final DatabaseAdapter databaseAdapter = DatabaseAdapter
                .getInstanceOfDatabaseAdapter(this);
        ArrayList<Category> categories = databaseAdapter.getCategories();

        for (Category category : categories) {

            list1.add(category.getName());
        }

        list2.add("Papier Cuisson en Feuilles");
        adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, list1);
        spinner1.setAdapter(adapter1);

        adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, list2);
        spinner2.setAdapter(adapter2);

        spinner1.setOnItemSelectedListener(
                new OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        ArrayList<Product> produits = databaseAdapter.getProductsByCategoryName(String.valueOf(spinner1.getSelectedItem()));

                        for (Product produit : produits) {
                            list2.clear();
                            list2.add(produit.getName());
                        }
                        spinner2.setAdapter(adapter2);
                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

        spinner2.setOnItemSelectedListener(
                new OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {

                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


        Button button = (Button) findViewById(R.id.validation);

        produit = databaseAdapter.getProductByName(String.valueOf(spinner2.getSelectedItem()));
        Log.i("valeurs",produit.getId()+""+produit.getName()+""+produit.getPrice());
        button.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set title
                alertDialogBuilder.setTitle("Recherche de produits");

                // set dialog message
                alertDialogBuilder
                        .setMessage("Voulez-vous ajouter le produit ? votre liste de courses ??")
                        .setCancelable(false)

                        .setPositiveButton("Ajouter le produit",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {



                                final Intent intent = new Intent();
                                intent.putExtra(intentName, produit.getName());
                                Log.i("Name",produit.getName());
                                intent.putExtra(intentPrice, String.valueOf(produit.getPrice()));
                                Log.i("Price",String.valueOf(produit.getPrice()));
                                intent.setAction("com.example.handycart.mainIntent");
                                sendBroadcast(intent);


                            }
                        })
                        .setNegativeButton(" Annuler",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing

                                dialog.cancel();

                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        });





    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {


    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
}