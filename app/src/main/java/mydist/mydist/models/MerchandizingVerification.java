package mydist.mydist.models;

import android.database.Cursor;

import static mydist.mydist.data.MasterContract.MerchandizingListVerificationContract.*;

/**
 * Created by Blessing.Ekundayo on 1/14/2018.
 */

public class MerchandizingVerification {
    String dateAdded;
    String merchandizingId;
    String brandId;
    String retailerId;
    int available;
    public static final int STATUS_AVAILABLE = 1;
    public static final int STATUS_NOT_AVAILABLE = 0;

    public MerchandizingVerification(String dateAdded, String merchandizingId, String brandId, String retailerId, int available) {
        this.dateAdded = dateAdded;
        this.merchandizingId = merchandizingId;
        this.retailerId = retailerId;
        this.brandId = brandId;
        this.available = available;
    }

    public MerchandizingVerification(Cursor cursor) {
        dateAdded = cursor.getString(cursor.getColumnIndex(DATE_ADDED));
        merchandizingId = cursor.getString(cursor.getColumnIndex(MERCHANDIZING_ID));
        retailerId = cursor.getString(cursor.getColumnIndex(RETAILER_ID));
        brandId = cursor.getString(cursor.getColumnIndex(BRAND_ID));
        available = cursor.getInt(cursor.getColumnIndex(AVAILABLE));
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getMerchandizingId() {
        return merchandizingId;
    }

    public void setMerchandizingId(String merchandizingId) {
        this.merchandizingId = merchandizingId;
    }

    public String getRetailerId() {
        return retailerId;
    }

    public void setRetailerId(String retailerId) {
        this.retailerId = retailerId;
    }
}
