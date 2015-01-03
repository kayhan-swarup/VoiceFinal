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
    String CREATEUSER = "CREATE TABLE USER (USER_ID TEXT PRIMARY KEY,PASSWORD TEXT NOT NULL, EMAIL TEXT, FULL_NAME TEXT)";

    public MySqlHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
    }


    public MySqlHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public void addUser(User user){
        //for logging
        Log.d("addBook", user.toString());

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();

        values.put("USER_ID",user.getUserId()); // get title

        values.put("PASSWORD",user.getPassword()); // get author

        values.put("EMAIL",user.getEmail());
        values.put("FULL_NAME",user.getUserName());

        // 3. insert
        db.insert("USER", // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }public List<User> getAllBooks() {
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
                user.setUserName(cursor.getString(1));
                user.setPassword(cursor.getString(2));

                // Add book to books
                books.add(user);
            } while (cursor.moveToNext());
        }

        Log.d("getAllBooks()", books.toString());

        // return books
        return books;
    }

    public User getUser(int id){

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        String[] COLUMNS = {"USER_ID","PASSWORD","EMAIL","FULL_NAME"};
        Cursor cursor =
                db.query("USER", // a. table
                        COLUMNS, // b. column names
                        "USER_ID = ?", // c. selections
                        new String[] { String.valueOf(id) }, // d. selections args
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

        //log
        Log.d("getuser("+id+")", user.toString());

        // 5. return user
        return user;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATEUSER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS USER");
        this.onCreate(db);
    }
}
