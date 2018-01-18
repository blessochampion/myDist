package mydist.mydist.models;

import java.util.List;

/**
 * Created by Blessing.Ekundayo on 1/13/2018.
 */

public class Invoice {
    String id;
    String retailerId;
    String dateAdded;
    String total;
    String amountPaid;
    String paymentMode;
    String paymentModeValue;
    int status;
    List<ProductOrder> productOrders;
    public static final int KEY_STATUS_SUCCESS = 0;
    public static final int KEY_STATUS_CANCEL = 1;
    public static final String NO_AMOUNT = "0";
    public static final String MODE_CASH = "Cash";
    public static final String MODE_DRAFT = "Draft";
    public static final String MODE_CHEQUE = "Cheque";
    public static final String CASH_DEFAULT_VALUE = "0";

    public Invoice(String id, String retailerId, String dateAdded, String total, String amountPaid, String paymentMode, String paymentModeValue, int status, List<ProductOrder> productOrders) {
        this.id = id;
        this.retailerId = retailerId;
        this.dateAdded = dateAdded;
        this.total = total;
        this.amountPaid = amountPaid;
        this.paymentMode = paymentMode;
        this.paymentModeValue = paymentModeValue;
        this.status = status;
        this.productOrders = productOrders;
    }

    public String getPaymentModeValue() {
        return paymentModeValue;
    }

    public void setPaymentModeValue(String paymentModeValue) {
        this.paymentModeValue = paymentModeValue;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public String getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(String amountPaid) {
        this.amountPaid = amountPaid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRetailerId() {
        return retailerId;
    }

    public void setRetailerId(String retailerId) {
        this.retailerId = retailerId;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<ProductOrder> getProductOrders() {
        return productOrders;
    }

    public void setProductOrders(List<ProductOrder> productOrders) {
        this.productOrders = productOrders;
    }
}
