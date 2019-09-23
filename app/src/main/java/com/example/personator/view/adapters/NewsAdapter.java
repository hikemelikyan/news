package com.example.personator.view.adapters;

import android.annotation.SuppressLint;
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
import com.example.personator.model.Result;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.GridViewHolder> {

    private Context mContext;
    private ImageClickListener imageClickListener;
    private List<Result> mList;

    public interface ImageClickListener {
        void clicked(int code);
    }

    public NewsAdapter(Context context, ImageClickListener imageClickListener, List<Result> mList) {
        this.mContext = context;
        this.mList = new ArrayList<>();
        this.mList = mList;
        this.imageClickListener = imageClickListener;
    }

    private Result getData(int pos) {
        return mList.get(pos);
    }

    @NonNull
    @Override
    public GridViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new GridViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.articles_grid_view, viewGroup, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull GridViewHolder gridViewHolder, int i) {
        if (getData(i).getFields().getHeadline() == null) {
            gridViewHolder.mName.setText("This article has no headline.");
        } else {
            gridViewHolder.mName.setText(getData(i).getFields().getHeadline());
        }
        if (getData(i).getSectionName() == null) {
            gridViewHolder.mCategory.setText("Unknown");
        } else {
            gridViewHolder.mCategory.setText(getData(i).getSectionName());
        }
        if (getData(i).getFields().getThumbnail() != null) {
            Picasso.get().load(getData(i).getFields().getThumbnail()).into(gridViewHolder.mImage);
        } else {
            Picasso.get().load(R.drawable.nophoto).into(gridViewHolder.mImage);
        }
        if (getData(i).getFields().getFirstPublicationDate() == null) {
            gridViewHolder.mTime.setText(getData(i).getWebPublicationDate().substring(0, 10));
        } else {
            gridViewHolder.mTime.setText(getData(i).getFields().getFirstPublicationDate().substring(0, 10));
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class GridViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mImage;
        private TextView mName;
        private Button mCategory;
        private TextView mTime;

        GridViewHolder(@NonNull View itemView) {
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
            imageClickListener.clicked(getAdapterPosition());
        }
    }
}
