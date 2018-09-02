package ly.coursetable.demo;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cclovecc.*;

public class LoginActivity extends AppCompatActivity {

    Button mBtnLogin;
    TextInputLayout mUsernameWrapper, mKeyWrapper;
    ProgressBar progressBar;
    private CheckBox remberPassword;
    private CheckBox honor;


    private MyDatabaseHelper dbHelper;
    private SharedPreferences sp;
    private SharedPreferences.Editor spEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("Log in ♪");
        /**
         * 获取控件
         */
        mBtnLogin = (Button) findViewById(R.id.button_login);
        mUsernameWrapper = (TextInputLayout) findViewById(R.id.usernameWrapper);
        mKeyWrapper = (TextInputLayout) findViewById(R.id.keyWrapper);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        honor = (CheckBox) findViewById(R.id.honor);
        remberPassword = (CheckBox)findViewById(R.id.remember);
        dbHelper = new MyDatabaseHelper(this, "BookStore.db", null, 1);

        /**
         * 权限判断
         */

//        PackageManager pm = getPackageManager();
//        boolean permission = (PackageManager.PERMISSION_GRANTED ==
//                pm.checkPermission("android.permission.INTERNET", "com.wjiangnan.stasct"));
//        if (!permission) {
//            Toast.makeText(this, "无INTERNET权限", Toast.LENGTH_SHORT).show();
//            mBtnLogin.setEnabled(false);
//            mBtnLogin.setText("( •̥́ ˍ •̀ू )");
//            return;
//        }

        /**
         * 初始化按键和输入框事件
         */
        mBtnLogin.setOnClickListener(new LoginOnClickListener());

        TextWatcher watcher=new EditTextWatch();
        mUsernameWrapper.getEditText().addTextChangedListener(watcher);
        mKeyWrapper.getEditText().addTextChangedListener(watcher);
        mKeyWrapper.getEditText().setOnEditorActionListener(new EditTextEnterListener());

        honor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(honor.isChecked()){
                    Toast.makeText(LoginActivity.this, "记得改学号后缀", Toast.LENGTH_SHORT).show();
                }
            }
        });


        Toast.makeText(LoginActivity.this, "要连接校园网哦", Toast.LENGTH_SHORT).show();
        /**
         * 记住密码
         */
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        if(sp.getBoolean("rember",false)){
            mUsernameWrapper.getEditText().setText(""+sp.getString("account",""));
            mKeyWrapper.getEditText().setText(""+sp.getString("password",""));
            remberPassword.setChecked(true);
        }

        /**
         * 初始化Cookie
         */
        getCookieAsyc("JNUD5");


    }

    /**
     * 获取Cookie
     * @param schoolName:
     */
    private void getCookieAsyc(String schoolName) {
        GetCookieTask getCookieTask = new GetCookieTask();
        getCookieTask.execute("JNUD5");
    }
    /**
     * 异步类-获取cookie
     */
    public class GetCookieTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {//请求时禁用按钮
            super.onPreExecute();
            mBtnLogin.setEnabled(false);
            mBtnLogin.setEnabled(false);
            mBtnLogin.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            try {
                ZhengFangUrlFactory.initUrl(strings[0]);
                ZhengFangCookie.initCookie();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean o) {
            super.onPostExecute(o);
            progressBar.setVisibility(View.INVISIBLE);
            if (o == false) {
                mBtnLogin.setText("( •̥́ ˍ •̀ू ) AIRJ?Retry~");
                mBtnLogin.setEnabled(true);
                mBtnLogin.setOnClickListener(new GetCookieOnClickListener());//----------------------
            } else {
                mBtnLogin.setEnabled(true);
                mBtnLogin.setText("LOGIN");
                mBtnLogin.setOnClickListener(new LoginOnClickListener());
            }
        }
    }

    /**
     * 异步类-登录
     */
    private class ZhengfangLoginTask extends AsyncTask<Void, Void, LoginState> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            mBtnLogin.setText("ヾ(❀╹◡╹)ﾉ~ ...");
            mBtnLogin.setClickable(false);//请求时禁用button
            Login.getInstance().setUserName(mUsernameWrapper.getEditText().getText().toString())
                    .setKey(mKeyWrapper.getEditText().getText().toString());
        }

        @Override
        protected LoginState doInBackground(Void... voids) {
            LoginState state = null;
            try {
                state = Login.getInstance().login();
            } catch (Exception e) {
                e.printStackTrace();
//                return LoginState.UNKNOWN_ERROR;
            }
            return state == null ? LoginState.UNKNOWN_ERROR : state;
        }

        @Override
        protected void onPostExecute(LoginState loginState) {
            super.onPostExecute(loginState);
            progressBar.setVisibility(View.INVISIBLE);
            mBtnLogin.setText("LOGIN");
            mBtnLogin.setClickable(true);
            switch (loginState) {
                case SUCCESS:{
                    Toast.makeText(LoginActivity.this, "Yeal!!", Toast.LENGTH_SHORT).show();

                    CourseTable courseTable;
                    List<Course> courses;
                    try{
                        courseTable = MainPage.getInstance().getCourseTable();
                        courses = courseTable.getCourses();
                        saveCourses(courses);
                    }catch(Exception e){

                    }
                    Toast.makeText(getApplicationContext(), "或有缺课情况,\n请务必与教务系统核对", Toast.LENGTH_LONG).show();
                    judgeRemberPassword();
                    finish();
                    return;
                }
                case UNKNOWN_ERROR:{
                    Toast.makeText(LoginActivity.this, "UNKNOWN EXCEPTION", Toast.LENGTH_SHORT).show();
                    return;
                }
                case PASSWORD_ERRER:{
                    Toast.makeText(LoginActivity.this, "PASSWORD ERROR", Toast.LENGTH_SHORT).show();
                    mKeyWrapper.setError("密码错误");
                    mKeyWrapper.requestFocus();
                    return;
                }
                case CHECKCODE_ERROR:{
                    Toast.makeText(LoginActivity.this, "CHECKCODE ERROR", Toast.LENGTH_SHORT).show();
                    return;
                }
                case USER_DOESNOT_EXIST:{
                    Toast.makeText(LoginActivity.this, "USERNAME ERROR", Toast.LENGTH_SHORT).show();
                    mUsernameWrapper.setError("用户名不存在");
                    mUsernameWrapper.requestFocus();
                    return;
                }
            }
        }
    }

    /**
     * 登陆按键监听
     */
    class LoginOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            hideKeyboard();
            String username = mUsernameWrapper.getEditText().getText().toString();
            if (username.equals("")) {
                mUsernameWrapper.setError("♪（＾∀＾●）");
                mUsernameWrapper.requestFocus();
                return;
            }
            String password = mKeyWrapper.getEditText().getText().toString();
            if (password.equals("")) {
                mKeyWrapper.setError("♪（＾∀＾●）");
                mKeyWrapper.requestFocus();
                return;
            }

            ZhengfangLoginTask loginTask = new ZhengfangLoginTask();
            loginTask.execute();
        }
    }

    /**
     * 重新获得cookie按键监听
     */
    class GetCookieOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            getCookieAsyc("JNUD5");
        }
    }

    /**
     * 文本框事件监听
     */
    class EditTextWatch implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s == mUsernameWrapper.getEditText().getText()) {
                mUsernameWrapper.setError(null);
            } else if (s == mKeyWrapper.getEditText().getText()) {
                mKeyWrapper.setError(null);
            }
        }
    }

    /**
     * 监听回车键
     */
    class EditTextEnterListener implements TextView.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            hideKeyboard();
            if (actionId == EditorInfo.IME_ACTION_GO
                    && mBtnLogin.isEnabled()
                    && ZhengFangCookie.hasInit()){
                new LoginOnClickListener().onClick(mBtnLogin);
            }
            return true;
        }
    }

    /**
     * 隐藏键盘
     */
    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    final String TAG = "LoginActivity";

    void saveCourses(List<Course> courses){
        spliteOperate SO;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if(!honor.isChecked()){
            db.delete("Book",null,null);
        }

        for(int i=0;i<courses.size();i++){
            SO = new spliteOperate(this);
            SO.auto(courses.get(i));

        }
    }

    void judgeRemberPassword(){
        if(remberPassword.isChecked()){
            spEditor = sp.edit();
            spEditor.putString("account",mUsernameWrapper.getEditText().getText().toString());
            spEditor.putString("password",mKeyWrapper.getEditText().getText().toString());
            spEditor.putBoolean("rember",true);
        }else {
            spEditor.clear();
        }
        spEditor.apply();
    }
}
