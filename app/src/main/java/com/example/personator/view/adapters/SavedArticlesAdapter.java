package com.example.personator.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.personator.R;
import com.example.personator.shared.data.local.SavedItem;
import com.squareup.picasso.Picasso;

import io.realm.Realm;
import io.realm.RealmResults;

public class SavedArticlesAdapter extends RecyclerView.Adapter<SavedArticlesAdapter.SavedViewHolder> {
    private Context context;
    private Realm mRealm;
    private OnSavedItemCLickedListener onSavedItemCLickedListener;
    public static RealmResults<SavedItem> results;

    public interface OnSavedItemCLickedListener {
        void savedItemClicked(int code);
    }


    public SavedArticlesAdapter(Context context, OnSavedItemCLickedListener onSavedItemCLickedListener) {
        this.context = context;
        mRealm = Realm.getDefaultInstance();
        mRealm.beginTransaction();
        mRealm.commitTransaction();
        this.onSavedItemCLickedListener = onSavedItemCLickedListener;
    }

    @NonNull
    @Override
    public SavedViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new SavedViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.articles_grid_view, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SavedViewHolder savedViewHolder, int i) {
        results = mRealm.where(SavedItem.class).findAll();
        savedViewHolder.mCategory.setText(results.get(i).getSectionName());
        savedViewHolder.mName.setText(results.get(i).getHeadline());
        savedViewHolder.mTime.setText(results.get(i).getPublicationDate());
        if (results.get(i).getImageSrc().equals("R.drawable.nophoto")) {
            Picasso.get().load(R.drawable.nophoto).into(savedViewHolder.mImage);
        } else
            Picasso.get().load(results.get(i).getImageSrc()).into(savedViewHolder.mImage);
    }

    @Override
    public int getItemCount() {
        return mRealm.where(SavedItem.class).findAll().size();
    }

    class SavedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mImage;
        private TextView mName;
        private Button mCategory;
        private TextView mTime;

        public SavedViewHolder(@NonNull View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.id_image_illustration);
            mTime = itemView.findViewById(R.id.id_field_time);
            mCategory = itemView.findViewById(R.id.id_field_category);
            mName = itemView.findViewById(R.id.id_field_name);
            mImage.setOnClickListener(this);
            mTime.setOnClickListener(this);
            mCategory.setOnClickListener(this);
            mName.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onSavedItemCLickedListener.savedItemClicked(getAdapterPosition());
        }
    }
}
