package mydist.mydist.models.push;

import android.database.Cursor;

import static mydist.mydist.data.MasterContract.*;

/**
 * Created by Blessing.Ekundayo on 1/17/2018.
 */

public class CollectionPush
{
    String invoiceNo;
    String modeOfPayment;
    String amount;
    String collectionAmount ;
    String modeId;

    public CollectionPush(Cursor cursor){
        invoiceNo = cursor.getString(cursor.getColumnIndex(InvoiceContract.INVOICE_ID));
        modeOfPayment = cursor.getString(cursor.getColumnIndex(InvoiceContract.PAYMENT_MODE));
        amount = cursor.getString(cursor.getColumnIndex(InvoiceContract.TOTAL));
        collectionAmount = cursor.getString(cursor.getColumnIndex(InvoiceContract.AMOUNT_PAID));
        modeId = cursor.getString(cursor.getColumnIndex(InvoiceContract.PAYMENT_MODE_VALUE));
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getModeOfPayment() {
        return modeOfPayment;
    }

    public void setModeOfPayment(String modeOfPayment) {
        this.modeOfPayment = modeOfPayment;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCollectionAmount() {
        return collectionAmount;
    }

    public void setCollectionAmount(String collectionAmount) {
        this.collectionAmount = collectionAmount;
    }

    public String getModeId() {
        return modeId;
    }

    public void setModeId(String modeId) {
        this.modeId = modeId;
    }
}
