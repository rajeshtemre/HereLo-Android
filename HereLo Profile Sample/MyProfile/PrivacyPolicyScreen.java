package com.tv.herelo.MyProfile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tv.herelo.R;
import com.tv.herelo.constants.Constant;
import com.tv.herelo.tab.MainTabActivity;

/**
 * Created by vikram on 28/9/15.
 */
public class PrivacyPolicyScreen extends Fragment implements View.OnClickListener{

        ImageView back_btn;
        TextView header_tv;
        ImageView next_btn;

        private TextView data_tv;

        View view;



        private String screen;

        public View onCreateView(LayoutInflater inflater, ViewGroup container,	Bundle savedInstanceState) {

                view = inflater.inflate(R.layout.privacy_policy, container, false);


                screen = getArguments().get("Screen").toString();
                LinearLayout headerLLMain = (LinearLayout) view.findViewById(R.id.headerLLMain);
                Constant.changeHeaderColor(headerLLMain);
                initView();
                onCLikListener();

                return view;
        }

        private void initView() {


                back_btn = (ImageView)view.findViewById(R.id.back_btn);
                next_btn  = (ImageView)view.findViewById(R.id.next_btn2);
                header_tv = (TextView)view.findViewById(R.id.header_tv);

                data_tv = (TextView)view.findViewById(R.id.data_tv);

                next_btn.setVisibility(View.GONE);

                if(screen.equalsIgnoreCase("Privacy Policy"))
                {
                        header_tv.setText("Privacy Policy");
                }
                else {
                        header_tv.setTextSize(16);
                        header_tv.setText("Terms & Conditions");
                }

                //fonts of the textview START

                data_tv.setTypeface(Constant.typeface(getActivity(),"HelveticaNeueLTStd-LtEx.otf"));
                header_tv.setTypeface(Constant.typeface(getActivity(),"HelveticaNeueLTStd-LtEx.otf"));

                //fonts of the textview ENDS

        }

        private void onCLikListener()
        {
                back_btn.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {


                switch (v.getId())
                {
                        case R.id.back_btn:

                                MainTabActivity.backbutton();

                                break;


                }

        }
}
