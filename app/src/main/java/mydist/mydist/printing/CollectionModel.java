package mydist.mydist.printing;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Blessing.Ekundayo on 12/28/2017.
 */

public class CollectionModel implements Parcelable
{
    private String invoiceNumber;
    private String invoiceDate;
    private String chequeNumber;
    private String draftNumber;
    private String total;
    private String retailer;
    private String salesRep;

    public CollectionModel(String invoiceNumber, String invoiceDate, String total, String retailer, String salesRep) {
        this.invoiceNumber = invoiceNumber;
        this.invoiceDate = invoiceDate;
        this.chequeNumber = chequeNumber;
        this.total = total;
        this.retailer = retailer;
        this.salesRep = salesRep;
    }

    protected CollectionModel(Parcel in) {
        invoiceNumber = in.readString();
        invoiceDate = in.readString();
        chequeNumber = in.readString();
        draftNumber = in.readString();
        total = in.readString();
        retailer = in.readString();
        salesRep = in.readString();

    }

    public static final Creator<CollectionModel> CREATOR = new Creator<CollectionModel>() {
        @Override
        public CollectionModel createFromParcel(Parcel in) {
            return new CollectionModel(in);
        }

        @Override
        public CollectionModel[] newArray(int size) {
            return new CollectionModel[size];
        }
    };


    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getChequeNumber() {
        return chequeNumber;
    }

    public void setChequeNumber(String chequeNumber) {
        this.chequeNumber = chequeNumber;
    }

    public String getDraftNumber() {
        return draftNumber;
    }

    public void setDraftNumber(String draftNumber) {
        this.draftNumber = draftNumber;
    }

    public String getRetailer() {
        return retailer;
    }

    public void setRetailer(String retailer) {
        this.retailer = retailer;
    }

    public String getSalesRep() {
        return salesRep;
    }

    public void setSalesRep(String salesRep) {
        this.salesRep = salesRep;
    }

    public String getExtras(){
        if(chequeNumber != null){
            return PrintingFormatter.formatNameValuePair("Cheque Number", chequeNumber);
        }
        if(draftNumber != null){
            return PrintingFormatter.formatNameValuePair("Draft Number", draftNumber);
        }
        return "";
   }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(invoiceNumber);
        dest.writeString(invoiceDate);
        dest.writeString(chequeNumber);
        dest.writeString(draftNumber);
        dest.writeString(total);
        dest.writeString(retailer);
        dest.writeString(salesRep);
    }
}
