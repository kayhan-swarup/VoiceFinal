package com.swarup.kayhan.voice;

        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.DatabaseErrorHandler;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.util.Log;

        import java.util.LinkedList;
        import java.util.List;

/**
 * Created by Kayhan on 12/28/2014.
 */
public class MySqlHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "Voice";
    String CREATE_USER = "CREATE TABLE USER (USER_ID TEXT PRIMARY KEY,PASSWORD TEXT NOT NULL, EMAIL TEXT, FULL_NAME TEXT,FIRST_NAME TEXT,LAST_NAME TEXT,SEX TEXT)";
    String CREATE_POST = "CREATE TABLE POST (POST_ID INTEGER PRIMARY KEY AUTOINCREMENT, USER_ID TEXT, POST_TITLE TEXT,POST_TEXT TEXT, FOREIGN KEY (USER_ID) REFERENCES USER(USER_ID))";
    String CREATE_FOLLOW = "CREATE TABLE FOLLOW (FOLLOWING_ID TEXT, FOLLOWER_ID TEXT, FOREIGN KEY (FOLLOWER_ID) REFERENCES USER(USER_ID)," +
            "FOREIGN KEY (FOLLOWING_ID) REFERENCES USER(USER_ID), PRIMARY KEY(FOLLOWING_ID,FOLLOWER_ID))";
    String CREATE_LIKE = "CREATE TABLE LIKE (USER_ID TEXT REFERENCES USER(USER_ID),POST_ID INTEGER REFERENCES POST(POST_ID),PRIMARY KEY(USER_ID,POST_ID))";
    String CREATE_VIEW = "CREATE TABLE VIEW (USER_ID TEXT REFERENCES USER(USER_ID),POST_ID INTEGER REFERENCES POST(POST_ID),PRIMARY KEY "+
            "(USER_ID,POST_ID))";
//    String CREATE_COMMENT = "CREATE TABLE COMMENT (COMMENT_ID INTEGER PRIMARY KEY AUTOINCREMENT, COMMENT TEXT, USER_ID TEXT REFERENCES USER(USER_ID), POST_ID INTEGER REFERENCES POST(POST_ID))";
    String[] COLUMNS_USER = {"USER_ID","PASSWORD","EMAIL","FULL_NAME","FIRST_NAME","LAST_NAME","SEX"};
    String[] COLUMN_POST = {"POST_ID","USER_ID","POST_TITLE","POST_TEXT"};

    public MySqlHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
    }


    public MySqlHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public void addFollow(String following,String follower){
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();

        values.put("FOLLOWING_ID",following); // get title
        values.put("FOLLOWER_ID",follower);
         db.insert("FOLLOW", // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }
    public void addComment(Comment comment){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("COMMENT",comment.comment);
        values.put("USER_ID",comment.userID);
        values.put("POST_ID",comment.postId);
        db.insert("POST",null,values);
        db.close();
    }
    public List<Comment> getComments(int postId){
        String query = "SELECT * FROM COMMENT WHERE POST_ID=?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,new String[]{String.valueOf(postId)});

        if(cursor!=null)
            cursor.moveToFirst();
        List<Comment> commentList = new LinkedList<>();
        do{
            Comment comment = new Comment(cursor.getString(1),cursor.getString(2),cursor.getInt(3));
            comment.commentId = cursor.getInt(0);
            commentList.add(comment);
        }while (cursor.moveToNext());

        return commentList;
    }
    public int addPost(Post post){
        Log.d("addUser", post.toString());

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();

        values.put("USER_ID",post.getUserId()); // get title

        values.put("POST_TITLE",post.getPostTitle()); // get author

        values.put("POST_TEXT",post.getPostText());

        // 3. insert
        db.insert("POST", // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT MAX(POST_ID) FROM POST",null);
        int count = cursor.getInt(0);
        return count;

    }
    public void addUser(User user){
        //for logging
        Log.d("addUser", user.toString());

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();

        values.put("USER_ID",user.getUserId()); // get title

        values.put("PASSWORD",user.getPassword()); // get author

        values.put("EMAIL",user.getEmail());
        values.put("FULL_NAME",user.getUserName());
        values.put("FIRST_NAME",user.getFirstName());
        values.put("LAST_NAME",user.getLastName());
        values.put("SEX",user.getSex());
        // 3. insert
        db.insert("USER", // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }
    public List<Post> getAllPosts(String userId){
        List<Post> list  =  new LinkedList<Post>();
        String query = "SELECT * FROM POST WHERE USER_ID=?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{userId});
        Post post = null;

        if(cursor.moveToFirst()){
            do{
                post = new Post(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3));
                list.add(post);
            }while(cursor.moveToNext());
        }




        return list;
    }
    public void addView(String userId,int postId){
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();

        values.put("USER_ID",userId); // get title
        values.put("POST_ID",postId);
        db.insert("VIEW",null,values);
        db.close();

    }
    public void addLike(String userId,int postId){
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();

        values.put("USER_ID",userId); // get title
        values.put("POST_ID",postId);
        db.insert("LIKE",null,values);
        db.close();

    }
    public int getLikeCount(int postId){
        int count = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT COUNT(USER_ID) FROM LIKE WHERE POST_ID=?";
        Cursor cursor = db.rawQuery(query,new String[]{String.valueOf(postId)});
        if(cursor!=null)
            cursor.moveToFirst();
        else return 0;
        count = cursor.getInt(0);
        db.close();
        return count;
    }
    public int getViewCount(int postId){
        int count = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT COUNT(USER_ID) FROM VIEW WHERE POST_ID=?";
        Cursor cursor = db.rawQuery(query,new String[]{String.valueOf(postId)});
        if(cursor!=null)
            cursor.moveToFirst();
        else return 0;
        count = cursor.getInt(0);
        db.close();
        return count;
    }

    public boolean isLiked(String userId,int postId){
        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor =
                db.query("LIKE", // a. table
                        new String[]{"USER_ID","POST_ID"}, // b. column names
                        "USER_ID =?"+" AND "+" POST_ID =?", // c. selections
                        new String[] { userId,String.valueOf(postId)}, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit
        if(cursor!=null)
            cursor.moveToFirst();
        if(cursor.getCount()>0)
            return true;
        return false;
    }
    public List<String> getAllFollowers(String followingId){
        String query = "Select * FROM "+"FOLLOW WHERE FOLLOWING_ID=?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,new String[]{followingId});
        List <String> list = new LinkedList<String>();
        if(cursor.moveToFirst()){
            do{
                list.add(cursor.getString(1));
            }while (cursor.moveToNext());
        }
        return list;
    }
    public List<String> getAllFollowings(String followerId){

        String query = "Select * FROM "+"FOLLOW WHERE FOLLOWER_ID=?" ;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,new String[]{followerId});
        List <String> list = new LinkedList<String>();
        if(cursor.moveToFirst()){
            do{
                    list.add(cursor.getString(0));
            }while (cursor.moveToNext());
        }
        return list;
    }

    public List<User> getAllUsers () {
        List<User> books = new LinkedList<User>();

        // 1. build the query
        String query = "SELECT  * FROM " + "USER";

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        User user = null;
        if (cursor.moveToFirst()) {
            do {
                user = new User();
                user.setUserId(cursor.getString(0));
                user.setPassword(cursor.getString(1));
                user.setEmail(cursor.getString(2));
                user.setUserName(cursor.getString(3));
                user.setFirstName(cursor.getString(4));
                user.setLastName(cursor.getString(5));
                user.setSex(cursor.getString(6));
                // Add book to books
                books.add(user);
            } while (cursor.moveToNext());
        }

        Log.d("getAllBooks()", books.toString());

        // return books
        return books;
    }

    public List<Post> getRecentPost(String userId){
        this.close();
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT POST.POST_ID,POST.USER_ID,POST.POST_TITLE,POST.POST_TEXT,COUNT(VIEW.USER_ID) AS COUNT FROM POST,FOLLOW," +
                "VIEW WHERE FOLLOW.FOLLOWER_ID=? AND FOLLOW.FOLLOWING_ID=POST.USER_ID AND  VIEW.POST_ID=POST.POST_ID" +
                " GROUP BY VIEW.POST_ID ORDER BY COUNT DESC";
        Cursor c = db.rawQuery("SELECT COUNT(POST_ID) FROM POST",null);
        if(c!=null){
            c.moveToFirst();
            if(c.getInt(0)==0)return null;
        }
        db.close();
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,new String[]{userId});
        if(cursor!=null) cursor.moveToFirst();
        else return  null;
        List<Post> list = new LinkedList<Post>();
        do{
            Post post = new Post(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3));

            list.add(post);
        }while(cursor.moveToNext());

        return list;
    }

    public User getUser(String userId){

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query

        Cursor cursor =
                db.query("USER", // a. table
                        COLUMNS_USER, // b. column names
                        "USER_ID = ?", // c. selections
                        new String[] { userId }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();

//        cursor.getI
        // 4. build book object
        User user = new User();
        user.setUserId(cursor.getString(0));
        user.setPassword(cursor.getString(1));
        user.setEmail(cursor.getString(2));
        user.setUserName(cursor.getString(3));
        user.setFirstName(cursor.getString(4));
        user.setLastName(cursor.getString(5));
        user.setSex(cursor.getString(6));

        //log
        Log.d("getuser("+userId+")", user.toString());

        // 5. return user
        return user;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_USER);
        db.execSQL(CREATE_POST);
        db.execSQL(CREATE_FOLLOW);
        db.execSQL(CREATE_LIKE);
        db.execSQL(CREATE_VIEW);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS USER");
        db.execSQL("DROP TABLE IF EXISTS POST");
        db.execSQL("DROP TABLE IF EXISTS FOLLOW");
        db.execSQL("DROP TABLE IF EXISTS LIKE");
        db.execSQL("DROP TABLE IF EXISTS VIEW");
        this.onCreate(db);
    }
}
