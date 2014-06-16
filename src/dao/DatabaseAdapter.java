package dao;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import configuration.Configuration;
import data.Product;

public class DatabaseAdapter extends SQLiteOpenHelper {

	public static DatabaseAdapter getInstanceOfDatabaseAdapter(Context context) {

		if (instanceOfDatabaseAdapter == null) {

			instanceOfDatabaseAdapter = new DatabaseAdapter(context);
		}

		return instanceOfDatabaseAdapter;
	}

	private static DatabaseAdapter instanceOfDatabaseAdapter;

	private DatabaseAdapter(Context context) {

		super(context, Configuration.DATABASE_NAME, null, 1);

		this.context = context;

		DATABASE_PATH = Configuration.DATABASE_PATH;

		DATABASE_NAME = Configuration.DATABASE_NAME;

		try {

			createDataBase();

		} catch (IOException ioe) {

			throw new Error("Unable to create database");
		}

		try {

			openDataBase();

		} catch (SQLException sqle) {

			throw sqle;
		}
	}

	private Context context;

	private final String DATABASE_PATH;

	private final String DATABASE_NAME;

	private SQLiteDatabase sqliteDatabase;

	private void createDataBase() throws IOException {

        try {

            copyDataBase();

        } catch (IOException e) {

            throw new Error("Error copying database");
        }

	}

	private boolean checkDataBase() {

		SQLiteDatabase sqliteDatabase = null;

		try {

			String myPath = DATABASE_PATH + DATABASE_NAME;

			sqliteDatabase = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY);

		} catch (SQLiteException e) {

		}

		if (sqliteDatabase != null) {

			sqliteDatabase.close();
		}

		return sqliteDatabase != null ? true : false;
	}

	private void copyDataBase() throws IOException {

		InputStream inputStream = context.getAssets().open(DATABASE_NAME);

		String outFileName = DATABASE_PATH + DATABASE_NAME;

		OutputStream outputStream = new FileOutputStream(outFileName);

		byte[] buffer = new byte[1024];

		int length;

		while ((length = inputStream.read(buffer)) > 0) {

			outputStream.write(buffer, 0, length);
		}

		outputStream.flush();

		outputStream.close();

		inputStream.close();
	}

	public void openDataBase() throws SQLException {

		String path = DATABASE_PATH + DATABASE_NAME;

		sqliteDatabase = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);
	}

	public synchronized void close() {

		if (sqliteDatabase != null) {

			sqliteDatabase.close();
		}

		super.close();
	}

	public void onCreate(SQLiteDatabase db) {

	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public Product getProductByBarCode(String barCode) {

		Product product = null;

        String sql = "SELECT prd.libelle, mqp.mqp_libelle, prd.prd_prix FROM PRD_PRODUIT prd, mqp_marque_produit mqp WHERE prd.mqp_id = mqp.mqp_id AND prd_code_barre = '"
                + barCode + "'";

		Cursor cursor = sqliteDatabase.rawQuery(sql, null);

		if (cursor.moveToFirst()) {

			product = new Product(barCode, cursor.getString(cursor
					.getColumnIndex("LIBELLE")), cursor.getString(cursor
					.getColumnIndex("MQP_LIBELLE")), cursor.getDouble(cursor
					.getColumnIndex("PRD_PRIX")));
		}

		cursor.close();

		return product;
	}

    public Product getProductsbyId(String id)
    {
        Product product = null;

        String sql = "SELECT prd.libelle, mqp.mqp_libelle, prd.prd_prix FROM prd_produit prd, mqp_marque_produit mqp WHERE prd.mqp_id = "
                    + id ;
        Cursor cursor = sqliteDatabase.rawQuery(sql, null);

        if (cursor.moveToFirst()) {

            product = new Product(id, cursor.getString(cursor
                    .getColumnIndex("LIBELLE")), cursor.getString(cursor
                    .getColumnIndex("MQP_LIBELLE")), cursor.getDouble(cursor
                    .getColumnIndex("PRD_PRIX")));
        }

        cursor.close();

        return null;
    }

}
