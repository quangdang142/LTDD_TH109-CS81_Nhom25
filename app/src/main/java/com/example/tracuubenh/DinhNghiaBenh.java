package com.example.tracuubenh;

import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.tracuubenh.fragments.FragmentCachTri;
import com.example.tracuubenh.fragments.FragmentPhongNgua;
import com.example.tracuubenh.fragments.FragmentTrieuChung;
import com.example.tracuubenh.fragments.FragmentViDu;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class DinhNghiaBenh extends AppCompatActivity {
    private ViewPager viewPager;
    String tenBenh;
    DatabaseHelper myDbHelper;
    Cursor c = null;
    public String trieuChung ="test";
    public String viDu;
    public String cachTri;
    public String phongNgua;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dinh_nghia_benh);

        //nhan gia tri
        Bundle bundle = getIntent().getExtras();
        tenBenh = bundle.getString("tenbenh");

        myDbHelper = new DatabaseHelper(this);
        try{
            myDbHelper.openDatabase();
        }catch (SQLException sqle)
        {
            throw sqle;
        }

        c = myDbHelper.getMeaning(tenBenh);
        trieuChung = String.valueOf(c.getCount());
        if(c.moveToFirst())
        {
            trieuChung = c.getString(c.getColumnIndex("trieuchung"));
            viDu = c.getString(c.getColumnIndex("vidu"));
            cachTri = c.getString(c.getColumnIndex("cachtri"));
            phongNgua = c.getString(c.getColumnIndex("phongngua"));
        }

        myDbHelper.insertHistory(tenBenh);

        Toolbar toolbar = (Toolbar) findViewById(R.id.mToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(tenBenh);

        toolbar.setNavigationIcon(getResources().getDrawable(17301593)); //xài tạm icon kính lúp vì không lấy được icon back T.T
        viewPager = (ViewPager) findViewById(R.id.tab_viewpager);
        if(viewPager!=null)
        {
            setupViewPager(viewPager);
        }
        //tab layout
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override //change viewpager to selected tab
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }
    private class ViewPagerAdapter extends FragmentPagerAdapter
    {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList =  new ArrayList<>();
        ViewPagerAdapter(FragmentManager manager)
        {
            super(manager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT); //phải múa lửa vì fragpageadap deprecated ?
        }
        void addFrag(Fragment fragment, String title)
        {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public  Fragment getItem(int position)
        {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount()
        {
            return mFragmentList.size();
        }
        @Override
        public CharSequence getPageTitle(int position)
        {
            return mFragmentTitleList.get(position);
        }
    }

    private void setupViewPager(ViewPager viewPager) // cài view pager, title đặt tạm
    {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new FragmentTrieuChung(), "Triệu chứng");
        adapter.addFrag(new FragmentPhongNgua(), "Phòng ngừa");
        adapter.addFrag(new FragmentCachTri(), "Cách trị");
        adapter.addFrag(new FragmentViDu(),"Giới thiệu");
        viewPager.setAdapter(adapter);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == android.R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}