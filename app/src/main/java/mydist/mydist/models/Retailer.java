package mydist.mydist.models;

import android.database.Cursor;

import static mydist.mydist.data.MasterContract.*;

/**
 * Created by Blessing.Ekundayo on 1/8/2018.
 */

public class Retailer
{  String dateAdded;
    String name;
    String contactPersonName;
    String address;
    String phone;
    String channel;
    String subChannel;
    String retailerId;

    public Retailer(String dateAdded, String name, String contactPersonName, String address, String phone, String channel, String subChannel, String retailerId) {
        this.dateAdded = dateAdded;
        this.name = name;
        this.contactPersonName = contactPersonName;
        this.address = address;
        this.phone = phone;
        this.channel = channel;
        this.subChannel = subChannel;
        this.retailerId = retailerId;
    }

    public Retailer(Cursor cursor)
    {
        this.dateAdded = cursor.getString(cursor.getColumnIndex(RetailerContract.DATE_ADDED));
        this.name = cursor.getString(cursor.getColumnIndex(RetailerContract.RETAILER_NAME));
        this.contactPersonName = cursor.getString(cursor.getColumnIndex(RetailerContract.CONTACT_PERSON_NAME));
        this.address = cursor.getString(cursor.getColumnIndex(RetailerContract.ADDRESS));
        this.phone = cursor.getString(cursor.getColumnIndex(RetailerContract.PHONE));
        this.channel = cursor.getString(cursor.getColumnIndex(RetailerContract.CHANNEL_ID));
        this.subChannel = cursor.getString(cursor.getColumnIndex(RetailerContract.SUB_CHANNEL_ID));
        this.retailerId = cursor.getString(cursor.getColumnIndex(RetailerContract.RETAILER_ID));

    }
    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactPersonName() {
        return contactPersonName;
    }

    public void setContactPersonName(String contactPersonName) {
        this.contactPersonName = contactPersonName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getSubChannel() {
        return subChannel;
    }

    public void setSubChannel(String subChannel) {
        this.subChannel = subChannel;
    }

    public String getRetailerId() {
        return retailerId;
    }

    public void setRetailerId(String retailerId) {
        this.retailerId = retailerId;
    }
}
