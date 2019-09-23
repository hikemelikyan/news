package com.example.personator.view.activity.latestNewsActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.personator.R;
import com.example.personator.model.Result;
import com.example.personator.view.activity.newsDetailsActivity.NewsDetails;
import com.example.personator.view.adapters.NewsAdapter;
import com.example.personator.view.adapters.PinnedNewsAdapter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.realm.Realm;

public class LatestNews extends Fragment implements View.OnClickListener/*, PinnedNewsAdapter.PinnedClickListener */{

    public static final String LIST = "list";
    public static final String GRID = "grid";
    private static final String ARG_PARAM1 = "size";
    private static final String ARG_PARAM2 = "order";
    private static final String ARG_PARAM3 = "view";
    private int page = 1;
    private int pageSize;
    private int newArticlesCount = 0;
    private String order;
    private String view;
    private List<Result> mList;
    private TextView arrowOrder;
    private TextView arrowPage;
    private TextView arrowView;
    private RecyclerView mRecycler;
    private RecyclerView mRecyclerPinned;
    private NewsAdapter newsAdapter;
    private PinnedNewsAdapter pinnedNewsAdapter;
    private SharedPreferences mSharedPref;
    private SharedPreferences.Editor mEditor;
    private CompositeDisposable compositeDisposable;
    private Realm mRealm;
    private ForPopupListener listener;

    public interface ForPopupListener {
        void openPopup(int code);
    }

    public static LatestNews newInstance(int pageSize, String order, String view) {
        LatestNews fragment = new LatestNews();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, pageSize);
        args.putString(ARG_PARAM2, order);
        args.putString(ARG_PARAM3, view);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pageSize = getArguments().getInt(ARG_PARAM1);
            order = getArguments().getString(ARG_PARAM2);
            view = getArguments().getString(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.latest_news, container, false);
        InitView(mView);
//        sendRequest(1);
        mRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isLastItemDisplaying(mRecycler)) {
//                    sendRequest();
                }
            }
        });

        return mView;
    }

    private void InitView(View mView) {
        arrowOrder = mView.findViewById(R.id.id_order_by);
        arrowPage = mView.findViewById(R.id.id_page_size);
        arrowView = mView.findViewById(R.id.id_view);
        arrowPage.setOnClickListener(this);
        arrowOrder.setOnClickListener(this);
        arrowView.setOnClickListener(this);
        mRecycler = mView.findViewById(R.id.id_online_recycler_view);
        mRecyclerPinned = mView.findViewById(R.id.id_recycler_for_pinned);
//        mSharedPref = getActivity().getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        mEditor = mSharedPref.edit();

        if (order.equals("")) {
            order = mSharedPref.getString("ordering", "newest");
        } else {
            mEditor.putString("ordering", order);
            mEditor.apply();
        }

        if (pageSize == 0) {
            pageSize = mSharedPref.getInt("page_size", 10);
        } else {
            mEditor.putInt("page_size", pageSize);
            mEditor.apply();
        }

        if (view.equals("")) {
            view = mSharedPref.getString("view", "list");
        } else {
            mEditor.putString("view", view);
            mEditor.apply();
        }

        if (mSharedPref.getString("view", "").equals(GRID)) {
            mRecycler.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        } else if (mSharedPref.getString("view", "").equals(LIST))
            mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerPinned.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        newsAdapter = new NewsAdapter(getActivity(), LatestNews.this::clicked,new ArrayList<>());
//        pinnedNewsAdapter = new PinnedNewsAdapter(getActivity(), this);
        mRecycler.setHasFixedSize(true);
        mRecycler.setAdapter(newsAdapter);
        mRecyclerPinned.setHasFixedSize(true);
        mRecyclerPinned.setAdapter(pinnedNewsAdapter);
        mList = new ArrayList<>();

        mRealm = Realm.getDefaultInstance();
        mRealm.beginTransaction();
        mRealm.commitTransaction();
    }


    public void clicked(int code) {
        Intent intent = new Intent(getActivity(), NewsDetails.class);
        intent.putExtra("code", code);
        String transitionName = "myTrans";
        startActivity(intent);
    }
//
//    private void sendRequest() {
//        NewsAPI newsAPI = GetConnect.getInstance().create(NewsAPI.class);
//        compositeDisposable = new CompositeDisposable();
//        compositeDisposable.add(newsAPI.getContent(order,++page)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(news -> {
//                    if (mSharedPref.getBoolean("first_time", true)) {
//                        mEditor.putInt("count_of_articles", (int) news.getResponse().getTotal());
//                        mEditor.putBoolean("first_time", false);
//                        mEditor.apply();
//                    } else {
//                        newArticlesCount = (int) (news.getResponse().getTotal() - mSharedPref.getInt("count_of_articles", 0));
//                        mEditor.putInt("count_of_articles", (int) news.getResponse().getTotal());
//                        mEditor.apply();
//                    }
//                    mList.addAll(news.getResponse().getResults());
//                    newsAdapter.setData(mList);
//                }, throwable -> {
//                    Toast.makeText(getActivity(), "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
//                }));
//    }
//
//    private void sendRequest(int pagefor) {
//        NewsAPI newsAPI = GetConnect.getInstance().create(NewsAPI.class);
//        compositeDisposable = new CompositeDisposable();
//        compositeDisposable.add(newsAPI.getContent(JSON, order, FIELDS_TO_SHOW, pageSize, pagefor, API_KEY)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(news -> {
//                    if (mSharedPref.getBoolean("first_time", true)) {
//                        mEditor.putInt("count_of_articles", (int) news.getResponse().getTotal());
//                        mEditor.putBoolean("first_time", false);
//                        mEditor.apply();
//                    } else {
//                        newArticlesCount = (int) (news.getResponse().getTotal() - mSharedPref.getInt("count_of_articles", 0));
//                        mEditor.putInt("count_of_articles", (int) news.getResponse().getTotal());
//                        mEditor.apply();
//                    }
//                    mList.addAll(news.getResponse().getResults());
//                    newsAdapter.setData(mList);
//                }, throwable -> {
//                    Toast.makeText(getActivity(), "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
//                }));
//    }
//
//    private void checkForUpdates() {
//        for (; ; ) {
//            try {
//                TimeUnit.SECONDS.sleep(30);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            NewsAPI newsAPI = GetConnect.getInstance().create(NewsAPI.class);
//            compositeDisposable = new CompositeDisposable();
//            compositeDisposable.add(newsAPI.getContent(JSON, order, FIELDS_TO_SHOW, pageSize, ++page, API_KEY)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(news -> {
//                        newArticlesCount = (int) (news.getResponse().getTotal() - mSharedPref.getInt("count_of_articles", 0));
//                        mEditor.putInt("count_of_articles", (int) news.getResponse().getTotal());
//                        mEditor.apply();
//                        if (newArticlesCount > 0) {
//                            mList.addAll(news.getResponse().getResults());
//                            newsAdapter.setData(mList);
//                        }
//                    }, throwable -> {
//                        Toast.makeText(getActivity(), "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
//                    }));
//        }
//    }

    private boolean isLastItemDisplaying(RecyclerView mRecycler) {
        if (mRecycler.getAdapter().getItemCount() != 0) {
            if (view.equals("grid")) {
                int[] lastVisibleItemPos = ((StaggeredGridLayoutManager) mRecycler.getLayoutManager())
                        .findLastCompletelyVisibleItemPositions(new int[2]);
                return lastVisibleItemPos[1] == mRecycler.getAdapter().getItemCount() - 1 || lastVisibleItemPos[0] == mRecycler.getAdapter().getItemCount() - 1;
            } else if (view.equals("list")) {
                int lastVisibleItemPos = ((LinearLayoutManager) mRecycler.getLayoutManager())
                        .findLastCompletelyVisibleItemPosition();
                return lastVisibleItemPos != RecyclerView.NO_POSITION && lastVisibleItemPos == mRecycler.getAdapter().getItemCount() - 1;
            }
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_view:
                listener.openPopup(1);
                break;
            case R.id.id_order_by:
                listener.openPopup(2);
                break;
            case R.id.id_page_size:
                listener.openPopup(3);
                break;
        }
    }

//    @Override
//    public void pinnedItemClicked(int code) {
//        getActivity().getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.id_container, PinnedItemFragment.newInstance(code))
//                .addToBackStack(null)
//                .commit();
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (ForPopupListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }
}