package com.example.personator.view.activity.pinnedItemActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.personator.R;
import com.example.personator.view.adapters.PinnedNewsAdapter;
import com.example.personator.shared.data.local.PinnedItem;
import com.squareup.picasso.Picasso;

import io.realm.Realm;
import io.realm.RealmResults;

public class PinnedItemFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "code";
    private int pos;
    private ImageView image;
    private ImageView btnPin;
    private TextView articleBody;
    private TextView articleDate;
    private SharedPreferences mSharedPref;
    private SharedPreferences.Editor editor;
    private Realm mRealm;

    public static PinnedItemFragment newInstance(int position) {
        PinnedItemFragment fragment = new PinnedItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pos = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pinned_item, container, false);
        InitView(view);
        return view;
    }

    private void InitView(View view) {
        articleBody = view.findViewById(R.id.id_article_body);
        articleDate = view.findViewById(R.id.id_article_date);
        image = view.findViewById(R.id.id_image_for_transition);
        btnPin = view.findViewById(R.id.id_btn_pin);
        btnPin.setOnClickListener(this);
        mSharedPref = getActivity().getSharedPreferences(PinnedNewsAdapter.results.get(pos).getHeadline(), Context.MODE_PRIVATE);
        editor = mSharedPref.edit();

        articleBody.setText(PinnedNewsAdapter.results.get(pos).getPublicationBody());
        articleDate.setText(PinnedNewsAdapter.results.get(pos).getPublicationDate().substring(0, 10));
        if (PinnedNewsAdapter.results.get(pos).getImageSrc().equals("R.drawable.nophoto")) {
            Picasso.get().load(R.drawable.nophoto).into(image);
        } else
            Picasso.get().load(PinnedNewsAdapter.results.get(pos).getImageSrc()).into(image);

        if (mSharedPref.getString("pinned", "").equals("yes")) {
            btnPin.setImageResource(R.drawable.ic_unpin);
            editor.putString("pinned", "yes");
            editor.apply();
        }
        mRealm = Realm.getDefaultInstance();
        mRealm.beginTransaction();
        mRealm.commitTransaction();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.id_btn_pin) {
            if (mSharedPref.getString("pinned", "").equals("") || mSharedPref.getString("pinned", "").equals("no")) {
                Toast.makeText(getActivity(), "You can't pin again!", Toast.LENGTH_SHORT).show();
            } else {
                btnPin.setImageResource(R.drawable.ic_pin);
                editor.putString("pinned", "no");
                editor.commit();
                UnPinData(mRealm, PinnedNewsAdapter.results.get(pos).getImageSrc());
            }
        }
    }

    private void UnPinData(Realm mRealm, String imgSrc) {
        mRealm.executeTransaction(realm -> {
            RealmResults<PinnedItem> obj = mRealm.where(PinnedItem.class).equalTo("imageSrc", imgSrc).findAll();
            obj.deleteAllFromRealm();
        });
        Toast.makeText(getActivity(), "Article is unpinned!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mRealm.close();
    }
}
