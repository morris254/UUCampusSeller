package com.dreamspace.uucampusseller.ui.activity.Login;

import android.app.ProgressDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dreamspace.uucampusseller.R;
import com.dreamspace.uucampusseller.api.ApiManager;
import com.dreamspace.uucampusseller.common.SharedData;
import com.dreamspace.uucampusseller.common.utils.CommonUtils;
import com.dreamspace.uucampusseller.common.utils.NetUtils;
import com.dreamspace.uucampusseller.common.utils.PreferenceUtils;
import com.dreamspace.uucampusseller.model.api.LoginReq;
import com.dreamspace.uucampusseller.model.api.LoginRes;
import com.dreamspace.uucampusseller.model.api.UserInfoRes;
import com.dreamspace.uucampusseller.ui.MainActivity;
import com.dreamspace.uucampusseller.ui.base.AbsActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by money on 2015/10/25.
 */
public class LoginActivity extends AbsActivity {
    @Bind(R.id.Login_userName)
    EditText LoginUserName;
    @Bind(R.id.Login_pwd)
    EditText LoginPwd;
    @Bind(R.id.login_page_loginButton)
    Button loginPageLoginButton;
    @Bind(R.id.login_page_forget)
    TextView loginPageForget;
    @Bind(R.id.login_page_register)
    TextView loginPageRegister;

    ProgressDialog progressDialog;

    @Override
    protected int getContentView() {
        return R.layout.activity_login_page;
    }

    @Override
    protected void prepareDatas() {
        ButterKnife.bind(this);
    }

    @Override
    protected void initViews() {
        initListeners();
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("登录");
    }

    private void initListeners(){
        loginPageLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                测试
//                readyGoThenKill(MainActivity.class);
                String phoneNum = LoginUserName.getText().toString();
                String password = LoginPwd.getText().toString();
                if(isValid(phoneNum,password)){
                    LoginReq req = new LoginReq();
                    req.setPhone_num(phoneNum);
                    req.setPassword(password);
                    login(req);
                }
            }
        });

        loginPageForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyGo(FindBackActivity.class);
            }
        });

        loginPageRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyGo(RegisterActivity.class);
            }
        });
    }

    //登录操作
    private void login(LoginReq loginReq){
        progressDialog = ProgressDialog.show(this,"","正在登录",true,false);
        if(NetUtils.isNetworkConnected(this)){
            ApiManager.getService(this.getApplicationContext()).createAccessToken(loginReq,new Callback<LoginRes>(){
                @Override
                public void success(LoginRes loginRes, Response response) {
                    progressDialog.dismiss();
                    PreferenceUtils.putString(LoginActivity.this.getApplicationContext(),
                            PreferenceUtils.Key.ACCESS, loginRes.getAccess_token());
                    ApiManager.clear();

                    getUserInfo();
                }

                @Override
                public void failure(RetrofitError error) {
                    progressDialog.dismiss();
                    showInnerError(error);
                }
            });
        }else{
            progressDialog.dismiss();
            showNetWorkError();
        }
    }

    //获取用户信息
    private void getUserInfo(){
        ApiManager.getService(getApplicationContext()).getUserInfo(new Callback<UserInfoRes>() {

            @Override
            public void success(UserInfoRes userInfoRes, Response response) {
                if(userInfoRes != null){
                    Log.i("INFO", userInfoRes.toString());
                    saveUserInfo(userInfoRes);
                    progressDialog.dismiss();
                    showToast("登录成功~");
                    readyGoThenKill(MainActivity.class);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                progressDialog.dismiss();
                showInnerError(error);
            }
        });
    }

    //保存用户信息到本地
    private void saveUserInfo(UserInfoRes userInfoRes){
        SharedData.user = userInfoRes;
//        //这部分还需要修改~
//        PreferenceUtils.putString(getApplicationContext(),PreferenceUtils.Key.AVATAR,userInfoRes.getImage());
//        PreferenceUtils.putString(getApplicationContext(),PreferenceUtils.Key.ACCOUNT,userInfoRes.getName());
//        //PreferenceUtils.putString(getApplicationContext(),PreferenceUtils.Key.SEX,userInfoRes.getSex());
//        PreferenceUtils.putString(getApplicationContext(), PreferenceUtils.Key.PHONE, LoginUserName.getText().toString());
    }

    //输入有效性判断
    private boolean isValid(String phoneNum,String pwd){

        if(CommonUtils.isEmpty(phoneNum)){
            showToast("请输入您的手机号码");
            LoginUserName.requestFocus();
            return false;
        }
        if(phoneNum.length()!=11){
            showToast("请检查您的输入格式");
            LoginUserName.requestFocus();
            return false;
        }
        if(CommonUtils.isEmpty(pwd)){
            showToast("请输入您的密码");
            return false;
        }
        return true;
    }
}
