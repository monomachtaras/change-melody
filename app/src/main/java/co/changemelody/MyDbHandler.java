package co.changemelody;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

class MyDBHandler  extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "melodiesDB.db";
    public static final String TABLE_MELODIES = "melodies";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_MELODYNAME = "melodyname";
    public static final String COLUMN_URINAME = "uriname";
    public static final String TAG = "tarasmonomach";
    private static InputStream inputStream;
    private static InputStreamReader inputStreamReader;
    private  static BufferedReader bufferedReader;
    private static   StringBuilder stringBuilder;
    private static OutputStreamWriter outputStreamWriter;

    //We need to pass database information along to superclass
    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_MELODIES + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_MELODYNAME + " TEXT, " +
                COLUMN_URINAME + " TEXT " +
                ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MELODIES);
        onCreate(db);
    }


    public void addMelody(Melodies melody){
        ContentValues values = new ContentValues();
        values.put(COLUMN_MELODYNAME, melody.get_melodyname());
        values.put(COLUMN_URINAME, melody.get_uriname());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_MELODIES, null, values);
        db.close();
    }


    public void deleteMelody(String melodyId){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_MELODIES + " WHERE " + COLUMN_ID + "=\"" + melodyId + "\";");
    }

    public String onSelectRow(int number){
        String result = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_MELODIES + " WHERE "+ COLUMN_ID + "=\"" + number + "\";";
        Cursor recordSet = db.rawQuery(query, null);
        recordSet.moveToFirst();
        if (!recordSet.isAfterLast()) {
            if (recordSet.getString(recordSet.getColumnIndex("uriname")) != null) {
                result =recordSet.getString(recordSet.getColumnIndex("uriname"));
            }
        }
        db.close();
        return result;
    }
    public String databaseToString(){
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_MELODIES + " WHERE 1";// why not leave out the WHERE  clause?

        //Cursor points to a location in your results
        Cursor recordSet = db.rawQuery(query, null);
        //Move to the first row in your results
        recordSet.moveToFirst();

        //Position after the last row means the end of the results
        while (!recordSet.isAfterLast()) {
            // null could happen if we used our empty constructor
            if (recordSet.getString(recordSet.getColumnIndex("melodyname")) != null) {
                dbString +=recordSet.getString(recordSet.getColumnIndex("_id")) + " "+
                        recordSet.getString(recordSet.getColumnIndex("melodyname"));
                dbString += "\n";
            }
            recordSet.moveToNext();
        }
        db.close();
        return dbString;
    }

    public String selectAllNumbers(){
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_MELODIES + " WHERE 1";
        Cursor recordSet = db.rawQuery(query, null);
        recordSet.moveToFirst();
        while (!recordSet.isAfterLast()) {
            if (recordSet.getString(recordSet.getColumnIndex("melodyname")) != null) {
                dbString +=recordSet.getString(recordSet.getColumnIndex("_id")) + " ";
            }
            recordSet.moveToNext();
        }
        db.close();
        return dbString;
    }

    public static void writeToFile(String data,Context context) {
        try {
            outputStreamWriter = new OutputStreamWriter(context.openFileOutput("config.txt",  Context.MODE_APPEND));
            outputStreamWriter.write(data+"\n");
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public static String readFromFile(Context context) {
        String ret = "";

        try {
             inputStream = context.openFileInput("config.txt");

            if ( inputStream != null ) {
                inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                    stringBuilder.append("\n");
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e(TAG, "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e(TAG, "Can not read file: " + e.toString());
        }

        return ret;
    }



}


