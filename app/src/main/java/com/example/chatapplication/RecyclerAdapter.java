package com.example.chatapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatapplication.databinding.ConversationBinding;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.UserViewHolder>{
    Context context;
    ArrayList<User> users;
    public RecyclerAdapter(Context context, ArrayList<User> users){
        this.context = context;
        this.users = users;

    }
    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.conversation,parent,false);


        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = users.get(position);

        holder.binding.txtUserName.setText(user.getName());
        Glide.with(context).load(user.getProfileImage()).placeholder(R.drawable.avatar).into(holder.binding.imgProfile);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,Chatting.class);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder{
        ConversationBinding binding;

        public UserViewHolder(@NonNull View itemView){
            super(itemView);
            binding = ConversationBinding.bind(itemView);
        }
    }
}
