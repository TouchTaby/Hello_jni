package fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.hcpda.hello_jni.R;
import com.hcpda.hello_jni.activity.MainActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReadTagFragment extends Fragment implements View.OnClickListener{

    Spinner sp_lock_epc_select;
    private MainActivity context;
    private String select_epc = "";
    Button bt_lock_tag;
    Button bt_kill_tag;
    private EditText et_psw;
    Spinner sp_lock_type;
    String[] lockArr = {"开放","锁定","永久开放","永久锁定"};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tag, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        context = (MainActivity) getActivity();
//        sp_lock_epc_select = context.findViewById(R.id.sp_select_epc_fragment_tag);
//        ArrayAdapter adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, context.listepc);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        sp_lock_epc_select.setAdapter(adapter);
//        bt_kill_tag = (Button) fv(R.id.bt_kill_tag);
//        sp_lock_type = (Spinner) fv(R.id.sp_lock_type);
//        bt_lock_tag = (Button) fv(R.id.bt_lock_tag);
//        et_psw = (EditText) fv(R.id.et_psw_lock);
//        bt_lock_tag.setOnClickListener(this);
//        bt_kill_tag.setOnClickListener(this);
//        ArrayAdapter adapter1 = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item,lockArr);
//        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        sp_lock_type.setAdapter(adapter1);
//        sp_lock_epc_select.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
////                select_epc = context.listepc.get(position);
//        }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
    }
//    public View fv(int id){
//        return context.findViewById(id);
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_kill_tag:
//                context.uhfManager.selectEPC(Tools.HexString2Bytes(select_epc));
//                String psw = et_psw.getText().toString().trim();
//                if (context.uhfManager.kill6C(Tools.HexString2Bytes(psw))){
//                    Toast.makeText(context,"锁定成功！",Toast.LENGTH_LONG).show();
//                }else {
//                    Toast.makeText(context,"锁定失败！",Toast.LENGTH_LONG).show();
//                }
                break;
            case R.id.bt_lock_tag:
//                context.uhfManager.selectEPC(Tools.HexString2Bytes(select_epc));
//                String psw1 = et_psw.getText().toString().trim();
//                if (context.uhfManager.lock6C(Tools.HexString2Bytes(psw1),1,1)){
//                    Toast.makeText(context,"锁定成功！",Toast.LENGTH_LONG).show();
//                }else {
//                    Toast.makeText(context,"锁定失败！",Toast.LENGTH_LONG).show();
//                }
                break;
        }
    }
}
