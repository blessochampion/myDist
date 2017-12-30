package mydist.mydist.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import mydist.mydist.models.Brand;
import mydist.mydist.models.SubChannel;

import static mydist.mydist.data.MasterContract.*;
/**
 * Database helper class to facilitate creating and updating
 * the database from the chosen schema.
 */
public class RouteDbHelper extends SQLiteOpenHelper {
    private static final String TAG = RouteDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "insects.db";
    private static final int DATABASE_VERSION = 1;
    private static final String SQL_CREATE_BRAND_ENTRIES =
            "CREATE TABLE " + BrandContract.TABLE_NAME + " (" +
                    BrandContract._ID + " INTEGER PRIMARY KEY," +
                    BrandContract.COLUMN_NAME + " TEXT," +
                    BrandContract.COLUMN_BRAND_ID + " TEXT UNIQUE)";

    private static final String SQL_CREATE_CHANNEL_ENTRIES =
            "CREATE TABLE " + ChannelContract.TABLE_NAME + " (" +
                    ChannelContract._ID + " INTEGER PRIMARY KEY," +
                    ChannelContract.COLUMN_NAME + " TEXT," +
                    ChannelContract.COLUMN_CHANNEL_ID + " TEXT UNIQUE)";

    private static final String SQL_CREATE_MERCHANDIZE_ENTRIES =
            "CREATE TABLE " + MerchandizeContract.TABLE_NAME + " (" +
                    MerchandizeContract._ID + " INTEGER PRIMARY KEY," +
                    MerchandizeContract.BRAND_NAME + " TEXT," +
                    MerchandizeContract.COLUMN_MERCHANDIZE_ITEM + " TEXT)";

    private static final String SQL_CREATE_PRODUCT_ENTRIES =
            "CREATE TABLE " + ProductContract.TABLE_NAME + " (" +
                    ProductContract._ID + " INTEGER PRIMARY KEY," +
                    ProductContract.COLUMN_NAME + " TEXT," +
                    ProductContract.COLUMN_PRODUCT_ID + " TEXT UNIQUE," +
                    ProductContract.COLUMN_CASE_PRICE + " REAL," +
                    ProductContract.COLUMN_PIECE_PRICE + " REAL," +
                    ProductContract.COLUMN_BRAND_ID + " TEXT)";

    private static final String SQL_CREATE_SUB_CHANNEL_ENTRIES =
            "CREATE TABLE " + SubChannelContract.TABLE_NAME + " (" +
                    SubChannelContract._ID + " INTEGER PRIMARY KEY," +
                    SubChannelContract.COLUMN_NAME + " TEXT," +
                    SubChannelContract.COLUMN_SUB_CHANNEL_ID + " TEXT UNIQUE)";

    private static final String SQL_DELETE_BRAND_ENTRIES =
            "DROP TABLE IF EXISTS " + BrandContract.TABLE_NAME;

    private static final String SQL_DELETE_CHANNEL_ENTRIES =
            "DROP TABLE IF EXISTS " + ChannelContract.TABLE_NAME;

    private static final String SQL_DELETE_MERCHANDIZE_ENTRIES =
            "DROP TABLE IF EXISTS " + MerchandizeContract.TABLE_NAME;

    private static final String SQL_DELETE_PRODUCT_ENTRIES =
            "DROP TABLE IF EXISTS " + ProductContract.TABLE_NAME;

    private static final String SQL_DELETE_SUB_CHANNEL_ENTRIES =
            "DROP TABLE IF EXISTS " + SubChannelContract.TABLE_NAME;


    public RouteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_BRAND_ENTRIES);
        db.execSQL(SQL_CREATE_CHANNEL_ENTRIES);
        db.execSQL(SQL_CREATE_MERCHANDIZE_ENTRIES);
        db.execSQL(SQL_CREATE_PRODUCT_ENTRIES);
        db.execSQL(SQL_CREATE_SUB_CHANNEL_ENTRIES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_BRAND_ENTRIES);
        db.execSQL(SQL_DELETE_CHANNEL_ENTRIES);
        db.execSQL(SQL_DELETE_MERCHANDIZE_ENTRIES);
        db.execSQL(SQL_DELETE_PRODUCT_ENTRIES);
        db.execSQL(SQL_DELETE_SUB_CHANNEL_ENTRIES);
        onCreate(db);
    }


}
