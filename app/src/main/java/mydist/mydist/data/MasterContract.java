package mydist.mydist.data;

import android.provider.BaseColumns;

/**
 * Created by Blessing.Ekundayo on 11/25/2017.
 */

public class MasterContract
{

    public static class ChannelContract implements BaseColumns {
        public static final String TABLE_NAME = "channel";
        public static final String COLUMN_CHANNEL_ID = "channel_id";
        public static final String COLUMN_NAME = "name";
    }

    public static class SubChannelContract implements BaseColumns {
        public static final String TABLE_NAME = "sub_channel";
        public static final String COLUMN_SUB_CHANNEL_ID = "sub_channel_id";
        public static final String COLUMN_NAME = "name";
    }

    public static class BrandContract implements BaseColumns {
        public static final String TABLE_NAME = "brand";
        public static final String COLUMN_BRAND_ID = "brand_id";
        public static final String COLUMN_NAME = "name";
    }

    public static class ProductContract implements BaseColumns {
        public static final String TABLE_NAME = "product";
        public static final String COLUMN_PRODUCT_ID = "product_id";
        public static final String COLUMN_BRAND_ID = "brand_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_CASE_PRICE = "case_price";
        public static final String COLUMN_PIECE_PRICE = "piece_price";
    }

    public static class MerchandizeContract implements BaseColumns {
        public static final String TABLE_NAME = "merchandize";
        public static final String BRAND_NAME = "brand_name";
        public static final String COLUMN_MERCHANDIZE_ITEM = "merchandize_item";
    }

    public static class RetailerContract implements BaseColumns{
        public static final String TABLE_NAME = "retailer";
        public static final String DATE_ADDED = "date_added";
        public static final String RETAILER_NAME = "retailer_name";
        public static final String CONTACT_PERSON_NAME = "contact_person_name";
        public static final String ADDRESS = "address";
        public static final String PHONE = "phone";
        public static final String CHANNEL_ID = "channel_id";
        public static final String SUB_CHANNEL_ID = "sub_channel_id";
        public static final String RETAILER_ID = "retailer_id";
    }

    public static class VisitingInfoContract implements BaseColumns{
        public static final String TABLE_NAME = "visiting_info";
        public static final String DATE_ADDED = "date_added";
        public static final String RETAILER_ID = "retailer_id";
        public static final String WEEK = "week";
        public static final String DAY = "day";

    }
}
