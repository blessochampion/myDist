package mydist.mydist.utils;

/**
 * Created by Blessing.Ekundayo on 9/5/2017.
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import mydist.mydist.data.UserPreference;
import mydist.mydist.models.AuthenticationResponse;


public class DataUtils {


    public static void saveUser(AuthenticationResponse.User user, UserPreference userPreference){
        userPreference.setRepId(user.getRepId());
        userPreference.setRepCode(user.getRepCode());
        userPreference.setUsername(user.getUserName());
        userPreference.setFullName(user.getFullName());
    }
}
