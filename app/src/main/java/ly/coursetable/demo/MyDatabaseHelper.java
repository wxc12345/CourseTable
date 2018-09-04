package ly.coursetable.demo;

/**
 * Created by happts on 2017/3/7.
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by happts on 2017/3/6.
 * 建立数据库
 *
 * 建表语句定义成字符串常量
 * 在onCreate()方法中调用  execSQL()方法执行建表语句
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {
    //一张表
    public static final String CREATE_BOOK = "create table Book ("
            + "id integer primary key autoincrement, "//主键
            + "address text, "//地点
            + "teacher text,"//老师
            + "week integer, "//
            + "first integer,"//起始周
            + "last integer,"//结束周
            + "each integer,"//
            + "start integer,"//第几节课开始
            + "num integer,"//总共几节课
            + "name text)";//课程名字

    private Context mContext;

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BOOK);
        Toast.makeText(mContext, "Create succeeded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //如果表已经存在，则删除重建
        db.execSQL("drop table if exists Book");
        onCreate(db);
    }

}
