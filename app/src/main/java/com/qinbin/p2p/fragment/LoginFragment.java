package com.qinbin.p2p.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.qinbin.p2p.R;
import com.qinbin.p2p.bean.AccountInfo;
import com.qinbin.p2p.bean.request.LoginRequest;
import com.qinbin.p2p.bean.response.LoginResponse;
import com.qinbin.p2p.constant.UserConstant;
import com.qinbin.p2p.net.BaseRequest;
import com.qinbin.p2p.net.SuccessCallback;
import com.qinbin.p2p.widget.StateLayout;

/**
 * Created by teacher on 2016/5/5.
 */
public class LoginFragment extends BaseFragment {


    @Override
    protected int getContentLayoutRes() {
        return R.layout.fragment_login;
    }

    EditText userNameTv;
    EditText passWordTv;
    Button loginBtn;

    @Override
    protected void initView(View childView, TextView titleTv) {
        titleTv.setText("请登录");

        userNameTv = (EditText) childView.findViewById(R.id.fragment_login_username);
        passWordTv = (EditText) childView.findViewById(R.id.fragment_login_password);
        loginBtn = (Button) childView.findViewById(R.id.fragment_login_login);

        loginBtn.setOnClickListener(loginOcl);
    }

    @Override
    protected void initData() {
        getStateLayout().setState(StateLayout.STATE_SUCCESS);
    }


    private View.OnClickListener loginOcl = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // 点击登陆，不能直接发送信息到服务器
            // 应该先在本地进行检查
            boolean canSendToServer = checkInput();
            if (canSendToServer) {
                //TODO
//                Toast.makeText(userNameTv.getContext(),"登陆成功",Toast.LENGTH_SHORT).show();
                sendRequest(new LoginRequest(getText(userNameTv), getText(passWordTv)), LoginResponse.class, successCallback);
            }
        }
    };

    private boolean checkInput() {
        String userName = getText(userNameTv);
        String passWord = getText(passWordTv);
        if (TextUtils.isEmpty(userName)) {
            // 可以进行更复杂的判断逻辑
            Toast.makeText(userNameTv.getContext(), "用户名不能为空！", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(passWord)) {
            Toast.makeText(userNameTv.getContext(), "密码不能为空！", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private String getText(EditText editText) {
        return editText.getText().toString();
    }

    private SuccessCallback<LoginResponse> successCallback = new SuccessCallback<LoginResponse>() {
        @Override
        public void onSuccess(BaseRequest request, LoginResponse resp) {
            // 判断是否登陆成功
            AccountInfo account = resp.getData().account;
            boolean success = account != null;
            if (success) {
                UserConstant.userName = account.userName;
                UserConstant.name = account.name;
                UserConstant.token = account.token;
                UserConstant.header = account.header;

                Toast.makeText(userNameTv.getContext(),"登陆成功",Toast.LENGTH_SHORT).show();
                getActivity().finish();
            } else {
                Toast.makeText(userNameTv.getContext(), resp.getData().msg, Toast.LENGTH_SHORT).show();
            }
        }
    };
}
