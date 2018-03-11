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
import mydist.mydist.models.SubChannel;

import static mydist.mydist.data.MasterContract.AreaContract;
import static mydist.mydist.data.MasterContract.BrandContract;
import static mydist.mydist.data.MasterContract.ChannelContract;
import static mydist.mydist.data.MasterContract.HighestPurchaseValueContract;
import static mydist.mydist.data.MasterContract.InvoiceContract;
import static mydist.mydist.data.MasterContract.MerchandizeContract;
import static mydist.mydist.data.MasterContract.MerchandizingListVerificationContract;
import static mydist.mydist.data.MasterContract.ProductContract;
import static mydist.mydist.data.MasterContract.ProductOrderContract;
import static mydist.mydist.data.MasterContract.RetailerContract;
import static mydist.mydist.data.MasterContract.StockCountContract;
import static mydist.mydist.data.MasterContract.SubChannelContract;
import static mydist.mydist.data.MasterContract.VisitingInfoContract;

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

    public void persistAllMerchandize(List<Merchandize> merchandizeList, String dateAdded) {
        ContentValues values;
        SQLiteDatabase mDataBase = mRouteDbHelper.getWritableDatabase();
        for (Merchandize merchandize : merchandizeList) {
            values = new ContentValues();
            values.put(MerchandizeContract.BRAND_NAME, merchandize.getBrandName());
            values.put(MerchandizeContract.COLUMN_MERCHANDIZE_ITEM, merchandize.getMerchandizeItem());
            values.put(MerchandizeContract.MERCHANDIZE_ID, merchandize.getMerchantId());
            values.put(MerchandizeContract.BRAND_ID, merchandize.getBrandId());
            values.put(MerchandizeContract.DATE_ADDED, dateAdded);
            mDataBase.insertWithOnConflict(MerchandizeContract.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
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

    public boolean updateHPV(String retailerId, String values) {
        SQLiteDatabase mDatabase = mRouteDbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(HighestPurchaseValueContract.VALUE, values);
        long noOfRowsAffected = mDatabase.update(HighestPurchaseValueContract.TABLE_NAME, cv, HighestPurchaseValueContract.RETAILER_ID + " = ?", new String[]{retailerId});
        mDatabase.close();
        return noOfRowsAffected > 0;
    }

    private boolean saveHPV(SQLiteDatabase mDataBase, String retailerId, String values) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(HighestPurchaseValueContract.RETAILER_ID, retailerId);
        contentValues.put(HighestPurchaseValueContract.VALUE, values);
        long rowId = mDataBase.insertWithOnConflict(HighestPurchaseValueContract.TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
        mDataBase.close();
        return rowId > -1;
    }

    public boolean persistNewRetailer(NewRetailer newRetailer, String initialHPV) {
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
        return saveHPV(mDataBase, newRetailer.getRetailerId(), initialHPV);
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

    public Cursor getAllRetailerExceptTheCurrentDate(String filter, String nameFilter) {
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
                RetailerContract.AREA_ID + "," +
                HighestPurchaseValueContract.VALUE +
                " FROM " + RetailerContract.TABLE_NAME +
                " INNER JOIN " + HighestPurchaseValueContract.TABLE_NAME + " ON " +
                RetailerContract.TABLE_NAME + "." + RetailerContract.RETAILER_ID + " = " +
                HighestPurchaseValueContract.TABLE_NAME + "." + HighestPurchaseValueContract.RETAILER_ID;
        if (filter != null) {
            QUERY += " WHERE " + RetailerContract.TABLE_NAME + "." + RetailerContract.RETAILER_ID + " NOT IN " + filter;
        }
        if (nameFilter != null) {
            if (filter != null) {
                QUERY += " AND " + RetailerContract.RETAILER_NAME + " LIKE \"%" + nameFilter + "%\"";
            } else {
                QUERY += " WHERE " + RetailerContract.RETAILER_NAME + " LIKE \"%" + nameFilter + "%\"";
            }
        }
        SQLiteDatabase db = mRouteDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(QUERY, new String[]{});
        return cursor;
    }

    public Cursor getAllRetailerExceptTheCurrentDate(String filter) {
        return getAllRetailerExceptTheCurrentDate(filter, null);
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
                ChannelContract.COLUMN_NAME + "," +
                SubChannelContract.COLUMN_NAME + "," +
                AreaContract.COLUMN_NAME +
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

    public Cursor getRetailerByVisitingInfo(String week, String day, String filter) {
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
                RetailerContract.AREA_ID + " , " +
                HighestPurchaseValueContract.VALUE +
                " FROM " + RetailerContract.TABLE_NAME +
                " INNER JOIN " + VisitingInfoContract.TABLE_NAME + " ON " +
                VisitingInfoContract.TABLE_NAME + "." +
                VisitingInfoContract.RETAILER_ID + " = " +
                RetailerContract.TABLE_NAME + "." +
                RetailerContract.RETAILER_ID +
                " INNER JOIN " + HighestPurchaseValueContract.TABLE_NAME + " ON " +
                RetailerContract.TABLE_NAME + "." + RetailerContract.RETAILER_ID + " = " +
                HighestPurchaseValueContract.TABLE_NAME + "." + HighestPurchaseValueContract.RETAILER_ID +
                " WHERE " +
                VisitingInfoContract.TABLE_NAME + "." + VisitingInfoContract.WEEK + " =? AND " +
                VisitingInfoContract.TABLE_NAME + "." + VisitingInfoContract.DAY + " =? ";
        if (filter != null) {
            QUERY += " AND " + RetailerContract.RETAILER_NAME + " LIKE \"%" + filter + "%\"";

        }
        SQLiteDatabase db = mRouteDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(QUERY, new String[]{week, day});
        return cursor;
    }

    public String getHighestPurchaseValue(String retailerId) {
        String[] projection = {
                HighestPurchaseValueContract.VALUE};
        String selection = null;
        String selectionArgs[] = null;
        String sortOrder = null;
        SQLiteDatabase db = mRouteDbHelper.getReadableDatabase();
        Cursor retailerCursor = db.query(
                HighestPurchaseValueContract.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        retailerCursor.moveToFirst();
        return retailerCursor.getString(retailerCursor.getColumnIndex(HighestPurchaseValueContract.VALUE));
    }

    public Cursor getRetailerByVisitingInfo(String week, String day) {
        return getRetailerByVisitingInfo(week, day, null);
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

    public Cursor queryAllInvoiceByRetailerId(String retailerId) {
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

        String selection = InvoiceContract.RETAILER_ID + " = ? ";
        String[] selectionArgs = new String[]{retailerId};
        String sortOrder = InvoiceContract.DATE_ADDED + " DESC";
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
                "SUM(CAST(" + InvoiceContract.AMOUNT_PAID + " AS FLOAT))  AS " + InvoiceContract.AMOUNT_PAID_ALIAS + ", " +
                InvoiceContract.TABLE_NAME + "." + InvoiceContract.INVOICE_ID +
                " FROM " + InvoiceContract.TABLE_NAME +
                " WHERE " +
                InvoiceContract.TABLE_NAME + "." + InvoiceContract.DATE_ADDED + " = ? AND " +
                InvoiceContract.STATUS + " = ?";
        SQLiteDatabase db = mRouteDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(QUERY, new String[]{dateAdded, String.valueOf(status)});
        return cursor;
    }

    public Cursor queryAllOrderTotal(String retailerId, String dateAdded, int status) {
        String QUERY = "SELECT " +
                "SUM(CAST(" + InvoiceContract.TOTAL + " AS FLOAT))  AS " + InvoiceContract.TOTAL_ALIAS + ", " +
                "SUM(CAST(" + InvoiceContract.AMOUNT_PAID + " AS FLOAT))  AS " + InvoiceContract.AMOUNT_PAID_ALIAS + ", " +
                InvoiceContract.TABLE_NAME + "." + InvoiceContract.INVOICE_ID +
                " FROM " + InvoiceContract.TABLE_NAME +
                " WHERE " +
                InvoiceContract.TABLE_NAME + "." + InvoiceContract.RETAILER_ID + " = ? AND " +
                InvoiceContract.TABLE_NAME + "." + InvoiceContract.DATE_ADDED + " = ? AND " +
                InvoiceContract.STATUS + " = ?";
        SQLiteDatabase db = mRouteDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(QUERY, new String[]{retailerId, dateAdded, String.valueOf(status)});
        return cursor;
    }

    public Cursor queryAchievedThisMonth(String year, String month, String day) {
        String QUERY = "SELECT " +
                "SUM(CAST(" + InvoiceContract.TOTAL + " AS FLOAT)) AS " + InvoiceContract.TOTAL_ALIAS + " FROM " +
                InvoiceContract.TABLE_NAME + " WHERE SUBSTR(" + InvoiceContract.DATE_ADDED + ", 7, 4)" +
                "==? AND " + "SUBSTR(" + InvoiceContract.DATE_ADDED + ", 4, 2)" + "==? AND " +
                "SUBSTR(" + InvoiceContract.DATE_ADDED + ", 1, 2)" +
                ">=?";
        SQLiteDatabase db = mRouteDbHelper.getReadableDatabase();
        return db.rawQuery(QUERY, new String[]{year, month, day});
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

    public Cursor getMerchandisingVerificationGroupByRetailerId(String dateAdded, int available, String retailerId) {
        String QUERY = "SELECT " +
                MerchandizingListVerificationContract.RETAILER_ID + "," +
                "COUNT( " + MerchandizingListVerificationContract.RETAILER_ID + ") AS " +
                MerchandizingListVerificationContract.COUNT +
                " FROM " + MerchandizingListVerificationContract.TABLE_NAME + " WHERE " +
                MerchandizingListVerificationContract.DATE_ADDED + " = ? ";
        if (retailerId != null) {
            QUERY += " AND " + MerchandizingListVerificationContract.RETAILER_ID + " == '" + retailerId + "'";
        }
        QUERY += " AND " +
                MerchandizingListVerificationContract.AVAILABLE + " = ? GROUP BY " + MerchandizingListVerificationContract.RETAILER_ID;
        SQLiteDatabase db = mRouteDbHelper.getReadableDatabase();
        return db.rawQuery(QUERY, new String[]{dateAdded, String.valueOf(available)});
    }

    public Cursor getMerchandisingVerificationGroupByRetailerId(String dateAdded, int available) {
        return getMerchandisingVerificationGroupByRetailerId(dateAdded, available, null);
    }

    public Cursor getDistributionRate(String dateAdded, String retailerId) {
        String QUERY = "SELECT " +
                InvoiceContract.RETAILER_ID + "," +
                "COUNT(" + ProductOrderContract.PRODUCT_ID + ") AS " +
                InvoiceContract.TOTAL_ALIAS +
                " FROM " + InvoiceContract.TABLE_NAME + " INNER JOIN " +
                ProductOrderContract.TABLE_NAME + " ON " +
                InvoiceContract.TABLE_NAME + "." + InvoiceContract.INVOICE_ID + " == " +
                ProductOrderContract.TABLE_NAME + "." + ProductOrderContract.INVOICE_ID;
        if (dateAdded != null) {
            QUERY += " WHERE " + InvoiceContract.TABLE_NAME + "." + InvoiceContract.DATE_ADDED + " == '" + dateAdded + "'";
            if (retailerId != null) {
                QUERY += " AND " + InvoiceContract.RETAILER_ID + " == '" + retailerId + "'";
            }
        } else {
            if (retailerId != null) {
                QUERY += " WHERE " + InvoiceContract.RETAILER_ID + " == '" + retailerId + "'";
            }
        }
        QUERY += " GROUP BY " + InvoiceContract.RETAILER_ID;
        SQLiteDatabase db = mRouteDbHelper.getReadableDatabase();
        return db.rawQuery(QUERY, null);
    }

    public Cursor getDistributionRate(String dateAdded) {
        return getDistributionRate(dateAdded, null);
    }

    public String getMerchandizingCount(String dateAdded) {
        String count = "count";
        final String QUERY = "SELECT " +
                "COUNT( " + MerchandizeContract.DATE_ADDED + ") AS " +
                count +
                " FROM " + MerchandizeContract.TABLE_NAME + " WHERE " +
                MerchandizeContract.DATE_ADDED + " = ?";
        SQLiteDatabase db = mRouteDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(QUERY, new String[]{dateAdded});
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            return cursor.getString(cursor.getColumnIndex(count));
        } else {
            return String.valueOf(0);
        }
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
                + InvoiceContract.STATUS + " = ? ";
        SQLiteDatabase db = mRouteDbHelper.getReadableDatabase();
        return db.rawQuery(QUERY, new String[]{todayDate, String.valueOf(status)});
    }

    public Cursor getRetailerIdsForTodaysCoverage(String todayDate, int status) {
        final String QUERY = "SELECT " + InvoiceContract.RETAILER_ID +
                " FROM " + InvoiceContract.TABLE_NAME + " WHERE " +
                InvoiceContract.DATE_ADDED + " = ?" + " AND "
                + InvoiceContract.STATUS + " = ? ";
        SQLiteDatabase db = mRouteDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(QUERY, new String[]{todayDate, String.valueOf(status)});
        return cursor;
    }

    public Cursor getRetailerIdsForTodaysCoverage(String todayDate, int status, String filter) {
        final String QUERY = "SELECT " + InvoiceContract.TABLE_NAME + "." + InvoiceContract.RETAILER_ID +
                " FROM " + InvoiceContract.TABLE_NAME +
                " INNER JOIN " + RetailerContract.TABLE_NAME +
                " ON " + InvoiceContract.TABLE_NAME + "." + InvoiceContract.RETAILER_ID + " = " +
                RetailerContract.TABLE_NAME + "." + RetailerContract.RETAILER_ID +
                " WHERE " +
                InvoiceContract.TABLE_NAME + "." + InvoiceContract.DATE_ADDED + " = ?" + " AND "
                + InvoiceContract.STATUS + " = ? " +
                " AND " + RetailerContract.RETAILER_NAME + " LIKE '%" + filter + "%'";
        SQLiteDatabase db = mRouteDbHelper.getReadableDatabase();
        return db.rawQuery(QUERY, new String[]{todayDate, String.valueOf(status)});
    }


    public Cursor getHPV(String retailerId) {
        String[] projection = new String[]{
                HighestPurchaseValueContract.VALUE};
        String selection = HighestPurchaseValueContract.RETAILER_ID + " = ?";
        String selectionArgs[] = new String[]{retailerId};
        String sortOrder = HighestPurchaseValueContract.RETAILER_ID + " ASC";
        SQLiteDatabase db = mRouteDbHelper.getReadableDatabase();

        Cursor HighestPurchaseValueCursor = db.query(
                HighestPurchaseValueContract.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        return HighestPurchaseValueCursor;
    }

    public Cursor getStockCount(String retailerId, String dateAdded) {
        String QUERY = "SELECT " +
                StockCountContract.TABLE_NAME + "." + StockCountContract.PRODUCT_ID + ", "
                + ProductContract.COLUMN_NAME + ", " +
                StockCountContract.OC + "," +
                StockCountContract.RETAILER_ID + " FROM " + StockCountContract.TABLE_NAME +
                " INNER JOIN " + ProductContract.TABLE_NAME + " ON " +
                StockCountContract.TABLE_NAME + "." + StockCountContract.PRODUCT_ID + " = " +
                ProductContract.TABLE_NAME + "." + ProductContract.COLUMN_PRODUCT_ID +
                " WHERE " + StockCountContract.RETAILER_ID + " = ? AND " +
                StockCountContract.DATE_ADDED + " = ?" +
                " ORDER BY " + ProductContract.COLUMN_NAME + " ASC";
        SQLiteDatabase db = mRouteDbHelper.getReadableDatabase();
        return db.rawQuery(QUERY, new String[]{retailerId, dateAdded});
    }
}
