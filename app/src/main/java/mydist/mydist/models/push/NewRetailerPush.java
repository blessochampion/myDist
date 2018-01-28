package mydist.mydist.models.push;

import android.database.Cursor;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import static mydist.mydist.data.MasterContract.*;

/**
 * Created by Blessing.Ekundayo on 1/17/2018.
 */

public class NewRetailerPush {
    @JsonProperty("retCode")
    String retCode;
    @JsonProperty("address")
    String address;
    @JsonProperty("channelId")
    String channelId;
    @JsonProperty("contactPerson")
    String contactPerson;
    @JsonProperty("phone")
    String phone;
    @JsonProperty("retailerName")
    String retailerName;
    @JsonProperty("subChannelId")
    String subChannelId;
    @JsonProperty("areaId")
    String areaId;
    @JsonProperty("suggestedVisitDays")
    List<String> suggestedVisitDays;
    @JsonProperty("weekNo")
    List<String> weekNo;

    public NewRetailerPush( Cursor cursor, List<String> suggestedVisitDays, List<String> weekNo) {
        retCode = cursor.getString(cursor.getColumnIndex(RetailerContract.RETAILER_ID));
        address = cursor.getString(cursor.getColumnIndex(RetailerContract.ADDRESS));
        channelId = cursor.getString(cursor.getColumnIndex(RetailerContract.CHANNEL_ID));
        contactPerson = cursor.getString(cursor.getColumnIndex(RetailerContract.CONTACT_PERSON_NAME));
        phone = cursor.getString(cursor.getColumnIndex(RetailerContract.PHONE));
        retailerName = cursor.getString(cursor.getColumnIndex(RetailerContract.RETAILER_NAME));
        areaId = cursor.getString(cursor.getColumnIndex(RetailerContract.AREA_ID));
        subChannelId = cursor.getString(cursor.getColumnIndex(RetailerContract.SUB_CHANNEL_ID));
        this.suggestedVisitDays = suggestedVisitDays;
        this.weekNo = weekNo;
    }

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRetailerName() {
        return retailerName;
    }

    public void setRetailerName(String retailerName) {
        this.retailerName = retailerName;
    }

    public String getSubChannelId() {
        return subChannelId;
    }

    public void setSubChannelId(String subChannelId) {
        this.subChannelId = subChannelId;
    }

    public List<String> getSuggestedVisitDays() {
        return suggestedVisitDays;
    }

    public void setSuggestedVisitDays(List<String> suggestedVisitDays) {
        this.suggestedVisitDays = suggestedVisitDays;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public List<String> getWeekNo() {
        return weekNo;
    }

    public void setWeekNo(List<String> weekNo) {
        this.weekNo = weekNo;
    }
}
