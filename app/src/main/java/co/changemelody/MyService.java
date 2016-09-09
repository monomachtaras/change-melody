package co.changemelody;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

public class MyService extends IntentService {
    private List<Integer> list = new ArrayList<>();
    public static final String TAG = "tarasmonomach";

    private MyDBHandler dbHandler= new MyDBHandler(this, null, null, 1);
    public static String time="";
    private String numbers;
    private StringTokenizer tokenizer;
    private int length;
    private Random r = new Random();
    private static int notif = 5643455;
    private Notification.Builder notification;

    public MyService() {
        super("MyService");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        numbers = dbHandler.selectAllNumbers();
        tokenizer = new StringTokenizer(numbers, " ");
        while(tokenizer.hasMoreTokens()){
            String a = tokenizer.nextToken();
            list.add(Integer.valueOf(a));
        }

        final DateFormat dateFormat = new SimpleDateFormat(" HH:mm:ss");
                    try {
                        setRingtone(Uri.parse(dbHandler.onSelectRow(getNum())));
                        Date date = new Date();
                        time = " "+" "+dateFormat.format(date).toString();
                        MyDBHandler.writeToFile(time, MyService.this);
                    } catch (Exception e) {
                        notification("exception happend");
                    }
        Log.i(TAG, " end of service");
    }


   /* @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG,"onStartCommand begins");
        numbers = dbHandler.selectAllNumbers();
        tokenizer = new StringTokenizer(numbers, " ");
        while(tokenizer.hasMoreTokens()){
            String a = tokenizer.nextToken();
            list.add(Integer.valueOf(a));
        }

        final DateFormat dateFormat = new SimpleDateFormat(" HH:mm:ss");
        notification("service created");

        Runnable r = new Runnable() {
            @Override
            public void run() {
                        synchronized (this) {
                            try {
                                setRingtone(Uri.parse(dbHandler.onSelectRow(getNum())));
                                Date date = new Date();
                                time = " "+" "+dateFormat.format(date).toString();
                                MyDBHandler.writeToFile(time, MyService.this);
                                Log.i(TAG, "loop " +time+ " finished");
                            } catch (Exception e) {
                                notification("exception happend");
                            }
                        }



            }
        };
        Thread thread = new Thread(r);
        thread.setPriority(10);
        Log.i(TAG, "priority "+thread.getPriority());
        thread.start();
        return Service.START_NOT_STICKY; // restarts the service if it gets destroyed.
    }
*/
    @Override
    public void onDestroy() {
    }

    public void notification(String a){
        final NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notification = new  Notification.Builder(this);
        notification.setAutoCancel(true);
        notification.setSmallIcon(R.drawable.melodylogo);
        notification.setTicker(a);
        notification.setContentTitle("ChangeMelody");
        notification.setContentText("app did something "+a);
        nm.notify(notif, notification.build());
    }


    @Override
    public IBinder onBind(Intent intent) {
        // This is not a bound service
        // throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    public void setRingtone(Uri uri){
        Log.i(TAG, "setted ringtone "+uri.toString());
        RingtoneManager.setActualDefaultRingtoneUri(
                MyService.this,
                RingtoneManager.TYPE_RINGTONE,
                uri);
    }

    public int getNum(){
        length = list.size();
        int result = r.nextInt(length);
        Log.i(TAG, "random worked"+String.valueOf(result));
        return list.get(result);
    }
}
