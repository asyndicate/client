package io.gitlab.asyndicate.asyndicate.activities;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.CustomViewAbove;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import io.gitlab.asyndicate.asyndicate.R;
import io.gitlab.asyndicate.asyndicate.adaptors.MenuAdapter;
import io.gitlab.asyndicate.asyndicate.fragments.EmptyHome;
import io.gitlab.asyndicate.asyndicate.helpers.PayloadRunnable;
import io.gitlab.asyndicate.asyndicate.helpers.Settings;
import io.gitlab.asyndicate.asyndicate.helpers.Utils;
import io.gitlab.asyndicate.asyndicate.models.MenuItem;

public class Home extends AppCompatActivity {
    private static final String TAG = "Home";
    private DrawerArrowDrawable drawerArrowDrawable;
    private SlidingMenu slidingMenu;
    FloatingActionButton fab;

    public Home() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Resources resources = getResources();
        drawerArrowDrawable = new DrawerArrowDrawable(this);
        drawerArrowDrawable.setColor(resources.getColor(android.R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(drawerArrowDrawable);
        drawerArrowDrawable.setSpinEnabled(true);
        drawerArrowDrawable.setProgress(0f);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        slidingMenu = new SlidingMenu(this);
        slidingMenu.setMode(SlidingMenu.LEFT);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
        slidingMenu.setMenu(R.layout.menu);
        slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        slidingMenu.setFadeEnabled(true);
        slidingMenu.setFadeDegree(0.35f);
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
        slidingMenu.setOnPageChangeListener(new CustomViewAbove.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                float offset = (Math.abs(positionOffset));
                offset += (offset / 0.8175f) * (1 - 0.8175f);
                drawerArrowDrawable.setProgress(offset);
            }

            @Override
            public void onPageSelected(int position) {
//                drawerArrowDrawable.setProgress(0f);
            }
        });
        initActivity();
        initMenu(slidingMenu.getMenu());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }


    private void initMenu(final View menu) {

        TextView name = (TextView) menu.findViewById(R.id.name);
        name.setText(Settings.getString("name", "Guest"));
        name = (TextView) menu.findViewById(R.id.secondaryText);
        name.setText("Aries");

        MenuAdapter menuAdapter = new MenuAdapter();
        RecyclerView menuList = (RecyclerView) findViewById(R.id.menuList);
        menuList.setAdapter(menuAdapter);

        menuList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        MenuItem menuItem = new MenuItem();
        menuItem.setType(MenuItem.TYPES.SECTION_MENU);
        menuItem.setIcon(R.drawable.ic_event);
        menuItem.setPrimaryText("Events");

        menuItem.setSecondaryText("");

        menuAdapter.add(menuItem);

        menuItem = new MenuItem();
        menuItem.setType(MenuItem.TYPES.SECTION_MENU);
        menuItem.setIcon(R.drawable.ic_gallery);
        menuItem.setPrimaryText("Gallery");

        menuItem.setSecondaryText("");

        menuAdapter.add(menuItem);

        menuItem = new MenuItem();
        menuItem.setType(MenuItem.TYPES.SECTION_MENU);
        menuItem.setIcon(R.drawable.ic_what);
        menuItem.setPrimaryText("What next?");

        menuItem.setSecondaryText("");

        menuAdapter.add(menuItem);

        menuItem = new MenuItem();
        menuItem.setType(MenuItem.TYPES.DIVIDER_THIN);

        menuAdapter.add(menuItem);

        menuItem = new MenuItem();
        menuItem.setType(MenuItem.TYPES.SECTION_MENU);
        menuItem.setPrimaryText("Settings");
        menuItem.setSecondaryText("");
        menuItem.setIcon(R.drawable.ic_settings);

        menuItem.setAction(new PayloadRunnable() {
            @Override
            public Object run(Object result) {
//                Intent intent = new Intent(Home.this, Profile.class);
//                startActivity(intent);
                return null;
            }
        });

        menuAdapter.add(menuItem);

    }

    private void initActivity() {
        EmptyHome empty = new EmptyHome();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, empty).commit();
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                slidingMenu.toggle(true);
                break;
        }
        return true;
    }
}