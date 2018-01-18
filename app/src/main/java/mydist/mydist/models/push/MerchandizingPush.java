package mydist.mydist.models.push;

import android.database.Cursor;

import com.fasterxml.jackson.annotation.JsonProperty;

import mydist.mydist.data.MasterContract;

/**
 * Created by Blessing.Ekundayo on 1/17/2018.
 */

public class MerchandizingPush
{
    @JsonProperty("merchId")
    String merchId;
    @JsonProperty("brandId")
    String brandId;

    public MerchandizingPush(Cursor cursor){
        merchId = cursor.getString(cursor.getColumnIndex(MasterContract.MerchandizingListVerificationContract.MERCHANDIZING_ID));
        brandId = cursor.getString(cursor.getColumnIndex(MasterContract.MerchandizingListVerificationContract.BRAND_ID));
    }

    public String getMerchId() {
        return merchId;
    }

    public void setMerchId(String merchId) {
        this.merchId = merchId;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }
}
