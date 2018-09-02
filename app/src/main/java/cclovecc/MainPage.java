package cclovecc;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.List;

public class MainPage {
    private MainPage() { }

    private Document mainPage;

    public void setMainPage(Document page) {
        this.mainPage = page;
    }

    public Document getMainPage() {
        return mainPage;
    }

    private static MainPage instance;

    public static MainPage getInstance() {
        if (instance==null)
            instance = new MainPage();
        return instance;
    }

    public CourseTable getCourseTable() throws InterruptedException {
//            Element table = CourseTable.getInstance(ZhengFangUrl.instance.URL_BASE + mainPage.select("a[onclick=GetMc('学生个人课表');]").first().attr("href"),2).getTable();
            CourseTable c = new CourseTable(ZhengFangUrl.instance.URL_BASE + mainPage.select("a[onclick=GetMc('学生个人课表');]").first().attr("href"));
            return c;
    }

}
