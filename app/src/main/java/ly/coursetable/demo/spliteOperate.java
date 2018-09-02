package ly.coursetable.demo;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.SQLData;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cclovecc.Course;

import static android.content.ContentValues.TAG;

/**
 * Created by happts on 2017/8/12.
 */

public class spliteOperate {

    private MyDatabaseHelper dbHelper ;

    private String name;
    private int total;
    private int startTime;
    private int firstWeek;
    private int lastWeek;
    private int each;
    private String teacher;
    private String address;
    private int week;

    spliteOperate(Context context){
        this.dbHelper = new MyDatabaseHelper(context,"BookStore.db", null, 1);
    }


    void add(String name,int total,int startTime,int firstWeek,int lastWeek,int each,String teacher,String address,int week){
        this.name = name;
        this.total = total;
        this.startTime =startTime;
        this.firstWeek = firstWeek;
        this.lastWeek = lastWeek;
        this.each = each;
        this.teacher = teacher;
        this.address = address;
        this.week = week;

    }

    void auto(Course oneCourse){
        boolean test = true;
        String pattern3 = "(\\S+)\\s\\S+\\s\\{第(\\d*)-(\\d*)周\\|(\\d)节\\S周\\S\\s(\\S+)\\s(\\S+)";
                        //1.课程                  2.起始周 3，结束周  4.总节数          5.老师    6.地点
                        // (\S+)\s\S+\s\{第(\d*)-(\d*)周\|(\d)节\S周\S\s(\S+)\s(\S+)
        String pattern0 = "(\\S+)\\s\\S+\\s周[一二三四五六日]第((?:\\d+,)*+(\\d+))节\\{第(\\d+)-(\\d+)周(?:\\|(.?)周)?\\S\\s(\\S+)\\s?(\\S+)?";
                        //  1.课程1                                2.总节数 3.结束节      4.起始周 5.结束周 6.单双周   7.老师  8.地点

        Pattern r0 = Pattern.compile(pattern0);
        Matcher m0 = r0.matcher(oneCourse.getLine());
        //// TODO: 2018/1/6  
        Log.d(TAG,oneCourse.getLine());
        while (m0.find()){
            test = false;
            int total = getTotal(m0.group(2)+"");
            int startTime = Integer.parseInt(m0.group(3)) - total +1;
            int each;
            switch (m0.group(6)+""){
                case "单":each = 1;break;
                case "双":each = 0;break;
                default:each=2;break;
            }
            add(m0.group(1),total,startTime,Integer.parseInt(m0.group(4)),Integer.parseInt(m0.group(5)),each,m0.group(7),m0.group(8),oneCourse.getJ());
            saveOrinsert();
        }

        if(test){
            Pattern r3 = Pattern.compile(pattern3);
            Matcher m3 = r3.matcher(oneCourse.getLine());
            if(m3.find()){
                int total = Integer.parseInt(m3.group(4));
                add(m3.group(1),total,oneCourse.getRow()-1,Integer.parseInt(m3.group(2)),Integer.parseInt(m3.group(3)),2,m3.group(5),m3.group(6),oneCourse.getJ());
                saveOrinsert();
            }
        }
    }

    void saveOrinsert(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("teacher", teacher);
        values.put("first",firstWeek);
        values.put("last",lastWeek);
        values.put("each",each);
        values.put("name", name);
        values.put("address", address);
        values.put("week", week);
        values.put("start", startTime);
        values.put("num",total);
        db.insert("Book", null, values);
    }
    int getTotal(String two){
        String[] array = two.split(",");
        return array.length;

    }
}
