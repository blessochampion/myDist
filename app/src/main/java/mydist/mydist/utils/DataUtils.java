package mydist.mydist.utils;

/**
 * Created by Blessing.Ekundayo on 9/5/2017.
 */

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TableLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mydist.mydist.data.DatabaseManager;
import mydist.mydist.data.ProductLogic;
import mydist.mydist.data.UserPreference;
import mydist.mydist.models.AuthenticationResponse;
import mydist.mydist.models.Brand;
import mydist.mydist.models.Channel;
import mydist.mydist.models.DownloadMastersResponse;
import mydist.mydist.models.Merchandize;
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

    public static boolean saveNewRetailer(NewRetailer newRetailer, Context context) {
        DatabaseManager manager = DatabaseManager.getInstance(context);
        return manager.persistNewRetailer(newRetailer);
    }

    public static  List<Retailer> getRetailerByVisitingInfo(String week, String day, Context context) {
        List<Retailer> retailers = new ArrayList<>();
        DatabaseManager manager = DatabaseManager.getInstance(context);
       Cursor cursor =  manager.getRetailerByVisitingInfo(week, day);
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
}
