package cin.ufpe.br.sdbuttomapp;

public class Occurrence {

    private long timestamp;
    private double lat;
    private double lng;

    Occurrence(long timestamp, double lat, double lng) {
        this.timestamp = timestamp;
        this.lat = lat;
        this.lng = lng;
    }

}
