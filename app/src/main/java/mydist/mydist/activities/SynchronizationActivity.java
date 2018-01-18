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
import mydist.mydist.models.Invoice;
import mydist.mydist.models.NewRetailer;
import mydist.mydist.models.push.CallAnalysis;
import mydist.mydist.models.push.CollectionPush;
import mydist.mydist.models.push.Coverage;
import mydist.mydist.models.push.InvoicePush;
import mydist.mydist.models.push.MasterPush;
import mydist.mydist.models.push.MerchandizingPush;
import mydist.mydist.models.push.NewRetailerPush;
import mydist.mydist.utils.DataUtils;
import mydist.mydist.utils.Days;
import mydist.mydist.utils.FontManager;
import mydist.mydist.utils.UIUtils;

public class SynchronizationActivity extends AuthenticatedActivity implements View.OnClickListener {

    Button mStartSyncButton;
    EditText mUsername;
    EditText mPassword;
    CheckBox mCloseForTheDay;
    TextView mUsernameLabel;
    TextView mPasswordLabel;
    ProgressDialog mProgressDialog;
    UserPreference userPreference;
    Cursor allInvoiceCursor;

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
        mUsername = (EditText) findViewById(R.id.et_username);
        mUsername.setText(getUsername());
        mPassword = (EditText) findViewById(R.id.password);
        mUsernameLabel = (TextView) findViewById(R.id.username_label);
        mPasswordLabel = (TextView) findViewById(R.id.password_label);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getString(R.string.synchronization_message));
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
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
            if (mCloseForTheDay.isChecked()) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String todayDate = dateFormat.format(new Date());
                UserPreference.getInstance(this).setUserCloseForTheDayDate(todayDate);
                AlertDialog dialog = new AlertDialog.Builder(SynchronizationActivity.this).
                        setMessage(this.getString(R.string.closing_message, todayDate)).
                        setPositiveButton(this.getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                generatePushMasters();
                            }
                        }).create();
                dialog.show();
            }
        }
    }

    private void generatePushMasters() {
        String repId = userPreference.getRepId();
        String repCode = userPreference.getRepCode();
        List<NewRetailerPush> newRetailerPushes = getNewRetailers();
        List<CollectionPush> collectionPushes = getCollectionPushes();
        List<Coverage> coverages = getCoverages();
        MasterPush push = new MasterPush(repId,
                repCode, newRetailerPushes, collectionPushes,
                coverages);
        try {
            ObjectMapper mapper = new ObjectMapper();
            System.out.println(mapper.writeValueAsString(push));
            Toast.makeText(this, "Report generated Successfully", Toast.LENGTH_SHORT).show();
        } catch (JsonProcessingException e) {
            Log.e("Exception", e.getMessage());
            Toast.makeText(this, "Unable to collate Report", Toast.LENGTH_SHORT).show();
        }
    }

    public String getUsername() {
        return UserPreference.getInstance(this).getUsername();
    }

    public List<NewRetailerPush> getNewRetailers() {
        List<NewRetailerPush> newRetailers = new ArrayList<>();
        Cursor cursor = DatabaseManager.getInstance(this).getAllNewRetailers(Days.getTodayDate());
        int count = cursor.getCount();
        if (count > 0) {
            cursor.moveToFirst();
            List<String> suggestedVisitDays;
            List<String> weekNo;
            String retailerId = cursor.getString(cursor.getColumnIndex( MasterContract.RetailerContract.RETAILER_ID));
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
            coverages.add(new Coverage(retailerId, date, invoicePushes, merchandizingPushes,
                    new CallAnalysis(20000, 12000, 20)));

        }

        return coverages;
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
        if (count > 0) {
            allInvoiceCursor.moveToFirst();
            for (int i = 0; i < count; i++) {
                retailerIds.add(allInvoiceCursor.getString(allInvoiceCursor.getColumnIndex(MasterContract.InvoiceContract.RETAILER_ID)));

            }
        }

        return retailerIds;
    }

    public HashMap<String, List<InvoicePush>> getInvoicePushMap() {
        HashMap<String, List<InvoicePush>> invoicePushMap = new HashMap<>();
        Cursor cursor = DatabaseManager.getInstance(this).getInvoicePush(Days.getTodayDate());
        int count = cursor.getCount();
        List<InvoicePush> invoicePushes;
        String retailerId;
        if (count > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < count; i++) {
                retailerId = cursor.getString(cursor.getColumnIndex(MasterContract.InvoiceContract.RETAILER_ID));
                if (invoicePushMap.containsKey(retailerId)) {
                    invoicePushMap.get(retailerId).add(new InvoicePush(cursor));
                } else {
                    invoicePushes = new ArrayList<>();
                    invoicePushes.add(new InvoicePush(cursor));
                    invoicePushMap.put(retailerId,invoicePushes);
                }
                cursor.moveToNext();
            }

        }

        return invoicePushMap;
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
}
