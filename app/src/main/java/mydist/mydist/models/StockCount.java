package mydist.mydist.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Blessing.Ekundayo on 3/11/2018.
 */

public class StockCount implements Parcelable {
    String productId;
    String productName;
    int oc;

    public StockCount(String productId, String productName, int oc) {
        this.productId = productId;
        this.productName = productName;
        this.oc = oc;
    }

    protected StockCount(Parcel in) {
        productId = in.readString();
        productName = in.readString();
        oc = in.readInt();
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public static final Creator<StockCount> CREATOR = new Creator<StockCount>() {
        @Override
        public StockCount createFromParcel(Parcel in) {
            return new StockCount(in);
        }

        @Override
        public StockCount[] newArray(int size) {
            return new StockCount[size];
        }
    };

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getOc() {
        return oc;
    }

    public void setOc(int oc) {
        this.oc = oc;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productId);
        dest.writeString(productName);
        dest.writeInt(oc);
    }
}
