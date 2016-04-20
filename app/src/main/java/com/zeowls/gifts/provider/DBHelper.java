package com.zeowls.gifts.provider;

/**
 * Created by root on 4/20/16.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zeowls.gifts.provider.Contract.ShopEntry;
import com.zeowls.gifts.provider.Contract.ItemEntry;
import com.zeowls.gifts.provider.Contract.CategoryEntry;
import com.zeowls.gifts.provider.Contract.ParentCategoryEntry;

public class DBHelper extends SQLiteOpenHelper {
    private static DBHelper instance;

    final static String DATABASE_NAME = "movies.db";

    private static int DATABASE_VERSION = 1;

    public static synchronized DBHelper getInstance(Context context)
    {
        if (instance == null) {
            instance = new DBHelper(context);
        }

        return instance;
    }

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_SHOP_TABLE = "CREATE TABLE " + ShopEntry.TABLE_NAME + " (" +

                ShopEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ShopEntry.COLUMN_ID + " INTEGER NOT NULL," +
                ShopEntry.COLUMN_FAV + " INTEGER NOT NULL," +
                ShopEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                ShopEntry.COLUMN_OWNER_NAME + " TEXT NOT NULL, " +
                ShopEntry.COLUMN_EMAIL + " TEXT NOT NULL, " +
                ShopEntry.COLUMN_MOBILE + " TEXT NOT NULL," +
                ShopEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL," +
                ShopEntry.COLUMN_SHORT_DESCRIPTION + " TEXT NOT NULL," +
                ShopEntry.COLUMN_ADDRESS + " TEXT NOT NULL," +
                ShopEntry.COLUMN_PROFILE_PIC + " TEXT NOT NULL," +
                ShopEntry.COLUMN_COVER_PIC + " TEXT NOT NULL," +
                " UNIQUE (" + ShopEntry.COLUMN_ID + ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_ITEM_TABLE = "CREATE TABLE " + ItemEntry.TABLE_NAME + " (" +

                ItemEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ItemEntry.COLUMN_ID + " INTEGER NOT NULL," +
                ItemEntry.COLUMN_FAV + " INTEGER NOT NULL," +
                ItemEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                ItemEntry.COLUMN_IMAGE + " TEXT NOT NULL, " +
                ItemEntry.COLUMN_PRICE + " TEXT NOT NULL, " +
                ItemEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                ItemEntry.COLUMN_SHOP_ID + " REAL NOT NULL, " +
                ItemEntry.COLUMN_CAT_ID + " REAL NOT NULL," +
                " FOREIGN KEY (" + ItemEntry.COLUMN_SHOP_ID + ") REFERENCES " +
                ShopEntry.TABLE_NAME + " (" + ShopEntry.COLUMN_ID + "), "+
                " UNIQUE (" + ItemEntry.COLUMN_ID + ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_PARENT_CAT_TABLE = "CREATE TABLE " + ParentCategoryEntry.TABLE_NAME + " (" +

                CategoryEntry._ID + " TEXT PRIMARY KEY," +
                CategoryEntry.COLUMN_ID + " TEXT NOT NULL, " +
                CategoryEntry.COLUMN_NAME + " TEXT NOT NULL, "+
//                CategoryEntry.COLUMN_PARENT_ID + " TEXT NOT NULL, " +
//                " FOREIGN KEY (" + CategoryEntry.COLUMN_PARENT_ID + ") REFERENCES " +
//                ShopEntry.TABLE_NAME + " (" + ParentCategoryEntry.COLUMN_ID + ");";
                " UNIQUE (" + ParentCategoryEntry.COLUMN_ID + ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_CAT_TABLE = "CREATE TABLE " + CategoryEntry.TABLE_NAME + " (" +

                CategoryEntry._ID + " TEXT PRIMARY KEY," +
                CategoryEntry.COLUMN_ID + " TEXT NOT NULL, " +
                CategoryEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                CategoryEntry.COLUMN_PARENT_ID + " TEXT NOT NULL, " +
                " FOREIGN KEY (" + CategoryEntry.COLUMN_PARENT_ID + ") REFERENCES " +
                ParentCategoryEntry.TABLE_NAME + " (" + ParentCategoryEntry.COLUMN_ID + "), "+
                " UNIQUE (" + CategoryEntry.COLUMN_ID + ") ON CONFLICT REPLACE);";

        db.execSQL(SQL_CREATE_SHOP_TABLE);
        db.execSQL(SQL_CREATE_ITEM_TABLE);
        db.execSQL(SQL_CREATE_CAT_TABLE);
        db.execSQL(SQL_CREATE_PARENT_CAT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ShopEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ItemEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CategoryEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ParentCategoryEntry.TABLE_NAME);
        onCreate(db);
    }}
