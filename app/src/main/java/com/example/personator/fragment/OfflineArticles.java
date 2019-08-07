package com.example.personator.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.personator.R;
import com.example.personator.adapters.SavedArticlesAdapter;

public class OfflineArticles extends Fragment implements SavedArticlesAdapter.OnSavedItemCLickedListener {
    private RecyclerView mRecycler;
    private SavedArticlesAdapter savedArticlesAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.offline_articles, container, false);
        mRecycler = mView.findViewById(R.id.id_offline_recycler_view);
        savedArticlesAdapter = new SavedArticlesAdapter(getActivity(), this);
        mRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mRecycler.setAdapter(savedArticlesAdapter);
        return mView;
    }

    @Override
    public void savedItemClicked(int code) {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.id_container, SavedItemFragment.newInstance(code))
                .addToBackStack(null)
                .commit();
    }
}
