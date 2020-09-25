package com.example.myapplication;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;

public class DBHelper extends SQLiteOpenHelper {
    public static  int DATABASE_VERSION=1;
    public static final String DATABASE_NAME="contact.db";
    public static final String TABLE_CONTACTS="contacts";


    public static final String COL_1="ID";
    public static final String COL_2="phone";
    public static final String COL_3="mail";
    public static final String COL_4="password";

    private static final String CREATE_TABLE= "CREATE TABLE " + TABLE_CONTACTS + "(" + COL_1
            + " integer primary key autoincrement, " + COL_3
            + " text not null, " + COL_4 + " text not null,"
            + COL_2 + " text not null" + ");";

    public DBHelper( Context context) {
        super(context,DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //создаем таблицу
        db.execSQL(CREATE_TABLE);


        ContentValues initialValues = createContentValues("","","");
        db.insert(TABLE_CONTACTS, null, initialValues);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+TABLE_CONTACTS);
        onCreate(db);
    }

    /*
     * Описываем структуру данных
     */
    private ContentValues createContentValues(String mail, String pass,
                                              String phone) {
        ContentValues values = new ContentValues();
        values.put(COL_3, mail);
        values.put(COL_4, pass);
        values.put(COL_2, phone);
        return values;
    }
}
