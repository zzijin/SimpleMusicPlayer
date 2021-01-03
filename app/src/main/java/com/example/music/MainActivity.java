package com.example.music;

import android.Manifest;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);


        getWritePermission();//申请读写权限
        //getInterPermission();//申请网络权限

        MusicManager.openService(this);
    }


    /**
     * 判断是否拥有读写权限
     */
    private void getWritePermission() {
        //判断是否已经赋予权限
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true。
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("提示：");
                dialog.setMessage("未授予读写权限将无法播放本地音乐，请确认权限");
                dialog.setCancelable(false);//设置是否可以通过点击对话框外区域或者返回按键关闭对话框
                dialog.setPositiveButton("申请权限", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        giveWritePermissions();
                    }
                });

                dialog.setNegativeButton("退出软件", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                dialog.show();
            } else {
                giveWritePermissions();
            }
        }
    }

    /**
     * 申请读写权限
     */
    private void giveWritePermissions(){
        /**
         * 读写权限
         */
        final String[] PERMISSIONS_STORAGE = {
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE};    //请求状态码
        final int REQUEST_PERMISSION_CODE = 2;

        //申请权限，字符串数组内是一个或多个要申请的权限，1是申请权限结果的返回参数，在onRequestPermissionsResult可以得知申请结果
        ActivityCompat.requestPermissions(this,PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
    }

    /**
     * 判断是否拥有网络权限
     */
    private void getInterPermission() {
        //判断是否已经赋予权限
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            //如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true。
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.INTERNET)) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("提示：");
                dialog.setMessage("未授予网络权限将无法使用联网服务，请确认权限");
                dialog.setCancelable(false);//设置是否可以通过点击对话框外区域或者返回按键关闭对话框
                dialog.setPositiveButton("申请权限", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getInterPermission();
                    }
                });

                dialog.setNegativeButton("不要网络", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this,"当前仅使用本地功能",Toast.LENGTH_LONG).show();
                    }
                });
                dialog.show();
            } else {
                //申请权限，字符串数组内是一个或多个要申请的权限，1是申请权限结果的返回参数，在onRequestPermissionsResult可以得知申请结果
                giveInterPermissions();
            }
        }
    }

    /**
     * 申请网络权限
     */
    private void giveInterPermissions(){

        final String[] PERMISSIONS_STORAGE = {
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_WIFI_STATE,};    //请求状态码
        final int REQUEST_PERMISSION_CODE = 2;

        //申请权限，字符串数组内是一个或多个要申请的权限，1是申请权限结果的返回参数，在onRequestPermissionsResult可以得知申请结果
        ActivityCompat.requestPermissions(this,PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
    }
}
