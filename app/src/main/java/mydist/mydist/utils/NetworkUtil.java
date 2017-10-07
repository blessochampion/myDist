package mydist.mydist.utils;

/**
 * Created by Blessing.Ekundayo on 9/5/2017.
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;


public class NetworkUtil {
    private static final String TAG = NetworkUtil.class.getSimpleName();

    private static final String BASE_URL = "https://loginmobileapp.herokuapp.com/api/users/";
    private static final String LOGIN = "authenticate";
    private static final String REGISTER = "register";
    private static final String CONTENT_UPDATE = "addcontent";
    public static final int FAILURE_RESPONSE_CODE = 0;
    public static final int SUCCESS_RESPONSE_CODE = 1;
    private static final String POINT_UPDATE = "updatepoint";


    public static String getLoginUrl() {
        Uri loginUri = Uri.parse(BASE_URL).buildUpon().appendPath(LOGIN)
                .build();

        return loginUri.toString();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public static String getRegisterUrl() {
        Uri registerUri = Uri.parse(BASE_URL).buildUpon().appendPath(REGISTER)
                .build();

        return registerUri.toString();
    }

    public static String getContentUpdateUrl() {
        Uri contentUpdateUri = Uri.parse(BASE_URL).buildUpon().appendPath(CONTENT_UPDATE)
                .build();

        return contentUpdateUri.toString();
    }

    public static String getPointUpdateUrl(long userId, int point){
        Uri pointUpdateUri = Uri.parse(BASE_URL).buildUpon().appendPath(POINT_UPDATE)
                .appendPath(String.valueOf(userId)).appendPath(String.valueOf(point))
                .build();

        return pointUpdateUri.toString();
    }
}
