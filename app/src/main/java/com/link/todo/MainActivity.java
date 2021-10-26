package com.link.todo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.link.todo.core.ToDoDatabaseContract;
import com.link.todo.core.ToDoDbHelper;
import com.link.todo.core.ToDoItem;

public class MainActivity extends AppCompatActivity {

    ViewPager vPager;
    ToDoPagerAdapter pagerAdapter;
    TabLayout tabLayout;

    FloatingActionButton floatingActionButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vPager = (ViewPager) findViewById(R.id.vpPager);
        pagerAdapter = new ToDoPagerAdapter(getSupportFragmentManager());
        vPager.setAdapter(pagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(vPager);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddUpdateItemActivity.class);
                intent.putExtra("link.todo.ToDoItem", new ToDoItem(MainActivity.this));
                startActivity(intent);
           }
    });

        ActionBar actionBar = getSupportActionBar();
        actionBar.setElevation(0);
    }
}