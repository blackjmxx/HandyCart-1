package com.example.handycart;

import android.app.Activity;
import android.os.Bundle;

import dao.DatabaseAdapter;
import data.Product;

/**
 * Premiere activite pour faire un hello world
 * 
 * 
 */
public class HelloWorldActivity extends Activity {

	/**
	 * Appele a la creation de l'activite
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	public void onCreate(Bundle savedInstanceState) {

		// de l'etat precedent
		super.onCreate(savedInstanceState);

		// affichage de la vue
		setContentView(R.layout.main);

		DatabaseAdapter databaseAdapter = DatabaseAdapter
				.getInstanceOfDatabaseAdapter(this);

		Product product = databaseAdapter.getProductByBarCode("1234567891248");

		if (product != null) {

			System.out.println(product.toString());

		} else {

			System.out.println("Produit inconnu");
		}
	}
}