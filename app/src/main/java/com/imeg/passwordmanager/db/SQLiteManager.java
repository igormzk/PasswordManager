package com.imeg.passwordmanager.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.imeg.passwordmanager.model.Spot;

import java.util.ArrayList;
import java.util.List;

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

    public String saveSpot(Spot spot) {
        long result = -1;
        try {
            dataBase = dbHelper.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("description", spot.getDescription());
            cv.put("login", spot.getLogin());
            cv.put("password", spot.getPassword());
            result = dataBase.insert(TBL_SPOT,null, cv);
            dataBase.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (result == -1) {
            return "Erro ao inserir registro";
        } else {
            return "Registro inserido com sucesso";
        }
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
            s.setPassword(c.getString(c.getColumnIndex("password")));
            list.add(s);
            c.moveToNext();
        }
        c.close();
        dataBase.close();
        return list;
    }

    public String deleteSpot(int id) {
        long result = -1;
        dataBase = dbHelper.getWritableDatabase();
        result = dataBase.delete(TBL_SPOT, "id = " + id, null);
        if (result == -1) {
            return "Erro ao deletar senha";
        } else {
            return "Senha excluída!";
        }
    }
}