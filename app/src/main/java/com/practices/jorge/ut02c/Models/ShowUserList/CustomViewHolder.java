package com.practices.jorge.ut02c.Models.ShowUserList;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.practices.jorge.ut02c.R;

public class CustomViewHolder extends RecyclerView.ViewHolder {

    TextView nameTextView;
    TextView emailTextView;
    TextView phoneTextView;
    ImageView crossButtonImageView;
    ImageView editButtonImageView;

    public CustomViewHolder(View itemView) {
        super(itemView);

        nameTextView = itemView.findViewById( R.id.nameTextView );
        emailTextView = itemView.findViewById( R.id.emailTextView );
        phoneTextView = itemView.findViewById( R.id.phoneTextView );
        crossButtonImageView = itemView.findViewById( R.id.crossImageView );
        editButtonImageView = itemView.findViewById( R.id.editImageView );
    }
}
