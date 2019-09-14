package com.example.personator.view.activity.newsDetailsActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.personator.R;
import com.example.personator.view.adapters.NewsAdapter;
import com.example.personator.shared.data.local.PinnedItem;
import com.example.personator.shared.data.local.SavedItem;
import com.squareup.picasso.Picasso;

import io.realm.Realm;
import io.realm.RealmResults;

public class NewsDetails extends AppCompatActivity implements View.OnClickListener {

    private int pos;
    private ImageView image;
    private ImageView btnSave;
    private ImageView btnPin;
    private TextView articleBody;
    private TextView articleDate;
    private SharedPreferences mSharedPref;
    private SharedPreferences.Editor editor;
    private Realm mRealm;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.news_detalis);
        InitView();
    }

    private void InitView() {
        pos = getIntent().getIntExtra("code", -11);
        articleBody = findViewById(R.id.id_article_body);
        articleDate = findViewById(R.id.id_article_date);
        image = findViewById(R.id.id_image_for_transition);
        btnSave = findViewById(R.id.id_btn_save);
        btnPin = findViewById(R.id.id_btn_pin);
        btnSave.setOnClickListener(this);
        btnPin.setOnClickListener(this);
        mSharedPref = getSharedPreferences(NewsAdapter.mList.get(pos).getFields().getHeadline(), Context.MODE_PRIVATE);
        editor = mSharedPref.edit();

        if (NewsAdapter.mList.get(pos).getFields().getBodyText() == null) {
            articleBody.setText("This article has no body.");
        } else {
            articleBody.setText(NewsAdapter.mList.get(pos).getFields().getBodyText());
        }
        if (NewsAdapter.mList.get(pos).getFields().getThumbnail() != null) {
            Picasso.get().load(NewsAdapter.mList.get(pos).getFields().getThumbnail()).into(image);
        } else {
            Picasso.get().load(R.drawable.nophoto).into(image);
        }

        if (NewsAdapter.mList.get(pos).getFields().getFirstPublicationDate() == null) {
            articleDate.setText(NewsAdapter.mList.get(pos).getWebPublicationDate().substring(0, 10));
        } else {
            articleDate.setText(NewsAdapter.mList.get(pos).getFields().getFirstPublicationDate().substring(0, 10));
        }
        if (mSharedPref.getString("pinned", "").equals("yes")) {
            btnPin.setImageResource(R.drawable.ic_unpin);
            editor.putString("pinned", "yes");
            editor.apply();
        }
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
        switch (v.getId()) {
            case R.id.id_btn_pin:
                if (mSharedPref.getString("pinned", "").equals("") || mSharedPref.getString("pinned", "").equals("no")) {
                    btnPin.setImageResource(R.drawable.ic_unpin);
                    editor.putString("pinned", "yes");
                    editor.commit();
                    PinData();
                } else {
                    btnPin.setImageResource(R.drawable.ic_pin);
                    editor.putString("pinned", "no");
                    editor.commit();
                    UnPinData(mRealm, NewsAdapter.mList.get(pos).getFields().getThumbnail());
                }
                break;
            case R.id.id_btn_save:
                if (mSharedPref.getString("saved", "").equals("") || mSharedPref.getString("saved", "").equals("no")) {
                    btnSave.setImageResource(R.drawable.ic_bookmark_black_shape);
                    editor.putString("saved", "yes");
                    editor.commit();
                    SaveData();
                } else {
                    btnSave.setImageResource(R.drawable.ic_bookmark_white);
                    editor.putString("saved", "no");
                    editor.commit();
                    DeleteData(mRealm, NewsAdapter.mList.get(pos).getFields().getThumbnail());
                }
                break;
        }
    }

    private void SaveData() {
        mRealm.executeTransaction(realm -> {
            SavedItem toSave = mRealm.createObject(SavedItem.class);
            if (NewsAdapter.mList.get(pos).getFields().getBodyText() == null) {
                toSave.setPublicationBody("This article has no body.");
            } else {
                toSave.setPublicationBody(NewsAdapter.mList.get(pos).getFields().getBodyText());
            }
            if (NewsAdapter.mList.get(pos).getFields().getHeadline() == null) {
                toSave.setHeadline("This article has no headline.");
            } else {
                toSave.setHeadline(NewsAdapter.mList.get(pos).getFields().getHeadline());
            }
            if (NewsAdapter.mList.get(pos).getFields().getThumbnail() != null) {
                toSave.setImageSrc(NewsAdapter.mList.get(pos).getFields().getThumbnail());
            } else {
                toSave.setImageSrc("R.drawable.nophoto");
            }
            if (NewsAdapter.mList.get(pos).getFields().getFirstPublicationDate() == null) {
                toSave.setPublicationDate(NewsAdapter.mList.get(pos).getWebPublicationDate().substring(0, 10));
            } else {
                toSave.setPublicationDate(NewsAdapter.mList.get(pos).getFields().getFirstPublicationDate().substring(0, 10));
            }
            if (NewsAdapter.mList.get(pos).getSectionName() == null) {
                toSave.setSectionName("Unknown");
            } else {
                toSave.setSectionName(NewsAdapter.mList.get(pos).getSectionName());
            }
        });
        Toast.makeText(this, "Article is saved!", Toast.LENGTH_SHORT).show();
    }

    private void DeleteData(Realm mRealm, String imgSrc) {
        mRealm.executeTransaction(realm -> {
            RealmResults<SavedItem> obj = mRealm.where(SavedItem.class).equalTo("imageSrc", imgSrc).findAll();
            obj.deleteAllFromRealm();
        });
        Toast.makeText(this, "Article is unsaved!", Toast.LENGTH_SHORT).show();
    }

    private void PinData() {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                PinnedItem toPin = mRealm.createObject(PinnedItem.class);
                if (NewsAdapter.mList.get(pos).getFields().getBodyText() == null) {
                    toPin.setPublicationBody("This article has no body.");
                } else {
                    toPin.setPublicationBody(NewsAdapter.mList.get(pos).getFields().getBodyText());
                }
                if (NewsAdapter.mList.get(pos).getFields().getThumbnail() != null) {
                    toPin.setImageSrc(NewsAdapter.mList.get(pos).getFields().getThumbnail());
                } else {
                    toPin.setImageSrc("R.drawable.nophoto");
                }
                if (NewsAdapter.mList.get(pos).getFields().getFirstPublicationDate() == null) {
                    toPin.setPublicationDate(NewsAdapter.mList.get(pos).getWebPublicationDate().substring(0, 10));
                } else {
                    toPin.setPublicationDate(NewsAdapter.mList.get(pos).getFields().getFirstPublicationDate().substring(0, 10));
                }
                if (NewsAdapter.mList.get(pos).getFields().getHeadline() == null) {
                    toPin.setHeadline("This article has no headline!");
                } else {
                    toPin.setHeadline(NewsAdapter.mList.get(pos).getFields().getHeadline());
                }
            }
        });
        Toast.makeText(this, "Article is pinned!", Toast.LENGTH_SHORT).show();
    }

    private void UnPinData(Realm mRealm, String imgSrc) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<PinnedItem> obj = mRealm.where(PinnedItem.class).equalTo("imageSrc", imgSrc).findAll();
                obj.deleteAllFromRealm();
            }
        });
        Toast.makeText(this, "Article is unpinned!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }
}
