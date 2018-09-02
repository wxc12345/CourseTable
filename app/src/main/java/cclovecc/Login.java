package cclovecc;

import android.util.Log;

import org.jsoup.nodes.Document;

import static android.content.ContentValues.TAG;

public class Login {
    private String userName;
    private String key;
    private String checkCode;

    private Login() { }

    private static Login instance;
    public static Login getInstance() {
        if (instance == null)
            instance = new Login();
        return instance;
    }

    public Login setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public Login setKey(String key) {
        this.key = key;
        return this;
    }

    public Login setCheckCode(String checkCode) {
        this.checkCode = checkCode;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public LoginState login()  {
        LoginRequest request = new LoginRequest(userName, key, checkCode);
        Document mainPage;
        String mainPageText = "";
        try {
            request.start();
            request.run();
            mainPage = request.getMainPage();
            mainPageText= mainPage.toString();
        }catch (Exception ie){

        }
        Log.d(TAG, "学号" + userName + "身份证号" + key + "\nmainPageText" + mainPageText);

        if (mainPageText.indexOf("验证码不正确")!=-1) {
            MainPage.getInstance().setMainPage(null);
            return LoginState.CHECKCODE_ERROR;
        } else if (mainPageText.indexOf("密码错误") != -1) {
            MainPage.getInstance().setMainPage(null);
            return LoginState.PASSWORD_ERRER;
        } else if (mainPageText.indexOf("用户名不存在") != -1) {
            MainPage.getInstance().setMainPage(null);
            return LoginState.USER_DOESNOT_EXIST;
        } else {
            MainPage.getInstance().setMainPage(request.getMainPage());
            return LoginState.SUCCESS;
        }
    }
}
