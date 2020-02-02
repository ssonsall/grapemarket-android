package com.bitc502.grapemarket;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bitc502.grapemarket.connect2server.Connect2Server;
import com.bitc502.grapemarket.currentuserinfo.Session;
import com.bitc502.grapemarket.gps.Gps;
import com.bitc502.grapemarket.model.UserLocationSetting;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MyLocationAuthActivity extends AppCompatActivity {
    private TextView savedAddress,savedAddressX,savedAddressY,currentAddress,currentAddressX,currentAddressY,adressAuthResult,infoAlreadyAdressAuth;
    private Context locationAuthContext;
    private LinearLayout addressAuthLayout;
    private Button btnAuthAddress;

    //gps 관련
    private Gps gps;
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_location_auth);
        savedAddress = findViewById(R.id.savedAddress);
        savedAddressX = findViewById(R.id.savedAddressX);
        savedAddressY = findViewById(R.id.savedAddressY);
        currentAddress = findViewById(R.id.currentAddress);
        currentAddressX = findViewById(R.id.currentAddressX);
        currentAddressY = findViewById(R.id.currentAddressY);
        adressAuthResult = findViewById(R.id.adressAuthResult);
        locationAuthContext = getApplicationContext();
        btnAuthAddress = findViewById(R.id.btnAddressAuth);
        addressAuthLayout = findViewById(R.id.adressAuth_current_layout);
        infoAlreadyAdressAuth = findViewById(R.id.info_already_auth_address);
        setSavedAddressData();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void btnAddressAuthClicked(View V){
        if(savedAddress != null && !savedAddress.getText().toString().equals("설정된 주소가 없습니다.")) {
            //현재 위치 가져오기
            if (!checkLocationServicesStatus()) {
                showDialogForLocationServiceSetting();
            } else {
                checkRunTimePermission();
            }

            gps = new Gps(locationAuthContext);
            double addressX = gps.getLatitude();
            double addressY = gps.getLongitude();
            String address = getCurrentAddress(addressX, addressY);
            //Toast.makeText(locationAuthContext,address,Toast.LENGTH_LONG).show();
            currentAddress.setText(address);
            currentAddressX.setText(addressX + "");
            currentAddressY.setText(addressY + "");
            Double xDiff = Math.abs(Double.parseDouble(currentAddressX.getText().toString()) - Double.parseDouble(savedAddressX.getText().toString()));
            Double yDiff = Math.abs(Double.parseDouble(currentAddressY.getText().toString()) - Double.parseDouble(savedAddressY.getText().toString()));

            if (Math.pow(xDiff, 2) + Math.pow(yDiff, 2) <= 0.0004) {
                new AsyncTask<Void, Boolean, Boolean>() {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                    }

                    @Override
                    protected Boolean doInBackground(Void... voids) {
                        return Connect2Server.saveAddressAuth();
                    }

                    @Override
                    protected void onProgressUpdate(Boolean... values) {
                        super.onProgressUpdate(values);
                    }

                    @Override
                    protected void onPostExecute(Boolean aBoolean) {
                        super.onPostExecute(aBoolean);
                        if (aBoolean) {
                            Toast.makeText(locationAuthContext, "주소인증에 성공했습니다.", Toast.LENGTH_LONG).show();
                            Session.currentUserInfo.getUser().setAddressAuth(1);
                            adressAuthResult.setText("주소인증 성공");

                        } else {
                            Toast.makeText(locationAuthContext, "주소인증에 성공했지만, 서버에 반영하는데 실패했습니다.", Toast.LENGTH_LONG).show();
                            adressAuthResult.setText("주소인증 실패");
                        }
                    }
                }.execute();
            } else {
                Toast.makeText(locationAuthContext, "주소인증에 실패했습니다.", Toast.LENGTH_LONG).show();
                adressAuthResult.setText("주소인증 실패");
            }
        }else{
            Toast.makeText(locationAuthContext, "주소 설정을 먼저 해주세요.", Toast.LENGTH_LONG).show();
        }
    }

    public void setSavedAddressData(){
        try {
            new AsyncTask<Void, UserLocationSetting, UserLocationSetting>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }

                @Override
                protected UserLocationSetting doInBackground(Void... voids) {
                    return Connect2Server.getSavedAddress();
                }

                @Override
                protected void onProgressUpdate(UserLocationSetting... values) {
                    super.onProgressUpdate(values);
                }

                @Override
                protected void onPostExecute(UserLocationSetting userLocationSetting) {
                    super.onPostExecute(userLocationSetting);
                    Log.d("myreload", "실행");
                    savedAddress.setText(userLocationSetting.getAddress());
                    savedAddressX.setText(userLocationSetting.getAddressX());
                    savedAddressY.setText(userLocationSetting.getAddressY());
                    if(savedAddress.getText().toString() != null && !savedAddress.getText().toString().equals("")){
                        //myCurrentLocationShowLayout.setVisibility(View.VISIBLE);
                        if(userLocationSetting.getAddressAuth() == 1){
                            addressAuthLayout.setVisibility(View.GONE);
                            btnAuthAddress.setEnabled(false);
                            btnAuthAddress.setVisibility(View.GONE);
                            infoAlreadyAdressAuth.setVisibility(View.VISIBLE);
                        }
                    }else{
                        savedAddress.setText("설정된 주소가 없습니다.");
                        savedAddressX.setText("설정된 주소가 없습니다.");
                        savedAddressY.setText("설정된 주소가 없습니다.");
                        adressAuthResult.setText("");
                    }

                }
            }.execute();
        }catch (Exception e){
            Log.d("myerror", e.toString());
        }
    }

    public void checkRunTimePermission(){

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)
            // 3.  위치 값을 가져올 수 있음
        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.
            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])) {
                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }
        }
    }

    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("동네 인증을 하려면 위치서비스가 필요합니다.");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public String getCurrentAddress( double latitude, double longitude) {
        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try {

            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    7);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";

        }

        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";
        }
        Address address = addresses.get(0);
        return address.getAddressLine(0);
    }
}
