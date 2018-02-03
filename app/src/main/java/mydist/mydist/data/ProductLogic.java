package mydist.mydist.data;

import mydist.mydist.models.Product;

/**
 * Created by Blessing.Ekundayo on 12/16/2017.
 */

public class ProductLogic {
    Product product;
    public int oc;
    public int op;
    double total;

    public ProductLogic(Product product) {
        this.product = product;
        this.oc = 0;
        this.op = 0;
    }

    public double getTotal() {
        total = Double.valueOf(this.product.getCasePrice()) * oc +
                Double.valueOf(this.product.getPiecePrice()) * op;
        return total;
    }

    public Product getProduct() {
        return product;
    }
}
