package fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.hcpda.hello_jni.R;
import com.hcpda.hello_jni.activity.MainActivity;

import java.util.Arrays;

import utils.Tools;
import utils.UhfManager;


/**
 * A simple {@link Fragment} subclass.
 */
public class WriteFragment extends Fragment implements OnClickListener {
    EditText et_input_psw;
    EditText et_input_start_address;
    EditText et_input_length;
    EditText et_input_write_data;
    Button bt_read_single_tag;
    Button bt_write_tag;
    Spinner sp_storage;
    ScrollView uhf_scrollview;
    TextView tv_result_uhf;
    MainActivity context;
    Spinner sp_listEpc;
    String select_epc = "";
    int select_storage;
    String[] storage_data = {"RESERVE", "EPC", "TID", "USER"};
    int addr = 0;
    int length = 1;
    static String TAG = "TAG";

    public WriteFragment() {
        // Required empty public constructor
        //mainactivity用回调的方式去获取epc, 在mainactivity 里用相同的数据源
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_write, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = (MainActivity) getActivity();
        initView();
        action();
    }

    private void action() {
        //选择的EPC获取
        sp_listEpc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                select_epc = context.listepc.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //选择的存储区获取
        sp_storage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        select_storage = UhfManager.RESERVE;
                        break;
                    case 1:
                        select_storage = UhfManager.EPC;
                        break;
                    case 2:
                        select_storage = UhfManager.TID;
                        break;
                    case 3:
                        select_storage = UhfManager.USER;
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void initView() {
        sp_listEpc = context.findViewById(R.id.sp_select_epc);
        ArrayAdapter adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, context.listepc);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_listEpc.setAdapter(adapter);
        et_input_psw = context.findViewById(R.id.et_input_psw);
        et_input_start_address = context.findViewById(R.id.et_input_start_address);
        et_input_length = context.findViewById(R.id.et_input_length);
        et_input_write_data = context.findViewById(R.id.et_input_write_data);
        bt_read_single_tag = context.findViewById(R.id.bt_read_single_tag);
        bt_write_tag = context.findViewById(R.id.bt_write_single_tag);
        et_input_write_data = context.findViewById(R.id.et_input_write_data);
        sp_storage = context.findViewById(R.id.sp_storage);
        uhf_scrollview = context.findViewById(R.id.uhf_scrollView);
        tv_result_uhf = context.findViewById(R.id.uhf_tv_result);
        bt_read_single_tag.setOnClickListener(this);
        bt_write_tag.setOnClickListener(this);
        ArrayAdapter adapter2 = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, storage_data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_storage.setAdapter(adapter2);
    }


    @Override
    public void onClick(View v) {
        byte[] accessPassword = Tools.HexString2Bytes(et_input_psw.getText()
                .toString());
        addr = Integer.valueOf(et_input_start_address.getText().toString());
        length = Integer.valueOf(et_input_length.getText().toString());
        switch (v.getId()) {
            case R.id.bt_read_single_tag:
                context.uhfManager.selectEPC(Tools.HexString2Bytes(select_epc));
                byte[] read_data = context.uhfManager.readFrom6C(select_storage, addr, length, accessPassword);
                String dataStr = Tools.Bytes2HexString(read_data, read_data.length);
                tv_result_uhf.append(dataStr + "\n");
                scrollToBottom(uhf_scrollview,tv_result_uhf);
                break;
            case R.id.bt_write_single_tag:
                context.uhfManager.selectEPC(Tools.HexString2Bytes(select_epc));
                String write = et_input_write_data.getText().toString().trim();
                byte[] dataBytes = Tools.HexString2Bytes(write);
                Log.e(TAG, "write =  " + write);
                Log.e(TAG, "addr =  " + addr);
                Log.e(TAG, "accessPassword =  " + Arrays.toString(accessPassword));
                if (context.uhfManager.writeTo6C(accessPassword, select_storage, addr, dataBytes.length / 2, dataBytes)) {
                    tv_result_uhf.append("写入成功\n");
                    scrollToBottom(uhf_scrollview,tv_result_uhf);
                } else {
                    tv_result_uhf.append("写入失败\n");
                    scrollToBottom(uhf_scrollview,tv_result_uhf);
                }
                break;
        }
    }
    public void scrollToBottom(final View scroll, final View inner) {

        Handler mHandler = new Handler();

        mHandler.post(new Runnable() {
            public void run() {
                if (scroll == null || inner == null) {
                    return;
                }
                int offset = inner.getMeasuredHeight() - scroll.getHeight();
                if (offset < 0) {
                    offset = 0;
                }
                scroll.scrollTo(0, offset);
            }
        });
    }
}
