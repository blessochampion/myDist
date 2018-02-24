package mydist.mydist.activities;

import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mydist.mydist.R;
import mydist.mydist.fragments.AllCoverageFragment;
import mydist.mydist.fragments.CoverageFragment;
import mydist.mydist.utils.Days;
import mydist.mydist.utils.FontManager;
import mydist.mydist.utils.UIUtils;

public class CoverageActivity extends AuthenticatedActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    SearchView searchView;
    private ViewPagerAdapter adapter;
    private AllCoverageFragment ALL_COVERAGE = new AllCoverageFragment();
    private CoverageFragment SUNDAY_COVERAGE;
    private CoverageFragment MONDAY_COVERAGE;
    private CoverageFragment TUESDAY_COVERAGE;
    private CoverageFragment WEDNESDAY_COVERAGE;
    private CoverageFragment THURSDAY_COVERAGE;
    private CoverageFragment FRIDAY_COVERAGE;
    private CoverageFragment SATURDAY_COVERAGE;
    private boolean isSearchActive = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coverage);
        setupToolBar();
        getReferencesToViews();
        setFonts();
    }

    private void loadDailyRetailers() {
        String week = Days.getThisWeek();
        SUNDAY_COVERAGE = CoverageFragment.getNewInstance(week, Days.SUN.toString());
        MONDAY_COVERAGE = CoverageFragment.getNewInstance(week, Days.MON.toString());
        TUESDAY_COVERAGE = CoverageFragment.getNewInstance(week, Days.TUE.toString());
        WEDNESDAY_COVERAGE = CoverageFragment.getNewInstance(week, Days.WED.toString());
        THURSDAY_COVERAGE = CoverageFragment.getNewInstance(week, Days.THUR.toString());
        FRIDAY_COVERAGE = CoverageFragment.getNewInstance(week, Days.FRI.toString());
        SATURDAY_COVERAGE = CoverageFragment.getNewInstance(week, Days.SAT.toString());
    }

    private void setFonts() {
        Typeface ralewayFont = FontManager.getTypeface(getApplicationContext(), FontManager.RALEWAY_REGULAR);
        FontManager.setFontsForView(findViewById(R.id.coverage_parent_layout), ralewayFont);
    }

    private void setupToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.trade_coverage));
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_coverage, menu);
        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                isSearchActive = hasFocus;
            }
        });
        searchView.setOnQueryTextListener(new OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(CoverageActivity.this, query, Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                queryRetailers(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void queryRetailers(String newText) {
        if (newText.isEmpty()) {
            newText = CoverageFragment.QUERY_ALL;
        }
        Fragment fragment = adapter.getItem(viewPager.getCurrentItem());
        if (fragment instanceof CoverageFragment) {
            ((CoverageFragment) fragment).filter(newText);
        }else {
            ((AllCoverageFragment) fragment).filter(newText);
        }
    }

    public void getReferencesToViews() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        loadDailyRetailers();
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        Calendar calendar = Calendar.getInstance();
        Date today = new Date();
        calendar.setTime(today);
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SUNDAY:
                loadAdapterForSunday(adapter);
            case Calendar.MONDAY:
                loadAdapterForMonday(adapter);
                break;
            case Calendar.TUESDAY:
                loadAdapterForTuesday(adapter);
                break;
            case Calendar.WEDNESDAY:
                loadAdapterForWednesday(adapter);
                break;
            case Calendar.THURSDAY:
                loadAdapterForThursday(adapter);
                break;
            case Calendar.FRIDAY:
                loadAdapterForFriday(adapter);
                break;
            case Calendar.SATURDAY:
                loadAdapterForSaturday(adapter);
                break;
            default:
                break;
        }
        viewPager.setAdapter(adapter);
    }

    private void loadAdapterForSaturday(ViewPagerAdapter adapter) {
        adapter.addFragment(SATURDAY_COVERAGE, Days.SATURDAY.toString());
        adapter.addFragment(ALL_COVERAGE, Days.ALL.toString());
        adapter.addFragment(SUNDAY_COVERAGE, Days.SUNDAY.toString());
        adapter.addFragment(MONDAY_COVERAGE, Days.MONDAY.toString());
        adapter.addFragment(TUESDAY_COVERAGE, Days.TUESDAY.toString());
        adapter.addFragment(WEDNESDAY_COVERAGE, Days.WEDNESDAY.toString());
        adapter.addFragment(THURSDAY_COVERAGE, Days.THURSDAY.toString());
        adapter.addFragment(FRIDAY_COVERAGE, Days.FRIDAY.toString());
    }

    private void loadAdapterForFriday(ViewPagerAdapter adapter) {
        adapter.addFragment(FRIDAY_COVERAGE, Days.FRIDAY.toString());
        adapter.addFragment(ALL_COVERAGE, Days.ALL.toString());
        adapter.addFragment(SATURDAY_COVERAGE, Days.SATURDAY.toString());
        adapter.addFragment(SUNDAY_COVERAGE, Days.SUNDAY.toString());
        adapter.addFragment(MONDAY_COVERAGE, Days.MONDAY.toString());
        adapter.addFragment(TUESDAY_COVERAGE, Days.TUESDAY.toString());
        adapter.addFragment(WEDNESDAY_COVERAGE, Days.WEDNESDAY.toString());
        adapter.addFragment(THURSDAY_COVERAGE, Days.THURSDAY.toString());
    }

    private void loadAdapterForThursday(ViewPagerAdapter adapter) {
        adapter.addFragment(THURSDAY_COVERAGE, Days.THURSDAY.toString());
        adapter.addFragment(ALL_COVERAGE, Days.ALL.toString());
        adapter.addFragment(FRIDAY_COVERAGE, Days.FRIDAY.toString());
        adapter.addFragment(SATURDAY_COVERAGE, Days.SATURDAY.toString());
        adapter.addFragment(SUNDAY_COVERAGE, Days.SUNDAY.toString());
        adapter.addFragment(MONDAY_COVERAGE, Days.MONDAY.toString());
        adapter.addFragment(TUESDAY_COVERAGE, Days.TUESDAY.toString());
        adapter.addFragment(WEDNESDAY_COVERAGE, Days.WEDNESDAY.toString());
    }

    private void loadAdapterForWednesday(ViewPagerAdapter adapter) {
        adapter.addFragment(WEDNESDAY_COVERAGE, Days.WEDNESDAY.toString());
        adapter.addFragment(ALL_COVERAGE, Days.ALL.toString());
        adapter.addFragment(THURSDAY_COVERAGE, Days.THURSDAY.toString());
        adapter.addFragment(FRIDAY_COVERAGE, Days.FRIDAY.toString());
        adapter.addFragment(SATURDAY_COVERAGE, Days.SATURDAY.toString());
        adapter.addFragment(SUNDAY_COVERAGE, Days.SUNDAY.toString());
        adapter.addFragment(MONDAY_COVERAGE, Days.MONDAY.toString());
        adapter.addFragment(TUESDAY_COVERAGE, Days.TUESDAY.toString());
    }

    private void loadAdapterForTuesday(ViewPagerAdapter adapter) {
        adapter.addFragment(TUESDAY_COVERAGE, Days.TUESDAY.toString());
        adapter.addFragment(ALL_COVERAGE, Days.ALL.toString());
        adapter.addFragment(WEDNESDAY_COVERAGE, Days.WEDNESDAY.toString());
        adapter.addFragment(THURSDAY_COVERAGE, Days.THURSDAY.toString());
        adapter.addFragment(FRIDAY_COVERAGE, Days.FRIDAY.toString());
        adapter.addFragment(SATURDAY_COVERAGE, Days.SATURDAY.toString());
        adapter.addFragment(SUNDAY_COVERAGE, Days.SUNDAY.toString());
        adapter.addFragment(MONDAY_COVERAGE, Days.MONDAY.toString());
    }

    private void loadAdapterForSunday(ViewPagerAdapter adapter) {
        adapter.addFragment(SUNDAY_COVERAGE, Days.SUNDAY.toString());
        adapter.addFragment(ALL_COVERAGE, Days.ALL.toString());
        adapter.addFragment(MONDAY_COVERAGE, Days.MONDAY.toString());
        adapter.addFragment(TUESDAY_COVERAGE, Days.TUESDAY.toString());
        adapter.addFragment(WEDNESDAY_COVERAGE, Days.WEDNESDAY.toString());
        adapter.addFragment(THURSDAY_COVERAGE, Days.THURSDAY.toString());
        adapter.addFragment(FRIDAY_COVERAGE, Days.FRIDAY.toString());
        adapter.addFragment(SATURDAY_COVERAGE, Days.SATURDAY.toString());
    }

    private void loadAdapterForMonday(ViewPagerAdapter adapter) {
        adapter.addFragment(MONDAY_COVERAGE, Days.MONDAY.toString());
        adapter.addFragment(ALL_COVERAGE, Days.ALL.toString());
        adapter.addFragment(TUESDAY_COVERAGE, Days.TUESDAY.toString());
        adapter.addFragment(WEDNESDAY_COVERAGE, Days.WEDNESDAY.toString());
        adapter.addFragment(THURSDAY_COVERAGE, Days.THURSDAY.toString());
        adapter.addFragment(FRIDAY_COVERAGE, Days.FRIDAY.toString());
        adapter.addFragment(SATURDAY_COVERAGE, Days.SATURDAY.toString());
        adapter.addFragment(SATURDAY_COVERAGE, Days.SUNDAY.toString());
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onBackPressed() {
        if (isSearchActive) {
            queryRetailers(CoverageFragment.QUERY_ALL);
            UIUtils.hideKeyboard(this);
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }
}
