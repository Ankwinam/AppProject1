package com.example.ankwinam.myapplication;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MapActivity extends AppCompatActivity implements MapView.MapViewEventListener, MapView.CurrentLocationEventListener,
        MapView.OpenAPIKeyAuthenticationResultListener, MapView.POIItemEventListener {
    public static String API_KEY = "2c4fd138e21a2323748097a5fdaa61ae";

    private MapView mapView;
    private FloatingActionButton fab;
    private MapPOIItem[] mPOIPoints = null;
    private MapPoint[] mPoints = null;

    private double[] yangjae_X;
    private double[] yangjae_Y;

    private double[] distance;
    private double[] time;
    private double[] time_s;

    private int pinpoint[];

    private int controlcount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);

        mapView = new MapView(this);
        mapView.setDaumMapApiKey(API_KEY);

        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);

        mapView.setMapViewEventListener(this);
        mapView.setPOIItemEventListener(this);
        MapView.setMapTilePersistentCacheEnabled(true);

        mapView.setShowCurrentLocationMarker(true);

        fab = (FloatingActionButton) findViewById(R.id.current_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //현위치 모드
                mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading);
                Toast.makeText(MapActivity.this, "현재위치 찾는 중", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMapViewInitialized(final MapView mapView) {
//        if(mapView.getCurrentLocationTrackingMode() == MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading){
//            mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff); //현위치가 켜져 있으면 끄기
//        }
        // Using TedPermission library
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(MapActivity.this, "권한 허가", Toast.LENGTH_SHORT).show();

                //Polyline 이어주는 좌표들
                yangjae_X = new double[30];
                yangjae_X[0]=127.035333;yangjae_X[1]=127.034134;yangjae_X[2]=127.032526;yangjae_X[3]=127.031072;yangjae_X[4]=127.029540;
                yangjae_X[5]=127.028775;yangjae_X[6]=127.028740;yangjae_X[7]=127.029263;yangjae_X[8]=127.029926;yangjae_X[9]=127.030411;
                yangjae_X[10]=127.031872;yangjae_X[11]=127.032459;yangjae_X[12]=127.032554;yangjae_X[13]=127.033090;yangjae_X[14]=127.033798;
                yangjae_X[15]=127.034915;yangjae_X[16]=127.036382;yangjae_X[17]=127.037518;yangjae_X[18]=127.038034;yangjae_X[19]=127.039502;
                yangjae_X[20]=127.043519;yangjae_X[21]=127.043938;yangjae_X[22]=127.040449;yangjae_X[23]=127.040008;yangjae_X[24]=127.038835;
                yangjae_X[25]=127.038235;yangjae_X[26]=127.037878;yangjae_X[27]=127.037718;yangjae_X[28]=127.036541;yangjae_X[29]=127.035333;
                yangjae_Y = new double[30];
                yangjae_Y[0]=37.473206;yangjae_Y[1]=37.472943;yangjae_Y[2]=37.471748;yangjae_Y[3]=37.470290;yangjae_Y[4]=37.467718;
                yangjae_Y[5]=37.467596;yangjae_Y[6]=37.467955;yangjae_Y[7]=37.468674;yangjae_Y[8]=37.469413;yangjae_Y[9]=37.470162;
                yangjae_Y[10]=37.471631;yangjae_Y[11]=37.472167;yangjae_Y[12]=37.472390;yangjae_Y[13]=37.472790;yangjae_Y[14]=37.473200;
                yangjae_Y[15]=37.473529;yangjae_Y[16]=37.473833;yangjae_Y[17]=37.474714;yangjae_Y[18]=37.474729;yangjae_Y[19]=37.475225;
                yangjae_Y[20]=37.479573;yangjae_Y[21]=37.479353;yangjae_Y[22]=37.475176;yangjae_Y[23]=37.474715;yangjae_Y[24]=37.474052;
                yangjae_Y[25]=37.473880;yangjae_Y[26]=37.473353;yangjae_Y[27]=37.473277;yangjae_Y[28]=37.473434;yangjae_Y[29]=37.473206;

                MapPolyline polyline = new MapPolyline();
                polyline.setTag(1000);
                polyline.setLineColor(Color.argb(200, 255, 51, 5)); // Polyline 컬러 지정.
                // Polyline 좌표 지정
                for(int i =0;i<yangjae_X.length;i++) {
                    polyline.addPoint(MapPoint.mapPointWithGeoCoord(yangjae_Y[i], yangjae_X[i]));
                }
                mapView.addPolyline(polyline); // Polyline 지도에 올리기.
                // 지도뷰의 중심좌표와 줌레벨을 Polyline이 모두 나오도록 조정.
                MapPointBounds mapPointBounds = new MapPointBounds(polyline.getMapPoints());
                int padding = 100; // px
                mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding));

                //핀 마커 좌표들
                mPoints = new MapPoint[10];
                mPoints[0] = MapPoint.mapPointWithGeoCoord(37.473206, 127.035333);mPoints[1] = MapPoint.mapPointWithGeoCoord(37.471748, 127.032526);
                mPoints[2] = MapPoint.mapPointWithGeoCoord(37.467955, 127.028740);mPoints[3] = MapPoint.mapPointWithGeoCoord(37.470162, 127.030411);
                mPoints[4] = MapPoint.mapPointWithGeoCoord(37.472790, 127.033090);mPoints[5] = MapPoint.mapPointWithGeoCoord(37.473833, 127.036382);
                mPoints[6] = MapPoint.mapPointWithGeoCoord(37.475225, 127.039502);mPoints[7] = MapPoint.mapPointWithGeoCoord(37.479573, 127.043519);
                mPoints[8] = MapPoint.mapPointWithGeoCoord(37.475176, 127.040449);mPoints[9] = MapPoint.mapPointWithGeoCoord(37.473880, 127.038235);
                //마커 생성
                mPOIPoints = new MapPOIItem[10];
                for(int i =1;i<=mPOIPoints.length;i++) {
                    mPOIPoints[i-1] = new MapPOIItem();
                    if(i==1)
                        mPOIPoints[i-1].setItemName("양재천 출발지");
                    else
                        mPOIPoints[i-1].setItemName("양재천 "+i);
                    mPOIPoints[i-1].setTag(i-1);
                    mPOIPoints[i-1].setMapPoint(mPoints[i-1]);
                    mPOIPoints[i-1].setMarkerType(MapPOIItem.MarkerType.BluePin); //기본으로 제공하는 BluePin 마커 모양
                    if(i==1)
                        mPOIPoints[i-1].setSelectedMarkerType(MapPOIItem.MarkerType.YellowPin);
                    else
                        mPOIPoints[i-1].setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); //마커를 클릭했을때 기본으로 제공하는 RedPin 마커 모양
                    mapView.addPOIItem(mPOIPoints[i-1]);
                }
                mapView.selectPOIItem(mPOIPoints[0], true);
            }
            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(MapActivity.this, "권한 거부\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        new TedPermission(this)
                .setPermissionListener(permissionlistener)
                .setRationaleMessage("위치 접근 권한 필요")
                .setDeniedMessage("거부\n[설정] > [권한]에서 권한 허용")
                .setPermissions(ACCESS_FINE_LOCATION)
                .check();
    }

    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
        distance = new double[10];
        time = new double[10];
        time_s = new double[10];
        double distancesum = 0.0;
        double timesum = 0.0;
        double timesum_s;

        MapPolyline polyline2 = new MapPolyline();
        polyline2.setTag(1001);
        polyline2.setLineColor(Color.GREEN); // Polyline 컬러 지정.

        pinpoint = new int[9];
        pinpoint[0]=2;pinpoint[1]=6;pinpoint[2]=9;pinpoint[3]=13;pinpoint[4]=16;
        pinpoint[5]=19;pinpoint[6]=20;pinpoint[7]=22;pinpoint[8]=25;

        // Polyline 좌표 지정
        if(controlcount == 0 && mPOIPoints[mapPOIItem.getTag()].getItemName().equals("양재천 출발지")){
            controlcount++;
        }
        else if(controlcount == 10 && mPOIPoints[mapPOIItem.getTag()].getItemName().equals("양재천 출발지")){
            for (int i = 0; i < yangjae_X.length; i++) {
                polyline2.addPoint(MapPoint.mapPointWithGeoCoord(yangjae_Y[i], yangjae_X[i]));
            }
            controlcount++;
        }
        else {
        }
        for(int i = 0; i<9; i++){
            if(controlcount == i+1 && mPOIPoints[mapPOIItem.getTag()].getItemName().equals("양재천 "+(i+2))) {
                for (int j = 0; j <= pinpoint[i]; j++) {
                    polyline2.addPoint(MapPoint.mapPointWithGeoCoord(yangjae_Y[j], yangjae_X[j])); }
                controlcount++;
            }
        }
        mapView.addPolyline(polyline2); // Polyline 지도에 올리기.

        //좌표를 계산하기 위한 객체 생성
        Location locationA = new Location("point A");locationA.setLongitude(yangjae_Y[0]);locationA.setLatitude(yangjae_X[0]);//핀마커
        Location locationB = new Location("point B");locationB.setLongitude(yangjae_Y[1]);locationB.setLatitude(yangjae_X[1]);
        Location locationC = new Location("point C");locationC.setLongitude(yangjae_Y[2]);locationC.setLatitude(yangjae_X[2]);//핀마커
        Location locationD = new Location("point D");locationD.setLongitude(yangjae_Y[3]);locationD.setLatitude(yangjae_X[3]);
        Location locationE = new Location("point E");locationE.setLongitude(yangjae_Y[4]);locationE.setLatitude(yangjae_X[4]);
        Location locationF = new Location("point F");locationF.setLongitude(yangjae_Y[5]);locationF.setLatitude(yangjae_X[5]);
        Location locationG = new Location("point G");locationG.setLongitude(yangjae_Y[6]);locationG.setLatitude(yangjae_X[6]);//핀마커
        Location locationH = new Location("point H");locationH.setLongitude(yangjae_Y[7]);locationH.setLatitude(yangjae_X[7]);
        Location locationI = new Location("point I");locationI.setLongitude(yangjae_Y[8]);locationI.setLatitude(yangjae_X[8]);
        Location locationJ = new Location("point J");locationJ.setLongitude(yangjae_Y[9]);locationJ.setLatitude(yangjae_X[9]);//핀마커
        Location locationK = new Location("point K");locationK.setLongitude(yangjae_Y[10]);locationK.setLatitude(yangjae_X[10]);
        Location locationL = new Location("point L");locationL.setLongitude(yangjae_Y[11]);locationL.setLatitude(yangjae_X[11]);
        Location locationM = new Location("point M");locationM.setLongitude(yangjae_Y[12]);locationM.setLatitude(yangjae_X[12]);
        Location locationN = new Location("point N");locationN.setLongitude(yangjae_Y[13]);locationN.setLatitude(yangjae_X[13]);//핀마커
        Location locationO = new Location("point O");locationO.setLongitude(yangjae_Y[14]);locationO.setLatitude(yangjae_X[14]);
        Location locationP = new Location("point P");locationP.setLongitude(yangjae_Y[15]);locationP.setLatitude(yangjae_X[15]);
        Location locationQ = new Location("point Q");locationQ.setLongitude(yangjae_Y[16]);locationQ.setLatitude(yangjae_X[16]);//핀마커
        Location locationR = new Location("point R");locationR.setLongitude(yangjae_Y[17]);locationR.setLatitude(yangjae_X[17]);
        Location locationS = new Location("point S");locationS.setLongitude(yangjae_Y[18]);locationS.setLatitude(yangjae_X[18]);
        Location locationT = new Location("point T");locationT.setLongitude(yangjae_Y[19]);locationT.setLatitude(yangjae_X[19]);//핀마커
        Location locationU = new Location("point U");locationU.setLongitude(yangjae_Y[20]);locationU.setLatitude(yangjae_X[20]);
        Location locationV = new Location("point V");locationV.setLongitude(yangjae_Y[21]);locationV.setLatitude(yangjae_X[21]);//핀마커
        Location locationW = new Location("point W");locationW.setLongitude(yangjae_Y[22]);locationW.setLatitude(yangjae_X[22]);//핀마커
        Location locationX = new Location("point X");locationX.setLongitude(yangjae_Y[23]);locationX.setLatitude(yangjae_X[23]);
        Location locationY = new Location("point Y");locationY.setLongitude(yangjae_Y[24]);locationY.setLatitude(yangjae_X[24]);
        Location locationZ = new Location("point Z");locationZ.setLongitude(yangjae_Y[25]);locationZ.setLatitude(yangjae_X[25]);//핀마커
        Location locationAA = new Location("point AA");locationAA.setLongitude(yangjae_Y[26]);locationAA.setLatitude(yangjae_X[26]);
        Location locationBB = new Location("point BB");locationBB.setLongitude(yangjae_Y[27]);locationBB.setLatitude(yangjae_X[27]);
        Location locationCC = new Location("point CC");locationCC.setLongitude(yangjae_Y[28]);locationCC.setLatitude(yangjae_X[28]);
        Location locationDD = new Location("point DD");locationDD.setLongitude(yangjae_Y[29]);locationDD.setLatitude(yangjae_X[29]);
        //좌표 객체 더함
        distance[0] += (int) locationA.distanceTo(locationB);distance[0] += (int) locationB.distanceTo(locationC);//1
        distance[1] += (int) locationC.distanceTo(locationD);distance[1] += (int) locationD.distanceTo(locationE);
        distance[1] += (int) locationE.distanceTo(locationF);distance[1] += (int) locationF.distanceTo(locationG);//2
        distance[2] += (int) locationG.distanceTo(locationH);distance[2] += (int) locationH.distanceTo(locationI);
        distance[2] += (int) locationI.distanceTo(locationJ);                                                       //3
        distance[3] += (int) locationJ.distanceTo(locationK);distance[3] += (int) locationK.distanceTo(locationL);
        distance[3] += (int) locationL.distanceTo(locationM);distance[3] += (int) locationM.distanceTo(locationN);//4
        distance[4] += (int) locationN.distanceTo(locationO);distance[4] += (int) locationO.distanceTo(locationP);
        distance[4] += (int) locationP.distanceTo(locationQ);                                                       //5
        distance[5] += (int) locationQ.distanceTo(locationR);distance[5] += (int) locationR.distanceTo(locationS);
        distance[5] += (int) locationS.distanceTo(locationT);                                                       //6
        distance[6] += (int) locationT.distanceTo(locationU);distance[6] += (int) locationU.distanceTo(locationV);//7
        distance[7] += (int) locationV.distanceTo(locationW);                                                       //8
        distance[8] += (int) locationW.distanceTo(locationX);distance[8] += (int) locationY.distanceTo(locationY);
        distance[8] += (int) locationY.distanceTo(locationZ);                                                       //9
        distance[9] += (int) locationZ.distanceTo(locationAA);distance[9] += (int) locationAA.distanceTo(locationBB);
        distance[9] += (int) locationBB.distanceTo(locationCC);distance[9] += (int) locationCC.distanceTo(locationDD);
        distance[9] += (int) locationDD.distanceTo(locationA);                                                      //10

        //남은 시간과 남은 거리 구하기
        for(int i = 9; i >= controlcount-1; i--){
            distancesum += distance[i];
            time[i] = (distance[i] / 4000) * 60;
            time_s[i] = (time[i] - (int)time[i]) * 60;
            timesum += time[i];
        }
        timesum_s = (timesum - (int)timesum) * 60;

        TextView distanceoutput = (TextView)findViewById(R.id.mapdistance);
        if(controlcount>=0 && controlcount<=10) {
            distanceoutput.setText("총 남은 거리 : " + (int) distancesum + " m"
                    + "\n총 남은 예상 시간 : " + (int) timesum + "분 " + (int) timesum_s + "초"
                    + "\n다음 지점까지 " + (int) distance[mapPOIItem.getTag()] + " m 남음");
        }
        else{
            distanceoutput.setText("코스 완주!");
            Toast.makeText(MapActivity.this, "코스 완주!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {}
    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {}
    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {}
    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {}
    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {}
    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {}
    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {}
    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {}
    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float v) {}
    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {}
    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {}
    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {}
    @Override
    public void onDaumMapOpenAPIKeyAuthenticationResult(MapView mapView, int i, String s) {}
    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {}
    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {}
    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {}
}

