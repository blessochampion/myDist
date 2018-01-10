package mydist.mydist.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.List;

import mydist.mydist.models.Brand;
import mydist.mydist.models.Channel;
import mydist.mydist.models.Merchandize;
import mydist.mydist.models.NewRetailer;
import mydist.mydist.models.Product;
import mydist.mydist.models.Retailer;
import mydist.mydist.models.SubChannel;

import static mydist.mydist.data.MasterContract.*;

/**
 * Singleton that controls access to the SQLiteDatabase instance
 * for this application.
 */
public class DatabaseManager {
    private static DatabaseManager sInstance;

    public static synchronized DatabaseManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseManager(context.getApplicationContext());
        }

        return sInstance;
    }

    private RouteDbHelper mRouteDbHelper;

    public RouteDbHelper getRouteDbHelper() {
        return mRouteDbHelper;
    }

    private DatabaseManager(Context context) {
        mRouteDbHelper = new RouteDbHelper(context);
    }

    public void persistAllBrand(List<Brand> brands) {
        ContentValues values;
        SQLiteDatabase mDataBase = mRouteDbHelper.getWritableDatabase();
        for (Brand brand : brands) {
            values = new ContentValues();
            values.put(BrandContract.COLUMN_BRAND_ID, brand.getBrandId());
            values.put(BrandContract.COLUMN_NAME, brand.getBrandName());
            mDataBase.insertWithOnConflict(BrandContract.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        }
        mDataBase.close();

    }

    public void persistAllChannel(List<Channel> channels) {
        ContentValues values;
        SQLiteDatabase mDataBase = mRouteDbHelper.getWritableDatabase();
        for (Channel channel : channels) {
            values = new ContentValues();
            values.put(ChannelContract.COLUMN_CHANNEL_ID, channel.getChannelId());
            values.put(ChannelContract.COLUMN_NAME, channel.getChannelName());
            mDataBase.insertWithOnConflict(ChannelContract.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        }

        mDataBase.close();
    }

    public void persistAllMerchandize(List<Merchandize> merchandizeList) {
        ContentValues values;
        SQLiteDatabase mDataBase = mRouteDbHelper.getWritableDatabase();
        for (Merchandize merchandize : merchandizeList) {
            values = new ContentValues();
            values.put(MerchandizeContract.BRAND_NAME, merchandize.getBrandName());
            values.put(MerchandizeContract.COLUMN_MERCHANDIZE_ITEM, merchandize.getMerchandizeItem());
            mDataBase.insertWithOnConflict(MerchandizeContract.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        }
        mDataBase.close();
    }

    public void persistAllProduct(List<Product> products) {
        ContentValues values;
        SQLiteDatabase mDataBase = mRouteDbHelper.getWritableDatabase();
        for (Product product : products) {
            values = new ContentValues();
            values.put(ProductContract.COLUMN_BRAND_ID, product.getBrandId());
            values.put(ProductContract.COLUMN_CASE_PRICE, Double.valueOf(product.getCasePrice()));
            values.put(ProductContract.COLUMN_NAME, product.getProductName());
            values.put(ProductContract.COLUMN_PIECE_PRICE, Double.valueOf(product.getPiecePrice()));
            values.put(ProductContract.COLUMN_PRODUCT_ID, product.getProductId());
            mDataBase.insertWithOnConflict(ProductContract.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        }
        mDataBase.close();
    }

    public void persistAllSubChannel(List<SubChannel> subChannels) {
        ContentValues values;
        SQLiteDatabase mDataBase = mRouteDbHelper.getWritableDatabase();
        for (SubChannel subChannel : subChannels) {
            values = new ContentValues();
            values.put(SubChannelContract.COLUMN_SUB_CHANNEL_ID, subChannel.getSubChannelId());
            values.put(SubChannelContract.COLUMN_NAME, subChannel.getSubChannelName());
            mDataBase.insertWithOnConflict(SubChannelContract.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        }

        mDataBase.close();
    }

    public boolean persistNewRetailer(NewRetailer newRetailer) {
        ContentValues values = new ContentValues();
        SQLiteDatabase mDataBase = mRouteDbHelper.getWritableDatabase();
        values.put(RetailerContract.DATE_ADDED, newRetailer.getDateAdded());
        values.put(RetailerContract.RETAILER_NAME, newRetailer.getName());
        values.put(RetailerContract.CONTACT_PERSON_NAME, newRetailer.getContactPersonName());
        values.put(RetailerContract.ADDRESS, newRetailer.getAddress());
        values.put(RetailerContract.PHONE, newRetailer.getPhone());
        values.put(RetailerContract.CHANNEL_ID, newRetailer.getChannel());
        values.put(RetailerContract.SUB_CHANNEL_ID, newRetailer.getSubChannel());
        values.put(RetailerContract.RETAILER_ID, newRetailer.getRetailerId());
        long id = mDataBase.insert(RetailerContract.TABLE_NAME, null, values);
        if (id > -1) {
            ContentValues visitingInfo;
            for (String week :
                    newRetailer.getWeekNos()) {
                for (String day : newRetailer.getVisitDays()
                        ) {
                    visitingInfo = new ContentValues();
                    visitingInfo.put(VisitingInfoContract.DATE_ADDED, newRetailer.getDateAdded());
                    visitingInfo.put(VisitingInfoContract.RETAILER_ID, newRetailer.getRetailerId());
                    visitingInfo.put(VisitingInfoContract.WEEK, week);
                    visitingInfo.put(VisitingInfoContract.DAY, day);
                    mDataBase.insertWithOnConflict(VisitingInfoContract.TABLE_NAME, null, visitingInfo, SQLiteDatabase.CONFLICT_IGNORE);
                }

            }
        } else {
            return false;
        }


        return true;
    }

    public Cursor queryAllBrand() {
        //TODO: Implement the query
        String[] projection = {
                BrandContract._ID,
                BrandContract.COLUMN_NAME,
                BrandContract.COLUMN_BRAND_ID
        };

        String selection = null;
        String selectionArgs[] = null;
        String sortOrder = BrandContract.COLUMN_NAME + " ASC";

        SQLiteDatabase db = mRouteDbHelper.getReadableDatabase();
        Cursor allBrand = db.query(
                BrandContract.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        return allBrand;
    }

    public Cursor queryAllChannel() {
        //TODO: Implement the query
        String[] projection = {
                ChannelContract._ID,
                ChannelContract.COLUMN_NAME,
                ChannelContract.COLUMN_CHANNEL_ID
        };

        String selection = null;
        String selectionArgs[] = null;
        String sortOrder = ChannelContract.COLUMN_NAME + " ASC";

        SQLiteDatabase db = mRouteDbHelper.getReadableDatabase();
        Cursor allChannel = db.query(
                ChannelContract.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        return allChannel;
    }

    public Cursor queryAllMerchandize() {
        //TODO: Implement the query
        String[] projection = {
                MerchandizeContract._ID,
                MerchandizeContract.BRAND_NAME,
                MerchandizeContract.COLUMN_MERCHANDIZE_ITEM
        };

        String selection = null;
        String selectionArgs[] = null;
        String sortOrder = MerchandizeContract.BRAND_NAME + " ASC";

        SQLiteDatabase db = mRouteDbHelper.getReadableDatabase();
        Cursor allMerchandize = db.query(
                MerchandizeContract.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        return allMerchandize;
    }

    public Cursor queryAllProduct() {
        //TODO: Implement the query
        String[] projection = {
                ProductContract._ID,
                ProductContract.COLUMN_NAME,
                ProductContract.COLUMN_PRODUCT_ID,
                ProductContract.COLUMN_BRAND_ID,
                ProductContract.COLUMN_CASE_PRICE,
                ProductContract.COLUMN_PIECE_PRICE
        };

        String selection = null;
        String selectionArgs[] = null;
        String sortOrder = ProductContract.COLUMN_NAME + " ASC";

        SQLiteDatabase db = mRouteDbHelper.getReadableDatabase();
        Cursor allProduct = db.query(
                ProductContract.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        return allProduct;
    }

    public Cursor queryAllProduct(String brandID) {
        //TODO: Implement the query
        String[] projection = {
                ProductContract._ID,
                ProductContract.COLUMN_NAME,
                ProductContract.COLUMN_PRODUCT_ID,
                ProductContract.COLUMN_BRAND_ID,
                ProductContract.COLUMN_CASE_PRICE,
                ProductContract.COLUMN_PIECE_PRICE
        };

        String selection = ProductContract.COLUMN_BRAND_ID + " = ?";
        String selectionArgs[] = {brandID};
        String sortOrder = ProductContract.COLUMN_NAME + " ASC";

        SQLiteDatabase db = mRouteDbHelper.getReadableDatabase();
        Cursor allProduct = db.query(
                ProductContract.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        return allProduct;
    }

    public Cursor queryAllSubChannel() {
        //TODO: Implement the query
        String[] projection = {
                SubChannelContract._ID,
                SubChannelContract.COLUMN_NAME,
                SubChannelContract.COLUMN_SUB_CHANNEL_ID
        };

        String selection = null;
        String selectionArgs[] = null;
        String sortOrder = SubChannelContract.COLUMN_NAME + " ASC";

        SQLiteDatabase db = mRouteDbHelper.getReadableDatabase();
        Cursor allSubChannel = db.query(
                SubChannelContract.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        return allSubChannel;
    }

    public void getAllVisitingInfo() {
        final String Query = "SELECT * FROM " + VisitingInfoContract.TABLE_NAME;
        SQLiteDatabase db = mRouteDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(Query, new String[]{});
        if (cursor != null) {
            int count = cursor.getCount();
            cursor.moveToFirst();
            for (int i = 0; i < count; i++) {
                Log.e("u: ",
                        cursor.getString(cursor.getColumnIndex(VisitingInfoContract.RETAILER_ID)) +
                                " " + cursor.getString(cursor.getColumnIndex(VisitingInfoContract.WEEK))
                                + " " + cursor.getString(cursor.getColumnIndex(VisitingInfoContract.DAY)));
                cursor.moveToNext();
            }
        }
    }

    public Cursor getAllRetailer() {
        final String QUERY = "SELECT " +
                RetailerContract.TABLE_NAME + "." + RetailerContract._ID + "," +
                RetailerContract.TABLE_NAME + "." + RetailerContract.DATE_ADDED + "," +
                RetailerContract.TABLE_NAME + "." + RetailerContract.RETAILER_ID + "," +
                RetailerContract.RETAILER_NAME + "," +
                RetailerContract.CONTACT_PERSON_NAME + "," +
                RetailerContract.ADDRESS + "," +
                RetailerContract.PHONE + "," +
                RetailerContract.CHANNEL_ID + "," +
                RetailerContract.SUB_CHANNEL_ID +
                " FROM " + RetailerContract.TABLE_NAME;
        SQLiteDatabase db = mRouteDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(QUERY, new String[]{});
        return cursor;
    }

    public Cursor getRetailerById(String id) {
        String[] projection = {
                RetailerContract._ID,
                RetailerContract.DATE_ADDED,
                RetailerContract.RETAILER_ID,
                RetailerContract.RETAILER_NAME,
                RetailerContract.CONTACT_PERSON_NAME,
                RetailerContract.ADDRESS,
                RetailerContract.PHONE,
                RetailerContract.CHANNEL_ID,
                RetailerContract.SUB_CHANNEL_ID
        };

        String selection = RetailerContract.RETAILER_ID + " = ?";
        String selectionArgs[] = {id};
        String sortOrder = RetailerContract.RETAILER_NAME + " ASC";

        SQLiteDatabase db = mRouteDbHelper.getReadableDatabase();
        Cursor retailer = db.query(
                RetailerContract.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        return retailer;
    }

    public Cursor getRetailerByVisitingInfo(String week, String day) {
        final String QUERY = "SELECT " +
                RetailerContract.TABLE_NAME + "." + RetailerContract._ID + "," +
                RetailerContract.TABLE_NAME + "." + RetailerContract.DATE_ADDED + "," +
                RetailerContract.TABLE_NAME + "." + RetailerContract.RETAILER_ID + "," +
                RetailerContract.RETAILER_NAME + "," +
                RetailerContract.CONTACT_PERSON_NAME + "," +
                RetailerContract.ADDRESS + "," +
                RetailerContract.PHONE + "," +
                RetailerContract.CHANNEL_ID + "," +
                RetailerContract.SUB_CHANNEL_ID +
                " FROM " + RetailerContract.TABLE_NAME +
                " INNER JOIN " + VisitingInfoContract.TABLE_NAME + " ON " +
                VisitingInfoContract.TABLE_NAME + "." +
                VisitingInfoContract.RETAILER_ID + " = " +
                RetailerContract.TABLE_NAME + "." +
                RetailerContract.RETAILER_ID +
                " WHERE " +
                VisitingInfoContract.TABLE_NAME + "." + VisitingInfoContract.WEEK + " =? AND " +
                VisitingInfoContract.TABLE_NAME + "." + VisitingInfoContract.DAY + " =? ";

        SQLiteDatabase db = mRouteDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(QUERY, new String[]{week, day});
        return cursor;

    }
}
