package mydist.mydist.fragments;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

import mydist.mydist.R;
import mydist.mydist.models.Product;
import mydist.mydist.utils.DataUtils;
import mydist.mydist.utils.FontManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoreInfoInvoiceFragment extends Fragment {


    public StoreInfoInvoiceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_store_info, container, false);
        TableLayout tableLayout = (TableLayout) view.findViewById(R.id.tl_products);
        List<Product> products = DataUtils.getAllProducts(getActivity());
        View rowView;
        int index = 1;
        for (Product product : products) {
            tableLayout.addView(getLine(getActivity()), index++);
            tableLayout.addView(getProductView(getActivity(), product), index++);
        }
        setFonts(view);
        return view;
    }

    private void setFonts(View v) {
        Typeface ralewayFont = FontManager.getTypeface(getActivity().getApplicationContext(), FontManager.RALEWAY_REGULAR);
        FontManager.setFontsForView(v.findViewById(R.id.parent_layout), ralewayFont);
    }

    private TableRow getLine(Context context) {
        TableRow tableRow = new TableRow(context);
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams
                (TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        tableRow.setLayoutParams(layoutParams);

        View textView = new View(context);
        textView.setLayoutParams(
                new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        toDp(1)));
        textView.setBackgroundColor(Color.parseColor("#ffffff"));
        tableRow.addView(textView);
        return tableRow;
    }

    private TableRow getProductView(Context context, Product product) {
        TableRow tableRow = getTableRow(context);

        return tableRow;
    }

    @NonNull
    private TableRow getTableRow(Context context) {
        TableRow tableRow = new TableRow(context);
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams
                (TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(toDp(0), toDp(4), 0, 0);
        tableRow.setMinimumHeight(toDp(56));
        tableRow.setLayoutParams(layoutParams);
        return tableRow;
    }

    private LinearLayout generatePriceSection(Context context, Product product) {
        LinearLayout ll = new LinearLayout(context);
        ll.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        ll.setPadding(toDp(4), toDp(4), 0, 0);
        ll.addView(getProductNameTV(getActivity(),product.getProductName() ));

        return ll;
    }

    private TextView getProductNameTV(Context context, String name) {
        TextView tv = new TextView(context);
        tv.setText(name);
        tv.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        tv.setMinWidth(toDp(160));
        setTextAppearance(context, tv, R.style.textAppearanceMedium);
        tv.setTextColor(Color.parseColor("#212121"));
        return tv;
    }

    @SuppressWarnings("deprecation")
    public void setTextAppearance(Context context, TextView tv, int resId) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            tv.setTextAppearance(context, resId);
        } else {
            tv.setTextAppearance(resId);
        }
    }

    private int toDp(int pixel) {
        float scale = getResources().getDisplayMetrics().density;
        int pixelsAsDp = (int) (pixel * scale + 0.5f);

        return pixel;
    }

}
