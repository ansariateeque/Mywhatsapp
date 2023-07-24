package com.example.mywhatsapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mywhatsapp.ChatDetails;
import com.example.mywhatsapp.Models.MessageModels;
import com.example.mywhatsapp.Models.Users;
import com.example.mywhatsapp.R;
import com.example.mywhatsapp.databinding.ChatlayoutBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyviewHolder> {

    ArrayList<Users> list;
    Context context;


    public ChatAdapter(ArrayList<Users> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chatlayout, parent, false);

        return new MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, int position) {
        Users users = list.get(position);
        holder.tvname.setText(users.getUsername());
        Picasso.get().load(users.getProfilepic()).placeholder(R.drawable.dplogo).into(holder.imageView);
        FirebaseDatabase.getInstance().getReference().child("chats").child(FirebaseAuth.getInstance().getUid() + users.getUserId())
                .orderByChild("time").limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChildren()) {
                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                holder.tvlastmsg.setText(snapshot1.child("msg").getValue(String.class));

                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatDetails.class);
                intent.putExtra("key_username", users.getUsername());
                intent.putExtra("key_id", users.getUserId());
                intent.putExtra("key_profile", users.getProfilepic());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tvname, tvlastmsg;

        public MyviewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.profile_image);
            tvname = itemView.findViewById(R.id.tvname);
            tvlastmsg = itemView.findViewById(R.id.tvlastmsg);
        }
    }
}
