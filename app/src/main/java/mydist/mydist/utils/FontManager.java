package mydist.mydist.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.AppBarLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by Blessing.Ekundayo on 11/1/2017.
 */

public class FontManager {
    public static final String ROOT = "fonts/",
            FONT_AWESOME = ROOT + "fontawesome_webfont.ttf";
    public static final String RALEWAY_BOLD = ROOT + "raleway_bold.ttf";
    public static final String RALEWAY_LIGHT = ROOT + "raleway_light.ttf";
    public static final String RALEWAY_REGULAR = ROOT + "raleway_regular.ttf";
    static HashMap<String, Typeface> fonts = new HashMap<>();
    public static final String IMMUTABLE_TYPFACE_USED = "immutable";

    public static Typeface getTypeface(Context context, String font) {
        if (fonts.containsKey(font)) {
            return fonts.get(font);
        } else {
            return Typeface.createFromAsset(context.getAssets(), font);
        }
    }

    public static void setFontsForView(View v, Typeface typeface) {

            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    setFontsForView(child, typeface);
                }
            } else if (v instanceof TextView || v instanceof EditText) {
                if(v.getTag() != null && v.getTag().equals(IMMUTABLE_TYPFACE_USED)) {
                    return;
                }
                ((TextView) v).setTypeface(typeface);
            } else if (v instanceof Button) {
                ((Button) v).setTypeface(getTypeface(v.getContext(), RALEWAY_BOLD));
            }

    }

}
