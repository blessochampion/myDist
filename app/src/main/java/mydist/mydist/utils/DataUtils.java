package mydist.mydist.utils;

/**
 * Created by Blessing.Ekundayo on 9/5/2017.
 */

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mydist.mydist.data.DatabaseManager;
import mydist.mydist.data.ProductLogic;
import mydist.mydist.data.UserPreference;
import mydist.mydist.models.Area;
import mydist.mydist.models.AuthenticationResponse;
import mydist.mydist.models.Brand;
import mydist.mydist.models.Channel;
import mydist.mydist.models.DownloadMastersResponse;
import mydist.mydist.models.Invoice;
import mydist.mydist.models.Merchandize;
import mydist.mydist.models.MerchandizingVerification;
import mydist.mydist.models.NewRetailer;
import mydist.mydist.models.Product;
import mydist.mydist.models.Retailer;
import mydist.mydist.models.SubChannel;
import mydist.mydist.printing.PrintingModel;


public class DataUtils {

    private static double totalAmountToBePaid;
    public static PrintingModel printingModel;
    private static HashMap<String, ProductLogic> selectedProducts = new HashMap<>();

    public static HashMap<String, ProductLogic> getSelectedProducts() {
        return selectedProducts;
    }

    public static void setSelectedProducts(HashMap<String, ProductLogic> selectedProducts) {
        DataUtils.selectedProducts = selectedProducts;
    }

    public static PrintingModel getPrintingModel() {
        return printingModel;
    }

    public static void setPrintingModel(PrintingModel printingModel) {
        DataUtils.printingModel = printingModel;
    }

    public static void saveUser(AuthenticationResponse.User user, UserPreference userPreference) {
        userPreference.setRepId(user.getRepId());
        userPreference.setRepCode(user.getRepCode());
        userPreference.setUsername(user.getUserName());
        userPreference.setFullName(user.getFullName());
    }

    public static boolean saveNewRetailer(NewRetailer newRetailer, String initialHPV, Context context) {
        DatabaseManager manager = DatabaseManager.getInstance(context);
        return manager.persistNewRetailer(newRetailer, initialHPV);
    }

    public static boolean saveNewRetailers(List<NewRetailer> newRetailers, Context context) {
        DatabaseManager manager = DatabaseManager.getInstance(context);
        boolean isSuccess = true;
        for (NewRetailer retailer : newRetailers) {
            isSuccess = isSuccess && manager.persistNewRetailer(retailer, DatabaseLogicUtils.getDefaultHpv());
        }
        return isSuccess;
    }

    public static boolean saveInvoice(Invoice invoice, Context context) {
        DatabaseManager manager = DatabaseManager.getInstance(context);
        boolean isSuccess = manager.persistInvoice(invoice);
        return isSuccess;
    }

    public static List<Retailer> getRetailerByVisitingInfo(String week, String day, Context context) {
        List<Retailer> retailers = new ArrayList<>();
        DatabaseManager manager = DatabaseManager.getInstance(context);
        Cursor cursor = manager.getRetailerByVisitingInfo(week, day);
        int count = cursor.getCount();
        cursor.moveToFirst();
        Retailer retailer;
        for (int i = 0; i < count; i++) {
            retailer = new Retailer(cursor);
            retailers.add(retailer);
            cursor.moveToNext();
        }

        return retailers;
    }

    public static void saveMasters(DownloadMastersResponse downloadMastersResponse, Context context) {
        DatabaseManager manager = DatabaseManager.getInstance(context);
        DownloadMastersResponse.Master master = downloadMastersResponse.getMaster();
        manager.persistAllBrand(master.getBrands());
        manager.persistAllChannel(master.getChannels());
        manager.persistAllMerchandize(master.getMerchandizingList());
        manager.persistAllProduct(master.getProducts());
        manager.persistAllSubChannel(master.getSubChannels());
        manager.persistAllArea(master.getAreas());

    }


    public static List<Channel> getAllChannel(Context context) {
        List<Channel> channels = new ArrayList<>();
        Cursor channelCursor = DatabaseManager.getInstance(context).queryAllChannel();
        Channel channel;
        if (channelCursor != null) {
            int count = channelCursor.getCount();
            channelCursor.moveToFirst();
            for (int i = 0; i < count; i++) {
                channel = new Channel(channelCursor);
                channels.add(channel);
                channelCursor.moveToNext();
            }
        }

        return channels;
    }

    public static List<Area> getAllArea(Context context) {
        List<Area> areas = new ArrayList<>();
        Cursor areaCursor = DatabaseManager.getInstance(context).queryAllArea();
        Area area;
        if (areaCursor != null) {
            int count = areaCursor.getCount();
            areaCursor.moveToFirst();
            for (int i = 0; i < count; i++) {
                area = new Area(areaCursor);
                areas.add(area);
                areaCursor.moveToNext();
            }
        }
        return areas;
    }

    public static List<Merchandize> getAllMerchandize(Context context) {
        List<Merchandize> merchandizes = new ArrayList<>();
        Cursor merchandizeCursor = DatabaseManager.getInstance(context).queryAllMerchandize();
        Merchandize merchandize;
        if (merchandizeCursor != null) {
            int count = merchandizeCursor.getCount();
            merchandizeCursor.moveToFirst();
            for (int i = 0; i < count; i++) {
                merchandize = new Merchandize(merchandizeCursor);
                merchandizes.add(merchandize);
                merchandizeCursor.moveToNext();
            }
        }

        return merchandizes;
    }

    public static List<SubChannel> getAllSubChannel(Context context) {
        List<SubChannel> subChannels = new ArrayList<>();
        Cursor subChannelCursor = DatabaseManager.getInstance(context).queryAllSubChannel();
        SubChannel subChannel;
        if (subChannelCursor != null) {
            int count = subChannelCursor.getCount();
            subChannelCursor.moveToFirst();
            for (int i = 0; i < count; i++) {
                subChannel = new SubChannel(subChannelCursor);
                subChannels.add(subChannel);
                subChannelCursor.moveToNext();
            }
        }

        return subChannels;
    }

    public static List<Brand> getAllBrands(Context context) {
        List<Brand> brands = new ArrayList<>();
        Cursor brandCursor = DatabaseManager.getInstance(context).queryAllBrand();
        Brand brand;
        if (brandCursor != null) {
            int count = brandCursor.getCount();
            brandCursor.moveToFirst();
            for (int i = 0; i < count; i++) {
                brand = new Brand(brandCursor);
                brands.add(brand);
                brandCursor.moveToNext();
            }
        }

        return brands;
    }

    public static List<Product> getAllProducts(Context context) {
        List<Product> products = new ArrayList<>();
        Cursor productCursor = DatabaseManager.getInstance(context).queryAllProduct();
        Product product;
        if (productCursor != null) {
            int count = productCursor.getCount();
            productCursor.moveToFirst();
            for (int i = 0; i < count; i++) {
                product = new Product(productCursor);
                products.add(product);
                productCursor.moveToNext();
            }
        }

        return products;
    }

    public static final Cursor getProductsOrderByInvoiceId(String id, Context context) {
        return DatabaseManager.getInstance(context).getProductsOrder(id, Days.getTodayDate());
    }

    public static final Cursor getAllInvoice(String retailerId, int status, Context context) {
        return DatabaseManager.getInstance(context).queryAllInvoiceByRetailerId(retailerId, Days.getTodayDate(), status);
    }

    public static final Cursor getAllOrder(int status, Context context) {
        return DatabaseManager.getInstance(context).queryAllOrder(Days.getTodayDate(), status);
    }

    public static final Cursor getAllOrderTotal(int status, Context context) {
        return DatabaseManager.getInstance(context).queryAllOrderTotal(Days.getTodayDate(), status);
    }

    public static final Cursor getAllOrderTotal(String retailerId, int status, Context context) {
        return DatabaseManager.getInstance(context).queryAllOrderTotal(retailerId,Days.getTodayDate(), status);
    }

    public static List<Product> getAllProductsByBrandId(Context context, String brandId) {
        List<Product> products = new ArrayList<>();
        Cursor productCursor = DatabaseManager.getInstance(context).queryAllProduct(brandId);
        Product product;
        if (productCursor != null) {
            int count = productCursor.getCount();
            productCursor.moveToFirst();
            for (int i = 0; i < count; i++) {
                product = new Product(productCursor);
                products.add(product);
                productCursor.moveToNext();
            }
        }

        return products;
    }

    public static void setTotalAmountToBePaid(double totalAmountToBePaid) {
        DataUtils.totalAmountToBePaid = totalAmountToBePaid;
    }

    public static double getTotalAmountToBePaid() {
        return totalAmountToBePaid;
    }

    public static boolean cancelInvoice(String invoiceId, Context context) {
        return DatabaseManager.getInstance(context).cancelInvoice(invoiceId);
    }

    public static boolean saveCollectionForInvoice(Context context, String invoiceId, String amount, String paymentMode, String paymentModeValue) {
        return DatabaseManager.getInstance(context).updateAmountPaidForInvoice(invoiceId, amount, paymentMode, paymentModeValue);
    }

    public static HashMap<String, MerchandizingVerification> getMerchandizingVerification(String retailerId, String todayDate, Context context) {

        HashMap<String, MerchandizingVerification> result = new HashMap<>();
        Cursor cursor = DatabaseManager.getInstance(context).getMerchandisingVerification(retailerId, todayDate);
        MerchandizingVerification currentItem;
        int count = cursor.getCount();
        if (count > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < count; i++) {
                currentItem = new MerchandizingVerification(cursor);
                result.put(currentItem.getMerchandizingId() + "_" + currentItem.getBrandId(), currentItem);
                cursor.moveToNext();
            }
        }

        return result;
    }

    public static boolean persistMerchandizingVerification(HashMap<String, MerchandizingVerification> mList, Context context) {

        List<MerchandizingVerification> verification = new ArrayList<MerchandizingVerification>(mList.values());

        return DatabaseManager.getInstance(context).persistMerchandizingVerification(verification);
    }

    public static Cursor getCoverageCount(String todayDate, int status, Context context) {
        return DatabaseManager.getInstance(context).queryCoverageCount(todayDate, status);
    }

    public static Cursor getRetailerIdsForTodaysCoverage(String todayDate, int status, Context context) {
        return DatabaseManager.getInstance(context).getRetailerIdsForTodaysCoverage(todayDate, status);
    }

    public static boolean updateHPV(String retailerId, String value, Context context) {
        return DatabaseManager.getInstance(context).updateHPV(retailerId, value);
    }
    public static Cursor getHPV(String retailerId,Context context){
        return DatabaseManager.getInstance(context).getHPV(retailerId);
    }
}
