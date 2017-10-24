package com.tv.herelo.MyProfile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tv.herelo.R;
import com.tv.herelo.constants.Constant;
import com.tv.herelo.tab.MainTabActivity;

/**
 * Created by vikram on 28/9/15.
 */
public class ChangePasswordScreen extends Fragment implements View.OnClickListener{

    ImageView back_btn;
    TextView header_tv;
    ImageView next_btn;
    View view;


    EditText old_password_et;
    EditText new_password_et;
    EditText confrom_pass_et;

    ImageView sunmit_iv;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,	Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.change_password, container, false);

        LinearLayout headerLLMain = (LinearLayout) view.findViewById(R.id.headerLLMain);
        Constant.changeHeaderColor(headerLLMain);
//        screen = getArguments().get("Screen").toString();

        initView();
        onCLikListener();

        return view;
    }

    private void initView() {


        back_btn = (ImageView)view.findViewById(R.id.back_btn);
        next_btn  = (ImageView)view.findViewById(R.id.next_btn2);
        header_tv = (TextView)view.findViewById(R.id.header_tv);

        next_btn.setVisibility(View.GONE);
        header_tv.setText("Change Password");
        header_tv.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-LtEx.otf"));

        old_password_et = (EditText)view.findViewById(R.id.old_password_et);
        new_password_et = (EditText)view.findViewById(R.id.new_password_et);
        confrom_pass_et = (EditText)view.findViewById(R.id.confrom_pass_et);

        sunmit_iv = (ImageView)view.findViewById(R.id.sunmit_iv);

    }

    private void onCLikListener()
    {
        back_btn.setOnClickListener(this);
        sunmit_iv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {


        switch (v.getId())
        {
            case R.id.back_btn:

                MainTabActivity.backbutton();

                break;

            case R.id.sunmit_iv:

                break;
        }

    }
}
