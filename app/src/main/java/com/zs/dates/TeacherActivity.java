package com.zs.dates;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TeacherActivity extends AppCompatActivity {
    Button insert;
    Button query;
    Button update;
    Button delete;
    Button querys;
    Uri uri = Uri.parse("content://hb.android.contentProvider/teacher/44");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        insert = (Button) findViewById(R.id.insert);
        query = (Button) findViewById(R.id.query);
        update = (Button) findViewById(R.id.update);
        delete = (Button) findViewById(R.id.delete);
        querys = (Button) findViewById(R.id.querys);

        insert.setOnClickListener(new InsertListener());
        query.setOnClickListener(new QueryListener());

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentResolver cr = getContentResolver();
                ContentValues cv = new ContentValues();
                cv.put("name","lvcha");
                int uri2 = cr.update(uri,cv,"_ID=?",new String[]{"3"});
                System.out.println("updated: "+uri2);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentResolver cr = getContentResolver();
                cr.delete(uri,"_ID=?",new String[]{"2"});
            }
        });

        querys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentResolver cr = getContentResolver();
                Cursor c = cr.query(uri, null, null, null, null);
                System.out.println(c.getCount());
                c.close();
            }
        });
    }

    class InsertListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            ContentResolver cr = getContentResolver();
            ContentValues cv = new ContentValues();
            cv.put("title","jiaoshou");
            cv.put("name", "jiaoshi");
            cv.put("sex", false);
            Uri uri2 = cr.insert(uri, cv);//Inserts a row into a table at the given URL. If the content provider supports transactions the insertion will be atomic.
            System.out.println(uri2.toString());
        }
    }

    class QueryListener  implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            ContentResolver cr = getContentResolver();
            //查找数据为1的数据
            Cursor c = cr.query(uri, null, "_ID=?", new String[]{"1"}, null);
            //这里必须要调用 c.moveToFirst将游标移动到第一条数据,不然会出现index -1 requested , with a size of 1错误；cr.query返回的是一个结果集。
            if(c.moveToFirst()==false){
                return;
            }
            int name = c.getColumnIndex("name");
            int title = c.getColumnIndex("title");
            int sex = c.getColumnIndex("sex");
            System.out.println(c.getString(name));
            System.out.println(c.getString(title));
            try {
                int anInt = c.getInt(sex);
                System.out.println(anInt);
                System.out.println();
            }catch (Exception e){
                e.printStackTrace();
            }
            c.close();

        }
    }
}
