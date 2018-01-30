package com.mars.android20180130_1;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity {
    //定位以及google地圖

    LocationManager lm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
    }

    public void click1(View v) {
        //需要有敏感權限
       //1.先檢查有沒有權限
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //沒有的話
            /*
            向使用者請求權限
            ActivityCompat類別亦提供向使用者請求權限的類別方法「requestPermissions」，方法的規格如下：
            第一個參數傳入Context物件，第二個字串陣列則是欲要求的權限，第三個int是本次請求的辨識編號，
            當使用者決定後返回onRequestPermissionsResult方法時的辨認號碼，因此，應在類別中定義符合其功能的常數名稱
            ，以提高程式可讀性，例如請求外部儲存時，常數名稱為「REQUEST_EXTERNEL_PERMISSION」
            第二行傳入一個字串陣列，因本例使用import static故不需要如「android.Manifest.permission.WRITE_EXTERNAL_STORAGE」這麼長的常數，
            直接使用即可。第三行則需要在類別中先定義int常數，值可以是0或1等不與其他權限相關重複的直即可。

            提示：在Android Studio中使用常數時，其實不需要先去定義，直接在程式碼中鍵入常數全大寫的名稱後，按下「Alt+Enter」，
            會立即偵測到常數需求，再選擇「Create constant field ‘常數名稱’」後，再選擇資料型態與值即可，如下圖：
             */
            ActivityCompat.requestPermissions(this,
                    new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, 321
            );
            return;
        } else {
            startLoc();
        }

    }
    //敏感權限必須要覆寫此方法告訴她如果得到權限後要怎麼做
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 321)
        {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //取得權限，開始偵測位置
                startLoc();
            } else {
                    //如果沒有..可以不寫程式處理
            }
        }
    }

    public void startLoc() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new MyListener());
    }

    class MyListener implements LocationListener {

        //位置有改變時會執行的程式
        @Override
        public void onLocationChanged(Location location) {
            //得到經緯度的資料location.getLatitude(),location.getLongitude()
            Log.d("LOC", "Change!!" +  + location.getLatitude() + "," + location.getLongitude());
            //可以利用經緯度來算直線距離
            //起一個101的位置物件
            Location loc101 = new Location("LOC");
            //設定經緯度
            loc101.setLatitude(25.0336);
            loc101.setLongitude(121.5646);
            //使用內建的方法distanceTo 就可以算出距離,單位公尺
            float dist = location.distanceTo(loc101);
            Log.d("LOC", "Dist:" + dist);

        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {
            //定位提供者如果開啟時會回呼，並將開啟的提供者傳至provider字串中
        }

        @Override
        public void onProviderDisabled(String s) {
            //定位提供者如果關閉時會回呼，並將關閉的提供者傳至provider字串中
        }
    }
}

