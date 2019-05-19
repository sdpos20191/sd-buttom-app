package cin.ufpe.br.sdbuttomapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.sql.Timestamp;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void sendOccurrence(View view) {
        getLastLocation(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    System.out.println("Location received: " + location);
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    Occurrence occurrence = new Occurrence(timestamp.getTime(), location.getLatitude(), location.getLongitude());
                    createOccurrence(occurrence);
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
}
