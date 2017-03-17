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
import com.mark.traveller.ftwy.R;
import com.mark.traveller.ftwy.bean.User;
import com.mark.traveller.ftwy.db.UserDao;
import com.mark.traveller.ftwy.utils.MobileNumberUtils;
import com.mark.traveller.ftwy.utils.ToastUtils;
import com.mark.traveller.ftwy.widget.ClearEditText;

import java.util.List;
import java.util.Random;

import static com.mark.traveller.ftwy.R.id.ui_register_to_login;

/**
 * Created by Mark on 2016/11/14 0014.
 */

public class RegisterUI extends Activity implements View.OnClickListener {

    public static final String EXTRA_PHONE = "com.mark.traveller.ftwy.ui.PHONE";

    private static final long DIALOG_DELAY = 1500;
    private TextView mToLoginTextView;
    private ClearEditText mPhoneNumberClearEditText;
    private ClearEditText mPasswordClearEditText;
    private ClearEditText mNameClearEditText;
    private Button mRegisterBtn;
    private User mUser;
    private UserDao mDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_register);

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
    }

    /**
     * 处理事件
     */
    private void initEvent() {
        // 去登录
        mToLoginTextView.setOnClickListener(this);

        // 点击注册
        mRegisterBtn.setOnClickListener(this);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        // 去登录
        mToLoginTextView = (TextView) findViewById(ui_register_to_login);

        // 电话号码
        mPhoneNumberClearEditText = (ClearEditText) findViewById(R.id.et_register_phonenumber);
        // 注册密码
        mPasswordClearEditText = (ClearEditText) findViewById(R.id.et_register_password);
        // 注册姓名
        mNameClearEditText = (ClearEditText) findViewById(R.id.et_register_name);

        // 注册
        mRegisterBtn = (Button) findViewById(R.id.btn_register_register);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        final ProgressDialog dialog = ProgressDialog.show(this, "提示", "正在注册...");

        if (id == ui_register_to_login) {
            Intent intent = new Intent(RegisterUI.this, LoginUI.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.btn_register_register) {
            // dialog.show();

            // 电话号码
            final String phoneNumber = mPhoneNumberClearEditText.getText().toString().trim();
            // 密码
            final String password = mPasswordClearEditText.getText().toString().trim();
            // 姓名
            final String name = mNameClearEditText.getText().toString().trim();

            // 遍历所有数据库中的数据
            List<User> users = mDao.findAll();
            for (int i = 0; i < users.size(); i++) {
                mUser = users.get(i);
            }

            // 判断
            if (TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(password) || TextUtils.isEmpty(name)) {

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        dialog.dismiss();
                        ToastUtils.showToast(RegisterUI.this, "不能为空");
                        return;
                    }
                }, DIALOG_DELAY);

            } else if (!MobileNumberUtils.isMobileNumber(phoneNumber)) {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        dialog.dismiss();
                        ToastUtils.showToast(RegisterUI.this, "请输入正确的手机号码");
                        return;
                    }
                }, DIALOG_DELAY);

            } else if (phoneNumber.equals(mUser.getPhone())) {
                // 手机号码已注册
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        ToastUtils.showToast(RegisterUI.this, "该手机号码已注册");
                        return;
                    }
                }, DIALOG_DELAY);

            } else {
                // 预留短信验证模块

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 保存数据到数据库,跳转至登录(该处的业务逻辑可以更改)
                        Random random = new Random();
                        int ID = random.nextInt(1000000) + 1;

                        // 设置数据
                        mUser.setId(ID);
                        mUser.setPhone(phoneNumber);
                        mUser.setPassword(password);
                        mUser.setName(name);

                        // 保存数据到数据库
                        mDao.insert(mUser);

                        dialog.dismiss();
                        ToastUtils.showToast(RegisterUI.this, "注册成功");

                        // 跳转
                        Intent intent = new Intent(RegisterUI.this, LoginUI.class);
                        intent.putExtra(EXTRA_PHONE, phoneNumber);
                        startActivity(intent);
                        finish();
                    }
                }, DIALOG_DELAY);

            }

        }

    }


}
