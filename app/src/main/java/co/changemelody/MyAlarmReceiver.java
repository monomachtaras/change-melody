package co.changemelody;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by taras on 03.09.16.
 */
public class MyAlarmReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 12345;
    public static final String ACTION = "com.codepath.example.servicesdemo.alarm";
    private MyDBHandler dbHandler;


    @Override
    public void onReceive(Context context, Intent intent) {
        dbHandler= new MyDBHandler(context, null, null, 1);
        Intent i = new Intent(context, MyService.class);
        i.putExtra("baza", dbHandler.databaseToString());
        context.startService(i);
    }
}