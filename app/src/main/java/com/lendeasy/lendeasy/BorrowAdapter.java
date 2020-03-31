package com.lendeasy.lendeasy;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class BorrowAdapter extends RecyclerView.Adapter<BorrowAdapter.ViewHolder> {

    ArrayList<BorrowModel> list;
    Context context;


    public BorrowAdapter(ArrayList<BorrowModel> list,Context context){
        this.list=list;
        this.context=context;
    }

    @NonNull
    @Override
    public BorrowAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view= LayoutInflater.from(parent.getContext()).inflate(R.layout.borrow_item ,parent,false);
        return new BorrowAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BorrowAdapter.ViewHolder holder, int position) {


        holder.Name.setText(list.get(position).getName());
        holder.Title.setText(list.get(position).getTitle());
        holder.Description.setText(list.get(position).getDescription());
        holder.Time.setText("Time: "+list.get(position).getTime());

        FirebaseStorage firebaseStorage=FirebaseStorage.getInstance();
        StorageReference storage=firebaseStorage.getReference().child("Images");

        StorageReference childreference=storage.child(list.get(position).getImageUrl());
        childreference.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("TAG","Heyy");
                        Glide.with(context).load(uri.toString()).override(900,900).into(holder.imageView);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView Name,Title,Description,Time;
        ImageView imageView;
        ImageButton lend,chat;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Name=itemView.findViewById(R.id.name);
            Title=itemView.findViewById(R.id.title);
            Description=itemView.findViewById(R.id.description);
            imageView=itemView.findViewById(R.id.image);
            Time=itemView.findViewById(R.id.time);

            lend=itemView.findViewById(R.id.lend);
            chat=itemView.findViewById(R.id.chat);
        }
    }
}
