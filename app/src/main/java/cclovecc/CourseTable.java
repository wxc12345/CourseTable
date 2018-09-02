package cclovecc;

import android.util.Log;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class CourseTable {
    Document courseHtml;

    private static final String TAG = "CourseTable";
    private Element table;
    public Element getTable() {
        return table;
    }

    private int term;
    public int getTerm() {
        return term;
    }
    private int year;
    private String fmtYear(int year){
        return String.format("%d-%d", year, year + 1);
    }
    public String getYear() {
        return fmtYear(year);
    }

    public String __VIEWSTATE;

    String url = null;
    public CourseTable(String url) throws InterruptedException {
        this.url = url;
        CourseGetter getter = new CourseGetter();
        getter.start();
        getter.join();
        this.courseHtml = getter.courseHtml;
        getInfoFromHtml();
    }

    private CourseTable(CourseTable lastTable, int yearOrTerm) throws IOException, InterruptedException {
        url = lastTable.url;
        __VIEWSTATE = lastTable.__VIEWSTATE;
        if (yearOrTerm > 1000) {
            term = lastTable.term;
            year = yearOrTerm;
        } else {
            year = lastTable.year;
            term = yearOrTerm;
        }
        Log.d(TAG, "CourseTable: " + year + term);
        CourseGetter getter = new CourseGetter(yearOrTerm);
        getter.start();
        getter.join();
        this.courseHtml = getter.courseHtml;
        getInfoFromHtml();
    }

    private class CourseGetter extends Thread {
        private int yearOrTerm = -1;

        public CourseGetter() {
        }

        public CourseGetter(int termOrYear) throws IOException {
            this.yearOrTerm = termOrYear;
        }

        private Document courseHtml;
        @Override
        public void run() {
            Connection connection = Jsoup.connect(url);
            try {
                //rebiuld this part
                //at 2018.1.5 evening
                if (yearOrTerm < 0) {
                    connection.cookie("ASP.NET_SessionId", ZhengFangCookie.ASP_NET_SessionId)
                            .referrer(ZhengFangUrl.instance.REFERER__MAINPAGE_BASE + Login.getInstance().getUserName());
                    courseHtml = connection.get();
                } else {
                    connection.cookie("ASP.NET_SessionId",ZhengFangCookie.ASP_NET_SessionId)
                            .referrer(ZhengFangUrl.instance.REFERER__MAINPAGE_BASE + Login.getInstance().getUserName());
                    HashMap<String, String> data = new HashMap<>();
                    data.put("__EVENTARGUMENT", "");
                    data.put("__VIEWSTATE", __VIEWSTATE);
                    if (yearOrTerm < 1000) {
                        data.put("__EVENTTARGET", "xqd");
                        data.put("xqd", String.valueOf(yearOrTerm));
//                        data.put("xnd", fmtYear(year));
                    }else {
                        data.put("__EVENTTARGET", "xnd");
                        data.put("xnd", fmtYear(yearOrTerm));
//                        data.put("xqd", String.valueOf(term));
                    }
                    connection.data(data);
                    courseHtml = connection.post();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void getInfoFromHtml() {
        table = courseHtml.getElementById("Table1");

        __VIEWSTATE = courseHtml.select ("input[name=__VIEWSTATE]").attr("value");

        term = Integer.parseInt(
                courseHtml.select("select[name=xqd]")
                        .first()
                        .selectFirst("option[selected=selected]")
                        .attr("value")
        );

        year = Integer.parseInt(
                courseHtml.select("select[name=xnd]")
                        .first()
                        .selectFirst("option[selected=selected]")
                        .attr("value")
                        .split("[-]")[0]
        );
    }

    /**
     *
     * @param year 输入第一年的年份，如果是2017-2018，就输入2017
     * @return 课程表啦
     * @throws IOException
     * @throws InterruptedException
     */
    public CourseTable changeYear(int year) throws Exception {
        if (this.year == year) {
            return this;
        }
        if (year < 2000) {
            throw new Exception("我知道你又在玩我的程序");
        }
        return new CourseTable(this, year);
    }

    public CourseTable changeTerm(int term) throws Exception {
        if (this.term == term) {
            return this;
        }
        if (term <= 0 || term > 3) {
            throw new Exception("我知道你又在玩我的程序");
        }
        return new CourseTable(this, term);
    }

    public ArrayList<Course> getCourses(){
        ArrayList<Course> courses = new ArrayList<>();
        Elements trs = table.select("tr");
        int[] rows = {2, 4, 7, 9, 11};
        int[] adds = {2, 1, 2, 1, 2};
        for (int i = 0; i < 5; i++) {
            Elements tds = trs.get(rows[i]).select("td");
            for (int j=0,s=adds[i];j<7;j++) {
                Element e = tds.get(j + s);
                String txt = e.text();
                if (!txt.equals("")) {
                    j++;
                    courses.add(new Course(j, rows[i], txt));
                    j--;
                }
            }
        }
        return courses;

    }

}
