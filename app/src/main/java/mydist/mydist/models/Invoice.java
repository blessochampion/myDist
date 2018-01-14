package mydist.mydist.models;

import java.util.List;

/**
 * Created by Blessing.Ekundayo on 1/13/2018.
 */

public class Invoice
{
    String id;
    String retailerId;
    String dateAdded;
    String total;
    List<ProductOrder> productOrders;

    public Invoice(String id, String retailerId, String dateAdded, String total, List<ProductOrder> productOrders) {
        this.id = id;
        this.retailerId = retailerId;
        this.dateAdded = dateAdded;
        this.total = total;
        this.productOrders = productOrders;
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
