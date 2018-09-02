package cclovecc;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;

public class LoginRequest extends Thread {
    private final String url = ZhengFangUrl.instance.URL_LOGIN_REQUEST;//IUrls.URL_LOGIN_REQUEST;

    private String userName;
    private String key;
    private String checkCode;
    public LoginRequest(String userName, String key, String checkCode) {
        this.userName = userName;
        this.key = key;
        this.checkCode = checkCode;
    }

    @Override
    public void run() {
        try {
            Connection connection = Jsoup.connect(url);
            connection.cookie("ASP.NET_SessionId", ZhengFangCookie.ASP_NET_SessionId)
                    .referrer(ZhengFangUrl.instance.REFERER_LOGIN_REQUEST)
                    .header("Upgrade-Insecure-Requests", "1")
                    .header("Content-Type", "application/x-www-form-urlencoded");
            HashMap<String, String> data = new HashMap<>();
            data.put("__VIEWSTATE", ZhengFangCookie.__VIEWSTATE);
            data.put("TextBox1", userName);
            data.put("TextBox2", key);
            if (checkCode != null) {
                data.put("TextBox3", checkCode);
            }
            data.put("RadioButtonList1", "%D1%A7%C9%FA");
            data.put("Button1", "");
            data.put("lbLanguage", "");
            connection.data(data);
            mainPage = connection.post();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Document mainPage;

    public Document getMainPage() {
        return mainPage;
    }
}
