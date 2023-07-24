package com.example.mywhatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.viewpager.widget.ViewPager;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.Toast;

import com.example.mywhatsapp.Adapter.ViewPagerAdapter;
import com.example.mywhatsapp.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    ActivityMainBinding binding;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();



        ViewPagerAdapter adapter=new ViewPagerAdapter(getSupportFragmentManager());
        binding.viewpager.setAdapter(adapter);

        binding.tablayout.setupWithViewPager(binding.viewpager);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.settings:
                startActivity(new Intent(MainActivity.this,Settings.class));
                break;
            case R.id.logout:
                auth.signOut();
                startActivity(new Intent(MainActivity.this,SignInActivity.class));
                break;
            case R.id.groupchat:
                startActivity(new Intent(MainActivity.this,GroupChatDetails.class));
                break;
        }
        return true;

    }


}