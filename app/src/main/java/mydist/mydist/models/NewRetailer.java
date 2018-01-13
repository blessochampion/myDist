package mydist.mydist.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mydist.mydist.utils.Days;

/**
 * Created by Blessing.Ekundayo on 12/31/2017.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class NewRetailer {

    String dateAdded;
    @JsonProperty("retailerName")
    String name;
    @JsonProperty("contactPerson")
    String contactPersonName;
    @JsonProperty("address")
    String address;
    @JsonProperty("phone")
    String phone;
    @JsonProperty("channelId")
    String channel;
    @JsonProperty("subChannelId")
    String subChannel;
    List<String> visitDays;
    List<String> weekNos;
    @JsonProperty("visitdays")
    String visitingDays;
    @JsonProperty("visitweeks")
    String visitingWeeks;
    @JsonProperty("retCode")
    String retailerId;

    public NewRetailer() {

    }

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
        if(visitDays == null){
            String[] vDays = getVisitingDays().split(",");
            visitDays = new ArrayList<>();
            for (int i = 0; i < vDays.length; i++) {
                visitDays.add(getVisitingDay(vDays[i]));
            }
        }

        return visitDays;
    }

    public void setVisitDays(List<String> visitDays) {
        this.visitDays = visitDays;
    }

    public List<String> getWeekNos() {
        if(weekNos == null) {
            weekNos = new ArrayList<>();
            String[] weeks = getVisitingWeeks().split(",");
            for (int i = 0; i < weeks.length; i++) {
                weekNos.add("wk"+weeks[i]);
            }
        }
        return weekNos;
    }

    public void setWeekNos(List<String> weekNos) {
        this.weekNos = weekNos;
    }

    public String getVisitingDays() {

        return visitingDays;
    }

    public void setVisitingDays(String visitingDays) {


        this.visitingDays = visitingDays;
    }

    public String getVisitingWeeks() {
        return visitingWeeks;
    }

    public void setVisitingWeeks(String visitingWeeks) {
        this.visitingWeeks = visitingWeeks;
    }

    private String getVisitingDay(String day) {
        switch (day) {
            case "sunday":
                return Days.SUN.toString();
            case "monday":
                return Days.MON.toString();
            case "tuesday":
                return Days.TUE.toString();
            case "wednesday":
                return Days.WED.toString();
            case "thursday":
                return Days.WED.toString();
            case "friday":
                return Days.FRI.toString();
            case "saturday":
                return Days.SAT.toString();
            default:
                return "";
        }
    }


}
