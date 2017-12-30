package mydist.mydist.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import mydist.mydist.models.Brand;
import mydist.mydist.models.Channel;
import mydist.mydist.models.Merchandize;
import mydist.mydist.models.Product;
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

    public void persistAllProduct(List<Product> products){
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
        String sortOrder =  MerchandizeContract.BRAND_NAME + " ASC";

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

}
