package com.example.ankwinam.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class Choice_NaviActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
// 바꿈
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choice_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //지역 별 페이지 버튼 Intent
        ImageButton local_go = (ImageButton)findViewById(R.id.local_go);
        local_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent localpage_go = new Intent(Choice_NaviActivity.this, Local_NaviActivity.class);
                startActivity(localpage_go);
            }
        });

        //테마 별 페이지 버튼 Intent
        ImageButton tema_go = (ImageButton)findViewById(R.id.tema_go);
        tema_go.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent temapage_go = new Intent(Choice_NaviActivity.this, Tema_NaviActivity.class);
                startActivity(temapage_go);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //오른쪽 옵션메뉴
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.navigation, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.menu_home) {
            Toast.makeText(getApplicationContext(), "홈", Toast.LENGTH_SHORT).show();
            Intent go_home = new Intent(Choice_NaviActivity.this, Choice_NaviActivity.class);
            startActivity(go_home);
            finish();
        } else if (id == R.id.menu_local) {
            Toast.makeText(getApplicationContext(), "지역 별 이동", Toast.LENGTH_SHORT).show();
            Intent go_local = new Intent(Choice_NaviActivity.this, Local_NaviActivity.class);
            startActivity(go_local);
            finish();
        } else if (id == R.id.menu_tema) {
            Toast.makeText(getApplicationContext(), "테마 별 이동", Toast.LENGTH_SHORT).show();
            Intent go_tema = new Intent(Choice_NaviActivity.this, Tema_NaviActivity.class);
            startActivity(go_tema);
            finish();
        } else if (id == R.id.menu_history) {
            Toast.makeText(getApplicationContext(), "내가 쓴 글", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.menu_stamp) {
            Toast.makeText(getApplicationContext(), "스탬프", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.menu_jjim) {
            Toast.makeText(getApplicationContext(), "찜 한 산책로",Toast.LENGTH_SHORT).show();
            Intent go_jjim = new Intent(Choice_NaviActivity.this, JJim_NaviActivity.class);
            startActivity(go_jjim);
            finish();
        } else if (id == R.id.menu_logout) {
            Intent go_main = new Intent(Choice_NaviActivity.this, MainActivity.class);
            startActivity(go_main);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}