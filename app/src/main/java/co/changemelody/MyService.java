package co.changemelody;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.location.LocationListener;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

public class MyService extends IntentService  {
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
    static GPSTracker gpsTracker;
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

        final DateFormat dateFormat = new SimpleDateFormat("dd.MM HH:mm:ss");
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

    @Override
    public void onDestroy() {
        gpsTracker = new GPSTracker(MyService.this);
        Thread thread = Thread.currentThread();

        try{thread.sleep(8000);} catch(InterruptedException e ){}

        if (gpsTracker.getIsGPSTrackingEnabled())        {
            new HttpAsyncTask().execute("https://monomach.mircloud.host/jsonservlet");
            String stringLatitude = String.valueOf(gpsTracker.latitude);
            Log.d(TAG, stringLatitude );

        }
        else
        {
            notification("gpsTracker.getIsGPSTrackingEnabled() is false");
        }
    }


    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return POST(urls[0]);
        }

        @Override
        protected void onPostExecute(String result) {
//            Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();
//            Log.d(TAG, "onPostExecute "+result);
        }
    }
    public static String POST(String url){
//        InputStream inputStream = null;
        String result = "result";
        try {
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            String json = time+" "+gpsTracker.getLatitude()+ " "+gpsTracker.getLongitude();

            StringEntity se = new StringEntity(json);

            httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

//            // 9. receive response as inputStream
//            inputStream = httpResponse.getEntity().getContent();
//
//            // 10. convert inputstream to string
//            if(inputStream != null)
//                result = convertInputStreamToString(inputStream);
//            else
//                result = "Did not work!";

        } catch (Exception e) {
            Log.d(TAG, e.getLocalizedMessage());
        }
        return result;
    }
//    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
//        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
//        String line = "";
//        String result = "";
//        while((line = bufferedReader.readLine()) != null)
//            result += line;
//
//        inputStream.close();
//        return result;
//
//    }

}
