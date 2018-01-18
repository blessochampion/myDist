package mydist.mydist.models.push;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Blessing.Ekundayo on 1/17/2018.
 */

public class Coverage {
    @JsonProperty("retailerId")
    String retailerId;
    @JsonProperty("date")
    String date;
    List<InvoicePush> invoices;
    @JsonProperty("callDuration")
    String callDuration = "01:02:44";
    @JsonProperty("storeTarget")
    double storeTarget = 23434.89;
    @JsonProperty("pskuCountTarget")
    int pskuCountTarget = 7;
    @JsonProperty("pskuCount")
    int pskuCount = 8;
    @JsonProperty("merchandisingTarget")
    int merchandisingTarget = 5;
    @JsonProperty("merchandisingList")
    List<MerchandizingPush> merchandizingList;
    @JsonProperty("callAnalysis")
    CallAnalysis callAnalysis;

    public Coverage(String retailerId, String date, List<InvoicePush> invoices, List<MerchandizingPush> merchandizingList, CallAnalysis callAnalysis) {
        this.retailerId = retailerId;
        this.date = date;
        this.invoices = invoices;
        this.merchandizingList = merchandizingList;
        this.callAnalysis = callAnalysis;
    }

    public String getRetailerId() {
        return retailerId;
    }

    public void setRetailerId(String retailerId) {
        this.retailerId = retailerId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<InvoicePush> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<InvoicePush> invoices) {
        this.invoices = invoices;
    }

    public String getCallDuration() {
        return callDuration;
    }

    public void setCallDuration(String callDuration) {
        this.callDuration = callDuration;
    }

    public double getStoreTarget() {
        return storeTarget;
    }

    public void setStoreTarget(double storeTarget) {
        this.storeTarget = storeTarget;
    }

    public int getPskuCountTarget() {
        return pskuCountTarget;
    }

    public void setPskuCountTarget(int pskuCountTarget) {
        this.pskuCountTarget = pskuCountTarget;
    }

    public int getPskuCount() {
        return pskuCount;
    }

    public void setPskuCount(int pskuCount) {
        this.pskuCount = pskuCount;
    }

    public int getMerchandisingTarget() {
        return merchandisingTarget;
    }

    public void setMerchandisingTarget(int merchandisingTarget) {
        this.merchandisingTarget = merchandisingTarget;
    }

    public List<MerchandizingPush> getMerchandizingList() {
        return merchandizingList;
    }

    public void setMerchandizingList(List<MerchandizingPush> merchandizingList) {
        this.merchandizingList = merchandizingList;
    }

    public CallAnalysis getCallAnalysis() {
        return callAnalysis;
    }

    public void setCallAnalysis(CallAnalysis callAnalysis) {
        this.callAnalysis = callAnalysis;
    }
}
