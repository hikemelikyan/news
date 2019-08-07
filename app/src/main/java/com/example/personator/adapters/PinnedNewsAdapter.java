package com.example.personator.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.personator.R;
import com.example.personator.database.PinnedItem;
import com.squareup.picasso.Picasso;

import io.realm.Realm;
import io.realm.RealmResults;

public class PinnedNewsAdapter extends RecyclerView.Adapter<PinnedNewsAdapter.PinnedViewHolder> {

    private Context context;
    private Realm mRealm;
    private PinnedClickListener pinnedClickListener;
    public static RealmResults<PinnedItem> results;

    public interface PinnedClickListener {
        void pinnedItemClicked(int code);
    }

    public PinnedNewsAdapter(Context context, PinnedClickListener pinnedClickListener) {
        this.context = context;
        mRealm = Realm.getDefaultInstance();
        mRealm.beginTransaction();
        mRealm.commitTransaction();
        this.pinnedClickListener = pinnedClickListener;
    }

    @NonNull
    @Override
    public PinnedViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new PinnedViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.pinned_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PinnedViewHolder pinnedViewHolder, int i) {
        results = mRealm.where(PinnedItem.class).findAll();
        if (results.get(i).getImageSrc().equals("R.drawable.nophoto")) {
            Picasso.get().load(R.drawable.nophoto).into(pinnedViewHolder.image);
        } else
            Picasso.get().load(results.get(i).getImageSrc()).into(pinnedViewHolder.image);
    }

    @Override
    public int getItemCount() {
        return mRealm.where(PinnedItem.class).findAll().size();
    }

    class PinnedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView image;

        public PinnedViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.id_pinned);
            image.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            pinnedClickListener.pinnedItemClicked(getAdapterPosition());
        }
    }
}
