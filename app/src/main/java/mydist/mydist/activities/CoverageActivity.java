package mydist.mydist.activities;

import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mydist.mydist.R;
import mydist.mydist.fragments.AllCoverageFragment;
import mydist.mydist.fragments.CoverageFragment;
import mydist.mydist.utils.Days;
import mydist.mydist.utils.FontManager;

public class CoverageActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private AllCoverageFragment ALL_COVERAGE = new AllCoverageFragment();
    private CoverageFragment SUNDAY_COVERAGE = new CoverageFragment();
    private CoverageFragment MONDAY_COVERAGE = new CoverageFragment();
    private CoverageFragment TUESDAY_COVERAGE = new CoverageFragment();
    private CoverageFragment WEDNESDAY_COVERAGE = new CoverageFragment();
    private CoverageFragment THURSDAY_COVERAGE = new CoverageFragment();
    private CoverageFragment FRIDAY_COVERAGE = new CoverageFragment();
    private CoverageFragment SATURDAY_COVERAGE = new CoverageFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coverage);
        setupToolBar();
        getReferencesToViews();
        setFonts();
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
/*
        // Associate searchable configuration with the SearchView
        // SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        // searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));*/
        return super.onCreateOptionsMenu(menu);
    }

    public void getReferencesToViews() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

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
}
