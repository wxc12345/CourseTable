package ly.coursetable.demo;


import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import java.util.List;

import cclovecc.*;


public class autoImport extends AppCompatActivity {

    private ImageView codeImage;
    private Bitmap bitmapView;
    private EditText loginIDText;
    private EditText passwordText;
    private EditText codeText;
    private Button login;

    private CheckBox rememberPassword;
    private CheckBox honor;

    private SharedPreferences sp;
    private SharedPreferences.Editor spEditor;

    private MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_import);

        dbHelper = new MyDatabaseHelper(this, "BookStore.db", null, 1);

        codeImage = (ImageView)this.findViewById(R.id.view);
        loginIDText = (EditText)this.findViewById(R.id.loginID);
        passwordText = (EditText)this.findViewById(R.id.password);
        codeText = (EditText)this.findViewById(R.id.code);
        login = (Button)this.findViewById(R.id.login) ;

        rememberPassword = (CheckBox)this.findViewById(R.id.remember);
        honor = (CheckBox)this.findViewById(R.id.honor);

        Toast.makeText(getApplicationContext(), "先导入普通课程,再勾选至善/辅修导入特殊课程", Toast.LENGTH_LONG).show();

        sp = PreferenceManager.getDefaultSharedPreferences(this);

        if(sp.getBoolean("rember",false)){
            loginIDText.setText(""+sp.getString("account",""));
            passwordText.setText(""+sp.getString("password",""));
            rememberPassword.setChecked(true);
        }


//        codeImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getCodeView();
//                codeImage.setImageBitmap(bitmapView);
//                codeImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
//            }
//        });


        //登录操作
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)  {

                String account = loginIDText.getText().toString();
                if(honor.isChecked()){
                    account+="Z";
                }

                try{
                    startLogin(account);
                }catch (Exception e){

                }


            }
        });
    }

//    private void getCodeView(){
//        try {
//            CheckCodeDealer checkCodeDealer = CheckCodeDealer.newInstance();
//            bitmapView = checkCodeDealer.Bytes2Bimap(checkCodeDealer.getGif());
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }

    void saveCourses(List<Course> courses){

        spliteOperate SO;
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        if(!honor.isChecked()){
            db.delete("Book",null,null);
        }

        for(int i=0;i<courses.size();i++){
            SO = new spliteOperate(autoImport.this);
            SO.auto(courses.get(i));

        }
    }
    void judgeRemberPassword(){
        if(rememberPassword.isChecked()){
            spEditor = sp.edit();
            spEditor.putString("account",loginIDText.getText().toString());
            spEditor.putString("password",passwordText.getText().toString());
            spEditor.putBoolean("rember",true);
        }else {
            spEditor.clear();
        }
        spEditor.apply();
    }

    void startLogin(String account) {

        try{

//            Login.getInstance().setUserName(account)
//                    .setKey(passwordText.getText().toString());
            Login.getInstance().setUserName("1030616412").setKey("130732199711190031");
            LoginState loginState = Login.getInstance().login();
            switch (loginState) {
                case SUCCESS:{
                    CourseTable courseTable = MainPage.getInstance().getCourseTable();
//                    Thread.sleep(IDelayTime.NORMAL);
                    List<Course> courses = courseTable.getCourses();
                    saveCourses(courses);
                    Toast.makeText(getApplicationContext(), "或有缺课情况,\n请务必与教务系统核对", Toast.LENGTH_LONG).show();
                    judgeRemberPassword();

                    finish();
                    break;
                }

//                case CHECKCODE_ERROR:{
//                    Toast.makeText(getApplicationContext(), "验证码不正确", Toast.LENGTH_SHORT).show();
//
//                    codeText.setText("");
////                    getCodeView();
//                    codeImage.setImageBitmap(bitmapView);
//                    codeImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
//                    break;
//                }
                case PASSWORD_ERRER:{
                    Toast.makeText(getApplicationContext(), "密码错误", Toast.LENGTH_SHORT).show();
                    passwordText.setText("");
//                    getCodeView();
//                    codeImage.setImageBitmap(bitmapView);
//                    codeImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    break;
                }
                case USER_DOESNOT_EXIST:{
                    Toast.makeText(getApplicationContext(), "用户名不存在", Toast.LENGTH_SHORT).show();
//                    getCodeView();
//                    codeImage.setImageBitmap(bitmapView);
//                    codeImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    break;
                }

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
