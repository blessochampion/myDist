package mydist.mydist.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import mydist.mydist.models.ProductOrder;

import static mydist.mydist.data.MasterContract.*;

/**
 * Database helper class to facilitate creating and updating
 * the database from the chosen schema.
 */
public class RouteDbHelper extends SQLiteOpenHelper {
    private static final String TAG = RouteDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "routes.db";
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
                    MerchandizeContract.MERCHANDIZE_ID + " TEXT," +
                    MerchandizeContract.BRAND_ID + " TEXT," +
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

    private static final String SQL_CREATE_NEW_RETAILER =
            "CREATE TABLE " + RetailerContract.TABLE_NAME + " (" +
                    RetailerContract._ID + " INTEGER PRIMARY KEY," +
                    RetailerContract.DATE_ADDED + " TEXT," +
                    RetailerContract.RETAILER_NAME + " TEXT," +
                    RetailerContract.CONTACT_PERSON_NAME + " TEXT," +
                    RetailerContract.ADDRESS + " TEXT," +
                    RetailerContract.PHONE + " TEXT," +
                    RetailerContract.CHANNEL_ID + " TEXT," +
                    RetailerContract.SUB_CHANNEL_ID + " TEXT," +
                    RetailerContract.RETAILER_ID + " TEXT UNIQUE)";

    private static final String SQL_CREATE_VISITING_INFO =
            "CREATE TABLE " + VisitingInfoContract.TABLE_NAME + " (" +
                    VisitingInfoContract._ID + " INTEGER PRIMARY KEY," +
                    VisitingInfoContract.DATE_ADDED + " TEXT," +
                    VisitingInfoContract.RETAILER_ID + " TEXT," +
                    VisitingInfoContract.WEEK + " TEXT," +
                    VisitingInfoContract.DAY + " TEXT)";

    private static final String SQL_CREATE_INVOICE =
            "CREATE TABLE " + InvoiceContract.TABLE_NAME + " (" +
                    InvoiceContract._ID + " INTEGER PRIMARY KEY," +
                    InvoiceContract.DATE_ADDED + " TEXT," +
                    InvoiceContract.RETAILER_ID + " TEXT," +
                    InvoiceContract.TOTAL + " TEXT," +
                    InvoiceContract.AMOUNT_PAID + " TEXT," +
                    InvoiceContract.PAYMENT_MODE + " TEXT," +
                    InvoiceContract.PAYMENT_MODE_VALUE + " TEXT," +
                    InvoiceContract.STATUS + " INTEGER," +
                    InvoiceContract.INVOICE_ID + " TEXT UNIQUE)";

    private static final String SQL_CREATE_PRODUCT_ORDER =
            "CREATE TABLE " + ProductOrderContract.TABLE_NAME + " (" +
                    ProductOrderContract._ID + " INTEGER PRIMARY KEY," +
                    ProductOrderContract.DATE_ADDED + " TEXT," +
                    ProductOrderContract.INVOICE_ID + " TEXT," +
                    ProductOrderContract.TOTAL + " TEXT," +
                    ProductOrderContract.PRODUCT_NAME + " TEXT," +
                    ProductOrderContract.PRODUCT_ID + " TEXT," +
                    ProductOrderContract.BRAND_ID + " TEXT," +
                    ProductOrderContract.OC + " INTEGER," +
                    ProductOrderContract.OP + " INTEGER)";

    private static final String SQL_CREATE_MERCHANDIZING_VERIFICATION =
            "CREATE TABLE " + MerchandizingListVerificationContract.TABLE_NAME + " (" +
                    MerchandizingListVerificationContract._ID + " INTEGER PRIMARY KEY," +
                    MerchandizingListVerificationContract.DATE_ADDED + " TEXT," +
                    MerchandizingListVerificationContract.MERCHANDIZING_ID + " TEXT," +
                    MerchandizingListVerificationContract.BRAND_ID + " TEXT," +
                    MerchandizingListVerificationContract.RETAILER_ID + " TEXT," +
                    MerchandizingListVerificationContract.AVAILABLE + " INTEGER)";

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

    private static final String SQL_DELETE_NEW_RETAILER_ENTRIES =
            "DROP TABLE IF EXISTS " + RetailerContract.TABLE_NAME;

    private static final String SQL_DELETE_VISITING_INFO_ENTRIES =
            "DROP TABLE IF EXISTS " + VisitingInfoContract.TABLE_NAME;

    private static final String SQL_DELETE_INVOICE =
            "DROP TABLE IF EXISTS " + InvoiceContract.TABLE_NAME;

    private static final String SQL_DELETE_PRODUCT_ORDER =
            "DROP TABLE IF EXISTS " + ProductOrderContract.TABLE_NAME;

    private static final String SQL_DELETE_MERCHANDIZING_VERIFICATION =
            "DROP TABLE IF EXISTS " + MerchandizingListVerificationContract.TABLE_NAME;


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
        db.execSQL(SQL_CREATE_NEW_RETAILER);
        db.execSQL(SQL_CREATE_VISITING_INFO);
        db.execSQL(SQL_CREATE_INVOICE);
        db.execSQL(SQL_CREATE_PRODUCT_ORDER);
        db.execSQL(SQL_CREATE_MERCHANDIZING_VERIFICATION);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_BRAND_ENTRIES);
        db.execSQL(SQL_DELETE_CHANNEL_ENTRIES);
        db.execSQL(SQL_DELETE_MERCHANDIZE_ENTRIES);
        db.execSQL(SQL_DELETE_PRODUCT_ENTRIES);
        db.execSQL(SQL_DELETE_SUB_CHANNEL_ENTRIES);
        db.execSQL(SQL_DELETE_NEW_RETAILER_ENTRIES);
        db.execSQL(SQL_DELETE_VISITING_INFO_ENTRIES);
        db.execSQL(SQL_DELETE_INVOICE);
        db.execSQL(SQL_DELETE_PRODUCT_ORDER);
        db.execSQL(SQL_DELETE_MERCHANDIZING_VERIFICATION);

        onCreate(db);
    }


}
