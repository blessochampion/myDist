package mydist.mydist.models;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import static mydist.mydist.data.MasterContract.MerchandizeContract;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Blessing.Ekundayo on 11/25/2017.
 */

public class Merchandize implements Parcelable {
    @JsonProperty("brandName")
    public String brandName;
    @JsonProperty("merchItem")
    public String merchandizeItem;
    @JsonProperty("merchId")
    public String merchantId;
    @JsonProperty("brandId")
    public String brandId;

    public Merchandize() {
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public Merchandize(Cursor cursor) {
        this.brandName = cursor.getString(cursor.getColumnIndex(MerchandizeContract.BRAND_NAME));
        this.merchandizeItem = cursor.getString(cursor.getColumnIndex(MerchandizeContract.COLUMN_MERCHANDIZE_ITEM));
        this.merchantId = cursor.getString(cursor.getColumnIndex(MerchandizeContract.MERCHANDIZE_ID));
        this.brandId = cursor.getString(cursor.getColumnIndex(MerchandizeContract.BRAND_ID));
    }

    protected Merchandize(Parcel in) {
        brandName = in.readString();
        merchandizeItem = in.readString();
        merchantId = in.readString();
        brandId = in.readString();
    }

    public static final Creator<Merchandize> CREATOR = new Creator<Merchandize>() {
        @Override
        public Merchandize createFromParcel(Parcel in) {
            return new Merchandize(in);
        }

        @Override
        public Merchandize[] newArray(int size) {
            return new Merchandize[size];
        }
    };

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getMerchandizeItem() {
        return merchandizeItem;
    }

    public void setMerchandizeItem(String merchandizeItem) {
        this.merchandizeItem = merchandizeItem;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(brandName);
        dest.writeString(merchandizeItem);
        dest.writeString(merchantId);
        dest.writeString(brandId);
    }
}
