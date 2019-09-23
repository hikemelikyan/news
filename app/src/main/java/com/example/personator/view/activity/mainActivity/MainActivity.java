package com.example.personator.view.activity.mainActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.personator.R;
import com.example.personator.view.activity.base.BaseActivity;
import com.example.personator.view.activity.newsDetailsActivity.NewsDetails;
import com.example.personator.view.adapters.NewsAdapter;
import com.example.personator.viewmodel.HomeViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends BaseActivity /*implements *//*BottomNavigationView.OnNavigationItemSelectedListener,*//*
        LatestNews.ForPopupListener, PopupMenu.OnMenuItemClickListener*/ {

    private boolean isCheckedOn = false;
    private boolean isCheckedOff = false;
    RecyclerView rec;
    private HomeViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewModel = createViewModel(HomeViewModel.class, this);
        InitView();
        observToLiveData();
        mViewModel.getLatestNews();
    }

    private void InitView() {
        //toolbarInit
        Toolbar mToolbar = findViewById(R.id.id_toolbar);
        setSupportActionBar(mToolbar);
        //bottom_navigation_init
        BottomNavigationView mNavMenu = findViewById(R.id.id_nav_menu);
//        mNavMenu.setOnNavigationItemSelectedListener(this);
        mNavMenu.setSelectedItemId(R.id.id_latest_news);
        rec = findViewById(R.id.recycler_view);
    }

    private void cl(int a) {
        String transition = "myTrans";
        ImageView image = findViewById(R.id.id_image_illustration);
        Intent intent = new Intent(this, NewsDetails.class);
        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(this, image, transition);
        ActivityCompat.startActivity(this, intent, options.toBundle());
    }
//
//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//        Log.d("SOLOTASK", "onNavigationItemSelected MainActivity");
//        switch (menuItem.getItemId()) {
//            case R.id.id_latest_news:
//                if (isCheckedOn) return true;
//                if (Common.checkNetworkConnectionStatus(this)) {
//                    getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
//                            .replace(R.id.id_container, LatestNews.newInstance(0, "", "")).commit();
//                } else {
//                    getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
//                            .replace(R.id.id_container, new NoInternet()).commit();
//                }
//                isCheckedOn = true;
//                isCheckedOff = false;
//                return true;
//            case R.id.id_offline_articles:
//                if (isCheckedOff) return true;
//                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
//                        .replace(R.id.id_container, new OfflineArticles())
//                        .commit();
//                isCheckedOff = true;
//                isCheckedOn = false;
//                return true;
//        }
//        return false;
//    }


    private void observToLiveData() {
        mViewModel.getNewsLiveData().observe(this, results -> {
            NewsAdapter newsAdapter = new NewsAdapter(MainActivity.this, MainActivity.this::cl, results);
            rec.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
            rec.setAdapter(newsAdapter);
            ProgressBar pr = MainActivity.this.findViewById(R.id.progress);
            pr.setVisibility(View.GONE);
        });
    }

//    @Override
//    public void openPopup(int code) {
//        Log.d("SOLOTASK", "openPopup MainActivity");
//        View view;
//        PopupMenu popupMenu;
//        switch (code) {
//            case 1:
//                view = findViewById(R.id.id_view_view);
//                popupMenu = new PopupMenu(this, view);
//                popupMenu.inflate(R.menu.popup_view);
//                popupMenu.setOnMenuItemClickListener(this);
//                popupMenu.show();
//                break;
//            case 2:
//                view = findViewById(R.id.id_order_by_view);
//                popupMenu = new PopupMenu(this, view);
//                popupMenu.inflate(R.menu.popup_order);
//                popupMenu.setOnMenuItemClickListener(this);
//                popupMenu.show();
//                break;
//            case 3:
//                view = findViewById(R.id.id_page_size_view);
//                popupMenu = new PopupMenu(this, view);
//                popupMenu.inflate(R.menu.popup_page_size);
//                popupMenu.setOnMenuItemClickListener(this);
//                popupMenu.show();
//                break;
//        }
//    }
//
//    @Override
//    public boolean onMenuItemClick(MenuItem menuItem) {
//        Log.d("SOLOTASK", "onMenuItemClicked MainActivity");
//        int pageSize;
//        String order;
//        String view;
//        switch (menuItem.getItemId()) {
//            case R.id.id_popup_10:
//                pageSize = 10;
//                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
//                        .replace(R.id.id_container, LatestNews.newInstance(pageSize, "", "")).commit();
//                return true;
//            case R.id.id_popup_20:
//                pageSize = 20;
//                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
//                        .replace(R.id.id_container, LatestNews.newInstance(pageSize, "", "")).commit();
//                return true;
//            case R.id.id_popup_30:
//                pageSize = 30;
//                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
//                        .replace(R.id.id_container, LatestNews.newInstance(pageSize, "", "")).commit();
//                return true;
//            case R.id.id_popup_grid:
//                view = "grid";
//                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
//                        .replace(R.id.id_container, LatestNews.newInstance(0, "", view)).commit();
//                return true;
//            case R.id.id_popup_list:
//                view = "list";
//                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
//                        .replace(R.id.id_container, LatestNews.newInstance(0, "", view)).commit();
//                return true;
//            case R.id.id_popup_newest:
//                order = "newest";
//                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
//                        .replace(R.id.id_container, LatestNews.newInstance(0, order, "")).commit();
//                return true;
//            case R.id.id_popup_oldest:
//                order = "oldest";
//                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
//                        .replace(R.id.id_container, LatestNews.newInstance(0, order, "")).commit();
//                return true;
//            case R.id.id_popup_relevance:
//                order = "relevance";
//                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
//                        .replace(R.id.id_container, LatestNews.newInstance(0, order, "")).commit();
//                return true;
//            default:
//                return true;
//        }
//    }
}
