package com.example.personator.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.personator.R;
import com.example.personator.adapters.SavedArticlesAdapter;
import com.example.personator.database.SavedItem;
import com.squareup.picasso.Picasso;

import io.realm.Realm;
import io.realm.RealmResults;

public class SavedItemFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "code";
    private int pos;
    private ImageView image;
    private ImageView btnSave;
    private TextView articleBody;
    private TextView articleDate;
    private SharedPreferences mSharedPref;
    private SharedPreferences.Editor editor;
    private Realm mRealm;

    public static SavedItemFragment newInstance(int position) {
        SavedItemFragment fragment = new SavedItemFragment();
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
        View view = inflater.inflate(R.layout.fragment_saved_item, container, false);
        InitView(view);
        return view;
    }

    private void InitView(View view) {
        articleBody = view.findViewById(R.id.id_article_body);
        articleDate = view.findViewById(R.id.id_article_date);
        image = view.findViewById(R.id.id_image_for_transition);
        btnSave = view.findViewById(R.id.id_btn_save);
        btnSave.setOnClickListener(this);
        mSharedPref = getActivity().getSharedPreferences(SavedArticlesAdapter.results.get(pos).getHeadline(), Context.MODE_PRIVATE);
        editor = mSharedPref.edit();

        articleBody.setText(SavedArticlesAdapter.results.get(pos).getPublicationBody());
        articleDate.setText(SavedArticlesAdapter.results.get(pos).getPublicationDate().substring(0, 10));
        if (SavedArticlesAdapter.results.get(pos).getImageSrc().equals("R.drawable.nophoto")) {
            Picasso.get().load(R.drawable.nophoto).into(image);
        } else
            Picasso.get().load(SavedArticlesAdapter.results.get(pos).getImageSrc()).into(image);


        if (mSharedPref.getString("saved", "").equals("yes")) {
            btnSave.setImageResource(R.drawable.ic_bookmark_black_shape);
            editor.putString("saved", "yes");
            editor.commit();
        }
        mRealm = Realm.getDefaultInstance();
        mRealm.beginTransaction();
        mRealm.commitTransaction();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.id_btn_save) {
            if (mSharedPref.getString("saved", "").equals("") || mSharedPref.getString("saved", "").equals("no")) {
                Toast.makeText(getActivity(), "You can't save again!", Toast.LENGTH_SHORT).show();
            } else {
                btnSave.setImageResource(R.drawable.ic_bookmark_white);
                editor.putString("saved", "no");
                editor.commit();
                DeleteData(mRealm, SavedArticlesAdapter.results.get(pos).getImageSrc());
            }
        }
    }

    private void DeleteData(Realm mRealm, String imgSrc) {
        mRealm.executeTransaction(realm -> {
            RealmResults<SavedItem> obj = mRealm.where(SavedItem.class).equalTo("imageSrc", imgSrc).findAll();
            obj.deleteAllFromRealm();
        });
        Toast.makeText(getActivity(), "Article is unsaved!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mRealm.close();
    }
}
