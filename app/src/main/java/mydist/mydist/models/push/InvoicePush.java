package mydist.mydist.models.push;

import android.database.Cursor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import static mydist.mydist.data.MasterContract.*;

/**
 * Created by Blessing.Ekundayo on 1/17/2018.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class InvoicePush {
    @JsonProperty("invoiceNo")
    String invoiceNo;
    @JsonProperty("productId")
    String productId;
    @JsonProperty("description")
    String description = "short Description 1";
    @JsonProperty("oc")
    int oc;
    @JsonProperty("op")
    int op;
    @JsonProperty("unitPrice")
    double unitPrice;
    @JsonProperty("casePrice")
    double casePrice;
    @JsonProperty("total")
    double total;
    String amountPaid;
    @JsonProperty("orderStatus")
    int orderStatus;

    public InvoicePush(Cursor cursor) {
        invoiceNo = getStringValueFromCursor(cursor, ProductOrderContract.INVOICE_ID_ALIAS);
        productId = getStringValueFromCursor(cursor, ProductOrderContract.PRODUCT_ID_ALIAS);
        oc = getIntegerValueFromCursor(cursor, ProductOrderContract.OC);
        op = getIntegerValueFromCursor(cursor, ProductOrderContract.OP);
        unitPrice = Double.valueOf(getStringValueFromCursor(cursor, ProductContract.COLUMN_PIECE_PRICE));
        casePrice = Double.valueOf(getStringValueFromCursor(cursor, ProductContract.COLUMN_CASE_PRICE));
        total = Double.valueOf(getStringValueFromCursor(cursor, InvoiceContract.TOTAL_ALIAS));
        orderStatus = getIntegerValueFromCursor(cursor, InvoiceContract.STATUS);
        amountPaid = getStringValueFromCursor(cursor, InvoiceContract.AMOUNT_PAID);
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(String amountPaid) {
        this.amountPaid = amountPaid;
    }

    public String getStringValueFromCursor(Cursor c, String key) {
        return c.getString(c.getColumnIndex(key));
    }

    public int getIntegerValueFromCursor(Cursor c, String key) {
        return c.getInt(c.getColumnIndex(key));
    }

    public double getDoubleValueFromCursor(Cursor c, String key) {
        return c.getDouble(c.getColumnIndex(key));
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getOc() {
        return oc;
    }

    public void setOc(int oc) {
        this.oc = oc;
    }

    public int getOp() {
        return op;
    }

    public void setOp(int op) {
        this.op = op;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getCasePrice() {
        return casePrice;
    }

    public void setCasePrice(double casePrice) {
        this.casePrice = casePrice;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }
}
