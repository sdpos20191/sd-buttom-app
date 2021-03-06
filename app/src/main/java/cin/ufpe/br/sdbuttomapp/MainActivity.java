package cin.ufpe.br.sdbuttomapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.net.URI;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import cin.ufpe.br.sdbuttomapp.data.MainApplication;
import cin.ufpe.br.sdbuttomapp.model.Occurrence;
import cin.ufpe.br.sdbuttomapp.network.WSClient;
import cin.ufpe.br.sdbuttomapp.service.NotificationService;
import cin.ufpe.br.sdbuttomapp.service.OnNotificationEventListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnNotificationEventListener {
    public static final String EXTRA_MESSAGE = "cin.ufpe.br.sdbuttomapp.MESSAGE";
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startAlertActivity();
        try {
            WSClient client = new WSClient(new URI("ws://192.188.188.4:8888"));
            client.connectBlocking();
            NotificationService notificationService = new NotificationService();
            notificationService.registerListener(this);
            client.setNotification(notificationService);

        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    public void sendOccurrence(View view) {
        getLastLocation(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location  location) {
                if (location != null) {
                    System.out.println("Location received: " + location);
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    String pattern = "yyyy-MM-dd'T'HH:mm:ssZ";
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                    String date = dateFormat.format(new Date());
                    Occurrence occurrence = new Occurrence(date, location.getLatitude(), location.getLongitude());
                    createOccurrence(occurrence);
                    startSecondActivity(location.getLatitude(), location.getLongitude());
                }
            }
        });
    }

    void getLastLocation(OnSuccessListener<Location> listener) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            System.out.println("Permission not granted.");
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, listener);
    }

    void createOccurrence(Occurrence occurrence) {
        MainApplication.apiManager.createOccurrence(occurrence, new Callback<Occurrence>() {
            @Override
            public void onResponse(Call<Occurrence> call, Response<Occurrence> response) {
                System.out.println(response);
            }

            @Override
            public void onFailure(Call<Occurrence> call, Throwable t) {
                System.err.println(t);
            }
        });
    }

    void startSecondActivity(double lat, double lng) {
        Intent intent = new Intent(this, SecondActivity.class);
        intent.putExtra("lat", lat);
        intent.putExtra("long", lng);
        startActivity(intent);
    }

    private static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0;
        }
        else {
            double theta = lon1 - lon2;
            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
            if (unit == "K") {
                dist = dist * 1.609344;
            } else if (unit == "N") {
                dist = dist * 0.8684;
            }
            return (dist);
        }
    }

    @Override
    public void onNotificationEvent(final Occurrence occurrence) {
        getLastLocation(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    System.out.println("Location received: " + location);
                    System.out.println(occurrence.getLocalizacao().getLatitude());
                    double distance = distance(
                            location.getLatitude(),
                            location.getLongitude(),
                            occurrence.getLocalizacao().getLatitude(),
                            occurrence.getLocalizacao().getLongitude(),
                            "K"
                    );
                    if (distance == 0 && distance < 1) startAlertActivity();
                }
            }
        });
    }

    void startAlertActivity() {
        Intent intent = new Intent(this, AlertActivity.class);
        startActivity(intent);
    }
}
