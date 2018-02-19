package mydist.mydist.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.Locale;

import mydist.mydist.R;

/**
 * Created by Blessing.Ekundayo on 8/31/2017.
 */

public class UIUtils {
    public static void hideKeyboard(Activity activity) {

        View view = activity.getCurrentFocus();

        if (view != null) {

            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        }

    }

    public static void showToast(Activity activity, String message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }

    public static void setLightFont(TextView v, Activity activity) {
        Typeface typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/raleway_regular.ttf");
        v.setTypeface(typeface);
    }

    public static void setLightFont(CheckBox c, Activity activity) {
        Typeface typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/raleway_regular.ttf");
        c.setTypeface(typeface);
    }

    public static void setBoldFont(TextView v, Activity activity) {
        Typeface typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/raleway_bold.ttf");
        v.setTypeface(typeface);
    }

    public static void setLightFont(Button b, Activity activity) {
        Typeface typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/raleway_regular.ttf");
        b.setTypeface(typeface);
    }

    public static void setBoldFont(Button b, Activity activity) {
        Typeface typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/raleway_bold.ttf");
        b.setTypeface(typeface);
    }

    public static void setLightFont(EditText e, Activity activity) {
        Typeface typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/raleway_regular.ttf");
        e.setTypeface(typeface);
    }

    public static void setBoldFont(EditText e, Activity activity) {
        Typeface typeface = Typeface.createFromAsset(activity.getAssets(), "fonts/raleway_bold.ttf");
        e.setTypeface(typeface);
    }

    public static String formatValue(String value) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        return numberFormat.format(value);
    }

    public static void launchDialog(String message, Context context) {
        AlertDialog mDialog = new AlertDialog.Builder(context).
                setMessage(message).
                setPositiveButton(context.getText(R.string.ok), null).create();
        mDialog.show();
    }
}
