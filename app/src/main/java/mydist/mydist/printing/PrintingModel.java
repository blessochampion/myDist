package mydist.mydist.printing;

/**
 * Created by Blessing.Ekundayo on 12/25/2017.
 */

public class PrintingModel
{
    public String retailer;
    public String salesRep;
    public String invoiceNo;
    public String transactionDate;

    public PrintingModel(String retailer, String salesRep, String invoiceNo, String transactionDate) {
        this.retailer = retailer;
        this.salesRep = salesRep;
        this.invoiceNo = invoiceNo;
        this.transactionDate = transactionDate;
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

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }
}
