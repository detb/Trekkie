package com.github.detb.trekkie;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HikeAdapter extends RecyclerView.Adapter<HikeAdapter.ViewHolder> {

    private final ArrayList<Hike> hikes;
    final private OnListItemClickListener mOnListItemClickListener;

    public HikeAdapter(ArrayList<Hike> hikes, OnListItemClickListener listener) {
        this.hikes = hikes;
        this.mOnListItemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.hike_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.title.setText(hikes.get(position).getTitle());
        viewHolder.description.setText(hikes.get(position).getDescription());
        viewHolder.image.setImageResource(hikes.get(position).getPictureId());
    }

    @Override
    public int getItemCount() {
        return hikes.size();
    }

    public interface OnListItemClickListener {
        void onItemClick(Hike item);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView title;
        TextView description;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.hike_name);
            description = itemView.findViewById(R.id.hike_description);
            image = itemView.findViewById(R.id.hike_picture);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            //Calls these methods on click
            mOnListItemClickListener.onItemClick(hikes.get(getAdapterPosition()));

        }
    }
}
