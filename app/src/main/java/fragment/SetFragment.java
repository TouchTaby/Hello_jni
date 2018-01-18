package fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.hcpda.hello_jni.R;
import com.hcpda.hello_jni.activity.MainActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class SetFragment extends Fragment {
    Button bt_set;
    Spinner sp_power;
    Spinner sp_country_set;
    private  MainActivity contex;
    private String[] powerArr = {"26","24","20","18","17"};
    int power;
    private String[] countryArr = {"中国900MHz" , "中国800MHz" , "美国" , "欧洲" , "韩国"};
    ArrayAdapter<String> sp_country_adapter;//高亮是因为 private 修饰是全局变量 在别的地方没有用到变量，可以去掉private 也可以转为局部变量，如果有其他地方用到变量也不会高亮
    ArrayAdapter<String> sp_power_adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_set, container, false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        contex = (MainActivity) getActivity();
        bt_set = contex.findViewById(R.id.bt_set);
        sp_power = contex.findViewById(R.id.sp_power);
        sp_country_set = contex.findViewById(R.id.sp_country_set);
        sp_country_adapter = new ArrayAdapter<>(contex, android.R.layout.simple_dropdown_item_1line, countryArr);//工作区域设置适配器

        sp_country_set.setAdapter(sp_country_adapter);
        sp_power_adapter = new ArrayAdapter<>(contex, android.R.layout.simple_spinner_item, powerArr);//功率设置适配器
        sp_power_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_power.setAdapter(sp_power_adapter);
        sp_power.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        power = 26;
                        break;
                    case 1:
                        power =24;
                        break;
                    case 2:
                        power = 20;
                        break;
                    case 3:
                        power = 18;
                        break;
                    case 4:
                        power = 17;
                        break;
                    case 5:
                        power = 16;
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        bt_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (contex.uhfManager.setOutputPower(power)) {
                    Toast.makeText(contex,"设置"+power+"成功",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(contex,"设置"+power+"失败",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
