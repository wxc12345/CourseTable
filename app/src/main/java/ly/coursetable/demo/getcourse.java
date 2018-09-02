package ly.coursetable.demo;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class getcourse extends Activity {

    private EditText course;
    private EditText address;
    private EditText week;
    private EditText start;
    private EditText num;
    private EditText teacher;
    private EditText first;
    private EditText last;
    private EditText each;
    private Button buttonDelete;
    private Button buttonRefresh;

    private MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getcourse);
        getViews();

        Intent intent=getIntent();
        final String xid=intent.getStringExtra("id");

        dbHelper = new MyDatabaseHelper(this, "BookStore.db", null, 1);

        findData(xid);


        buttonDelete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                SQLiteDatabase db=dbHelper.getWritableDatabase();
                db.delete("Book","id=?",new String[]{xid});
                finish();
            }
        });


        buttonRefresh.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                String courseP = course.getText().toString();
                String addressP=address.getText().toString();
                String weekS=week.getText().toString();
                String startS=start.getText().toString();
                String numS=num.getText().toString();

                int weekP=Integer.parseInt(weekS);
                int startP=Integer.parseInt(startS);
                int numP=Integer.parseInt(numS);

                Toast.makeText(getcourse.this,courseP,Toast.LENGTH_SHORT).show();

                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                // 开始组装第一条数据
                values.put("name", courseP);
                values.put("address", addressP);
                values.put("week", weekP);
                values.put("start", startP);
                values.put("num",numP);
                db.update("Book", values, "id=?",new String[]{xid}); // 更新数据
                /*
                Intent intent=new Intent();
                intent.putExtra("data_return",courseP);
                setResult(RESULT_OK,intent);
                */
                finish();
            }
        });

    }

    void getViews(){
        course=(EditText)findViewById(R.id.course);
        address=(EditText)findViewById(R.id.add);
        week=(EditText)findViewById(R.id.week);
        start=(EditText)findViewById(R.id.start);
        num=(EditText)findViewById(R.id.num);
        teacher=(EditText)findViewById(R.id.teacher);
        first=(EditText)findViewById(R.id.first);
        last=(EditText)findViewById(R.id.last);
        each=(EditText)findViewById(R.id.each);

        buttonDelete=(Button)findViewById(R.id.delate);
        buttonRefresh=(Button)findViewById(R.id.button);
    }
    void findData(String xid){

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor= db.query("Book", null, "id=?", new String[]{xid} ,null,null, null);

        if(cursor.moveToFirst()) {
            String name1 = cursor.getString(cursor.getColumnIndex("name"));
            String address1 = cursor.getString(cursor.getColumnIndex("address"));
            int week1 = cursor.getInt(cursor.getColumnIndex("week"));
            int num1 = cursor.getInt(cursor.getColumnIndex("num"));
            int start1 = cursor.getInt(cursor.getColumnIndex("start"));
            int first1 =cursor.getInt(cursor.getColumnIndex("first"));
            int last1 = cursor.getInt(cursor.getColumnIndex("last"));
            int each1 =cursor.getInt(cursor.getColumnIndex("each"));
            String teacher1 =cursor.getString(cursor.getColumnIndex("teacher"));


            teacher.setText(teacher1);
            first.setText(""+first1);
            last.setText(""+last1);
            each.setText(""+each1);

            course.setText(name1);
            address.setText(address1);
            week.setText(""+week1);
            start.setText(""+start1);
            num.setText(""+num1);

        }
    }
}
