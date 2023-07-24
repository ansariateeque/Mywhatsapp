package com.example.mywhatsapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mywhatsapp.Adapter.ChatAdapter;
import com.example.mywhatsapp.MainActivity;
import com.example.mywhatsapp.Models.Users;
import com.example.mywhatsapp.R;
import com.example.mywhatsapp.databinding.ChatlayoutBinding;
import com.example.mywhatsapp.databinding.FragmentChatBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Chat#} factory method to
 * create an instance of this fragment.
 */
public class Chat extends Fragment {


    FragmentChatBinding binding;
    ArrayList<Users> list;
    FirebaseDatabase database;

    FirebaseAuth auth;

    public Chat() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChatBinding.inflate(inflater, container, false);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();


        list = new ArrayList<>();
        ChatAdapter chatAdapter = new ChatAdapter(list, getContext());
        binding.charrecycleview.setAdapter(chatAdapter);
        binding.charrecycleview.setLayoutManager(new LinearLayoutManager(getContext()));
        database.getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Users users = dataSnapshot.getValue(Users.class);
                    users.setUserId(dataSnapshot.getKey());
                    if (!users.getUserId().equals(FirebaseAuth.getInstance().getUid()))
                        list.add(users);
                }
                chatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return binding.getRoot();
    }
}