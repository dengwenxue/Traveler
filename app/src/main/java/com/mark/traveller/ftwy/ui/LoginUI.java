package com.mark.traveller.ftwy.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mark.traveller.ftwy.FTWYApplication;
import com.mark.traveller.ftwy.MainActivity;
import com.mark.traveller.ftwy.R;
import com.mark.traveller.ftwy.bean.User;
import com.mark.traveller.ftwy.db.UserDao;
import com.mark.traveller.ftwy.utils.MobileNumberUtils;
import com.mark.traveller.ftwy.utils.ToastUtils;
import com.mark.traveller.ftwy.widget.ClearEditText;

import java.util.ArrayList;
import java.util.List;

/**
 * 登录界面的实现
 * Created by Mark on 2016/11/14 0014.
 * <p>
 * 该部分保留将数据库放在服务器的需求
 */

public class LoginUI extends Activity implements View.OnClickListener {

    private static final long DIALOG_DELAY = 1500;
    private TextView mToRegisterUI;
    private ClearEditText mPhoneClearEditText;
    private ClearEditText mPasswordClearEditText;
    private Button mLoginBtn;
    private User mUser;
    private UserDao mDao;
    private List<User> mUsers;
    private List<String> mPhones;// 数据库中所有的电话号码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_login);

        initView();

        initData();

        initEvent();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mUser = new User();
        mDao = UserDao.getInstance(FTWYApplication.mContext);
        mPhones = new ArrayList<>();

        // 查询所有的记录,并将所有的电话号码添加到List集合
        mUsers = mDao.findAll();
        if (mUsers.size() > 0) {
            for (int i = 0; i < mUsers.size(); i++) {
                String result = mUsers.get(i).getPhone();
                mPhones.add(result);
            }
        } else {
            ToastUtils.showToast(LoginUI.this, "您还没注册驴友账号,请先注册...");
        }

        // 接受注册的手机号码
        Intent intent = getIntent();
        String phoneFromRegister = intent.getStringExtra(RegisterUI.EXTRA_PHONE);

        // 初始化数据
        if (TextUtils.isEmpty(phoneFromRegister)) {
            // 获取到最后一个手机号码
            if (mUsers.size() > 0) {
                String phone = mUsers.get(mUsers.size() - 1).getPhone();
                mPhoneClearEditText.setText(phone);
            }

        } else {
            mPhoneClearEditText.setText(phoneFromRegister);
        }

    }

    /**
     * 事件的处理
     */
    private void initEvent() {
        // 去注册
        mToRegisterUI.setOnClickListener(this);

        // 登录
        mLoginBtn.setOnClickListener(this);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        // 转注册
        mToRegisterUI = (TextView) findViewById(R.id.ui_login_to_register);

        // 手机和密码
        mPhoneClearEditText = (ClearEditText) findViewById(R.id.et_login_phone);
        mPasswordClearEditText = (ClearEditText) findViewById(R.id.et_login_password);

        // 登录
        mLoginBtn = (Button) findViewById(R.id.btn_login_login);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.ui_login_to_register) {
            // 去注册
            Intent intent = new Intent(LoginUI.this, RegisterUI.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.btn_login_login) {
            // 登录
            String phoneNumber = mPhoneClearEditText.getText().toString().trim();
            String password = mPasswordClearEditText.getText().toString().trim();
            final ProgressDialog dialog = ProgressDialog.show(this, "提示", "正在登录...");

            // 判断输入的手机号码是否存在于数据库中


            // 查询手机号码行的数据
            mUser = mDao.find(phoneNumber);

            // 判断
            if (TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(password)) {
                // 手机号码或密码为空

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        ToastUtils.showToast(LoginUI.this, "手机号或密码不能为空,请重新输入");
                        return;
                    }
                }, DIALOG_DELAY);
            } else if (!MobileNumberUtils.isMobileNumber(phoneNumber)) {
                // 不是标准的手机号码
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        ToastUtils.showToast(LoginUI.this, "请输入正确手机号码");
                        return;
                    }
                }, DIALOG_DELAY);
            } else if (!isDatabasePhone(phoneNumber)) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        // 该手机号未注册
                        ToastUtils.showToast(LoginUI.this, "该手机号未注册,请先注册驴友账号");
                        return;
                    }
                }, DIALOG_DELAY);
            } else if (!password.equals(mUser.getPassword())) {

                // 密码不正确
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        ToastUtils.showToast(LoginUI.this, "请输入正确的手机号和密码");
                        return;
                    }
                }, DIALOG_DELAY);
            } else {
                // 登录成功
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        ToastUtils.showToast(LoginUI.this, "登录成功");

                        // 跳转至主页
                        Intent intent = new Intent(LoginUI.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, DIALOG_DELAY);

            }

        }
    }

    /**
     * @param phoneStr
     * @return
     */
    private boolean isDatabasePhone(String phoneStr) {
        boolean result = false;

        for (String phone : mPhones) {
            if (mPhones.contains(phoneStr)) {
                result = true;
            }
        }

        return result;
    }
}
