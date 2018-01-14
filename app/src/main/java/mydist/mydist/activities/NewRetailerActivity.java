package mydist.mydist.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mydist.mydist.R;
import mydist.mydist.adapters.ChannelSpinnerAdapter;
import mydist.mydist.adapters.SubChannelSpinnerAdapter;
import mydist.mydist.data.DatabaseManager;
import mydist.mydist.data.UserPreference;
import mydist.mydist.models.Channel;
import mydist.mydist.models.NewRetailer;
import mydist.mydist.models.Retailer;
import mydist.mydist.models.SubChannel;
import mydist.mydist.utils.DataUtils;
import mydist.mydist.utils.Days;
import mydist.mydist.utils.FontManager;
import mydist.mydist.utils.UIUtils;

public class NewRetailerActivity extends AuthenticatedActivity {

    EditText mRetailerName;
    EditText mContactPerson;
    EditText mAddress;
    EditText mPhoneNumber;
    Spinner mChannelSpinner;
    Spinner mSubChannelSpinner;

    String retailerName;
    String contactPerson;
    String address;
    String phoneNumber;
    LinearLayout daysRowOne;
    LinearLayout daysRowTwo;
    LinearLayout weeks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_retailer);
        getReferencesToViews();
    }

    public void getReferencesToViews() {
        mRetailerName = (EditText) findViewById(R.id.et_new_retailer_name);
        mContactPerson = (EditText) findViewById(R.id.et_contact_person);
        mAddress = (EditText) findViewById(R.id.et_address);
        mPhoneNumber = (EditText) findViewById(R.id.et_phone);
        mChannelSpinner = (Spinner) findViewById(R.id.sp_channels);
        mSubChannelSpinner = (Spinner) findViewById(R.id.sp_sub_channels);
        setupToolBar();
        setupSpinners();
        daysRowOne = (LinearLayout) findViewById(R.id.cb_sun_wed);
        daysRowTwo = (LinearLayout) findViewById(R.id.cb_thur_sat);
        weeks = (LinearLayout) findViewById(R.id.cb_wk1_wk4);
    }

    private void setupSpinners() {
        final List<Channel> channels = DataUtils.getAllChannel(this);
        mChannelSpinner.setAdapter(new ChannelSpinnerAdapter(this, channels));
        final List<SubChannel> subChannels = DataUtils.getAllSubChannel(this);
        mSubChannelSpinner.setAdapter(new SubChannelSpinnerAdapter(this, subChannels));
    }

    private void setupToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String title = getString(R.string.new_retailer);
        getSupportActionBar().setTitle(title);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setFonts();
    }

    private void setFonts() {
        Typeface ralewayFont = FontManager.getTypeface(getApplicationContext(), FontManager.RALEWAY_REGULAR);
        FontManager.setFontsForView(findViewById(R.id.parent_layout), ralewayFont);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_retailer, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //overridePendingTransition(0, R.anim.transition_right_to_left);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.save) {
            saveRetailer();
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveRetailer() {
        UIUtils.hideKeyboard(this);
        if (userInputIsValid()) {

            NewRetailer newRetailer = getNewRetailer();
            if (DataUtils.saveNewRetailer(newRetailer, this)) {
                Toast.makeText(this, getString(R.string.new_retailer_info), Toast.LENGTH_SHORT).show();

            }
            onBackPressed();

        }
    }

    private boolean userInputIsValid() {
        retailerName = mRetailerName.getText().toString();
        if (retailerName.isEmpty()) {
            mRetailerName.setError(getString(R.string.input_empty_error));
            return false;
        }

        contactPerson = mContactPerson.getText().toString();
        if (contactPerson.isEmpty()) {
            mContactPerson.setError(getString(R.string.input_empty_error));
            return false;
        }

        address = mAddress.getText().toString();
        if (address.isEmpty()) {
            mAddress.setError(getString(R.string.input_empty_error));
            return false;
        }

        /*Todo: Try and add empty field for channel and sub channel*/

        phoneNumber = mPhoneNumber.getText().toString();
        if (phoneNumber.isEmpty()) {
            mPhoneNumber.setError(getString(R.string.input_empty_error));
            return false;
        }

        /*Check that at least one day is selected*/
        if (!hasAtLeastOneItemChecked(daysRowOne) && !hasAtLeastOneItemChecked(daysRowTwo)) {
            launchDialog(getString(R.string.days_error));
            return false;
        }

      /*Check that at least one week is selected*/
        if (!hasAtLeastOneItemChecked(weeks)) {
            launchDialog(getString(R.string.weeks_error));
            return false;
        }

        return true;
    }

    private void launchDialog(String message) {
        AlertDialog mDialog = new AlertDialog.Builder(NewRetailerActivity.this).
                setMessage(message).
                setPositiveButton(NewRetailerActivity.this.getText(R.string.ok), null).create();
        mDialog.show();
    }

    public boolean hasAtLeastOneItemChecked(LinearLayout linearLayout) {
        View view;
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            view = linearLayout.getChildAt(i);
            if (view instanceof CheckBox && ((CheckBox) view).isChecked())
                return true;
        }
        return false;
    }

    public NewRetailer getNewRetailer() {
        String dateAdded = Days.getTodayDate();
        String name = mRetailerName.getText().toString().trim();
        String contactPerson = mContactPerson.getText().toString().trim();
        String address = mAddress.getText().toString().trim();
        String phone = mPhoneNumber.getText().toString().trim();
        String channel = ((Channel) mChannelSpinner.getSelectedItem()).getChannelId();
        String subChannel = ((SubChannel) mSubChannelSpinner.getSelectedItem()).getSubChannelId();
        List<String> days = getSelectedDays();
        List<String> weeks = getSelectedWeeks();

        String salesRepFullName = UserPreference.getInstance(this).getFullName();
        String names[] = salesRepFullName.split(" ");
        String initials = names[0].charAt(0) + String.valueOf(names[1].charAt(0));
        String retailerId = initials + getRetailerIndex();
        Log.e("sass", retailerId);

        NewRetailer newRetailer = new NewRetailer(dateAdded,
                name, contactPerson, address, phone, channel, subChannel,
                days, weeks, retailerId);

        return newRetailer;
    }

    public List<String> getSelectedDays() {
        List<String> selectedDays = getSelectedItems(daysRowOne);
        selectedDays.addAll(getSelectedItems(daysRowTwo));
        return selectedDays;
    }

    public List<String> getSelectedItems(LinearLayout linearLayout) {
        List<String> selectedItems = new ArrayList<>();
        View view;
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            view = linearLayout.getChildAt(i);
            if (view instanceof CheckBox && ((CheckBox) view).isChecked())
                selectedItems.add(((CheckBox) view).getText().toString());
        }
        return selectedItems;
    }

    public List<String> getSelectedWeeks() {
        return getSelectedItems(weeks);
    }

    public String getRetailerIndex() {
        SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHHmmss");

        return f.format(new Date());
    }


}
