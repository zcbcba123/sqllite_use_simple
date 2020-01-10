package com.zs.dates;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class TeacherActivity extends AppCompatActivity {
    Button insert;
    Button query;
    Button update;
    Button delete;
    Button querys;
    TextView tv_resInsert,tv_resQuery;
    private Spinner sp_name_main;
    private EditText et_name_main,et_queryId_main,et_queryTitle_main,et_queryName_main;
    String mTitle ="title";

    //    Uri uri = Uri.parse("content://hb.android.contentProvider/teacher/6");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        insert = (Button) findViewById(R.id.insert);
        query = (Button) findViewById(R.id.query);
        update = (Button) findViewById(R.id.update);
        delete = (Button) findViewById(R.id.delete);
        querys = (Button) findViewById(R.id.querys);
        tv_resInsert = (TextView) findViewById(R.id.tv_resInsert);
        sp_name_main = (Spinner) findViewById(R.id.sp_name_main);
        et_name_main = (EditText) findViewById(R.id.et_insertValue_main);
        et_queryId_main = (EditText) findViewById(R.id.et_queryId_main);
        et_queryTitle_main = (EditText) findViewById(R.id.et_queryTitle_main);
        et_queryName_main = (EditText) findViewById(R.id.et_queryName_main);
        tv_resQuery = (TextView) findViewById(R.id.tv_resQuery);
        sp_name_main.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String[] s = getResources().getStringArray(R.array.spinnerarr);
//                Toast.makeText(TeacherActivity.this,"你点击的是"+s[i],Toast.LENGTH_LONG).show();
                mTitle =s[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        insert.setOnClickListener(new InsertListener());
        query.setOnClickListener(new QueryListener());

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentResolver cr = getContentResolver();
                ContentValues cv = new ContentValues();
                cv.put("name","lvcha");
                int uri2 = cr.update(ContentData.UserTableData.UPDATE_URI,cv,"_ID=?",new String[]{"3"});
                System.out.println("updated: "+uri2);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentResolver cr = getContentResolver();
                cr.delete(ContentData.UserTableData.DELETE_URI,"_ID=?",new String[]{"2"});
            }
        });

        querys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentResolver cr = getContentResolver();
                        Cursor c = cr.query(ContentData.UserTableData.QUERYS_URI, null, null, null, null);
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
            cv.put("title", mTitle);
            cv.put("name", et_name_main.getText().toString());
            cv.put("sex", false);
            Uri uri2 = cr.insert(ContentData.UserTableData.INSERT_URI, cv);//Inserts a row into a table at the given URL. If the content provider supports transactions the insertion will be atomic.
            System.out.println(uri2.toString());
            tv_resInsert.setText("插入的结果: "+uri2.toString());
        }
    }

    class QueryListener  implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            ContentResolver cr = getContentResolver();
            //查找数据为1的数据
            Cursor c = null;
            String id = et_queryId_main.getText().toString();
            String title = et_queryTitle_main.getText().toString();
            String name = et_queryName_main.getText().toString();
            if ("".equals(id)&&"".equals(title)&&"".equals(name)){
                Toast.makeText(TeacherActivity.this,"请填写要查的信息",Toast.LENGTH_LONG).show();
                return;
            }
            if ("".equals(id)){
                id="-1";
            }

            try {
                ArrayList<String> list = new ArrayList<String>();
                StringBuffer selection = new StringBuffer();
                if (!"".equals(title)){
                    selection.append("title=?");
                    list.add(title);
                }
                if (!"".equals(name)){
                    String end = selection.substring(selection.length() - 1, selection.length());
                    if ("?".equals(end)){
                        selection.append(" and name=?");
                    }else {
                        selection.append("name=?");
                    }
                    list.add(name);
                }
                String[] array =new String[list.size()];
                String[] strings=list.size()>0?(String[]) list.toArray(array):null;
                //"title=?",
                c = cr.query(Uri.parse(ContentData.UserTableData.QUERY_URI+id), null, selection.toString() ,strings, null);
                //这里必须要调用 c.moveToFirst将游标移动到第一条数据,不然会出现index -1 requested , with a size of 1错误；cr.query返回的是一个结果集。
                if (c.moveToFirst() == false) {
                    return;
                }
                int nameInt = c.getColumnIndex("name");
                int titleInt = c.getColumnIndex("title");
                String titleContent = c.getString(nameInt);
                String nameContent = c.getString(titleInt);
                tv_resQuery.setText("title: "+titleContent+"\n"+" name: "+nameContent );
            }catch (Exception e){
                e.printStackTrace();
            }
            c.close();

        }
    }
}
