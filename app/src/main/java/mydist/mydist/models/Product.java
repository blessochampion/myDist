package mydist.mydist.models;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import static mydist.mydist.data.MasterContract.ProductContract;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Blessing.Ekundayo on 11/25/2017.
 */

public class Product implements Parcelable {
    @JsonProperty("productId")
    public String productId;
    @JsonProperty("productName")
    public String productName;
    @JsonProperty("casePrice")
    public String casePrice;
    @JsonProperty("piecePrice")
    public String piecePrice;
    @JsonProperty("brandId")
    public String brandId;

    public Product() {
    }


    public Product(Cursor cursor) {
        this.productId = cursor.getString(cursor.getColumnIndex(ProductContract.COLUMN_PRODUCT_ID));
        this.productName = cursor.getString(cursor.getColumnIndex(ProductContract.COLUMN_NAME));
        this.casePrice = String.valueOf(cursor.getDouble(cursor.getColumnIndex(ProductContract.COLUMN_CASE_PRICE)));
        this.piecePrice = String.valueOf(cursor.getDouble(cursor.getColumnIndex(ProductContract.COLUMN_PIECE_PRICE)));
        this.brandId = cursor.getString(cursor.getColumnIndex(ProductContract.COLUMN_BRAND_ID));
    }

    public Product(String productId, String productName, String casePrice, String piecePrice) {
        this.productId = productId;
        this.productName = productName;
        this.casePrice = casePrice;
        this.piecePrice = piecePrice;
        this.brandId = "";
    }


    protected Product(Parcel in) {
        productId = in.readString();
        productName = in.readString();
        casePrice = in.readString();
        piecePrice = in.readString();
        brandId = in.readString();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCasePrice() {
        return casePrice;
    }

    public void setCasePrice(String casePrice) {
        this.casePrice = casePrice;
    }

    public String getPiecePrice() {
        return piecePrice;
    }

    public void setPiecePrice(String piecePrice) {
        this.piecePrice = piecePrice;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productId);
        dest.writeString(productName);
        dest.writeString(casePrice);
        dest.writeString(piecePrice);
        dest.writeString(brandId);
    }
}
