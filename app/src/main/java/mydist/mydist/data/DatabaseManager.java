package mydist.mydist.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.List;

import mydist.mydist.models.Area;
import mydist.mydist.models.Brand;
import mydist.mydist.models.Channel;
import mydist.mydist.models.Invoice;
import mydist.mydist.models.Merchandize;
import mydist.mydist.models.MerchandizingVerification;
import mydist.mydist.models.NewRetailer;
import mydist.mydist.models.Product;
import mydist.mydist.models.ProductOrder;
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

    public boolean persistInvoice(Invoice invoice) {
        ContentValues contentValues;
        SQLiteDatabase mDatabase = mRouteDbHelper.getWritableDatabase();
        contentValues = new ContentValues();
        contentValues.put(InvoiceContract.INVOICE_ID, invoice.getId());
        contentValues.put(InvoiceContract.RETAILER_ID, invoice.getRetailerId());
        contentValues.put(InvoiceContract.DATE_ADDED, invoice.getDateAdded());
        contentValues.put(InvoiceContract.TOTAL, invoice.getTotal());
        contentValues.put(InvoiceContract.AMOUNT_PAID, invoice.getAmountPaid());
        contentValues.put(InvoiceContract.PAYMENT_MODE, invoice.getPaymentMode());
        contentValues.put(InvoiceContract.PAYMENT_MODE_VALUE, invoice.getPaymentModeValue());
        contentValues.put(InvoiceContract.STATUS, invoice.getStatus());
        long response = mDatabase.insertWithOnConflict(InvoiceContract.TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        if (response > -1) {
            boolean isSuccess = true;
            for (ProductOrder productOrder : invoice.getProductOrders()) {
                contentValues = new ContentValues();
                contentValues.put(ProductOrderContract.DATE_ADDED, productOrder.getDateAdded());
                contentValues.put(ProductOrderContract.INVOICE_ID, productOrder.getInvoiceId());
                contentValues.put(ProductOrderContract.TOTAL, productOrder.getTotal());
                contentValues.put(ProductOrderContract.PRODUCT_NAME, productOrder.getProductName());
                contentValues.put(ProductOrderContract.PRODUCT_ID, productOrder.getProductId());
                contentValues.put(ProductOrderContract.BRAND_ID, productOrder.getBrandId());
                contentValues.put(ProductOrderContract.OC, productOrder.getOc());
                contentValues.put(ProductOrderContract.OP, productOrder.getOp());
                response = mDatabase.insertWithOnConflict(ProductOrderContract.TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
                isSuccess = isSuccess && response > -1;
                if (!isSuccess)
                    return false;
            }
            return isSuccess;
        }
        return false;
    }

    public Cursor getProductsOrder(String invoiceId, String dateAdded) {
        final String QUERY = "SELECT " +
                ProductOrderContract.TABLE_NAME + "." + ProductOrderContract.PRODUCT_NAME + ", " +
                ProductOrderContract.TABLE_NAME + "." + ProductOrderContract.PRODUCT_ID + ", " +
                ProductOrderContract.OC + ", " +
                ProductOrderContract.OP + ", " +
                ProductContract.COLUMN_PIECE_PRICE + ", " +
                ProductContract.COLUMN_CASE_PRICE + ", " +
                RetailerContract.RETAILER_NAME + "," +
                InvoiceContract.TABLE_NAME + "." + InvoiceContract.TOTAL +
                " FROM " + ProductOrderContract.TABLE_NAME +
                " INNER JOIN " + ProductContract.TABLE_NAME +
                " ON " +
                ProductOrderContract.TABLE_NAME + "." + ProductOrderContract.PRODUCT_ID + " = " +
                ProductContract.TABLE_NAME + "." + ProductContract.COLUMN_PRODUCT_ID +
                " INNER JOIN " + InvoiceContract.TABLE_NAME +
                " ON " + InvoiceContract.TABLE_NAME + "." + InvoiceContract.INVOICE_ID + " = " +
                ProductOrderContract.TABLE_NAME + "." + ProductOrderContract.INVOICE_ID +
                " INNER JOIN " + RetailerContract.TABLE_NAME +
                " ON " + RetailerContract.TABLE_NAME + "." + RetailerContract.RETAILER_ID + " = " +
                InvoiceContract.TABLE_NAME + "." + InvoiceContract.RETAILER_ID +
                " WHERE " + ProductOrderContract.TABLE_NAME + "." + ProductOrderContract.INVOICE_ID + " = ? AND " +
                ProductOrderContract.TABLE_NAME + "." + ProductOrderContract.DATE_ADDED + " = ?";
        SQLiteDatabase db = mRouteDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(QUERY, new String[]{invoiceId, dateAdded});
        return cursor;
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
            values.put(MerchandizeContract.MERCHANDIZE_ID, merchandize.getMerchantId());
            values.put(MerchandizeContract.BRAND_ID, merchandize.getBrandId());
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

    public void persistAllArea(List<Area> areas) {
        ContentValues values;
        SQLiteDatabase mDataBase = mRouteDbHelper.getWritableDatabase();
        for (Area area : areas) {
            values = new ContentValues();
            values.put(AreaContract.COLUMN_AREA_ID, area.getAreaId());
            values.put(AreaContract.COLUMN_NAME, area.getAreaName());
            mDataBase.insertWithOnConflict(AreaContract.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
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
        values.put(RetailerContract.AREA_ID, newRetailer.getAreaId());
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
                    mDataBase.insertWithOnConflict(VisitingInfoContract.TABLE_NAME, null, visitingInfo, SQLiteDatabase.CONFLICT_REPLACE);
                }
            }
        } else {
            return false;
        }
        return true;
    }

    public boolean changeRetailerVisitingDate(String id, String dateAdded, String day, String week) {
        ContentValues visitingInfo = new ContentValues();
        SQLiteDatabase mDataBase = mRouteDbHelper.getWritableDatabase();
        visitingInfo.put(VisitingInfoContract.DATE_ADDED, dateAdded);
        visitingInfo.put(VisitingInfoContract.RETAILER_ID, id);
        visitingInfo.put(VisitingInfoContract.WEEK, week);
        visitingInfo.put(VisitingInfoContract.DAY, day);
        return mDataBase.insertWithOnConflict(VisitingInfoContract.TABLE_NAME, null, visitingInfo, SQLiteDatabase.CONFLICT_IGNORE) > 0;

    }

    public boolean persistMerchandizingVerification(List<MerchandizingVerification> merchandizingVerificationList) {
        MerchandizingVerification firstItem = merchandizingVerificationList.get(0);
        deleteMerchandizingVerification(firstItem.getRetailerId(), firstItem.getDateAdded());
        ContentValues values;
        boolean result = true;
        long response;
        SQLiteDatabase mDataBase = mRouteDbHelper.getWritableDatabase();
        for (MerchandizingVerification m : merchandizingVerificationList) {
            values = new ContentValues();
            values.put(MerchandizingListVerificationContract.DATE_ADDED, m.getDateAdded());
            values.put(MerchandizingListVerificationContract.MERCHANDIZING_ID, m.getMerchandizingId());
            values.put(MerchandizingListVerificationContract.RETAILER_ID, m.getRetailerId());
            values.put(MerchandizingListVerificationContract.BRAND_ID, m.getBrandId());
            values.put(MerchandizingListVerificationContract.AVAILABLE, m.getAvailable());
            response = mDataBase.insertWithOnConflict(MerchandizingListVerificationContract.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
            result = result && response > -1;
        }

        return result;

    }

    public Cursor queryAllBrand() {
        String[] projection = {
                BrandContract._ID,
                BrandContract.COLUMN_NAME,
                BrandContract.COLUMN_BRAND_ID};
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
        String[] projection = {
                ChannelContract._ID,
                ChannelContract.COLUMN_NAME,
                ChannelContract.COLUMN_CHANNEL_ID};
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

    public Cursor queryAllArea() {
        String[] projection = {
                AreaContract._ID,
                AreaContract.COLUMN_NAME,
                AreaContract.COLUMN_AREA_ID
        };
        String selection = null;
        String selectionArgs[] = null;
        String sortOrder = AreaContract.COLUMN_NAME + " ASC";
        SQLiteDatabase db = mRouteDbHelper.getReadableDatabase();
        Cursor allArea = db.query(
                AreaContract.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        return allArea;
    }

    public Cursor queryAllMerchandize() {
        String[] projection = {
                MerchandizeContract._ID,
                MerchandizeContract.BRAND_NAME,
                MerchandizeContract.COLUMN_MERCHANDIZE_ITEM,
                MerchandizeContract.MERCHANDIZE_ID,
                MerchandizeContract.BRAND_ID};
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
                ProductContract.COLUMN_PIECE_PRICE};
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
                sortOrder);
        return allProduct;
    }

    public Cursor queryAllProduct(String brandID) {
        String[] projection = {
                ProductContract._ID,
                ProductContract.COLUMN_NAME,
                ProductContract.COLUMN_PRODUCT_ID,
                ProductContract.COLUMN_BRAND_ID,
                ProductContract.COLUMN_CASE_PRICE,
                ProductContract.COLUMN_PIECE_PRICE};
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
        String[] projection = {
                SubChannelContract._ID,
                SubChannelContract.COLUMN_NAME,
                SubChannelContract.COLUMN_SUB_CHANNEL_ID};
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

    public Cursor getVisitingInfo(String retailerId) {
        final String Query = "SELECT * FROM " + VisitingInfoContract.TABLE_NAME +
                " WHERE " + VisitingInfoContract.RETAILER_ID + " = ?";
        SQLiteDatabase db = mRouteDbHelper.getReadableDatabase();
        return db.rawQuery(Query, new String[]{retailerId});
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
                RetailerContract.SUB_CHANNEL_ID + "," +
                RetailerContract.AREA_ID +
                " FROM " + RetailerContract.TABLE_NAME;
        SQLiteDatabase db = mRouteDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(QUERY, new String[]{});
        return cursor;
    }

    public Cursor getAllRetailerExceptTheCurrentDate(String filter) {
        String QUERY = "SELECT " +
                RetailerContract.TABLE_NAME + "." + RetailerContract._ID + "," +
                RetailerContract.TABLE_NAME + "." + RetailerContract.DATE_ADDED + "," +
                RetailerContract.TABLE_NAME + "." + RetailerContract.RETAILER_ID + "," +
                RetailerContract.RETAILER_NAME + "," +
                RetailerContract.CONTACT_PERSON_NAME + "," +
                RetailerContract.ADDRESS + "," +
                RetailerContract.PHONE + "," +
                RetailerContract.CHANNEL_ID + "," +
                RetailerContract.SUB_CHANNEL_ID + "," +
                RetailerContract.AREA_ID +
                " FROM " + RetailerContract.TABLE_NAME;
        if (filter != null) {
            QUERY += " WHERE " + RetailerContract.RETAILER_ID + " NOT IN " + filter;
        }
        SQLiteDatabase db = mRouteDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(QUERY, new String[]{});
        return cursor;
    }

    public Cursor getAllNewRetailers(String date) {
        final String QUERY = "SELECT " +
                RetailerContract.TABLE_NAME + "." + RetailerContract.RETAILER_ID + "," +
                RetailerContract.TABLE_NAME + "." + RetailerContract.DATE_ADDED + "," +
                RetailerContract.TABLE_NAME + "." + RetailerContract.RETAILER_ID + "," +
                RetailerContract.RETAILER_NAME + "," +
                RetailerContract.CONTACT_PERSON_NAME + "," +
                RetailerContract.ADDRESS + "," +
                RetailerContract.PHONE + "," +
                RetailerContract.CHANNEL_ID + "," +
                RetailerContract.SUB_CHANNEL_ID + "," +
                RetailerContract.AREA_ID +
                " FROM " + RetailerContract.TABLE_NAME +
                " WHERE " + RetailerContract.DATE_ADDED + " = ?";
        SQLiteDatabase db = mRouteDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(QUERY, new String[]{date});
        return cursor;
    }

    public Cursor getRetailerProfileById(String id) {
        final String QUERY = "SELECT " +
                RetailerContract.TABLE_NAME + "." + RetailerContract._ID + "," +
                RetailerContract.TABLE_NAME + "." + RetailerContract.DATE_ADDED + "," +
                RetailerContract.TABLE_NAME + "." + RetailerContract.RETAILER_ID + "," +
                RetailerContract.RETAILER_NAME + "," +
                RetailerContract.CONTACT_PERSON_NAME + "," +
                RetailerContract.ADDRESS + "," +
                RetailerContract.PHONE + "," +
                ChannelContract.TABLE_NAME + "." + ChannelContract.COLUMN_NAME + "," +
                SubChannelContract.TABLE_NAME + "." + SubChannelContract.COLUMN_NAME +
                " FROM " + RetailerContract.TABLE_NAME +
                " INNER JOIN " + ChannelContract.TABLE_NAME + " ON " +
                ChannelContract.TABLE_NAME + "." +
                ChannelContract.COLUMN_CHANNEL_ID + " = " +
                RetailerContract.TABLE_NAME + "." +
                RetailerContract.CHANNEL_ID +
                " INNER JOIN " +
                SubChannelContract.TABLE_NAME + " ON " +
                SubChannelContract.TABLE_NAME + "." +
                SubChannelContract.COLUMN_SUB_CHANNEL_ID + " = " +
                RetailerContract.TABLE_NAME + "." +
                RetailerContract.SUB_CHANNEL_ID +
                " INNER JOIN " +
                AreaContract.TABLE_NAME + " ON " +
                AreaContract.TABLE_NAME + "." +
                AreaContract.COLUMN_AREA_ID + " = " +
                RetailerContract.TABLE_NAME + "." +
                RetailerContract.AREA_ID +
                " WHERE " + RetailerContract.RETAILER_ID + " = ?";


        SQLiteDatabase db = mRouteDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(QUERY, new String[]{id});
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
                RetailerContract.SUB_CHANNEL_ID,
                RetailerContract.AREA_ID
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
                RetailerContract.SUB_CHANNEL_ID + "," +
                RetailerContract.AREA_ID +
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

    public Cursor getInvoicePush(String day) {
        final String QUERY = "SELECT " +
                ProductOrderContract.TABLE_NAME + "." + ProductOrderContract.INVOICE_ID + " AS " +
                ProductOrderContract.INVOICE_ID_ALIAS + ", " +
                ProductOrderContract.TABLE_NAME + "." + ProductOrderContract.PRODUCT_ID + " AS " +
                ProductOrderContract.PRODUCT_ID_ALIAS + ", " +
                ProductOrderContract.OC + ", " +
                ProductOrderContract.OP + ", " +
                ProductContract.COLUMN_CASE_PRICE + ", " +
                ProductContract.COLUMN_PIECE_PRICE + ", " +
                InvoiceContract.TABLE_NAME + "." + InvoiceContract.TOTAL + " AS " +
                InvoiceContract.TOTAL_ALIAS + ", " +
                InvoiceContract.AMOUNT_PAID + ", " +
                InvoiceContract.RETAILER_ID + ", " +
                InvoiceContract.STATUS + " FROM " +
                ProductOrderContract.TABLE_NAME +
                " INNER JOIN " + ProductContract.TABLE_NAME + " ON " +
                ProductContract.TABLE_NAME + "." + ProductContract.COLUMN_PRODUCT_ID +
                " = " + ProductOrderContract.TABLE_NAME + "." + ProductOrderContract.PRODUCT_ID +
                " INNER JOIN " + InvoiceContract.TABLE_NAME + " ON " +
                ProductOrderContract.TABLE_NAME + "." + ProductOrderContract.INVOICE_ID + " = " +
                InvoiceContract.TABLE_NAME + "." + InvoiceContract.INVOICE_ID + " WHERE " +
                ProductOrderContract.TABLE_NAME + "." + ProductOrderContract.DATE_ADDED + " = ?";
        SQLiteDatabase db = mRouteDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(QUERY, new String[]{day});
        return cursor;
    }

    public Cursor getProductInvoiceByBrandId(String brandId, String dateAdded) {
        final String QUERY = "SELECT " +
                ProductOrderContract._ID + ", " +
                ProductOrderContract.PRODUCT_NAME + "," +
                "COUNT(" + ProductOrderContract.PRODUCT_NAME + ") AS " +
                ProductOrderContract.PRODUCT_COUNT +
                " FROM " + ProductOrderContract.TABLE_NAME + " WHERE " +
                ProductOrderContract.BRAND_ID + " = ? " + " AND " +
                ProductOrderContract.DATE_ADDED + " = ?" +
                " GROUP BY " + ProductOrderContract.PRODUCT_NAME;

        SQLiteDatabase db = mRouteDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(QUERY, new String[]{brandId, dateAdded});
        return cursor;
    }

    public Cursor queryAllInvoiceByRetailerId(String retailerId, String dateAdded, int status) {
        String[] projection = new String[]{
                InvoiceContract._ID,
                InvoiceContract.INVOICE_ID,
                InvoiceContract.RETAILER_ID,
                InvoiceContract.DATE_ADDED,
                InvoiceContract.TOTAL,
                InvoiceContract.AMOUNT_PAID,
                InvoiceContract.PAYMENT_MODE,
                InvoiceContract.PAYMENT_MODE_VALUE,
                InvoiceContract.STATUS};
        String firstClause = retailerId != null ? InvoiceContract.RETAILER_ID + " = ? " + " AND " :
                InvoiceContract.STATUS + " = ? AND ";
        String selection = firstClause + InvoiceContract.DATE_ADDED + " = ?";
        String selectionArgs[] = retailerId != null ? new String[]{retailerId, dateAdded} :
                new String[]{String.valueOf(status), dateAdded};
        if (retailerId != null && status == Invoice.KEY_STATUS_SUCCESS) {
            selection = InvoiceContract.RETAILER_ID + " = ? " + " AND " + InvoiceContract.DATE_ADDED + " = ? AND "
                    + InvoiceContract.STATUS + " = ? ";
            selectionArgs = new String[]{retailerId, dateAdded, String.valueOf(status)};
        }
        String sortOrder = InvoiceContract.INVOICE_ID + " ASC";
        SQLiteDatabase db = mRouteDbHelper.getReadableDatabase();
        Cursor invoice = db.query(
                InvoiceContract.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        return invoice;
    }

    public Cursor queryAllOrder(String dateAdded, int status) {
        String QUERY = "SELECT " +
                InvoiceContract.TABLE_NAME + "." + InvoiceContract._ID + ", " +
                InvoiceContract.TOTAL + ", " +
                InvoiceContract.TABLE_NAME + "." + InvoiceContract.INVOICE_ID + ", " +
                RetailerContract.RETAILER_NAME +
                " FROM " + InvoiceContract.TABLE_NAME +
                " INNER JOIN " + RetailerContract.TABLE_NAME +
                " ON " + RetailerContract.TABLE_NAME + "." + RetailerContract.RETAILER_ID + " = " +
                InvoiceContract.TABLE_NAME + "." + InvoiceContract.RETAILER_ID + " WHERE " +
                InvoiceContract.TABLE_NAME + "." + InvoiceContract.DATE_ADDED + " = ? AND " +
                InvoiceContract.STATUS + " = ?";

        SQLiteDatabase db = mRouteDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(QUERY, new String[]{dateAdded, String.valueOf(status)});
        return cursor;

    }

    public Cursor queryAllOrderTotal(String dateAdded, int status) {
        String QUERY = "SELECT " +
                "SUM(CAST(" + InvoiceContract.TOTAL + " AS FLOAT))  AS " + InvoiceContract.TOTAL_ALIAS + ", " +
                InvoiceContract.TABLE_NAME + "." + InvoiceContract.INVOICE_ID +
                " FROM " + InvoiceContract.TABLE_NAME +
                " WHERE " +
                InvoiceContract.TABLE_NAME + "." + InvoiceContract.DATE_ADDED + " = ? AND " +
                InvoiceContract.STATUS + " = ?";
        SQLiteDatabase db = mRouteDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(QUERY, new String[]{dateAdded, String.valueOf(status)});
        return cursor;

    }

    public Cursor queryCollectionTotal(String dateAdded, int status) {
        String QUERY = "SELECT  * "
               /* "SUM(CAST(" + InvoiceContract.AMOUNT_PAID + " AS FLOAT))  AS " + InvoiceContract.TOTAL_ALIAS */ +
                " FROM " + InvoiceContract.TABLE_NAME +
                " WHERE " +
                InvoiceContract.DATE_ADDED + " = ? AND " +
                InvoiceContract.STATUS + " = ?";
        SQLiteDatabase db = mRouteDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(QUERY, new String[]{dateAdded, String.valueOf(status)});
        return cursor;

    }

    public Cursor getMerchandisingVerification(String retailerId, String dateAdded) {
        String[] projection = new String[]{
                MerchandizingListVerificationContract._ID,
                MerchandizingListVerificationContract.DATE_ADDED,
                MerchandizingListVerificationContract.RETAILER_ID,
                MerchandizingListVerificationContract.MERCHANDIZING_ID,
                MerchandizingListVerificationContract.BRAND_ID,
                MerchandizingListVerificationContract.AVAILABLE};
        String selection = MerchandizingListVerificationContract.RETAILER_ID + " = ? " + " AND " + MerchandizingListVerificationContract.DATE_ADDED + " = ?";
        String selectionArgs[] = new String[]{retailerId, dateAdded};
        String sortOrder = MerchandizingListVerificationContract.MERCHANDIZING_ID + " ASC";
        SQLiteDatabase db = mRouteDbHelper.getReadableDatabase();
        Cursor merchandizingVerification = db.query(
                MerchandizingListVerificationContract.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
        return merchandizingVerification;
    }

    public Cursor getAllMerchandising(String dateAdded) {
        String[] projection = new String[]{
                MerchandizingListVerificationContract._ID,
                MerchandizingListVerificationContract.DATE_ADDED,
                MerchandizingListVerificationContract.RETAILER_ID,
                MerchandizingListVerificationContract.MERCHANDIZING_ID,
                MerchandizingListVerificationContract.BRAND_ID,
                MerchandizingListVerificationContract.AVAILABLE};
        String selection = MerchandizingListVerificationContract.DATE_ADDED + " = ?";
        String selectionArgs[] = new String[]{dateAdded};
        String sortOrder = MerchandizingListVerificationContract.MERCHANDIZING_ID + " ASC";
        SQLiteDatabase db = mRouteDbHelper.getReadableDatabase();

        Cursor merchandizingVerification = db.query(
                MerchandizingListVerificationContract.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        return merchandizingVerification;

    }

    private void deleteMerchandizingVerification(String retailerId, String date) {
        SQLiteDatabase db = mRouteDbHelper.getWritableDatabase();
        db.delete(MerchandizingListVerificationContract.TABLE_NAME
                , MerchandizingListVerificationContract.RETAILER_ID + " = ? AND " +
                        MerchandizingListVerificationContract.DATE_ADDED + " = ?", new String[]{retailerId, date});
    }

    public boolean cancelInvoice(String invoiceId) {
        SQLiteDatabase db = mRouteDbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(InvoiceContract.STATUS, Invoice.KEY_STATUS_CANCEL);
        long result = db.update(InvoiceContract.TABLE_NAME, cv, InvoiceContract.INVOICE_ID + " = ?", new String[]{invoiceId});
        return result > 0;
    }

    public boolean updateAmountPaidForInvoice(String invoiceId, String amount, String modeOfPayment, String modeOfPaymentValue) {
        SQLiteDatabase db = mRouteDbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(InvoiceContract.AMOUNT_PAID, amount);
        cv.put(InvoiceContract.PAYMENT_MODE, modeOfPayment);
        cv.put(InvoiceContract.PAYMENT_MODE_VALUE, modeOfPaymentValue);
        long result = db.update(InvoiceContract.TABLE_NAME, cv, InvoiceContract.INVOICE_ID + " = ?", new String[]{invoiceId});
        return result > 0;
    }

    public Cursor queryCoverageCount(String todayDate, int status) {
        final String QUERY = "SELECT " +
                "COUNT( DISTINCT " + InvoiceContract.RETAILER_ID + ") AS " +
                InvoiceContract.TOTAL_ALIAS +
                " FROM " + InvoiceContract.TABLE_NAME + " WHERE " +
                InvoiceContract.DATE_ADDED + " = ?" + " AND "
                + InvoiceContract.STATUS + " = ? " +
                " GROUP BY " + InvoiceContract.RETAILER_ID;
        SQLiteDatabase db = mRouteDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(QUERY, new String[]{todayDate, String.valueOf(status)});
        return cursor;
    }
}
