package mydist.mydist.models;

/**
 * Created by Blessing.Ekundayo on 1/13/2018.
 */

public class ProductOrder {
    String dateAdded;
    String invoiceId;
    String total;
    String productName;
    String productId;
    String brandId;
    int oc;
    int op;

    public ProductOrder(String dateAdded, String invoiceId, String total, String productName, String productId, String brandId,
                        int oc, int op) {
        this.dateAdded = dateAdded;
        this.invoiceId = invoiceId;
        this.total = total;
        this.productName = productName;
        this.productId = productId;
        this.brandId = brandId;
        this.oc = oc;
        this.op = op;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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
}
