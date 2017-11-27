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
import android.widget.TableLayout;

import java.util.ArrayList;
import java.util.List;

import mydist.mydist.data.DatabaseManager;
import mydist.mydist.data.UserPreference;
import mydist.mydist.models.AuthenticationResponse;
import mydist.mydist.models.Brand;
import mydist.mydist.models.Channel;
import mydist.mydist.models.DownloadMastersResponse;
import mydist.mydist.models.Merchandize;
import mydist.mydist.models.Product;
import mydist.mydist.models.SubChannel;


public class DataUtils {


    public static void saveUser(AuthenticationResponse.User user, UserPreference userPreference) {
        userPreference.setRepId(user.getRepId());
        userPreference.setRepCode(user.getRepCode());
        userPreference.setUsername(user.getUserName());
        userPreference.setFullName(user.getFullName());
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

    public static List<Product> getAllProducts(Context context){
        List<Product> products = new ArrayList<>();
        Cursor productCursor = DatabaseManager.getInstance(context).queryAllProduct();
        Product product;
        if(productCursor != null){
            int count = productCursor.getCount();
            productCursor.moveToFirst();
            for (int i=0; i< count; i++){
                product = new Product(productCursor);
                products.add(product);
                productCursor.moveToNext();
            }
        }

        return products;
    }
}
