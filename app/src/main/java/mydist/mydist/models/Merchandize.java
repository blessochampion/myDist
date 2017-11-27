package mydist.mydist.models;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import static mydist.mydist.data.MasterContract.MerchandizeContract;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Blessing.Ekundayo on 11/25/2017.
 */

public class Merchandize implements Parcelable{
        @JsonProperty("brandName")
        public String brandName;
        @JsonProperty("merchItem")
        public String merchandizeItem;

        public Merchandize() {
        }

        public Merchandize(Cursor cursor){
            this.brandName = cursor.getString(cursor.getColumnIndex(MerchandizeContract.BRAND_NAME));
            this.merchandizeItem = cursor.getString(cursor.getColumnIndex(MerchandizeContract.COLUMN_MERCHANDIZE_ITEM));
        }

    protected Merchandize(Parcel in) {
        brandName = in.readString();
        merchandizeItem = in.readString();
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
    }
}
