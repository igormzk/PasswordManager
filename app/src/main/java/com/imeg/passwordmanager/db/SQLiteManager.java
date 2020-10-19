package com.imeg.passwordmanager.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Base64;

import com.imeg.passwordmanager.model.Spot;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class SQLiteManager {
    private static final String DB_NAME="passwordmanager.db";
    private static final int DB_VERSION=1;
    private static final String TBL_SPOT="spot";

    private DBHelper dbHelper;
    private Context context;
    private SQLiteDatabase dataBase;

    private static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL("CREATE TABLE " + TBL_SPOT + " (id INTEGER PRIMARY KEY autoincrement, description TEXT, login TEXT, password TEXT);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TBL_SPOT + ";");
            onCreate(sqLiteDatabase);

        }
    }

    public SQLiteManager(Context context) {
        this.context = context;
    }

    public SQLiteManager open() throws SQLException {
        dbHelper = new DBHelper(context);
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public long saveSpot(Spot spot) {
        long result = -1;
        try {
            dataBase = dbHelper.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("description", spot.getDescription());
            cv.put("login", spot.getLogin());
            cv.put("password", encrypt(spot.getPassword()));
            result = dataBase.insert(TBL_SPOT,null, cv);
            dataBase.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
       return result;
    }

    public List<Spot> loadSpot() {
        List<Spot> list = new ArrayList<Spot>();
        dataBase = dbHelper.getReadableDatabase();
        String filtroCat = "";
        Cursor c = dataBase.rawQuery("SELECT * FROM " + TBL_SPOT, null);
        //Cursor c = dataBase.query(TBL_DESPESA, new String[]{"id","data","descricao","categoria","rendimento", "despesa"}, "data >= " + dataInicial + " AND data <= " + dataFinal,null,null,"data desc",null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            Spot s = new Spot();
            s.setId(c.getInt(c.getColumnIndex("id")));
            s.setDescription(c.getString(c.getColumnIndex("description")));
            s.setLogin(c.getString(c.getColumnIndex("login")));
            s.setPassword(decrypt(c.getString(c.getColumnIndex("password"))));
            list.add(s);
            c.moveToNext();
        }
        c.close();
        dataBase.close();
        return list;
    }

    public long deleteSpot(int id) {
        dataBase = dbHelper.getWritableDatabase();
        return dataBase.delete(TBL_SPOT, "id = " + id, null);
    }

    private String encrypt(String text) {
        try {
            SecretKeySpec sks = new SecretKeySpec(getRaw("ImeG", "ResultText"), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, sks, new IvParameterSpec("664419738851919".getBytes()));
            byte[] encrypted = cipher.doFinal(text.getBytes());
            return Base64.encodeToString(encrypted, Base64.DEFAULT);
        } catch (Exception e) {
        }

        return null;
    }
    private String decrypt(String text) {
        try {
            byte[] encrypted = Base64.decode(text, Base64.DEFAULT);

            SecretKeySpec sks = new SecretKeySpec(getRaw("ImeG", "ResultText"), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, sks, new IvParameterSpec("664419738851919".getBytes()));
            byte[] decrypted = cipher.doFinal(encrypted);
            return new String(decrypted);
        } catch (Exception e) {
        }
        return null;
    }

    private byte[] getRaw(String text, String alt) {
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec ks = new PBEKeySpec(text.toCharArray(), alt.getBytes(), 10, 128);
            return skf.generateSecret(ks).getEncoded();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }
}