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

    @Override
    public void onNotificationEvent(Occurrence occurrence) {
        Intent intent = new Intent(this, AlertActivity.class);
        startActivity(intent);
    }
}
