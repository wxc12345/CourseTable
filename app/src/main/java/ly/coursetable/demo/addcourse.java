package ly.coursetable.demo;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class addcourse extends Activity {

    private EditText course;
    private EditText address;
    private EditText week;
    private EditText start;
    private EditText num;

    private EditText teacher;
    private EditText first;
    private EditText last;
    private EditText each;

    private MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcourse);

        dbHelper = new MyDatabaseHelper(this, "BookStore.db", null, 1);

        course=(EditText)findViewById(R.id.course);
        address=(EditText)findViewById(R.id.add);
        week=(EditText)findViewById(R.id.week);
        start=(EditText)findViewById(R.id.start);
        num=(EditText)findViewById(R.id.num);

        teacher=(EditText)findViewById(R.id.teacher);
        first=(EditText)findViewById(R.id.first);
        last=(EditText)findViewById(R.id.last);
        each=(EditText)findViewById(R.id.each);

        Button button=(Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                String courseP = ""+course.getText().toString();
                String addressP= ""+address.getText().toString();
                String weekS=week.getText().toString();
                String startS=start.getText().toString();
                String numS=num.getText().toString();

                String teacherP=""+teacher.getText().toString();
                String firstS=first.getText().toString();
                String lastS=last.getText().toString();
                String eachS=each.getText().toString();


                int firstP=Integer.parseInt(firstS);
                int lastP=Integer.parseInt(lastS);
                int eachP=Integer.parseInt(eachS);

                int weekP=Integer.parseInt(weekS);
                int startP=Integer.parseInt(startS);
                int numP=Integer.parseInt(numS);

                Toast.makeText(addcourse.this,courseP,Toast.LENGTH_SHORT).show();

/*
                SharedPreferences.Editor editor=getSharedPreferences(courseP,MODE_PRIVATE).edit();
                editor.putString("address",addressP);
                editor.putString("course",courseP);
                editor.putInt("week",weekP);
                editor.putInt("start",startP);
                editor.putInt("num",numP);
                editor.apply();
*/

                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                // 开始组装第一条数据
                values.put("teacher",teacherP);
                values.put("first",firstP);
                values.put("last",lastP);
                values.put("each",eachP);

                values.put("name", courseP);
                values.put("address", addressP);
                values.put("week", weekP);
                values.put("start", startP);
                values.put("num",numP);
                db.insert("Book", null, values); // 插入第一条数据(表名，null,待添加数据)

                Intent intent=new Intent();
                intent.putExtra("data_return",courseP);
                setResult(RESULT_OK,intent);
                finish();

            }
        });
    }

}
