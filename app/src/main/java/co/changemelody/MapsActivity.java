package co.changemelody;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MapsActivity extends Activity {
    private static String TAG = "tarasmonomach";
    TextView textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // check if GPS enabled
        GPSTracker gpsTracker = new GPSTracker(MapsActivity.this);
        Thread thread = Thread.currentThread();

        try{thread.sleep(8000);} catch(InterruptedException e ){}

        if (gpsTracker.getIsGPSTrackingEnabled())
        {
            String stringLatitude = String.valueOf(gpsTracker.latitude);
            Log.d(TAG, stringLatitude );
            textview = (TextView)findViewById(R.id.fieldLatitude);
            textview.setText(String.valueOf(gpsTracker.getLatitude()));

            String stringLongitude = String.valueOf(gpsTracker.longitude);
            textview = (TextView)findViewById(R.id.fieldLongitude);
            textview.setText(stringLongitude);

            String city = gpsTracker.getLocality(this);
            textview = (TextView)findViewById(R.id.fieldCity);
            textview.setText(city);

            String addressLine = gpsTracker.getAddressLine(this);
            textview = (TextView)findViewById(R.id.fieldAddressLine);
            textview.setText(addressLine);
        }
        else
        {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gpsTracker.showSettingsAlert();
        }
    }

   }
