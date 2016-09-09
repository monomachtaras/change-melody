package co.changemelody;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
    private EditText userInput;
    private TextView recordsTextView;
    private MyDBHandler dbHandler;
    private TextView textUri;
    private String uriName;
    private String path;
    private Uri uri;
    private String melodyName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userInput = (EditText) findViewById(R.id.user_Input);
        textUri = (TextView)findViewById(R.id.uriText);
        recordsTextView = (TextView) findViewById(R.id.records_TextView);
        dbHandler = new MyDBHandler(this, null, null, 1);

        savedInstanceState = getIntent().getExtras();

        try{
            uriName = savedInstanceState.getString("uriName");
            uri = Uri.parse(uriName);
            path = getRealPathFromURI(this, uri);
            melodyName = path.substring(path.lastIndexOf("/")+1);
            Melodies melody = new Melodies(melodyName, uriName);
            dbHandler.addMelody(melody);
        } catch (NullPointerException e){

        }
        textUri.setText(MyDBHandler.readFromFile(MainActivity.this));
        printDatabase();

    }

    public void printDatabase(){
        String dbString = dbHandler.databaseToString();
        recordsTextView.setText(dbString);
        userInput.setText("");
    }

    public void addButtonClicked(View view){
        Intent intent = new Intent(MainActivity.this, SelectMelodyActivity.class);
        startActivity(intent);
    }

    public void deleteButtonClicked(View view){
        String inputText = userInput.getText().toString();
        dbHandler.deleteMelody(inputText);
        printDatabase();
    }

    public void startButtonClicked(View view){
        Intent intent = new Intent(this,MyAlarmReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, MyAlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long firstMillis = System.currentTimeMillis(); // alarm is set right away
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis,
                600000, pIntent);


    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Audio.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public void ButtonClickedStartGPS(View view){
        Intent intent;
        intent = new Intent(MainActivity.this, MapsActivity.class);
        startActivity(intent);
    }

    public void ButtonClickedStartMail(View view){
        Intent intent;
        intent = new Intent(MainActivity.this, ActivityJSON.class);
        startActivity(intent);
    }
}