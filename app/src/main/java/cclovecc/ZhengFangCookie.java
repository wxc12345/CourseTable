package cclovecc;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

public class ZhengFangCookie {
    public static String ASP_NET_SessionId;
    public static String __VIEWSTATE;

    public static boolean hasInit() {
        return __VIEWSTATE == null ? false : true;
    }

    private ZhengFangCookie() throws InterruptedException, IOException {
        CookieGetter getter = new CookieGetter();
        getter.start();
        getter.join();
        Connection.Response response = getter.getResponse();
        this.ASP_NET_SessionId = response.cookie("ASP.NET_SessionId");
        this.__VIEWSTATE = response.parse().select("input[name=__VIEWSTATE]").attr("value");
    }

    public static void initCookie() throws IOException, InterruptedException {
        new ZhengFangCookie();
    }

    private class CookieGetter extends Thread{
        private Connection.Response response;
        private final String url = ZhengFangUrl.instance.URL_LOGIN;
        @Override
        public void run() {
            try {
                Connection connection = Jsoup.connect(url);
                response = connection.method(Connection.Method.GET).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public Connection.Response getResponse() {
            return response;
        }
    }

}
