package mydist.mydist.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import mydist.mydist.R;
import mydist.mydist.data.DatabaseManager;
import mydist.mydist.data.MasterContract;
import mydist.mydist.data.UserPreference;
import mydist.mydist.listeners.UploadMastersListener;
import mydist.mydist.models.Invoice;
import mydist.mydist.models.MerchandizingVerification;
import mydist.mydist.models.NewRetailer;
import mydist.mydist.models.UploadMastersResponse;
import mydist.mydist.models.push.CallAnalysis;
import mydist.mydist.models.push.CollectionPush;
import mydist.mydist.models.push.Coverage;
import mydist.mydist.models.push.InvoicePush;
import mydist.mydist.models.push.MasterPush;
import mydist.mydist.models.push.MerchandizingPush;
import mydist.mydist.models.push.NewRetailerPush;
import mydist.mydist.network.NetworkUtils;
import mydist.mydist.network.UploadMastersClient;
import mydist.mydist.utils.DataUtils;
import mydist.mydist.utils.DatabaseLogicUtils;
import mydist.mydist.utils.Days;
import mydist.mydist.utils.FontManager;
import mydist.mydist.utils.UIUtils;

public class SynchronizationActivity extends AuthenticatedActivity implements View.OnClickListener, UploadMastersListener, DialogInterface.OnClickListener {

    private static final String TAG = SynchronizationActivity.class.getSimpleName();
    Button mStartSyncButton;
    CheckBox mCloseForTheDay;
    ProgressDialog mProgressDialog;
    UserPreference userPreference;
    Cursor allInvoiceCursor;
    TextView uploadIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synchronization);
        getReferencesToViews();
        setOnClickListeners();
        setFonts();
        userPreference = UserPreference.getInstance(this);
    }

    private void setFonts() {
        Typeface ralewayFont = FontManager.getTypeface(getApplicationContext(), FontManager.RALEWAY_REGULAR);
        FontManager.setFontsForView(findViewById(R.id.parent_layout), ralewayFont);
    }

    private void setOnClickListeners() {
        mStartSyncButton.setOnClickListener(this);
    }

    public void getReferencesToViews() {
        setupToolBar();
        mStartSyncButton = (Button) findViewById(R.id.bt_start_sync);
        mCloseForTheDay = (CheckBox) findViewById(R.id.cb_close_for_the_day);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getString(R.string.synchronization_message));
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        uploadIcon = (TextView) findViewById(R.id.upload_icon);
        uploadIcon.setTag(FontManager.IMMUTABLE_TYPFACE_USED);
        Typeface typeface = FontManager.getTypeface(this, FontManager.FONT_AWESOME);
        uploadIcon.setTypeface(typeface);
    }

    private void setupToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String title = getString(R.string.synchronization);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_synchronization, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        UIUtils.hideKeyboard(this);
        if (v.getId() == R.id.bt_start_sync) {
            if (!NetworkUtils.isNetworkAvailable(SynchronizationActivity.this)) {
                launchDialog(R.string.network_error);
            } else {
                if (mCloseForTheDay.isChecked()) {
                    AlertDialog dialog = new AlertDialog.Builder(SynchronizationActivity.this).
                            setMessage(this.getString(R.string.closing_message, Days.getTodayDate())).
                            setPositiveButton(this.getString(R.string.ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mProgressDialog.setMessage(getString(R.string.generating_masters));
                                    MasterPush push = generatePushMasters();
                                    if (push != null) {
                                        try {
                                            ObjectMapper mapper = new ObjectMapper();
                                            JSONObject masters = new JSONObject(mapper.writeValueAsString(push));
                                            Log.e(TAG, masters.toString());
                                            new UploadMastersClient().uploadMasters(masters, SynchronizationActivity.this);
                                        } catch (JsonProcessingException e) {

                                        } catch (JSONException e) {
                                        }
                                    } else {
                                        mProgressDialog.cancel();
                                        launchDialog(R.string.generating_masters_error);
                                    }
                                }
                            }).create();
                    dialog.show();
                }
            }
        }
    }

    private void launchDialog(int stringResource, DialogInterface.OnClickListener listener) {
        AlertDialog mDialog = new AlertDialog.Builder(SynchronizationActivity.this).
                setMessage(getString(stringResource)).
                setPositiveButton(SynchronizationActivity.this.getText(R.string.ok), listener).create();
        mDialog.show();
    }

    private void launchDialog(int stringResource) {
        launchDialog(stringResource, null);
    }

    private MasterPush generatePushMasters() {
        MasterPush push;
        try {
            String repId = userPreference.getRepId();
            String repCode = userPreference.getRepCode();
            String username = userPreference.getUsername();
            List<NewRetailerPush> newRetailerPushes = getNewRetailers();
            List<CollectionPush> collectionPushes = getCollectionPushes();
            List<Coverage> coverages = getCoverages();
            push = new MasterPush(repId, username,
                    repCode, newRetailerPushes, collectionPushes,
                    coverages);
            Toast.makeText(this, "Report generated Successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Unable to collate Report", Toast.LENGTH_SHORT).show();
            return null;
        }
        return push;
    }

    public String getUsername() {
        return UserPreference.getInstance(this).getUsername();
    }

    public List<NewRetailerPush> getNewRetailers() {
        List<NewRetailerPush> newRetailers = new ArrayList<>();
        Cursor cursor = DatabaseManager.getInstance(this).getAllNewRetailers(Days.getRetailerDate());
        int count = cursor.getCount();
        if (count > 0) {
            cursor.moveToFirst();
            List<String> suggestedVisitDays;
            List<String> weekNo;
            String retailerId = cursor.getString(cursor.getColumnIndex(MasterContract.RetailerContract.RETAILER_ID));
            HashMap<String, List<String>> visitingInfo;
            String key_days = "days", key_weeks = "weeks";
            for (int i = 0; i < count; i++) {
                visitingInfo = getVisitingInfo(retailerId, key_days, key_weeks);
                suggestedVisitDays = visitingInfo.get(key_days);
                weekNo = visitingInfo.get(key_weeks);
                newRetailers.add(new NewRetailerPush(cursor, suggestedVisitDays, weekNo));
                cursor.moveToNext();
            }
        }

        return newRetailers;
    }

    public List<CollectionPush> getCollectionPushes() {
        List<CollectionPush> pushes = new ArrayList<>();
        Cursor cursor = DataUtils.getAllInvoice(null, Invoice.KEY_STATUS_SUCCESS, this);
        int count = cursor.getCount();
        if (count > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < count; i++) {
                pushes.add(new CollectionPush(cursor));
                cursor.moveToNext();
            }
        }
        return pushes;
    }

    public List<Coverage> getCoverages() {
        List<Coverage> coverages = new ArrayList<>();
        List<String> retailers = getRetailerId();
        String date = Days.getTodayDate();
        HashMap<String, List<InvoicePush>> invoicePushesMap = getInvoicePushMap();
        HashMap<String, List<MerchandizingPush>> merchandizingPushesMap = getMerchandizingPushMap();
        List<InvoicePush> invoicePushes;
        List<MerchandizingPush> merchandizingPushes;
        for (String retailerId : retailers) {
            if (invoicePushesMap.containsKey(retailerId)) {
                invoicePushes = invoicePushesMap.get(retailerId);
            } else {
                invoicePushes = new ArrayList<>();
            }
            if (merchandizingPushesMap.containsKey(retailerId)) {
                merchandizingPushes = merchandizingPushesMap.get(retailerId);
            } else {
                merchandizingPushes = new ArrayList<>();
            }
            Cursor cursor = DataUtils.getAllOrderTotal(retailerId, Invoice.KEY_STATUS_SUCCESS, this);
            String storeTarget = DatabaseManager.getInstance(this).getHighestPurchaseValue(retailerId);
            storeTarget = DatabaseLogicUtils.getHighestPurchaseEver(storeTarget);
            String pskuTarget =  getPSKUM(null, retailerId);
            String pskuCount = getPSKUM(Days.getTodayDate(), retailerId);
            String merchandizingTarget = getMerchandizingVerification(Days.getTodayDate(), retailerId);
            double[] values = getValues(cursor);
            coverages.add(new Coverage(retailerId, date, invoicePushes, merchandizingPushes,
                    new CallAnalysis(values[0], values[1], 20), Double.valueOf(storeTarget), Integer.valueOf(pskuCount), Integer.valueOf(pskuTarget)
            , merchandizingTarget));
        }
        return coverages;
    }
    private String getMerchandizingVerification(String date, String retailerId) {
        String result = "0";
        Cursor cursor = DatabaseManager.getInstance(this).getMerchandisingVerificationGroupByRetailerId(date,
                MerchandizingVerification.STATUS_AVAILABLE, retailerId);
        String merch =  DatabaseManager.getInstance(this).getMerchandizingCount(date);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            result = cursor.getString(cursor.getColumnIndex(MasterContract.MerchandizingListVerificationContract.COUNT));
        }
        return getString(R.string.slashFormat, result,merch);
    }
    private String  getPSKUM(String date, String retailerId) {
        String result = "0";
        Cursor cursor = DatabaseManager.getInstance(this).getDistributionRate(date, retailerId);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            result = cursor.getString(cursor.getColumnIndex(MasterContract.InvoiceContract.TOTAL_ALIAS));
        }
       return result;
    }

    private double[] getValues(Cursor cursor) {

        float totalEarned = 0.00f;
        float amountCollected = 0.00f;
        try {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                if (!cursor.isNull(cursor.getColumnIndex(MasterContract.InvoiceContract.TOTAL_ALIAS))) {
                    totalEarned = cursor.
                            getFloat(cursor.getColumnIndex(MasterContract.InvoiceContract.TOTAL_ALIAS));
                }
                if (!cursor.isNull(cursor.getColumnIndex(MasterContract.InvoiceContract.AMOUNT_PAID_ALIAS))) {
                    amountCollected = cursor.
                            getFloat(cursor.getColumnIndex(MasterContract.InvoiceContract.AMOUNT_PAID_ALIAS));
                }
            }
        } catch (Exception e) {

        }
        return new double[]{totalEarned, amountCollected};
    }

    public HashMap<String, List<String>> getVisitingInfo(String retailerId, String keyDays, String keyWeeks) {

        HashMap<String, List<String>> visitingInfo = new HashMap<>();
        List<String> days = new ArrayList<>();
        List<String> weeks = new ArrayList<>();
        Cursor cursor = DatabaseManager.getInstance(this).getVisitingInfo(retailerId);
        cursor.moveToFirst();
        int count = cursor.getCount();
        String day;
        String week;
        for (int i = 0; i < count; i++) {
            day = cursor.getString(cursor.getColumnIndex(MasterContract.VisitingInfoContract.DAY));
            week = cursor.getString(cursor.getColumnIndex(MasterContract.VisitingInfoContract.WEEK));
            if (!days.contains(getDay(day))) {
                days.add(getDay(day));
            }
            if (!weeks.contains(getWeek(week))) {
                weeks.add(getWeek(week));
            }
            cursor.moveToNext();
        }
        visitingInfo.put(keyDays, days);
        visitingInfo.put(keyWeeks, weeks);
        return visitingInfo;
    }

    private String getWeek(String week) {
        return week.substring(week.length() - 1);
    }

    private String getDay(String day) {
        switch (day) {
            case "Sun":
                return "Sunday";
            case "Mon":
                return "Monday";
            case "Tue":
                return "Tuesday";
            case "Wed":
                return "Wednesday";
            case "Thur":
                return "Thursday";
            case "Fri":
                return "Friday";
            default:
                return "Saturday";
        }
    }

    public List<String> getRetailerId() {
        List<String> retailerIds = new ArrayList<>();
        allInvoiceCursor = DataUtils.getAllInvoice(null, Invoice.KEY_STATUS_SUCCESS, this);
        int count = allInvoiceCursor.getCount();
        String retailerId;
        if (count > 0) {
            allInvoiceCursor.moveToFirst();
            for (int i = 0; i < count; i++) {
                retailerId = allInvoiceCursor.getString(allInvoiceCursor.getColumnIndex(MasterContract.InvoiceContract.RETAILER_ID));
                if (!retailerIds.contains(retailerId)) {
                    retailerIds.add(retailerId);
                }
                allInvoiceCursor.moveToNext();
            }
        }
        return retailerIds;
    }

    public HashMap<String, List<InvoicePush>> getInvoicePushMap() {
        HashMap<String, List<InvoicePush>> invoicePushMap = new HashMap<>();
        HashMap<String, List<InvoicePush>> result = new HashMap<>();
        Cursor cursor = DatabaseManager.getInstance(this).getInvoicePush(Days.getTodayDate());
        int count = cursor.getCount();
        List<InvoicePush> invoicePushes;
        String retailerId, invoiceId;
        String temporaryMapKey;
        String DELIMITER = "_";
        if (count > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < count; i++) {
                retailerId = cursor.getString(cursor.getColumnIndex(MasterContract.InvoiceContract.RETAILER_ID));
                invoiceId = cursor.getString(cursor.getColumnIndex(MasterContract.ProductOrderContract.INVOICE_ID_ALIAS));
                temporaryMapKey = retailerId + DELIMITER + invoiceId;
                if (invoicePushMap.containsKey(temporaryMapKey)) {
                    invoicePushes = invoicePushMap.get(temporaryMapKey);
                    for (InvoicePush invoicePush : invoicePushes) {
                        if (invoicePush.getInvoiceNo().equalsIgnoreCase(invoiceId)) {
                            invoicePush.addProduct(cursor);
                            break;
                        }
                    }
                } else {
                    invoicePushes = new ArrayList<>();
                    InvoicePush invoicePush = new InvoicePush(cursor);
                    invoicePush.addProduct(cursor);
                    invoicePushes.add(invoicePush);
                    invoicePushMap.put(temporaryMapKey, invoicePushes);
                }
                cursor.moveToNext();
            }

            for (String key : invoicePushMap.keySet()) {
                temporaryMapKey = key.split(DELIMITER)[0];
                if (result.containsKey(temporaryMapKey)) {
                    result.get(temporaryMapKey).addAll(invoicePushMap.get(key));
                } else {
                    result.put(key.split(DELIMITER)[0], invoicePushMap.get(key));
                }
            }
        }
        return result;
    }

    public HashMap<String, List<MerchandizingPush>> getMerchandizingPushMap() {
        HashMap<String, List<MerchandizingPush>> merchandizingPushMap = new HashMap<>();
        Cursor cursor = DatabaseManager.getInstance(this).getAllMerchandising(Days.getTodayDate());
        int count = cursor.getCount();
        List<MerchandizingPush> merchandizingPushes;
        String retailerId;
        if (count > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < count; i++) {
                retailerId = cursor.getString(cursor.getColumnIndex(MasterContract.MerchandizingListVerificationContract.RETAILER_ID));
                if (merchandizingPushMap.containsKey(retailerId)) {
                    merchandizingPushMap.get(retailerId).add(new MerchandizingPush(cursor));
                } else {
                    merchandizingPushes = new ArrayList<>();
                    merchandizingPushes.add(new MerchandizingPush(cursor));
                    merchandizingPushMap.put(retailerId, merchandizingPushes);
                }
                cursor.moveToNext();
            }
        }
        return merchandizingPushMap;
    }

    @Override
    public void onStartUpload() {
        mProgressDialog.setMessage(getString(R.string.uploading_masters));
        mProgressDialog.show();
    }

    @Override
    public void onSuccess(UploadMastersResponse response) {
        mProgressDialog.cancel();
        if (response.getStatus().isSuccess()) {
            UserPreference.getInstance(this).setUserCloseForTheDayDate(Days.getTodayDate().toString());
            launchDialog(R.string.upload_success, this);
        } else {
            launchDialog(R.string.upload_failed);
        }
    }

    @Override
    public void onFailure()
    { mProgressDialog.cancel();
        launchDialog(R.string.upload_failed);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        onBackPressed();
    }
}
