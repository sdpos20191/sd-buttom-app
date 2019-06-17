package cin.ufpe.br.sdbuttomapp.model;

import java.util.UUID;

public class Location {

    private String dataHora;
    private String id;
    private double latitude;
    private double longitude;

    public Location(String dataHora, double latitude, double longitude) {
        this.dataHora = dataHora;
        this.id = UUID.randomUUID().toString();
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
