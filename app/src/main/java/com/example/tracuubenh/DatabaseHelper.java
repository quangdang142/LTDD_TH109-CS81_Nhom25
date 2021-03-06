package com.example.tracuubenh;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {

    private String DB_PATH = null;
    private static String DB_NAME = "tenbenh.db";
    private SQLiteDatabase myDatabase;
    private final Context myContext;

    public DatabaseHelper(Context context)
    {
        super(context, DB_NAME,null,1);
        this.myContext = context;
        this.DB_PATH = "/data/data/" + context.getPackageName()+"/"+"databases/";
        Log.e("Path 1", DB_PATH);
    }

    public void createDatabase() throws IOException
    {
        boolean dbExist = checkDatabase();
        if(!dbExist)
        {
            this.getReadableDatabase();
            try
            {
                myContext.deleteDatabase(DB_NAME);
                copyDatabase();
            }catch (IOException e)
            {
                throw new  Error("error copying data");
            }
        }
    }


    public boolean checkDatabase()
    {
        SQLiteDatabase checkDB = null;
        try
        {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath,null,SQLiteDatabase.OPEN_READONLY);
        }catch (SQLiteException e)
        {
            //
        }
        if(checkDB != null)
        {
            checkDB.close();
        }
        return checkDB != null? true : false;
    }

    private void copyDatabase() throws IOException
    {
        InputStream myInput = myContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while((length = myInput.read(buffer)) >0 )
        {
            myOutput.write(buffer, 0,length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
        Log.i("copyDatabase", "Database copied");
    }
    public void openDatabase()
    {
        String myPath = DB_PATH + DB_NAME;
        myDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    @Override
    public synchronized void close()
    {
        if(myDatabase != null)
            myDatabase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    //
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        try
        {
            this.getReadableDatabase();
            myContext.deleteDatabase(DB_NAME);
            copyDatabase();
        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }



    public Cursor getMeaning(String text)
    {
        Cursor c = myDatabase.rawQuery("SELECT trieuchung,vidu,cachtri,phongngua FROM benh WHERE tenbenh='"+text+"' ",null);
        return c;
    }
    public  Cursor getSuggestion(String text)
    {
        Cursor c = myDatabase.rawQuery("SELECT _id, tenbenh FROM benh WHERE tenbenh LIKE '"+text+"%'  LIMIT 40",null);
        return c;
    }

    public void insertHistory(String text)
    {
        myDatabase.execSQL("INSERT INTO history(word) VALUES(UPPER('"+text+"'))");
    }

    public Cursor getHistory()
    {
        Cursor c = myDatabase.rawQuery("SELECT DISTINCT word FROM history ORDER BY _id DESC", null);
        return c;
    }
    public  void deleteHistory()
    {
        myDatabase.execSQL("DELETE FROM history");
    }
}
