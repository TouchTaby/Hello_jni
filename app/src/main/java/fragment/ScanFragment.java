package fragment;


import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.hcpda.hello_jni.R;
import com.hcpda.hello_jni.activity.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.EPC;
import utils.Tools;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScanFragment extends Fragment implements View.OnClickListener {

    MainActivity contex;
    Button bt_Scan;
    Button bt_clear;
    ListView listView;
    TextView tv_tagCount;
    boolean scanStop = false;
    boolean isContinue = true;
    static String TAG = "TAG";
    private ArrayList<EPC> listEPC;
    private ArrayList<String> listepc = new ArrayList<>();
    private ArrayList<Map<String, Object>> listMap;
    InventoryThread thread;
    private MediaPlayer mMediaPlayer = null;
    boolean playSound = false;
    MyScanCallBack SCAN_CALL_BACK;
    private View rootView;

    public interface MyScanCallBack {
        //把扫描到的EPC 回调给write fragment
        void onCallBack(ArrayList<String> listepc);
    }

    public class Call {
        void setCannBack(MyScanCallBack myScanCallBack) {
            SCAN_CALL_BACK = myScanCallBack;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        SCAN_CALL_BACK = contex;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_scan, container, false);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        contex = (MainActivity) getActivity();
        init();
        new Call().setCannBack(contex);
    }

    private void init() {
        bt_clear = contex.findViewById(R.id.bt_clear);//清除按钮
        bt_Scan = contex.findViewById(R.id.bt_scan);//扫描按钮
        listView = contex.findViewById(R.id.listView);
        tv_tagCount = contex.findViewById(R.id.tv_tagCount);
        listEPC = new ArrayList<>();
        bt_Scan.setOnClickListener(this);
        bt_clear.setOnClickListener(this);
        thread = new InventoryThread();// 盘存线程
        thread.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_scan:
                if (!scanStop) {
                    Log.e(TAG, "onClick: 开始 ");
                    scanStop = true;
                    bt_Scan.setText("停止");
                } else {
                    Log.e(TAG, "onClick: 结束 ");
                    scanStop = false;
                    bt_Scan.setText("开始采集");
                }
                break;
            case R.id.bt_clear:
                clearData();
                break;
        }
    }

    private class InventoryThread extends Thread {
        private List<byte[]> epcList;

        @Override
        public void run() {
            super.run();
            while (isContinue) {
                if (scanStop) {
                    Log.e(TAG, "run: inventory thread is start! ");
                    epcList = contex.uhfManager.inventoryRealTime(); // inventory real time
                    if (epcList != null && !epcList.isEmpty()) {
                        contex.soundPool.play(1, 1, 1, 1, 1, 1f);
                        for (byte[] epc : epcList) {
                            String epcStr = Tools.Bytes2HexString(epc,
                                    epc.length);
                            addToList(listEPC, epcStr);
                            Log.e(TAG, "run: -------------------" + epcStr);
                            //保存数据后但是没有保存到 listepc 里的数据会刷新
                        }
                    } else {
                        Log.e(TAG, "run: epc == null");
                    }
                    epcList = null;

                }

            }

        }
    }

    private void clearData() {
        listEPC.removeAll(listEPC);
        listView.setAdapter(null);
        listepc.removeAll(listepc);
        tv_tagCount.setText("标签数量：0");
    }

    // EPC add to LISTVIEW
    private void addToList(final List<EPC> list, final String epc) {
        contex.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                tv_tagCount.setText("标签数量：" + listEPC.size());
                // The epc for the first time
                if (list.isEmpty()) {
                    EPC epcTag = new EPC();
                    epcTag.setEpc(epc);
                    epcTag.setCount(1);
                    list.add(epcTag);
                    listepc.add(epc);
                } else {
                    for (int i = 0; i < list.size(); i++) {

                        EPC mEPC = list.get(i);
                        // list contain this epc
                        if (epc.equals(mEPC.getEpc())) {
                            mEPC.setCount(mEPC.getCount() + 1);
                            list.set(i, mEPC);
                            break;
                        } else if (i == (list.size() - 1)) {
                            // list doesn't contain this epc
                            EPC newEPC = new EPC();
                            newEPC.setEpc(epc);
                            newEPC.setCount(1);
                            list.add(newEPC);
                            listepc.add(epc);
                        }
                    }
                }
                // add the data to ListView
                listMap = new ArrayList<>();
                int idcount = 1;
                for (EPC epcdata : list) {

                    Map<String, Object> map = new HashMap<>();
                    map.put("ID", idcount);
                    map.put("EPC", epcdata.getEpc());
                    map.put("COUNT", epcdata.getCount());
                    idcount++;
                    listMap.add(map);
                }
                listView.setAdapter(new SimpleAdapter(contex,
                        listMap, R.layout.listview_ite, new String[]{"ID",
                        "EPC", "COUNT"}, new int[]{
                        R.id.text_id,
                        R.id.text_epc,
                        R.id.text_count}));
            }
        });
        SCAN_CALL_BACK.onCallBack(listepc);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void playTipsMp3(int id) {

        if (playSound)
            return;
        playSound = true;
        mMediaPlayer = MediaPlayer.create(contex, id);
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playSound = false;
                ReleasePlayer();
            }
        });
        mMediaPlayer.start();
    }

    private void ReleasePlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
}
