package com.example.mywhatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import com.example.mywhatsapp.Adapter.ChatMessageAdapter;
import com.example.mywhatsapp.Models.MessageModels;
import com.example.mywhatsapp.Models.Users;
import com.example.mywhatsapp.databinding.ActivityChatDetailsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class ChatDetails extends AppCompatActivity {
    ActivityChatDetailsBinding binding;

    FirebaseDatabase database;
    FirebaseAuth auth;

    String sendername = "";

    String lastmsg="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        database.getReference().child("users").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users user = snapshot.getValue(Users.class);
                sendername += user.getUsername();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        final String mySenderId = auth.getUid();

        final String receivername = getIntent().getStringExtra("key_username");
        String receiveruserId = getIntent().getStringExtra("key_id");
        String profilepic = getIntent().getStringExtra("key_profile");

        binding.tvname.setText(receivername);
        Picasso.get().load(profilepic).placeholder(R.drawable.dplogo).into(binding.profileImage);
        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChatDetails.this, MainActivity.class));
            }
        });

        final ArrayList<MessageModels> list = new ArrayList<>();
        final ChatMessageAdapter adapter = new ChatMessageAdapter(receiveruserId,list, this);
        binding.chatrecycleview.setAdapter(adapter);

        binding.chatrecycleview.setLayoutManager(new LinearLayoutManager(this));
        final String senderRoom = mySenderId + receiveruserId;
        final String recieverRoom = receiveruserId + mySenderId;

        database.getReference().child("chats").child(senderRoom).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    MessageModels messageModels = snapshot1.getValue(MessageModels.class);
                    messageModels.setMsgId(snapshot1.getKey());
                    list.add(messageModels);
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = binding.etmsg.getText().toString();
                if(msg.equals("")){return;}
                MessageModels messageModels = new MessageModels(mySenderId, "sendby " + sendername + " to " + receivername, msg,receiveruserId);
                messageModels.setTime(new Date().getTime());
                binding.etmsg.setText("");

                database.getReference().child("chats").child(senderRoom).push().setValue(messageModels).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        if(mySenderId.equals(receiveruserId)){
                            return;
                        }
                        messageModels.setName("recieveby " + receivername + " from " + sendername);
                        database.getReference().child("chats").child(recieverRoom).push().setValue(messageModels).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                            }
                        });
                    }
                });


            }
        });

    }
}