package mydist.mydist.models;

import java.util.List;

/**
 * Created by Blessing.Ekundayo on 12/31/2017.
 */

public class NewRetailer {
    String dateAdded;
    String name;
    String contactPersonName;
    String address;
    String phone;
    String channel;
    String subChannel;
    List<String> visitDays;
    List<String> weekNos;
    String retailerId;


    public NewRetailer(String dateAdded, String name, String contactPersonName, String address, String phone, String channel, String subChannel, List<String> visitDays, List<String> weekNos, String retailerId) {
        this.dateAdded = dateAdded;
        this.name = name;
        this.contactPersonName = contactPersonName;
        this.address = address;
        this.phone = phone;
        this.channel = channel;
        this.subChannel = subChannel;
        this.visitDays = visitDays;
        this.weekNos = weekNos;
        this.retailerId = retailerId;
    }


    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getRetailerId() {
        return retailerId;
    }

    public void setRetailerId(String retailerId) {
        this.retailerId = retailerId;
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

    public List<String> getVisitDays() {
        return visitDays;
    }

    public void setVisitDays(List<String> visitDays) {
        this.visitDays = visitDays;
    }

    public List<String> getWeekNos() {
        return weekNos;
    }

    public void setWeekNos(List<String> weekNos) {
        this.weekNos = weekNos;
    }
}
