package com.example.ankwinam.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.graphics.BitmapFactory;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class Tema_NaviActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    boolean bLog = false; // false : 로그아웃 상태
    private ListView listView;
    ArrayList<Walk_Info> h_info_list;
    WalkListAdapter myadapter;
    Walk_Info myLocal1,myLocal2,myLocal3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tema_navigation);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //테마별 스크롤 스피너 매핑 부분
        String[] temaspnnier = getResources().getStringArray(R.array.Temaspnnier);
        ArrayAdapter<String> tema_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, temaspnnier);
        Spinner spnnier = (Spinner)findViewById(R.id.tema_spinner);
        spnnier.setAdapter(tema_adapter);

        spnnier.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected
                            (AdapterView<?> parent, View view, int position, long id) {
                        print(view, position); //아이템을 선택하면 print가 실행된다 (이 부분 수정해서 리스트 보여주기)
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );

        //글쓰기 버튼 이벤트
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent write = new Intent(Tema_NaviActivity.this, WritePageActivity.class);
                startActivity(write);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        //리스트 뷰 구현
        listView = (ListView)findViewById(R.id.listView_tema);
        myLocal1 = new Walk_Info("송지은", "여", "26", BitmapFactory.decodeResource(getResources(), R.drawable.time));
        myLocal2 = new Walk_Info("박보람", "여", "22", BitmapFactory.decodeResource(getResources(), R.drawable.time));
        myLocal3 = new Walk_Info("설현", "여", "21", BitmapFactory.decodeResource(getResources(), R.drawable.time));
        h_info_list = new ArrayList<Walk_Info>();
        h_info_list.add(myLocal1);
        h_info_list.add(myLocal2);
        h_info_list.add(myLocal3);
        h_info_list.add(myLocal1);
        h_info_list.add(myLocal2);
        h_info_list.add(myLocal3);
        h_info_list.add(myLocal1);
        h_info_list.add(myLocal2);
        h_info_list.add(myLocal3);

        myadapter = new WalkListAdapter(getApplicationContext(),R.layout.tema_info, h_info_list);
        listView.setAdapter(myadapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
//                Intent intent = new Intent(getApplicationContext(), TemaResultActivity.class); // 다음넘어갈 화면
//                Bitmap sendBitmap = h_info_list.get(position).image;
//
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                sendBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//                byte[] byteArray = stream.toByteArray();
//
//                intent.putExtra("image",byteArray);
//                startActivity(intent);
                Intent detail_go = new Intent(Tema_NaviActivity.this, ScrollingActivity.class);
                startActivity(detail_go);
            }
        });

    }

    //수정해야할 print 함수 부분
    public void print(View v, int position){
        Spinner sp = (Spinner)findViewById(R.id.tema_spinner);
        String res = "";
        if(sp.getSelectedItemPosition()>0){
            res=(String)sp.getAdapter().getItem(sp.getSelectedItemPosition());
        }
        if(res!=""){
            Toast.makeText(getApplicationContext(), res, Toast.LENGTH_SHORT).show();
        }
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

            // Handle the camera action
        if (id == R.id.menu_home) {
            Toast.makeText(getApplicationContext(), "홈", Toast.LENGTH_SHORT).show();
            Intent go_home = new Intent(Tema_NaviActivity.this, Choice_NaviActivity.class);
            startActivity(go_home);
            finish();
        } else if (id == R.id.menu_local) {
            Toast.makeText(getApplicationContext(), "지역 별 이동", Toast.LENGTH_SHORT).show();
            Intent go_local = new Intent(Tema_NaviActivity.this, Local_NaviActivity.class);
            startActivity(go_local);
            finish();
        } else if (id == R.id.menu_tema) {
            Toast.makeText(getApplicationContext(), "테마 별 이동", Toast.LENGTH_SHORT).show();
            Intent go_tema = new Intent(Tema_NaviActivity.this, Tema_NaviActivity.class);
            startActivity(go_tema);
            finish();
        } else if (id == R.id.menu_history) {
            Toast.makeText(getApplicationContext(), "내가 쓴 글", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.menu_stamp) {
            Toast.makeText(getApplicationContext(), "스탬프", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.menu_jjim) {
                Toast.makeText(getApplicationContext(),"찜 한 산책로",Toast.LENGTH_SHORT).show();
            Intent go_jjim = new Intent(Tema_NaviActivity.this, JJim_NaviActivity.class);
            startActivity(go_jjim);
            finish();
        } else if (id == R.id.menu_logout) {

            Intent go_main = new Intent(Tema_NaviActivity.this, MainActivity.class);
            startActivity(go_main);
            finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
