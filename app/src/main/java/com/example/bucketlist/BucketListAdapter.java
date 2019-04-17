package com.example.bucketlist;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

public class BucketListAdapter extends RecyclerView.Adapter<BucketListAdapter.ViewHolder> {

    private List<BucketItem> bucketItemList;

    public BucketListAdapter(List<BucketItem> bucketItemList) {
        this.bucketItemList = bucketItemList;
    }

    @NonNull
    @Override
    public BucketListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.content_item_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BucketListAdapter.ViewHolder viewHolder, int i) {
        final BucketItem bucketItem = bucketItemList.get(i);
        (viewHolder).title.setText(bucketItem.getTitle());
        (viewHolder).description.setText(bucketItem.getDescription());

        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    viewHolder.title.setPaintFlags(viewHolder.title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    viewHolder.description.setPaintFlags(viewHolder.description.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    viewHolder.title.setPaintFlags(viewHolder.title.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                    viewHolder.description.setPaintFlags(viewHolder.description.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return bucketItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView description;
        private CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.itemTextTitle);
            description = itemView.findViewById(R.id.itemTextDescription);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }
}


