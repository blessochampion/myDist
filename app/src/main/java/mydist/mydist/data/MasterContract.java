package mydist.mydist.data;

import android.provider.BaseColumns;

/**
 * Created by Blessing.Ekundayo on 11/25/2017.
 */

public class MasterContract {

    public static class ChannelContract implements BaseColumns {
        public static final String TABLE_NAME = "channel";
        public static final String COLUMN_CHANNEL_ID = "channel_id";
        public static final String COLUMN_NAME = "channel_name";
    }

    public static class AreaContract implements BaseColumns {
        public static final String TABLE_NAME = "area";
        public static final String COLUMN_AREA_ID = "area_id";
        public static final String COLUMN_NAME = "area_name";
    }

    public static class SubChannelContract implements BaseColumns {
        public static final String TABLE_NAME = "sub_channel";
        public static final String COLUMN_SUB_CHANNEL_ID = "sub_channel_id";
        public static final String COLUMN_NAME = "sub_channel_name";
    }

    public static class BrandContract implements BaseColumns {
        public static final String TABLE_NAME = "brand";
        public static final String COLUMN_BRAND_ID = "brand_id";
        public static final String COLUMN_NAME = "brand_name";
    }

    public static class ProductContract implements BaseColumns {
        public static final String TABLE_NAME = "product";
        public static final String COLUMN_PRODUCT_ID = "product_id";
        public static final String COLUMN_BRAND_ID = "brand_id";
        public static final String COLUMN_NAME = "product_name";
        public static final String COLUMN_CASE_PRICE = "case_price";
        public static final String COLUMN_PIECE_PRICE = "piece_price";
    }

    public static class MerchandizeContract implements BaseColumns {
        public static final String TABLE_NAME = "merchandize";
        public static final String BRAND_NAME = "brand_name";
        public static final String COLUMN_MERCHANDIZE_ITEM = "merchandize_item";
        public static final String MERCHANDIZE_ID = "merchId";
        public static final String BRAND_ID = "brandId";
    }

    public static class RetailerContract implements BaseColumns {
        public static final String TABLE_NAME = "retailer";
        public static final String DATE_ADDED = "date_added";
        public static final String RETAILER_NAME = "retailer_name";
        public static final String CONTACT_PERSON_NAME = "contact_person_name";
        public static final String ADDRESS = "address";
        public static final String PHONE = "phone";
        public static final String CHANNEL_ID = "channel_id";
        public static final String SUB_CHANNEL_ID = "sub_channel_id";
        public static final String AREA_ID = "area_id";
        public static final String RETAILER_ID = "retailer_id";
    }

    public static class VisitingInfoContract implements BaseColumns {
        public static final String TABLE_NAME = "visiting_info";
        public static final String DATE_ADDED = "date_added";
        public static final String RETAILER_ID = "retailer_id";
        public static final String WEEK = "week";
        public static final String DAY = "day";

    }

    public static class InvoiceContract implements BaseColumns {
        public static final String TABLE_NAME = "invoice";
        public static final String DATE_ADDED = "date_added";
        public static final String RETAILER_ID = "retailer_id";
        public static final String TOTAL = "total";
        public static final String TOTAL_ALIAS = "total_alias";
        public static final String AMOUNT_PAID = "amount_paid";
        public static final String PAYMENT_MODE = "payment_mode";
        public static final String PAYMENT_MODE_VALUE = "payment_mode_value";
        public static final String INVOICE_ID = "invoice_id";
        public static final String STATUS = "status";
    }

    public static class ProductOrderContract implements BaseColumns {
        public static final String TABLE_NAME = "product_order";
        public static final String DATE_ADDED = "date_added";
        public static final String INVOICE_ID = "invoice_id";
        public static final String INVOICE_ID_ALIAS = "invoice_id_alias";
        public static final String TOTAL = "total";
        public static final String PRODUCT_NAME = "product_name";
        public static final String PRODUCT_ID = "product_id";
        public static final String PRODUCT_ID_ALIAS = "product_id_alias";
        public static final String BRAND_ID = "brand_id";
        public static final String OC = "oc";
        public static final String OP = "op";
        public static final String PRODUCT_COUNT = "count";
    }

    public static class MerchandizingListVerificationContract implements BaseColumns {
        public static final String TABLE_NAME = "merchandizing_list_verification";
        public static final String DATE_ADDED = "date_added";
        public static final String MERCHANDIZING_ID = "merchandizing_id";
        public static final String RETAILER_ID = "retailer_id";
        public static final String BRAND_ID = "brand_id";
        public static final String AVAILABLE = "available";
    }
}
