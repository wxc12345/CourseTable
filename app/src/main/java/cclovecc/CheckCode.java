package cclovecc;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CheckCode {

    public static byte[] gif;

    private CheckCode() throws InterruptedException {
        CheckCodeGetter getter = new CheckCodeGetter();
        getter.start();
        getter.join();
        gif = getter.gif;
    }

    public static void downloadCheckCode() throws InterruptedException {
        new CheckCode();
    }


    private class CheckCodeGetter extends Thread {
        private final String url = ZhengFangUrl.instance.URL_CHECKCODE;
        private byte[] gif;

        @Override
        public void run() {
            try {
                Connection.Response res = Jsoup.connect(url)
                        .cookie("ASP.NET_SessionId", ZhengFangCookie.ASP_NET_SessionId)
                        .referrer(ZhengFangUrl.instance.REFERER_CHECKCODE)
                        .ignoreContentType(true)
                        .method(Connection.Method.GET)
                        .execute();
                gif = res.bodyAsBytes();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Output gif as a file, this is a solution for console programme.
     * @param filePath Directory weren't created if not exists.
     */
    public static void outPutGifAsAFile(String filePath) throws IOException {
        File file = new File(filePath);//"C:\\Users\\sdfsfd\\Pictures\\checkcode.gif"
        FileOutputStream out = new FileOutputStream(file,false);
        out.write(gif);
        out.close();
    }
}
