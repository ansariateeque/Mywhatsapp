package com.example.mywhatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.mywhatsapp.Adapter.ChatMessageAdapter;
import com.example.mywhatsapp.Adapter.GroupMessageChatAdapter;
import com.example.mywhatsapp.Models.GroupMessageModels;
import com.example.mywhatsapp.Models.MessageModels;
import com.example.mywhatsapp.Models.Users;
import com.example.mywhatsapp.databinding.ActivityGroupChatDetailsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class GroupChatDetails extends AppCompatActivity {
    FirebaseAuth auth;

    FirebaseDatabase database;
    ActivityGroupChatDetailsBinding binding;
    String sendername = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupChatDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        getSupportActionBar().hide();
        binding.tvname.setText("Friends Group");
       // Picasso.get().load("2wCEAAoHCBUWFRgWFRYYGBgYGBgYGhoaFRgYGBgSGBgZGRgYGBgcIS4lHB4rIRgYJjgmKy8xNTU1GiQ7QDs0Py40NTEBDAwMEA8QHxISHzErJSsxNDQ0NDQ0NDQxNDQ0NDQ0NDQ0NDE0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDQ0NDE0NDQ0NP").placeholder(R.drawable.dplogo).into(binding.profileImage);
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


        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GroupChatDetails.this, MainActivity.class));
            }
        });


      final  ArrayList<MessageModels>list1=new ArrayList<>();
      final   ChatMessageAdapter chatMessageAdapter=new ChatMessageAdapter(list1,this);

        binding.chatrecycleview.setAdapter(chatMessageAdapter);

        binding.chatrecycleview.setLayoutManager(new LinearLayoutManager(this));

        database.getReference().child("Group Chat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list1.clear();

                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    MessageModels messageModels=snapshot1.getValue(MessageModels.class);
                    list1.add(messageModels);

                }
                    chatMessageAdapter.notifyDataSetChanged();

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
                MessageModels messageModels=new MessageModels(auth.getUid(), sendername,msg);
                messageModels.setTime(new Date().getTime());
                binding.etmsg.setText("");

                database.getReference().child("Group Chat").push().setValue(messageModels).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                });
            }
        });


    }
}