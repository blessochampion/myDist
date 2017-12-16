package mydist.mydist.fragments;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import mydist.mydist.R;
import mydist.mydist.activities.LoginActivity;
import mydist.mydist.data.ProductLogic;
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

    public static final int TOTAL_POSITION = 4;
    TableLayout mTableLayout;
    TableLayout mPagination;
    List<Product> products;
    TextView mTotalAmount;
    int currentPage = 0;
    Context context;
    double totalAmountToBePaid = 0;
    HashMap<String, ProductLogic> selectedProducts = new HashMap<>();
    private static final int OC_POSITION = 2;
    private static final int OP_POSITION = 3;
    private static final String DELIMETER = ":";
    private String EMPTY_STRING = "";


    public StoreInfoInvoiceFragment() {
        // Required empty public constructor
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.forward){
            if(selectedProducts.size() == 0){
                new AlertDialog.Builder(context).
                        setMessage(getString(R.string.no_product_error)).
                        setPositiveButton(getString(R.string.ok), null).create().show();

            }else {
                ProductLogic productLogic;
                for (String key : selectedProducts.keySet()) {
                    productLogic = selectedProducts.get(key);
                    if (productLogic.getTotal() == 0) {
                        new AlertDialog.Builder(context).
                                setMessage("Select OC/OP for " + productLogic.getProduct().getProductName()).
                                setPositiveButton(getString(R.string.ok), null).create().show();
                        return true;
                    }
                }

                //go to next activity
            }

            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_store_info, container, false);
        mTableLayout = (TableLayout) view.findViewById(R.id.tl_products);
        mPagination = (TableLayout) view.findViewById(R.id.pagination);
        mTotalAmount = (TextView) view.findViewById(R.id.total_amount);
        products = DataUtils.getAllProducts(getActivity());
        context = getActivity();
        setHasOptionsMenu(true);
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

    private void updateTotalValue(String indicator, String value, EditText totalEditText) {
        String[] keys = indicator.split(DELIMETER);
        if (selectedProducts.containsKey(keys[0])) {
            ProductLogic productLogic = selectedProducts.get(keys[0]);
            //first remove the sum from total
            totalAmountToBePaid -= productLogic.getTotal();
            int viewPosition = Integer.valueOf(keys[1]);
            if (viewPosition == OC_POSITION) {
                productLogic.oc = Integer.valueOf(value);
            }
            if (viewPosition == OP_POSITION) {
                productLogic.op = Integer.valueOf(value);
            }

            totalAmountToBePaid += productLogic.getTotal();
            mTotalAmount.setText(String.format("%.2f",totalAmountToBePaid));
            totalEditText.setText(productLogic.getTotal() == 0 ? EMPTY_STRING : String.format("%.2f",productLogic.getTotal()));
        }
    }

    public void checkBoxClicked(CheckBox checkBox) {
        String[] values = ((String) checkBox.getTag()).split(DELIMETER);
        final String productId = values[0];
        int position = Integer.valueOf(values[1]);
        TableRow selectedRow = (TableRow) mTableLayout.getChildAt(position);
        if (checkBox.isChecked()) {
            selectedProducts.put(productId, new ProductLogic(products.get(currentPage * 10 + position - 1)));
            final EditText totalValueEditText = (EditText) selectedRow.getChildAt(TOTAL_POSITION);
            for (int i = 2; i < 4; i++) {
                View view = selectedRow.getChildAt(i);
                final int viewPosition = i;
                if (view instanceof EditText) {
                    view.setEnabled(true);
                    final EditText editText = ((EditText) view);
                    editText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            int length = s.toString().length();
                            if (length == 1 && s.toString().equalsIgnoreCase("0")) {
                                editText.setText("");
                            } else if (length >= 1) {
                                char lastChar = s.toString().charAt(length - 1);
                                if (!('0' <= lastChar && lastChar <= '9')) {
                                    editText.setText(s.toString().substring(0, count - 1));
                                } else {
                                    updateTotalValue(productId + DELIMETER + viewPosition, s.toString(), totalValueEditText);
                                }
                            }else{
                                updateTotalValue(productId + DELIMETER + viewPosition, "0", totalValueEditText);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                }
            }

        } else {
            for (int i = 2; i <= 4; i++) {
                View view = selectedRow.getChildAt(i);
                if (view instanceof EditText) {
                    ((EditText) view).setText(EMPTY_STRING);
                    view.setEnabled(false);
                }
            }
            ProductLogic productLogic = selectedProducts.get(productId);

            totalAmountToBePaid -= productLogic.getTotal();
            mTotalAmount.setText(String.format("%.2f",totalAmountToBePaid));
            selectedProducts.remove(productId);
        }
    }

    private void loadProductIntoRow(TableRow productRow, Product currentProduct, int position) {
        //product name
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        layoutParams.setMargins(0, 0, 10, 0);
        TextView productName = new TextView(context);
        productName.setText(currentProduct.getProductName());
        productName.setGravity(CENTER_VERTICAL | LEFT);
        productName.setLayoutParams(layoutParams);
        productRow.addView(productName);

        //select
        CheckBox selectCheckbox = generateCheckbox(context);
        selectCheckbox.setMinHeight(80);
        selectCheckbox.setTag(currentProduct.getProductId() + ":" + (position + 1));
        selectCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBoxClicked((CheckBox) v);
            }
        });

        productRow.addView(selectCheckbox);


        //oc
        EditText ocEditText = new EditText(context);
        ocEditText.setBackgroundResource(R.drawable.product_item);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            ocEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        ocEditText.setLayoutParams(layoutParams);
        ocEditText.setMinHeight(80);
        ocEditText.setPadding(4, 0, 4, 0);
        ocEditText.setGravity(CENTER);
        ocEditText.setEnabled(false);
        productRow.addView(ocEditText);

        //op
        EditText opEditText = new EditText(context);
        opEditText.setBackgroundResource(R.drawable.product_item);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            opEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        opEditText.setLayoutParams(layoutParams);
        opEditText.setMinHeight(80);
        opEditText.setPadding(4, 0, 4, 0);
        opEditText.setGravity(CENTER);
        opEditText.setEnabled(false);
        productRow.addView(opEditText);

        //total
        EditText totalEditText = new EditText(context);
        totalEditText.setBackgroundResource(R.drawable.product_item);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            totalEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        totalEditText.setLayoutParams(layoutParams);
        totalEditText.setMinHeight(80);
        totalEditText.setPadding(4, 0, 4, 0);
        totalEditText.setGravity(CENTER);
        totalEditText.setEnabled(false);
        productRow.addView(totalEditText);
        if (selectedProducts.containsKey(currentProduct.getProductId())){
            ProductLogic logic = selectedProducts.get(currentProduct.getProductId());
            selectCheckbox.setChecked(true);
            ocEditText.setText(logic.oc == 0 ? "" : String.valueOf(logic.oc));
            ocEditText.setEnabled(true);
            opEditText.setText(logic.op == 0 ? "" : String.valueOf(logic.op));
            opEditText.setEnabled(true);
            totalEditText.setText(String.valueOf(logic.getTotal()));
        }


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
