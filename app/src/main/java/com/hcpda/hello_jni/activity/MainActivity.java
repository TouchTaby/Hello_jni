package com.hcpda.hello_jni.activity;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.WindowManager;

import com.hcpda.hello_jni.R;

import java.util.ArrayList;

import fragment.ReadTagFragment;
import fragment.ScanFragment;
import fragment.SetFragment;
import fragment.WriteFragment;
import utils.UhfManager;
import utils.ZstBarcodeCtrl;

public class MainActivity extends SerialPortActivity  implements ScanFragment.MyScanCallBack {

    static String TAG = "TAG";
    public ArrayList<String> listepc = new ArrayList<>();//写卡数据源
    public UhfManager uhfManager;
    FragmentTabHost tabHost;
    ZstBarcodeCtrl zstBarcodeCtrl;
    public SoundPool soundPool;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_main);
        //寻找ID要android开头，命名的时候也要注意
        tabHost =  findViewById(android.R.id.tabhost);
        tabHost.setup(this, getSupportFragmentManager(), R.id.fragment);
        //添加标题跟fragment
        tabHost.addTab(tabHost.newTabSpec("扫描").setIndicator("扫描"), ScanFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec("销毁").setIndicator("销毁"), ReadTagFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec("读写").setIndicator("读写"), WriteFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec("设置").setIndicator("设置"), SetFragment.class, null);
        uhfManager = new UhfManager(mSerialPort.getInputStream(), mSerialPort.getOutputStream());
        zstBarcodeCtrl = ZstBarcodeCtrl.getInstance();
        zstBarcodeCtrl.set_gpio(0,81);
        zstBarcodeCtrl.set_gpio(0,113);
        //声音初始化
        soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM,5);
        soundPool.load(this,R.raw.msg,1);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        zstBarcodeCtrl.set_gpio(1,81);
        zstBarcodeCtrl.set_gpio(1,113);
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        zstBarcodeCtrl.set_gpio(0,81);
        zstBarcodeCtrl.set_gpio(0,113);
    }
    @Override
    protected void onResume() {
        super.onResume();
        zstBarcodeCtrl.set_gpio(0,81);
        zstBarcodeCtrl.set_gpio(0,113);
    }
    @Override
    protected void onStop() {
        super.onStop();
        zstBarcodeCtrl.set_gpio(1,81);
        zstBarcodeCtrl.set_gpio(1,113);
    }
    @Override
    public void onCallBack(ArrayList<String> listepc) {
        //从scan接收的
        this.listepc = listepc;
    }
}

