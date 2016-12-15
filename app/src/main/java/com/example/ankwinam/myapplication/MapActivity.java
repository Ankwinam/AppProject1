package com.example.ankwinam.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import net.daum.mf.map.api.CalloutBalloonAdapter;
import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapLayout;
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
    private MapPOIItem mDefaultMarker;

    private double[] yangjae_X;
    private double[] yangjae_Y;

    private Location[] location;

    private static final MapPoint DEFAULT_MARKER_POINT = MapPoint.mapPointWithGeoCoord(37.473206, 127.035333);

//    private double[] yangjae_X = {127.035333,127.034134,127.032526,127.031072,127.029540,127.028775,127.028740,127.029263,127.029926,127.030411,127.031872
//            ,127.032459,127.032554,127.033090,127.033798,127.034915,127.036382,127.037518,127.038034,127.039502,127.043519,127.043938,127.040449,127.040008
//            ,127.038835,127.038235,127.037878,127.037718,127.036541,127.035333};
//    private double[] yangjae_Y = {37.473206,37.472943,37.471748,37.470290,37.467718,37.467596,37.467955,37.468674,37.469413,37.470162,37.471631,37.472167,37.472390,
//            37.472790,37.473200,37.473529,37.473833,37.474714,37.474729,37.475225,37.479573,37.479353,37.475176,37.474715,37.474052,37.473880,37.473353,37.473277,
//            37.473434,37.473206};

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
                //현위치, 나침반 모드
                //mapView.setCurrentLocationEventListener(this);
                mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading);
                Toast.makeText(MapActivity.this, "현재위치 찾는 중", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createDefaultMarker(MapView mapView) {
        mDefaultMarker = new MapPOIItem();
        String name = "Default Marker";
        mDefaultMarker.setItemName(name);
        mDefaultMarker.setTag(0);
        mDefaultMarker.setMapPoint(DEFAULT_MARKER_POINT);
        mDefaultMarker.setMarkerType(MapPOIItem.MarkerType.BluePin);
        mDefaultMarker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);

        mapView.addPOIItem(mDefaultMarker);
        mapView.selectPOIItem(mDefaultMarker, true);
        mapView.setMapCenterPoint(DEFAULT_MARKER_POINT, true);
    }

    @Override
    public void onMapViewInitialized(final MapView mapView) {
        // Using TedPermission library
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(MapActivity.this, "권한 허가", Toast.LENGTH_SHORT).show();

//                //지도 이동
//                MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(37.473206, 127.035333);
//                mapView.setMapCenterPoint(mapPoint, true);

                //핀 마커 좌표들
                mPoints = new MapPoint[10];
                mPoints[0] = MapPoint.mapPointWithGeoCoord(37.473206, 127.035333);mPoints[1] = MapPoint.mapPointWithGeoCoord(37.471748, 127.032526);
                mPoints[2] = MapPoint.mapPointWithGeoCoord(37.467955, 127.028740);mPoints[3] = MapPoint.mapPointWithGeoCoord(37.470162, 127.030411);
                mPoints[4] = MapPoint.mapPointWithGeoCoord(37.472790, 127.033090);mPoints[5] = MapPoint.mapPointWithGeoCoord(37.473833, 127.036382);
                mPoints[6] = MapPoint.mapPointWithGeoCoord(37.475225, 127.039502);mPoints[7] = MapPoint.mapPointWithGeoCoord(37.479573, 127.043519);
                mPoints[8] = MapPoint.mapPointWithGeoCoord(37.475176, 127.040449);mPoints[9] = MapPoint.mapPointWithGeoCoord(37.473880, 127.038235);

//                //마커 생성
//                MapPOIItem marker = new MapPOIItem();
//                marker.setItemName("양재천 산책길");
//                marker.setTag(0);
//                marker.setMapPoint(mapPoint);
//                marker.setMarkerType(MapPOIItem.MarkerType.BluePin);  //기본으로 제공하는 BluePin 마커 모양
//                marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); //마커를 클릭했을때 기본으로 제공하는 RedPin 마커 모양

                //donghee
                mPOIPoints = new MapPOIItem[10];
                for(int i =1;i<=mPOIPoints.length;i++) {
                    mPOIPoints[i-1] = new MapPOIItem();
                    mPOIPoints[i-1].setItemName("양재천 "+i);
                    mPOIPoints[i-1].setTag(i-1);
                    mPOIPoints[i-1].setMapPoint(mPoints[i-1]);
                    mPOIPoints[i-1].setMarkerType(MapPOIItem.MarkerType.BluePin);
                    mPOIPoints[i-1].setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
                    mapView.addPOIItem(mPOIPoints[i-1]);
                }

                //마커 추가
                //mapView.addPOIItem(marker);

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
                // Polyline 지도에 올리기.
                mapView.addPolyline(polyline);

                // 지도뷰의 중심좌표와 줌레벨을 Polyline이 모두 나오도록 조정.
                MapPointBounds mapPointBounds = new MapPointBounds(polyline.getMapPoints());
                int padding = 100; // px
                mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding));

                //좌표 사이의 거리 재기
                double distance=0.0;
                String meter;

//                //좌표를 계산하기 위한 객체 생성
//                location = new Location[31];
//                for(int i =0;i<location.length;i++) {
//                    location[i] = new Location("point "+i);location[i].setLongitude(yangjae_Y[i]);location[i].setLatitude(yangjae_X[i]);
//                }
//                for(int i =0;i<location.length-1;i++) {
//                    distance += (int) location[i].distanceTo(location[i+1]);
//                }
//                distance += (int) location[29].distanceTo(location[0]);

                //좌표를 계산하기 위한 객체 생성
                Location locationA = new Location("point A");locationA.setLongitude(37.473206);locationA.setLatitude(127.035333);
                Location locationB = new Location("point B");locationB.setLongitude(37.472943);locationB.setLatitude(127.034134);
                Location locationC = new Location("point C");locationC.setLongitude(37.471748);locationC.setLatitude(127.032526);
                Location locationD = new Location("point D");locationD.setLongitude(37.472943);locationD.setLatitude(127.031072);
                Location locationE = new Location("point E");locationE.setLongitude(37.467718);locationE.setLatitude(127.029540);
                Location locationF = new Location("point F");locationF.setLongitude(37.467596);locationF.setLatitude(127.028775);
                Location locationG = new Location("point G");locationG.setLongitude(37.467955);locationG.setLatitude(127.028740);
                Location locationH = new Location("point H");locationH.setLongitude(37.468674);locationH.setLatitude(127.029263);
                Location locationI = new Location("point I");locationI.setLongitude(37.469413);locationI.setLatitude(127.029926);
                Location locationJ = new Location("point J");locationJ.setLongitude(37.470162);locationJ.setLatitude(127.030411);
                Location locationK = new Location("point K");locationK.setLongitude(37.471631);locationK.setLatitude(127.031872);
                Location locationL = new Location("point L");locationL.setLongitude(37.472167);locationL.setLatitude(127.032459);
                Location locationM = new Location("point M");locationM.setLongitude(37.472390);locationM.setLatitude(127.032554);
                Location locationN = new Location("point N");locationN.setLongitude(37.472790);locationN.setLatitude(127.033090);
                Location locationO = new Location("point O");locationO.setLongitude(37.473200);locationO.setLatitude(127.033798);
                Location locationP = new Location("point P");locationP.setLongitude(37.473529);locationP.setLatitude(127.034915);
                Location locationQ = new Location("point Q");locationQ.setLongitude(37.473833);locationQ.setLatitude(127.036382);
                Location locationR = new Location("point R");locationR.setLongitude(37.474714);locationR.setLatitude(127.037518);
                Location locationS = new Location("point S");locationS.setLongitude(37.474729);locationS.setLatitude(127.038034);
                Location locationT = new Location("point T");locationT.setLongitude(37.475225);locationT.setLatitude(127.039502);
                Location locationU = new Location("point U");locationU.setLongitude(37.479573);locationU.setLatitude(127.043519);
                Location locationV = new Location("point V");locationV.setLongitude(37.479353);locationV.setLatitude(127.043938);
                Location locationW = new Location("point W");locationW.setLongitude(37.475176);locationW.setLatitude(127.040449);
                Location locationX = new Location("point X");locationX.setLongitude(37.474715);locationX.setLatitude(127.040008);
                Location locationY = new Location("point Y");locationY.setLongitude(37.474052);locationY.setLatitude(127.038835);
                Location locationZ = new Location("point Z");locationZ.setLongitude(37.473880);locationZ.setLatitude(127.038235);
                Location locationAA = new Location("point AA");locationAA.setLongitude(37.473353);locationAA.setLatitude(127.037878);
                Location locationBB = new Location("point BB");locationBB.setLongitude(37.473277);locationBB.setLatitude(127.037718);
                Location locationCC = new Location("point CC");locationCC.setLongitude(37.473434);locationCC.setLatitude(127.036541);
                Location locationDD = new Location("point DD");locationDD.setLongitude(37.473206);locationDD.setLatitude(127.035333);
                //좌표 객체 더함
                distance += (int) locationA.distanceTo(locationB);distance += (int) locationB.distanceTo(locationC);
                distance += (int) locationC.distanceTo(locationD);distance += (int) locationD.distanceTo(locationE);
                distance += (int) locationE.distanceTo(locationF);distance += (int) locationF.distanceTo(locationG);
                distance += (int) locationG.distanceTo(locationH);distance += (int) locationH.distanceTo(locationI);
                distance += (int) locationI.distanceTo(locationJ);distance += (int) locationJ.distanceTo(locationK);
                distance += (int) locationK.distanceTo(locationL);distance += (int) locationL.distanceTo(locationM);
                distance += (int) locationM.distanceTo(locationN);distance += (int) locationN.distanceTo(locationO);
                distance += (int) locationO.distanceTo(locationP);distance += (int) locationP.distanceTo(locationQ);
                distance += (int) locationQ.distanceTo(locationR);distance += (int) locationR.distanceTo(locationS);
                distance += (int) locationS.distanceTo(locationT);distance += (int) locationT.distanceTo(locationU);
                distance += (int) locationU.distanceTo(locationV);distance += (int) locationV.distanceTo(locationW);
                distance += (int) locationW.distanceTo(locationX);distance += (int) locationY.distanceTo(locationY);
                distance += (int) locationY.distanceTo(locationZ);distance += (int) locationZ.distanceTo(locationAA);
                distance += (int) locationAA.distanceTo(locationBB);distance += (int) locationBB.distanceTo(locationCC);
                distance += (int) locationCC.distanceTo(locationDD);distance += (int) locationDD.distanceTo(locationA);
                //더한 총거리 표시
                meter = Double.toString(distance);
                TextView distanceoutput = (TextView)findViewById(R.id.mapdistance);
                distanceoutput.setText("총 거리 : "+meter+ " m");

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
        Log.e("IN", String.valueOf(mapPOIItem.getTag()));
        Log.e("selected : ",mPOIPoints[mapPOIItem.getTag()].getItemName());
        mPOIPoints[mapPOIItem.getTag()].setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);

    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {
    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float v) {

    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {

    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {

    }

    @Override
    public void onDaumMapOpenAPIKeyAuthenticationResult(MapView mapView, int i, String s) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }

}

