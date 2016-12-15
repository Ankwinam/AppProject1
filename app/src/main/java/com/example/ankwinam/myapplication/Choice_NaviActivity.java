package com.example.ankwinam.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
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

    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choice_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pref = getSharedPreferences("auto_login",MODE_PRIVATE);
        Toast.makeText(getApplicationContext(),pref.getString("email","")+"님 반갑습니다.",Toast.LENGTH_SHORT);
        //지역 별 페이지 버튼 Intent
        ImageButton local_go = (ImageButton)findViewById(R.id.local_go);
        local_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent localpage_go = new Intent(Choice_NaviActivity.this, Local_NaviActivity.class);
                startActivity(localpage_go);
//                Intent localpage_go = new Intent(Choice_NaviActivity.this, CommentActivity.class);
//                startActivity(localpage_go);
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
            Intent go_his = new Intent(Choice_NaviActivity.this, CommunityHistoryActivity.class);
            startActivity(go_his);
        } else if (id == R.id.menu_stamp) {
            Toast.makeText(getApplicationContext(), "스탬프", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.menu_jjim) {
            Toast.makeText(getApplicationContext(), "찜 한 산책로",Toast.LENGTH_SHORT).show();
            Intent go_jjim = new Intent(Choice_NaviActivity.this, JJim_NaviActivity.class);
            startActivity(go_jjim);
        } else if (id == R.id.menu_logout) {
            SharedPreferences pref = getSharedPreferences("auto_login",MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.putString("auto","false");
            editor.commit();

            Intent go_main = new Intent(Choice_NaviActivity.this, MainActivity.class);
            startActivity(go_main);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}