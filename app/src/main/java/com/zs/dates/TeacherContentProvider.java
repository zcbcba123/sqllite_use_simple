package com.zs.dates;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import static com.zs.dates.ContentData.UserTableData.CONTENT_TYPE;
import static com.zs.dates.ContentData.UserTableData.CONTENT_TYPE_ITME;
import static com.zs.dates.ContentData.UserTableData.TEACHER;
import static com.zs.dates.ContentData.UserTableData.TEACHERS;
import static com.zs.dates.ContentData.UserTableData.uriMatcher;

/**
 * 这个类给外部程序提供访问内部数据的一个接口
 */
public class TeacherContentProvider extends ContentProvider{
    private DBOpenHelper dbOpenHelper=null;
    //UriMatcher类用来匹配Uri，使用match()方法匹配路径时返回匹配码
    /**
     * 是一个回调函数，在ContentProvider创建的时候，就会运行,第二个参数为指定数据库名称，如果不指定，就会找不到数据库；
     * 如果数据库存在的情况下是不会再创建一个数据库的。（当然首次调用 在这里也不会生成数据库必须调用SQLiteDatabase的 getWritableDatabase,getReadableDatabase两个方法中的一个才会创建数据库）
     */

    @Override
    public boolean onCreate() {
        dbOpenHelper=new DBOpenHelper(this.getContext(),ContentData.DATABASE_NAME,ContentData.DATABASE_VERSION);
        return false;
    }

    /**
     * 当执行这个方法的时候，如果没有数据库，他会创建，同时也会创建表，但是如果没有表，下面在执行insert的时候就会出错
     * 这里的插入数据也完全可以用sql语句书写，然后调用 db.execSQL(sql)执行。
     */
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        //获得一个可写的数据库引用，如果数据库不存在，则根据onCreate的方法里创建；
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        long id =0;
        switch (uriMatcher.match(uri)){
            case TEACHERS:
                id = db.insert("teacher",null,values);
                return ContentUris.withAppendedId(uri,id);//Appends the given ID to the end of the path.
            case TEACHER:
                id = db.insert("teacher",null,values);
                String path = uri.toString();
                return Uri.parse(path.substring(0,path.lastIndexOf("/"))+id);
            default:
                throw new IllegalArgumentException("Unknown URI"+uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        int count=0;
        switch (uriMatcher.match(uri)) {
            case TEACHERS:
                count = db.delete("teacher", selection, selectionArgs);
                break;
            case TEACHER:
                // 下面的方法用于从URI中解析出id，对这样的路径content://hb.android.teacherProvider/teacher/10
                // 进行解析，返回值为10
                long personid = ContentUris.parseId(uri);
                String where = "_ID=" + personid; //删除指定id的记录
                where += !TextUtils.isEmpty(selection) ? " and (" + selection + ")" : "";// 把其它条件附加上
                System.out.println("teacher" + where + selectionArgs);
                count = db.delete("teacher", where, selectionArgs);
                System.out.println("count: " + count);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        db.close();
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        int count=0;
        switch (uriMatcher.match(uri)) {
            case TEACHERS:
                count = db.update("teacher", values, selection, selectionArgs);
                break;
            case TEACHER:
                // 下面的方法用于从URI中解析出id，对这样的路径content://com.ljq.provider.personprovider/person/10
                // 进行解析，返回值为10
                long personid = ContentUris.parseId(uri);
                String where = "_ID=" + personid;//获取指定id的记录
                where += !TextUtils.isEmpty(selection) ? " and(" + selection + ")" : "";
                count = db.update("teacher", values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI" + uri);
        }
        db.close();
        return count;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)){
            case TEACHERS:
                return CONTENT_TYPE;
            case TEACHER:
                return CONTENT_TYPE_ITME;
            default:
                throw new IllegalArgumentException("Unknown URI"+uri);
        }
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        switch (uriMatcher.match(uri)) {
            case TEACHERS:
                return db.query("teacher", projection, selection, selectionArgs, null, null, sortOrder);
            case TEACHER:
                // 进行解析，返回值为10
                long personid = ContentUris.parseId(uri);
                String where = "_ID=" + personid;//获取指定id的记录
                where += !TextUtils.isEmpty(selection) ? "and(" + selection + ")" : "";//把其他条件附加上
                return db.query("teacher", projection, where, selectionArgs, null, null, sortOrder);
            default:
                throw new IllegalArgumentException("Unkown URI " + uri);
        }
    }
}
