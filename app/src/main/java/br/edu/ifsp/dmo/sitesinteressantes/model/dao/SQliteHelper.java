package br.edu.ifsp.dmo.sitesinteressantes.model.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQliteHelper extends SQLiteOpenHelper {

    public SQliteHelper(Context context){
        super(context, DatabaseContracts.DATABASE_NAME, null, DatabaseContracts.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("PRAGMA foreign_keys=ON;");
        sqLiteDatabase.execSQL(DatabaseContracts.TableTag.CREATE_TABLE);
        sqLiteDatabase.execSQL(DatabaseContracts.TableSite.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
