package mydist.mydist.data;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Blessing.Ekundayo on 13/11/2017.
 */

public class UserPreference {
    private static final String USER_PREFERENCE_NAME = "user_preference";
    private static final String KEY_REP_ID = "repId";
    private static final String KEY_REP_CODE = "repCode";
    private static final String KEY_USERNAME = "userName";
    private static final String KEY_FULLNAME = "fullName";
    private static final String KEY_USER_LOGGED_IN = "logged_in";
    private static final String KEY_CLOSED_FOR_THE_DAY = "closed_for_the_day";
    SharedPreferences sharedPreferences;
    static UserPreference INSTANCE;

    private UserPreference(Context context) {
        sharedPreferences = context.getSharedPreferences(USER_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public void setUsername(String name) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USERNAME, name);
        editor.commit();
    }

    public boolean isUserLoggedIn() {
        return sharedPreferences.getBoolean(KEY_USER_LOGGED_IN, false);
    }

    public void setUserLoggedIn(boolean isLoggedIn) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_USER_LOGGED_IN, isLoggedIn);
        editor.commit();
    }


    public boolean isUserClosedForTheDay() {
        return sharedPreferences.getBoolean(KEY_CLOSED_FOR_THE_DAY, false);
    }

    public void setUserCloseForTheDay(boolean closedForTheDay) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_CLOSED_FOR_THE_DAY, closedForTheDay);
        editor.commit();
    }

    public String getUsername() {
        return sharedPreferences.getString(KEY_USERNAME, "");
    }


    public void setRepId(String repId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_REP_ID, repId);
        editor.commit();
    }

    public String getRepId() {
        return sharedPreferences.getString(KEY_REP_ID, "0");
    }

    public void setRepCode(String repCode) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_REP_CODE, repCode);
        editor.commit();
    }

    public String getRepCode() {
        return sharedPreferences.getString(KEY_REP_CODE, "0");
    }

    public void setFullName(String fullName) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_FULLNAME, fullName);
        editor.commit();
    }

    public String getFullName() {
        return sharedPreferences.getString(KEY_FULLNAME, "");
    }


    public static UserPreference getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new UserPreference(context);
        }

        return INSTANCE;
    }
}