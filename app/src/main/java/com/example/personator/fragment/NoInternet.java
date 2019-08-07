package com.example.personator.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.personator.R;

public class NoInternet extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_no_internet, container, false);
        ImageView imageView  = view.findViewById(R.id.id_image_for_anim);
        imageView.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.no_internet));
        return view;
    }
}
