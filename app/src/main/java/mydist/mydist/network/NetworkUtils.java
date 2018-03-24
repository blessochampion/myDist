package mydist.mydist.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

/**
 * Created by Blessing.Ekundayo on 11/9/2017.
 */

public class NetworkUtils
{
   // private static final String BASE_URL = "http://35.189.215.9/webservices";
    private static final String BASE_URL = "http://35.187.59.36/webservices";
   // private static final String BASE_URL = "http://139.162.249.130/webservices";

    public static String getAuthenticationURL(String username, String password){
        String usernameKey = "username";
        String passwordKey = "password";
        String salesRepPath = "getsalesrep.php";

        Uri authenticationUri = Uri.parse(BASE_URL).buildUpon().appendPath(salesRepPath)
                .appendQueryParameter(usernameKey, username)
                .appendQueryParameter(passwordKey, password).build();
        return authenticationUri.toString();
    }

    public static String getMastersDownloadURL(String username, String password){
        String usernameKey = "username";
        String passwordKey = "password";
        String mastersDownloadPath = "masters.php";

        Uri mastersDownloadUri = Uri.parse(BASE_URL).buildUpon().appendPath(mastersDownloadPath)
                .appendQueryParameter(usernameKey, username)
                .appendQueryParameter(passwordKey, password).build();
        return mastersDownloadUri.toString();
    }

    public static String getUploadURL(){
        String mastersUploadPath = "senddata.php";
        Uri mastersUploadUri = Uri.parse(BASE_URL).buildUpon().appendPath(mastersUploadPath).build();
        return mastersUploadUri.toString();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
