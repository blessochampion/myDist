package mydist.mydist.models.push;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Blessing.Ekundayo on 1/17/2018.
 */

public class CallAnalysis
{
    @JsonProperty("sales")
    double sale;
    @JsonProperty("collectionAmount")
    double collectionAmount;
    @JsonProperty("initiativeRate")
    double initiativeRate;

    public CallAnalysis(double sale, double collectionAmount, double initiativeRate) {
        this.sale = sale;
        this.collectionAmount = collectionAmount;
        this.initiativeRate = initiativeRate;
    }

    public double getSale() {
        return sale;
    }

    public void setSale(double sale) {
        this.sale = sale;
    }

    public double getCollectionAmount() {
        return collectionAmount;
    }

    public void setCollectionAmount(double collectionAmount) {
        this.collectionAmount = collectionAmount;
    }

    public double getInitiativeRate() {
        return initiativeRate;
    }

    public void setInitiativeRate(double initiativeRate) {
        this.initiativeRate = initiativeRate;
    }
}
