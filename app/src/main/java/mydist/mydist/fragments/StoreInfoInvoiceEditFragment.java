package mydist.mydist.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import mydist.mydist.R;
import mydist.mydist.activities.LoginActivity;
import mydist.mydist.adapters.InvoiceAdapter;
import mydist.mydist.data.DatabaseManager;
import mydist.mydist.utils.DataUtils;
import mydist.mydist.utils.FontManager;

import static mydist.mydist.activities.StoreOverviewActivity.retailerId;


public class StoreInfoInvoiceEditFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    InvoiceAdapter adapter;
    ListView listView;
    TextView invoiceMessage;
    LinearLayout contentContainer;
    View page;
    public static final String KEY_RETAILER_ID = "retailer_id";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_store_info_invoice_cancel, container, false);

        invoiceMessage = (TextView) view.findViewById(R.id.invoice_message);
        contentContainer = (LinearLayout) view.findViewById(R.id.parent_layout);
        String retailerId = getArguments().getString(KEY_RETAILER_ID);
        listView = (ListView) view.findViewById(R.id.list_view);
        page = view;
        bindView();
        return view;
    }

    private void bindView() {
        Cursor cursor = DataUtils.getAllInvoice(retailerId, getActivity());
        if (cursor.getCount() > 0) {
            adapter = new InvoiceAdapter(getActivity(), cursor, this);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(this);
            invoiceMessage.setVisibility(View.GONE);
            contentContainer.setVisibility(View.VISIBLE);
        } else {
            invoiceMessage.setVisibility(View.VISIBLE);
            contentContainer.setVisibility(View.GONE);
        }
        setFonts(page);
    }

    public static StoreInfoInvoiceEditFragment getNewInstance(String retailerId) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_RETAILER_ID, retailerId);
        StoreInfoInvoiceEditFragment storeInfoInvoiceEditFragment = new StoreInfoInvoiceEditFragment();
        storeInfoInvoiceEditFragment.setArguments(bundle);
        return storeInfoInvoiceEditFragment;
    }

    private void setFonts(View v) {
        Typeface ralewayFont = FontManager.getTypeface(getActivity().getApplicationContext(), FontManager.RALEWAY_REGULAR);
        FontManager.setFontsForView(v.findViewById(R.id.parent_layout), ralewayFont);
    }

    @Override
    public void onClick(View v) {
        final String invoiceId = v.getTag().toString();
        if (v.getId() == R.id.icon_invoice_edit) {

        } else if (v.getId() == R.id.icon_invoice_delete) {
            AlertDialog mDialog = new AlertDialog.Builder(getActivity()).
                    setMessage(getString(R.string.inoice_delete_question)).
                    setPositiveButton(getString(R.string.yes),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    boolean result = DataUtils.deleteRetailer(invoiceId, getContext());
                                    if (result) {
                                        Toast.makeText(getActivity(), getString(R.string.invoice_delete_success), Toast.LENGTH_SHORT).show();
                                        bindView();
                                    } else {
                                        Toast.makeText(getActivity(), getString(R.string.invoice_delete_error), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).
                    setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).create();
            mDialog.show();
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
