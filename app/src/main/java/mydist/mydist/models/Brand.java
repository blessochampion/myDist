package mydist.mydist.models;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import static mydist.mydist.data.MasterContract.BrandContract;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Blessing.Ekundayo on 11/25/2017.
 */

public class Brand implements Parcelable{
    @JsonProperty("brandId")
    public String brandId;
    @JsonProperty("brandName")
    public String brandName;

    public Brand() {
    }

    public Brand(String brandId, String brandName) {
        this.brandId = brandId;
        this.brandName = brandName;
    }

    protected Brand(Parcel in) {
        brandId = in.readString();
        brandName = in.readString();
    }

    public static final Creator<Brand> CREATOR = new Creator<Brand>() {
        @Override
        public Brand createFromParcel(Parcel in) {
            return new Brand(in);
        }

        @Override
        public Brand[] newArray(int size) {
            return new Brand[size];
        }
    };

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }


    public Brand(Cursor cursor) {
        this.brandId = String.valueOf(cursor.getDouble(cursor.getColumnIndex(BrandContract.COLUMN_BRAND_ID)));
        this.brandName = cursor.getString(cursor.getColumnIndex(BrandContract.COLUMN_NAME));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(brandId);
        dest.writeString(brandName);
    }
}
