package mydist.mydist.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import mydist.mydist.R;
import mydist.mydist.adapters.ChannelSpinnerAdapter;
import mydist.mydist.adapters.SubChannelSpinnerAdapter;
import mydist.mydist.models.Channel;
import mydist.mydist.models.SubChannel;
import mydist.mydist.utils.DataUtils;
import mydist.mydist.utils.FontManager;
import mydist.mydist.utils.UIUtils;

public class NewRetailerActivity extends AppCompatActivity {

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
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
         return true;
        }else if(item.getItemId() == R.id.save){
            saveRetailer();
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveRetailer()
    {
        UIUtils.hideKeyboard(this);
        if(userInputIsValid()){
            Toast.makeText(this, getString(R.string.new_retailer_info), Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
    }

    private boolean userInputIsValid()
    {
        retailerName = mRetailerName.getText().toString();
        if(retailerName.isEmpty()){
            mRetailerName.setError(getString(R.string.input_empty_error));
            return false;
        }

        contactPerson = mContactPerson.getText().toString();
        if(contactPerson.isEmpty()){
            mContactPerson.setError(getString(R.string.input_empty_error));
            return false;
        }

        address = mAddress.getText().toString();
        if(address.isEmpty()){
            mAddress.setError(getString(R.string.input_empty_error));
            return false;
        }

        phoneNumber = mPhoneNumber.getText().toString();
        if(phoneNumber.isEmpty()){
            mPhoneNumber.setError(getString(R.string.input_empty_error));
            return false;
        }

        return true;
    }
}
