package com.zs.dates;

import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * 提供一些常量
 */
public class ContentData {
    public static final String AUTHORITY = "hb.android.contentProvider";
    public static final String DATABASE_NAME = "teacher.db";
    //创建 数据库的时候，都必须加上版本信息；并且必须大于4
    public static final int DATABASE_VERSION = 4;
    public static final String USERS_TABLE_NAME = "teacher";

    public static final class UserTableData implements BaseColumns {
        public static final String TABLE_NAME = "teacher";
        //Uri
        public static final Uri INSERT_URI = Uri.parse("content://" + AUTHORITY + "/teacher/insert");
        public static final Uri DELETE_URI = Uri.parse("content://" + AUTHORITY + "/teacher/6");
        public static final Uri UPDATE_URI = Uri.parse("content://" + AUTHORITY + "/teacher/6");
        public static final String QUERY_URI = "content://" + AUTHORITY + "/teacher/query/";
        public static final Uri QUERYS_URI = Uri.parse("content://" + AUTHORITY + "/teacher/6");
        //数据集的MIME类型字符串应该以vnd.android.cursor.dir/开头
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/hb.android.teachers";
        //单一数据的MIME类型字符串应该以vnd.android.cursor.item/开头
        public static final String CONTENT_TYPE_ITME = "vnd.android.cursor.item/hb.android.teacher";
        /*自定义匹配代码*/
        public static final int TEACHERS = 1;
        /*自定义匹配代码*/
        public static final int TEACHER = 2;
        public static final int INSERT = 3;
        public static final int QUERY = 4;

        public static final String TITLE = "title";

        public static final String NAME = "name";
        public static final String DATE_ADDED = "date_added";
        public static final String SEX = "sex";
        public static final String DEFAULT_SORT_ORDER = "_id desc";

        public static final UriMatcher uriMatcher;

        static {
            //NO_MATCH表示不匹配任何路径的返回码
            uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
            // 如果match()方法匹配content://hb.android.teacherProvider/teachern路径,返回匹配码为TEACHERS
            uriMatcher.addURI(ContentData.AUTHORITY, "teacher", TEACHERS);
            // 如果match()方法匹配content://hb.android.teacherProvider/teacher/230,路径，返回匹配码为TEACHER
            uriMatcher.addURI(ContentData.AUTHORITY, "teacher/#", TEACHER);
            uriMatcher.addURI(ContentData.AUTHORITY, "teacher/insert", INSERT);
            uriMatcher.addURI(ContentData.AUTHORITY, "teacher/query/#", QUERY);
        }

    }
}
