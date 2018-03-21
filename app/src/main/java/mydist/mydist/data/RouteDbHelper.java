package mydist.mydist.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

    private static final String SQL_CREATE_AREA_ENTRIES =
            "CREATE TABLE " + AreaContract.TABLE_NAME + " (" +
                    AreaContract._ID + " INTEGER PRIMARY KEY," +
                    AreaContract.COLUMN_NAME + " TEXT," +
                    AreaContract.COLUMN_AREA_ID + " TEXT UNIQUE)";

    private static final String SQL_CREATE_MERCHANDIZE_ENTRIES =
            "CREATE TABLE " + MerchandizeContract.TABLE_NAME + " (" +
                    MerchandizeContract._ID + " INTEGER PRIMARY KEY," +
                    MerchandizeContract.BRAND_NAME + " TEXT," +
                    MerchandizeContract.DATE_ADDED + " TEXT," +
                    MerchandizeContract.MERCHANDIZE_ID + " TEXT UNIQUE," +
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
                    RetailerContract.AREA_ID + " TEXT," +
                    RetailerContract.RETAILER_ID + " TEXT UNIQUE)";

    private static final String SQL_CREATE_VISITING_INFO =
            "CREATE TABLE " + VisitingInfoContract.TABLE_NAME + " (" +
                    VisitingInfoContract._ID + " INTEGER PRIMARY KEY," +
                    VisitingInfoContract.DATE_ADDED + " TEXT," +
                    VisitingInfoContract.RETAILER_ID + " TEXT," +
                    VisitingInfoContract.WEEK + " TEXT," +
                    VisitingInfoContract.DAY + " TEXT, UNIQUE(" + VisitingInfoContract.RETAILER_ID + "," + VisitingInfoContract.WEEK +
                    "," + VisitingInfoContract.DAY + "))";

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

    private static final String SQL_CREATE_HIGHEST_PURCHASE_VALUE =
            "CREATE TABLE " + HighestPurchaseValueContract.TABLE_NAME + " (" +
                    HighestPurchaseValueContract._ID + " INTEGER PRIMARY KEY," +
                    HighestPurchaseValueContract.RETAILER_ID + " TEXT UNIQUE," +
                    HighestPurchaseValueContract.VALUE + " TEXT )";

    private static final String SQL_CREATE_STOCK_COUNT =
            "CREATE TABLE " + StockCountContract.TABLE_NAME + " (" +
                    StockCountContract._ID + " INTEGER PRIMARY KEY," +
                    StockCountContract.PRODUCT_ID + " TEXT, " +
                    StockCountContract.DATE_ADDED + " TEXT," +
                    StockCountContract.RETAILER_ID + " TEXT," +
                    StockCountContract.OC + " TEXT, UNIQUE(" +
                    StockCountContract.PRODUCT_ID + "," +
                    StockCountContract.RETAILER_ID +
                    "))";

    private static final String SQL_CREATE_MERCHANDIZE_IMAGE_URL =
            "CREATE TABLE " + MerchandizeImageContract.TABLE_NAME + " (" +
                    MerchandizeImageContract._ID + " INTEGER PRIMARY KEY, " +
                    MerchandizeImageContract.DATE_ADDED + " TEXT, " +
                    MerchandizeImageContract.RETAILER_ID + " TEXT, " +
                    MerchandizeImageContract.CLOUDINARY_URL + " TEXT, " +
                    MerchandizeImageContract.CLOUDINARY_REQUEST_ID + " TEXT, " +
                    MerchandizeImageContract.UPLOAD_STATE + " TEXT, " +
                    MerchandizeImageContract.IMAGE_URI_ON_DISK + " TEXT )";

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

    private static final String SQL_DELETE_AREA_ENTRIES =
            "DROP TABLE IF EXISTS " + AreaContract.TABLE_NAME;

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

    private static final String SQL_DELETE_HIGHEST_PURCHASING_VALUE =
            "DROP TABLE IF EXISTS " + HighestPurchaseValueContract.TABLE_NAME;

    private static final String SQL_DELETE_STOCK_COUNT =
            "DROP TABLE IF EXISTS " + StockCountContract.TABLE_NAME;

    private static final String SQL_DELETE_MERCHANDIZE_IMAGE_URL =
            "DROP TABLE IF EXISTST " + MerchandizeImageContract.TABLE_NAME;


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
        db.execSQL(SQL_CREATE_AREA_ENTRIES);
        db.execSQL(SQL_CREATE_NEW_RETAILER);
        db.execSQL(SQL_CREATE_VISITING_INFO);
        db.execSQL(SQL_CREATE_INVOICE);
        db.execSQL(SQL_CREATE_PRODUCT_ORDER);
        db.execSQL(SQL_CREATE_MERCHANDIZING_VERIFICATION);
        db.execSQL(SQL_CREATE_HIGHEST_PURCHASE_VALUE);
        db.execSQL(SQL_CREATE_STOCK_COUNT);
        db.execSQL(SQL_CREATE_MERCHANDIZE_IMAGE_URL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_BRAND_ENTRIES);
        db.execSQL(SQL_DELETE_CHANNEL_ENTRIES);
        db.execSQL(SQL_DELETE_MERCHANDIZE_ENTRIES);
        db.execSQL(SQL_DELETE_PRODUCT_ENTRIES);
        db.execSQL(SQL_DELETE_SUB_CHANNEL_ENTRIES);
        db.execSQL(SQL_DELETE_AREA_ENTRIES);
        db.execSQL(SQL_DELETE_NEW_RETAILER_ENTRIES);
        db.execSQL(SQL_DELETE_VISITING_INFO_ENTRIES);
        db.execSQL(SQL_DELETE_INVOICE);
        db.execSQL(SQL_DELETE_PRODUCT_ORDER);
        db.execSQL(SQL_DELETE_MERCHANDIZING_VERIFICATION);
        db.execSQL(SQL_DELETE_HIGHEST_PURCHASING_VALUE);
        db.execSQL(SQL_DELETE_STOCK_COUNT);
        db.execSQL(SQL_CREATE_MERCHANDIZE_IMAGE_URL);
        onCreate(db);
    }


}
