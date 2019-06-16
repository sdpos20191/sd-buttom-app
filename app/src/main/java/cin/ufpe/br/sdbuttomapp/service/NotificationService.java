package cin.ufpe.br.sdbuttomapp.service;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import java.sql.Timestamp;

import cin.ufpe.br.sdbuttomapp.model.Frame;
import cin.ufpe.br.sdbuttomapp.model.Occurrence;

public class NotificationService  {

    private OnNotificationEventListener mListener;

    public NotificationService()  {}

    public void registerListener(OnNotificationEventListener mListener) {
        this.mListener = mListener;
    }


    public void on(String message) {
        Gson gson = new Gson();
        Frame frame = gson.fromJson(message, Frame.class);
        System.out.println( "received: " + message );
        System.out.println(frame.getOccurrence());
        this.mListener.onNotificationEvent(frame.getOccurrence());
    }

}
