package com.example.personator.activity;

import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.personator.R;
import com.example.personator.common.Common;
import com.example.personator.fragment.LatestNews;
import com.example.personator.fragment.NoInternet;
import com.example.personator.fragment.OfflineArticles;
import com.example.personator.service.NotificationService;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener,
        LatestNews.ForPopupListener, PopupMenu.OnMenuItemClickListener {

    private boolean isCheckedOn = false;
    private boolean isCheckedOff = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        stopService(new Intent(this, NotificationService.class));
        InitView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(NotificationService.NOTIFICATION_ID);
    }

    private void InitView() {
        //toolbarInit
        Toolbar mToolbar = findViewById(R.id.id_toolbar);
        setSupportActionBar(mToolbar);
        //bottom_navigation_init
        BottomNavigationView mNavMenu = findViewById(R.id.id_nav_menu);
        mNavMenu.setOnNavigationItemSelectedListener(this);
        mNavMenu.setSelectedItemId(R.id.id_latest_news);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Log.d("SOLOTASK", "onNavigationItemSelected MainActivity");
        switch (menuItem.getItemId()) {
            case R.id.id_latest_news:
                if (isCheckedOn) return true;
                if (Common.checkNetworkConnectionStatus(this)) {
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                            .replace(R.id.id_container, LatestNews.newInstance(0, "", "")).commit();
                } else {
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                            .replace(R.id.id_container, new NoInternet()).commit();
                }
                isCheckedOn = true;
                isCheckedOff = false;
                return true;
            case R.id.id_offline_articles:
                if (isCheckedOff) return true;
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                        .replace(R.id.id_container, new OfflineArticles())
                        .commit();
                isCheckedOff = true;
                isCheckedOn = false;
                return true;
        }
        return false;
    }


    @Override
    public void openPopup(int code) {
        Log.d("SOLOTASK", "openPopup MainActivity");
        View view;
        PopupMenu popupMenu;
        switch (code) {
            case 1:
                view = findViewById(R.id.id_view_view);
                popupMenu = new PopupMenu(this, view);
                popupMenu.inflate(R.menu.popup_view);
                popupMenu.setOnMenuItemClickListener(this);
                popupMenu.show();
                break;
            case 2:
                view = findViewById(R.id.id_order_by_view);
                popupMenu = new PopupMenu(this, view);
                popupMenu.inflate(R.menu.popup_order);
                popupMenu.setOnMenuItemClickListener(this);
                popupMenu.show();
                break;
            case 3:
                view = findViewById(R.id.id_page_size_view);
                popupMenu = new PopupMenu(this, view);
                popupMenu.inflate(R.menu.popup_page_size);
                popupMenu.setOnMenuItemClickListener(this);
                popupMenu.show();
                break;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        Log.d("SOLOTASK", "onMenuItemClicked MainActivity");
        int pageSize;
        String order;
        String view;
        switch (menuItem.getItemId()) {
            case R.id.id_popup_10:
                pageSize = 10;
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                        .replace(R.id.id_container, LatestNews.newInstance(pageSize, "", "")).commit();
                return true;
            case R.id.id_popup_20:
                pageSize = 20;
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                        .replace(R.id.id_container, LatestNews.newInstance(pageSize, "", "")).commit();
                return true;
            case R.id.id_popup_30:
                pageSize = 30;
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                        .replace(R.id.id_container, LatestNews.newInstance(pageSize, "", "")).commit();
                return true;
            case R.id.id_popup_grid:
                view = "grid";
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                        .replace(R.id.id_container, LatestNews.newInstance(0, "", view)).commit();
                return true;
            case R.id.id_popup_list:
                view = "list";
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                        .replace(R.id.id_container, LatestNews.newInstance(0, "", view)).commit();
                return true;
            case R.id.id_popup_newest:
                order = "newest";
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                        .replace(R.id.id_container, LatestNews.newInstance(0, order, "")).commit();
                return true;
            case R.id.id_popup_oldest:
                order = "oldest";
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                        .replace(R.id.id_container, LatestNews.newInstance(0, order, "")).commit();
                return true;
            case R.id.id_popup_relevance:
                order = "relevance";
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                        .replace(R.id.id_container, LatestNews.newInstance(0, order, "")).commit();
                return true;
            default:
                return true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(MainActivity.this, NotificationService.class);
        startService(intent);
    }

}
