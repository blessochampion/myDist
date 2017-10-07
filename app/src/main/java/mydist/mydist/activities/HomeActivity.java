package mydist.mydist.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;

import mydist.mydist.R;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout mNewRetailer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getReferencesToViews();
        setOnClickListeners();
    }

    private void setOnClickListeners()
    {
        mNewRetailer.setOnClickListener(this);
    }

    public void getReferencesToViews() {
        setupToolBar();
        mNewRetailer = (LinearLayout) findViewById(R.id.ll_new_retailer);

    }

    private void setupToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String title = getString(R.string.home);
        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.ll_new_retailer){
            Intent intent = new Intent(HomeActivity.this, NewRetailerActivity.class);
            startActivity(intent);
            return;
        }
    }
}
