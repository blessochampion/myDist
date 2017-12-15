package mydist.mydist.fragments;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

import mydist.mydist.R;
import mydist.mydist.models.Product;
import mydist.mydist.utils.DataUtils;
import mydist.mydist.utils.FontManager;

import static android.view.Gravity.CENTER;
import static android.view.Gravity.CENTER_VERTICAL;
import static android.view.Gravity.LEFT;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoreInfoInvoiceFragment extends Fragment implements View.OnClickListener {

    TableLayout mTableLayout;
    TableLayout mPagination;
    List<Product> products;
    int currentPage = 0;
    Context context;

    public StoreInfoInvoiceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_store_info, container, false);
        mTableLayout = (TableLayout) view.findViewById(R.id.tl_products);
        mPagination = (TableLayout) view.findViewById(R.id.pagination);
        products = DataUtils.getAllProducts(getActivity());
        context = getActivity();

        initProducts();
        initPagination();
        loadProducts();
        setFonts(view);
        return view;
    }

    private void loadProducts() {
        TableRow productRow;
        int start = currentPage * 10;
        int end = start + 10;
        Product currentProduct;
        TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.bottomMargin = 10;
        layoutParams.topMargin = 10;
        int index = 0;
        int childCount = mTableLayout.getChildCount();
        if (childCount > 1) {
            mTableLayout.removeViews(1, childCount - 1);
        }
        for (; start < end; start++, index++) {
            productRow = new TableRow(context);
            currentProduct = products.get(start);
            loadProductIntoRow(productRow, currentProduct, index);
            productRow.setLayoutParams(layoutParams);
            mTableLayout.addView(productRow);


        }


    }

    private void loadProductIntoRow(TableRow productRow, Product currentProduct, int position) {
        //product name
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        layoutParams.setMargins(0, 0, 10, 0);
        TextView productName = new TextView(context);
        productName.setText(currentProduct.getProductName());
        productName.setGravity(CENTER_VERTICAL|LEFT);
        productName.setLayoutParams(layoutParams);
        productRow.addView(productName);

        //select
        CheckBox selectCheckbox = generateCheckbox(context);
        selectCheckbox.setMinHeight(80);
        productRow.addView(selectCheckbox);


        //oc
        EditText ocEditText = new EditText(context);
        ocEditText.setBackgroundResource(R.drawable.product_item);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            ocEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        ocEditText.setLayoutParams(layoutParams);
        ocEditText.setMinHeight(80);
        ocEditText.setPadding(4, 0,4, 0);
        ocEditText.setGravity(CENTER);
        productRow.addView(ocEditText);

        //op
        EditText opEditText = new EditText(context);
        opEditText.setBackgroundResource(R.drawable.product_item);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            opEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        opEditText.setLayoutParams(layoutParams);
        opEditText.setMinHeight(80);
        opEditText.setPadding(4, 0,4, 0);
        opEditText.setGravity(CENTER);
        productRow.addView(opEditText);

        //total
        EditText totalEditText = new EditText(context);
        totalEditText.setBackgroundResource(R.drawable.product_item);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            totalEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        totalEditText.setLayoutParams(layoutParams);
        totalEditText.setMinHeight(80);
        totalEditText.setPadding(4, 0,4, 0);
        totalEditText.setGravity(CENTER);
        productRow.addView(totalEditText);


    }

    private void initProducts() {
        TableRow header = new TableRow(context);
        mTableLayout.setPadding(32, 0, 32, 0);
        header.setBackgroundColor(Color.WHITE);

        //product header
        TextView productHeader = getHeader(getResources().getString(R.string.product));
        header.addView(productHeader);

        //Select
        TextView selectHeader = getHeader(getResources().getString(R.string.select));
        header.addView(selectHeader);

        //OC
        TextView ocHeader = getHeader(getResources().getString(R.string.oc));
        header.addView(ocHeader);

        //OP
        TextView opHeader = getHeader(getResources().getString(R.string.op));
        header.addView(opHeader);

        //Total
        TextView totalHeader = getHeader(getResources().getString(R.string.total));
        header.addView(totalHeader);

        mTableLayout.addView(header);

        mTableLayout.setColumnStretchable(0, true);
        mTableLayout.setColumnStretchable(1, true);
        mTableLayout.setColumnStretchable(2, true);
        mTableLayout.setColumnStretchable(3, true);
        mTableLayout.setColumnStretchable(4, true);

    }

    private void initPagination() {
        TableRow pager = new TableRow(context);
        mPagination.setPadding(32, 0, 32, 0);
        int pageCount = (int) Math.ceil(products.size() / 10.0);
        loadPageToRow(pager, pageCount);
        mPagination.addView(pager);
    }

    private void loadPageToRow(TableRow pager, int pageCount) {
        for (int i = 0; i < pageCount; i++) {
            TextView page = new TextView(context);
            page.setTextSize(14);
            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, 80, 1);
            layoutParams.setMargins(0, 0, 15, 0);
            page.setLayoutParams(layoutParams);
            page.setGravity(CENTER);
            page.setMinWidth(100);
            page.setTextColor(Color.WHITE);
            if (i > 0) {
                unFill(page);
            } else {
                fill(page);
            }
            page.setText(i + 1 + "");
            pager.addView(page);
            page.setTag(i);
            page.setOnClickListener(this);
        }
    }

    private void fill(TextView page) {
        page.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        page.setTextColor(Color.WHITE);
    }

    private void unFill(TextView page) {
        page.setBackground(getResources().getDrawable(R.drawable.download_button));
        page.setTextColor(getResources().getColor(R.color.colorPrimary));
    }

    private TextView getHeader(String name) {
        TextView productHeader = new TextView(context);
        productHeader.setTextSize(14);
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, 90, 1);
        layoutParams.setMargins(0, 0, 6, 0);
        productHeader.setLayoutParams(layoutParams);
        //productHeader.setHeight(90);
        productHeader.setGravity(CENTER);
        productHeader.setMinWidth(200);
        productHeader.setPadding(16, 16, 16, 16);
        productHeader.setText(name);
        productHeader.setTextColor(Color.WHITE);
        productHeader.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        return productHeader;
    }

    private void setFonts(View v) {
        Typeface ralewayFont = FontManager.getTypeface(getActivity().getApplicationContext(), FontManager.RALEWAY_REGULAR);
        FontManager.setFontsForView(v.findViewById(R.id.parent_layout), ralewayFont);
    }

    private TableRow getLine(Context context) {
        TableRow tableRow = new TableRow(context);
        TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams
                (TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT);
        tableRow.setLayoutParams(layoutParams);

        View textView = new View(context);
        textView.setLayoutParams(
                new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        toDp(5)));
        textView.setBackgroundColor(Color.parseColor("#ffffff"));
        tableRow.addView(textView);
        return tableRow;
    }

    private TableRow getProductView(Context context, Product product) {
        TableRow tableRow = getTableRow(context);
        tableRow.addView(generatePriceSection(context, product));
        boolean enabled = true;
        int size = 56;
        int totalSize = 64;
        tableRow.addView(generateCheckbox(context));
        tableRow.addView(generateEditText(context, size, enabled));
        tableRow.addView(generateEditText(context, size, enabled));
        tableRow.addView(generateEditText(context, size, enabled));
        tableRow.addView(generateEditText(context, totalSize, !enabled));
        return tableRow;
    }

    private CheckBox generateCheckbox(Context context) {


        CheckBox checkbox = new CheckBox(context);
        TableRow.LayoutParams lpCheckbox = new TableRow.LayoutParams(
                toDp(56), TableRow.LayoutParams.MATCH_PARENT);
        lpCheckbox.setMargins(6, 0, 6, 0);
        checkbox.setLayoutParams(lpCheckbox);
        lpCheckbox.gravity = CENTER;
        checkbox.setTextColor(Color.parseColor("#212121"));

        return checkbox;
    }

    private EditText generateEditText(Context context, int size, boolean enable) {
        EditText editText = new EditText(context);
        TableRow.LayoutParams lpEditText = new TableRow.LayoutParams(
                toDp(size), TableRow.LayoutParams.MATCH_PARENT);
        lpEditText.setMargins(toDp(10), toDp(4), 8, 0);
        editText.setGravity(CENTER);
        // editText.setLayoutParams(lpEditText);
        editText.setTextColor(Color.parseColor("#212121"));
        if (!enable) {
            editText.setFocusable(false);
            editText.setFocusableInTouchMode(false);
            editText.setClickable(false);
        }
        editText.setBackgroundResource(R.drawable.product_item);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        }

        return editText;
    }

    @NonNull
    private TableRow getTableRow(Context context) {
        TableRow tableRow = new TableRow(context);
        TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams
                (TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
        //tableRow.setPadding(toDp(16), toDp(8), toDp(16), 8);
        tableRow.setMinimumHeight(toDp(90));
        tableRow.setLayoutParams(layoutParams);
        return tableRow;
    }

    private LinearLayout generatePriceSection(Context context, Product product) {
        LinearLayout ll = new LinearLayout(context);
        ll.setOrientation(LinearLayout.VERTICAL);
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams
                (TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        ll.setLayoutParams(layoutParams);
        ll.setPadding(toDp(4), toDp(4), 0, 0);
        ll.addView(getProductNameTV(getActivity(), product.getProductName()));
        ll.addView(getPPCAndPPQView(context, product));

        return ll;
    }

    private TextView getProductNameTV(Context context, String name) {
        TextView tv = new TextView(context);
        tv.setText(name);
        tv.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        tv.setMinWidth(toDp(136));
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

    public LinearLayout getPPCAndPPQView(Context context, Product product) {
        LinearLayout ll = new LinearLayout(context);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ll.setPadding(0, toDp(8), 0, 8);
        ll.addView(getPPViewText(context, getString(R.string.ppc)));
        ll.addView(getPPCViewValue(context, product.getCasePrice()));
        ll.addView(getPPViewText(context, getString(R.string.ppq)));
        ll.addView(getPPCViewValue(context, product.getPiecePrice()));

        return ll;
    }

    public TextView getPPViewText(Context context, String title) {
        TextView ppcTv = new TextView(context);
        ppcTv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
        ppcTv.setText(title);
        ppcTv.setGravity(CENTER);
        ppcTv.setTextColor(Color.parseColor("#212121"));
        ppcTv.setTextSize(toDp(18));

        return ppcTv;
    }

    public TextView getPPCViewValue(Context context, String price) {
        TextView ppcTv = new TextView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(toDp(4), 0, 0, 0);
        ppcTv.setText(price);
        ppcTv.setGravity(CENTER);
        ppcTv.setTextColor(Color.parseColor("#757575"));
        ppcTv.setTextSize(toDp(16));
        return ppcTv;
    }

    @Override
    public void onClick(View v) {
        int index = (Integer) v.getTag();
        TextView oldChild = (TextView) ((TableRow) mPagination.getChildAt(0)).getChildAt(currentPage);
        unFill(oldChild);
        TextView newChild = (TextView) ((TableRow) mPagination.getChildAt(0)).getChildAt(index);
        fill(newChild);
        currentPage = index;
        loadProducts();

    }
}
