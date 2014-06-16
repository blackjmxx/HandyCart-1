package dao;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import configuration.Configuration;
import data.Category;
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

    public Product getProductByID(int id) {

        Product product = null;

        String sql = "SELECT prd.prd_code_barre, prd.libelle, prd.prd_prix, prd.prd_description, mqp.mqp_libelle, ctp.ctp_libelle, ctp.ctp_localisation, fmp_libelle FROM fmp_famille_produit fmp INNER JOIN ctp_categorie_produit ctp ON (fmp.fmp_id = ctp.fmp_id) INNER JOIN prd_produit prd ON (ctp.ctp_id = prd.ctp_id) INNER JOIN mqp_marque_produit mqp ON (prd.mqp_id = mqp.mqp_id) WHERE prd.prd_id = "
                + id;

        Cursor cursor = sqliteDatabase.rawQuery(sql, null);

        if (cursor.moveToFirst()) {

            product = new Product(
                    id,
                    cursor.getString(cursor.getColumnIndex("PRD_CODE_BARRE")),
                    cursor.getString(cursor.getColumnIndex("LIBELLE")),
                    cursor.getDouble(cursor.getColumnIndex("PRD_PRIX")),
                    cursor.getString(cursor.getColumnIndex("PRD_DESCRIPTION")),
                    cursor.getString(cursor.getColumnIndex("MQP_LIBELLE")),
                    cursor.getString(cursor.getColumnIndex("CTP_LIBELLE")),
                    cursor.getString(cursor.getColumnIndex("CTP_LOCALISATION")),
                    cursor.getString(cursor.getColumnIndex("FMP_LIBELLE")));
        }

        cursor.close();

        return product;
    }

    public Product getProductByBarCode(String barCode) {

        Product product = null;

        String sql = "SELECT prd.prd_id, prd.libelle, prd.prd_prix, prd.prd_description, mqp.mqp_libelle, ctp.ctp_libelle, ctp.ctp_localisation, fmp_libelle FROM fmp_famille_produit fmp INNER JOIN ctp_categorie_produit ctp ON (fmp.fmp_id = ctp.fmp_id) INNER JOIN prd_produit prd ON (ctp.ctp_id = prd.ctp_id) INNER JOIN mqp_marque_produit mqp ON (prd.mqp_id = mqp.mqp_id) WHERE prd_code_barre = '"
                + barCode + "'";

        Cursor cursor = sqliteDatabase.rawQuery(sql, null);

        if (cursor.moveToFirst()) {

            product = new Product(
                    cursor.getInt(cursor.getColumnIndex("PRD_ID")),
                    barCode,
                    cursor.getString(cursor.getColumnIndex("LIBELLE")),
                    cursor.getDouble(cursor.getColumnIndex("PRD_PRIX")),
                    cursor.getString(cursor.getColumnIndex("PRD_DESCRIPTION")),
                    cursor.getString(cursor.getColumnIndex("MQP_LIBELLE")),
                    cursor.getString(cursor.getColumnIndex("CTP_LIBELLE")),
                    cursor.getString(cursor.getColumnIndex("CTP_LOCALISATION")),
                    cursor.getString(cursor.getColumnIndex("FMP_LIBELLE")));
        }

        cursor.close();

        return product;
    }

    public ArrayList<Product> getProductsByCategory(int categoryID) {

        ArrayList<Product> products = new ArrayList<Product>();

        String sql = "SELECT prd.prd_id, prd.prd_code_barre, prd.libelle, prd.prd_prix, prd.prd_description, mqp.mqp_libelle, ctp.ctp_libelle, ctp.ctp_localisation, fmp_libelle FROM fmp_famille_produit fmp INNER JOIN ctp_categorie_produit ctp ON (fmp.fmp_id = ctp.fmp_id) INNER JOIN prd_produit prd ON (ctp.ctp_id = prd.ctp_id) INNER JOIN mqp_marque_produit mqp ON (prd.mqp_id = mqp.mqp_id) WHERE ctp.ctp_id = "
                + categoryID;

        Cursor cursor = sqliteDatabase.rawQuery(sql, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            products.add(new Product(cursor.getInt(cursor
                    .getColumnIndex("PRD_ID")), cursor.getString(cursor
                    .getColumnIndex("PRD_CODE_BARRE")), cursor.getString(cursor
                    .getColumnIndex("LIBELLE")), cursor.getDouble(cursor
                    .getColumnIndex("PRD_PRIX")), cursor.getString(cursor
                    .getColumnIndex("PRD_DESCRIPTION")), cursor
                    .getString(cursor.getColumnIndex("MQP_LIBELLE")), cursor
                    .getString(cursor.getColumnIndex("CTP_LIBELLE")), cursor
                    .getString(cursor.getColumnIndex("CTP_LOCALISATION")),
                    cursor.getString(cursor.getColumnIndex("FMP_LIBELLE"))));

            cursor.moveToNext();
        }

        cursor.close();

        return products;
    }

    public ArrayList<Category> getCategories() {

        ArrayList<Category> categories = new ArrayList<Category>();

        String sql = "SELECT ctp_id, ctp_libelle, ctp_localisation, fmp_libelle FROM ctp_categorie_produit ctp INNER JOIN fmp_famille_produit fmp ON (ctp.fmp_id = fmp.fmp_id)";

        Cursor cursor = sqliteDatabase.rawQuery(sql, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            categories.add(new Category(cursor.getInt(cursor
                    .getColumnIndex("CTP_ID")), cursor.getString(cursor
                    .getColumnIndex("CTP_LIBELLE")), cursor.getString(cursor
                    .getColumnIndex("CTP_LOCALISATION")), cursor
                    .getString(cursor.getColumnIndex("FMP_LIBELLE"))));

            cursor.moveToNext();
        }

        cursor.close();

        return categories;
    }
}
